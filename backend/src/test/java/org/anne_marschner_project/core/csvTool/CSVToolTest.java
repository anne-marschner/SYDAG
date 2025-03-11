package org.anne_marschner_project.core.csvTool;

import org.anne_marschner_project.core.data.Attribute;
import org.anne_marschner_project.core.data.Relation;
import org.anne_marschner_project.core.data.Type;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class CSVToolTest {
    private MultipartFile testFileWithHeader;
    private MultipartFile testFileWithoutHeader;
    private MultipartFile emptyFile;
    private MultipartFile inconsistentFile;
    private MultipartFile mixedDataFile;
    private final String outputPath = "output";

    @BeforeEach
    public void setUp() {
        testFileWithHeader = createMockFile("test_with_header.csv", "id,name,age\n1,Anne,24\n2,,30\n3,Jonathan,22\n");
        testFileWithoutHeader = createMockFile("test_without_header.csv", "1,Anne,24\n2,,30\n3,Jonathan,22\n");
        inconsistentFile = createMockFile("inconsistent.csv", "id,name,age\n1,Anne\n2,,24\n");
        mixedDataFile = createMockFile("mixed_data.csv", "id,amount,date\n1,100.50,2024-10-01\n2,200,2024-10-02\n3,150.75,2024-10-03\n");
        emptyFile = createMockFile("empty.csv", "");
    }

    private MockMultipartFile createMockFile(String fileName, String content) {
        return new MockMultipartFile("file", fileName, "text/csv", content.getBytes(StandardCharsets.UTF_8)
        );
    }

    @Test
    public void testReadCSVColumns_withHeader() throws IOException {

        // Test for CSV with header
        CSVTool csvTool = new CSVTool();
        Relation relation = csvTool.readCSVColumns(testFileWithHeader, true, ',', '"', '\\');

        // Expected values
        Map<Integer, String> expectedColumnNames = Map.of(0, "id", 1, "name", 2, "age");
        Map<Integer, Type> expectedDataTypes = Map.of(0, Type.DOUBLE, 1, Type.STRING, 2, Type.DOUBLE);
        Map<Integer, List<String>> expectedData = Map.of(0, List.of("1", "2", "3"), 1, List.of("Anne", "", "Jonathan"), 2, List.of("24", "30", "22"));

        // Check schema and data size
        Map<Integer, Attribute> schema = relation.getSchema();
        Map<Integer, List<String>> data = relation.getData();
        assertEquals(expectedColumnNames.size(), schema.size());
        assertEquals(expectedData.size(), data.size());

        // Check column names, types and data
        for (int i = 0; i < expectedColumnNames.size(); i++) {
            assertEquals(expectedColumnNames.get(i), schema.get(i).getColumnName());
            assertEquals(expectedDataTypes.get(i), schema.get(i).getDataType());
        }

        for (int i = 0; i < expectedData.size(); i++) {
            assertEquals(expectedData.get(i), data.get(i));
        }
    }

    @Test
    public void testReadCSVColumns_withoutHeader() throws IOException {

        // Test for CSV without header
        CSVTool csvTool = new CSVTool();
        Relation relation = csvTool.readCSVColumns(testFileWithoutHeader, false, ',', '"', '\\');

        // Expected values
        Map<Integer, Type> expectedDataTypes = Map.of(0, Type.DOUBLE, 1, Type.STRING, 2, Type.DOUBLE);
        Map<Integer, List<String>> expectedData = Map.of(0, List.of("1", "2", "3"), 1, List.of("Anne", "", "Jonathan"), 2, List.of("24", "30", "22"));

        // Check schema and data size
        Map<Integer, Attribute> schema = relation.getSchema();
        Map<Integer, List<String>> data = relation.getData();
        assertEquals(expectedData.size(), schema.size());
        assertEquals(expectedData.size(), data.size());

        // Check column names, types and data
        for (int i = 0; i < expectedDataTypes.size(); i++) {
            assertNull(schema.get(i).getColumnName());
            assertEquals(expectedDataTypes.get(i), schema.get(i).getDataType());
        }

        for (int i = 0; i < expectedData.size(); i++) {
            assertEquals(expectedData.get(i), data.get(i));
        }
    }

    @Test
    public void testReadCSVColumns_emptyFile() {

        // Test for empty data
        CSVTool csvTool = new CSVTool();
        Exception exception = assertThrows(IOException.class, () -> {
            csvTool.readCSVColumns(emptyFile, true, ',', '"', '\\');
        });

        assertEquals("The CSV file is empty.", exception.getMessage());
    }

    @Test
    public void testReadCSVColumns_inconsistentColumns() throws IOException {

        // Test for data with inconsistent columns
        CSVTool csvTool = new CSVTool();
        Relation relation = csvTool.readCSVColumns(inconsistentFile, true, ',', '"', '\\');

        // Expected values
        Map<Integer, List<String>> expectedData = Map.of(0, List.of("1", "2"), 1, List.of("Anne", ""), 2, List.of("", "24"));

        // Check size and values
        Map<Integer, List<String>> data = relation.getData();
        assertEquals(expectedData.size(), data.size());

        for (int i = 0; i < expectedData.size(); i++) {
            assertEquals(expectedData.get(i), data.get(i));
        }
    }

    @Test
    public void testReadCSVColumns_withDifferentDataTypes() throws IOException {

        // Test data with mixed types
        CSVTool csvTool = new CSVTool();
        Relation relation = csvTool.readCSVColumns(mixedDataFile, true, ',', '"', '\\');

        // Expected values
        List<Type> expectedTypes = List.of(Type.DOUBLE, Type.DOUBLE, Type.STRING);

        // Check types
        Map<Integer, Attribute> schema = relation.getSchema();
        for (int i = 0; i < schema.size(); i++) {
            assertEquals(expectedTypes.get(i), schema.get(i).getDataType());
        }
    }

    @Test
    public void testWriteCSV_withHeader() throws IOException {

        // Prepare a relation with a header
        Map<Integer, Attribute> schema = new HashMap<>();
        schema.put(0, new Attribute("id", Type.DOUBLE));
        schema.put(1, new Attribute("name", Type.STRING));
        schema.put(3, new Attribute("age", Type.DOUBLE));

        Map<Integer, List<String>> data = new HashMap<>();
        data.put(0, List.of("1", "2", "3"));
        data.put(1, List.of("Anne", "", "Jonathan,Leon"));
        data.put(3, List.of("24", "30", "22"));

        Relation relation = new Relation(schema, data);

        // Write CSV
        CSVTool csvTool = new CSVTool();
        csvTool.writeCSV(relation, outputPath, ',', '"', "None");

        // Read the output CSV file and check its content
        List<String> lines = Files.readAllLines(Paths.get(outputPath));
        List<String> expectedLines = List.of("id,name,age", "1,Anne,24", "2,,30", "3,\"Jonathan,Leon\",22");
        assertEquals(expectedLines.size(), lines.size());
        for (int i = 0; i < expectedLines.size(); i++) {
            assertEquals(expectedLines.get(i), lines.get(i));
        }
    }

    @Test
    public void testWriteCSV_withoutHeader() throws IOException {

        // Prepare a relation without a header (column names are null)
        Map<Integer, Attribute> schema = new HashMap<>();
        schema.put(0, new Attribute(null, Type.DOUBLE));
        schema.put(1, new Attribute(null, Type.STRING));
        schema.put(3, new Attribute(null, Type.DOUBLE));
        schema.put(4, new Attribute(null, Type.DOUBLE));

        Map<Integer, List<String>> data = new HashMap<>();
        data.put(0, List.of("1", "2", "3"));
        data.put(1, List.of("Anne", "", "Jonathan"));
        data.put(3, List.of("24", "30", "22"));
        data.put(4, List.of("1", "2", "3"));

        Relation relation = new Relation(schema, data);

        // Write the relation to a CSV file
        CSVTool csvTool = new CSVTool();
        csvTool.writeCSV(relation, outputPath, ',', '"', "No Change");

        // Read the output CSV file and check its content
        List<String> lines = Files.readAllLines(Paths.get(outputPath));
        List<String> expectedLines = List.of("1,Anne,24,1", "2,,30,2", "3,Jonathan,22,3");
        assertEquals(expectedLines.size(), lines.size());
        for (int i = 0; i < expectedLines.size(); i++) {
            assertEquals(expectedLines.get(i), lines.get(i));
        }
    }

    @Test
    public void testWriteCSV_emptyDataset() {

        // Prepare an empty relation
        Map<Integer, Attribute> schema = new HashMap<>();
        Map<Integer, List<String>> data = new HashMap<>();
        Relation relation = new Relation(schema, data);
        CSVTool csvTool = new CSVTool();

        // Test for empty data
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            csvTool.writeCSV(relation, outputPath, ',', '"', "No Change");
        });

        assertEquals("Relation, schema, or data cannot be null.", exception.getMessage());
    }

    @Test
    public void testWriteCSV_shuffleColumns() throws IOException {

        // Prepare a relation with a header
        Map<Integer, Attribute> schema = new HashMap<>();
        schema.put(1, new Attribute("id", Type.DOUBLE));
        schema.put(2, new Attribute("name", Type.STRING));
        schema.put(4, new Attribute("age", Type.DOUBLE));

        Map<Integer, List<String>> data = new HashMap<>();
        data.put(1, List.of("1", "2", "3"));
        data.put(2, List.of("Anne", "", "Jonathan"));
        data.put(4, List.of("24", "30", "22"));

        Relation relation = new Relation(schema, data);

        // Write the relation to a CSV file
        CSVTool csvTool = new CSVTool();
        csvTool.writeCSV(relation, outputPath, ',', '"',"shuffleColumns");

        // Read the output CSV file and check its content
        List<String> lines = Files.readAllLines(Paths.get(outputPath));
        assertEquals(4, lines.size());  // 1 line for header + 3 data lines
    }

    @Test
    public void testWriteCSV_shuffleRows() throws IOException {

        // Prepare a relation with a header
        Map<Integer, Attribute> schema = new HashMap<>();
        schema.put(1, new Attribute("id", Type.DOUBLE));
        schema.put(2, new Attribute("name", Type.STRING));
        schema.put(4, new Attribute("age", Type.DOUBLE));

        Map<Integer, List<String>> data = new HashMap<>();
        data.put(1, List.of("1", "2", "3"));
        data.put(2, List.of("Anne", "", "Jonathan"));
        data.put(4, List.of("24", "30", "22"));

        Relation relation = new Relation(schema, data);

        // Write the relation to a CSV file
        CSVTool csvTool = new CSVTool();
        csvTool.writeCSV(relation, outputPath, ',', '"',"shuffleRows");

        // Read the output CSV file and check its content
        List<String> lines = Files.readAllLines(Paths.get(outputPath));
        assertEquals(4, lines.size());  // 1 line for header + 3 data lines
    }

    @AfterEach
    public void cleanUp() throws IOException {
        // Delete files after tests
        Files.deleteIfExists(Paths.get(outputPath));
    }
}