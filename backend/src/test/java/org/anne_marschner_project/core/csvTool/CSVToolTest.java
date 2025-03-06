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
    public void setUp() throws IOException {
        // create CSV-File with Header
        String csvContent = "id,name,age\n" +
                "1,Anne,24\n" +
                "2,,30\n" +
                "3,Jonathan,22\n";
        testFileWithHeader = new MockMultipartFile(
                "file",
                "test_with_header.csv",
                "text/csv",
                csvContent.getBytes(StandardCharsets.UTF_8)
        );

        // create CSV-File without Header
        String csvContentNoHeader = "1,Anne,24\n" +
                "2,,30\n" +
                "3,Jonathan,22\n";
        testFileWithoutHeader = new MockMultipartFile(
                "file",
                "test_without_header.csv",
                "text/csv",
                csvContentNoHeader.getBytes(StandardCharsets.UTF_8)
        );

        // create inconsistent CSV-File
        String csvInconsistentContent = "id,name,age\n" +
                "1,Anne\n" +
                "2,,24\n";
        inconsistentFile = new MockMultipartFile(
                "file",
                "inconsistent.csv",
                "text/csv",
                csvInconsistentContent.getBytes(StandardCharsets.UTF_8)
        );

        // create mixed data CSV-File
        String csvMixedContent = "id,amount,date\n" +
                "1,100.50,2024-10-01\n" +
                "2,200,2024-10-02\n" +
                "3,150.75,2024-10-03\n";
        mixedDataFile = new MockMultipartFile(
                "file",
                "mixed_data.csv",
                "text/csv",
                csvMixedContent.getBytes(StandardCharsets.UTF_8)
        );

        // create empty CSV-File
        emptyFile = new MockMultipartFile(
                "file",
                "empty.csv",
                "text/csv",
                new byte[0]
        );
    }

    @Test
    public void testReadCSVColumns_withHeader() throws IOException {
        // Test for CSV with header
        CSVTool csvTool = new CSVTool();
        Relation relation = csvTool.readCSVColumns(testFileWithHeader, true, ',', '"', '\\');

        // check schema
        Map<Integer, Attribute> schema = relation.getSchema();
        assertEquals(3, schema.size());  // there should be 3 attributes

        // check if column names are correct
        assertEquals("id", schema.get(0).getColumnName());
        assertEquals("name", schema.get(1).getColumnName());
        assertEquals("age", schema.get(2).getColumnName());

        // check datatype
        assertEquals(Type.DOUBLE, schema.get(0).getDataType());
        assertEquals(Type.STRING, schema.get(1).getDataType());
        assertEquals(Type.DOUBLE, schema.get(2).getDataType());

        // check data
        Map<Integer, List<String>> data = relation.getData();
        assertEquals(3, data.size());  // there should be 3 rows

        // check columns
        assertEquals(List.of("1", "2", "3"), data.get(0));
        assertEquals(List.of("Anne", "", "Jonathan"), data.get(1));
        assertEquals(List.of("24", "30", "22"), data.get(2));
    }

    @Test
    public void testReadCSVColumns_withoutHeader() throws IOException {
        // Test for CSV without header
        CSVTool csvTool = new CSVTool();
        Relation relation = csvTool.readCSVColumns(testFileWithoutHeader, false, ',', '"', '\\');

        // check Schema
        Map<Integer, Attribute> schema = relation.getSchema();
        assertEquals(3, schema.size());

        // check if column names are null because they don't exist
        assertNull(schema.get(0).getColumnName());
        assertNull(schema.get(1).getColumnName());
        assertNull(schema.get(2).getColumnName());

        // check datatypes
        assertEquals(Type.DOUBLE, schema.get(0).getDataType());
        assertEquals(Type.STRING, schema.get(1).getDataType());  // "Anne", "Bob", etc.
        assertEquals(Type.DOUBLE, schema.get(2).getDataType());

        // check data
        Map<Integer, List<String>> data = relation.getData();
        assertEquals(3, data.size());

        // check columns
        assertEquals(List.of("1", "2", "3"), data.get(0));
        assertEquals(List.of("Anne", "", "Jonathan"), data.get(1));
        assertEquals(List.of("24", "30", "22"), data.get(2));
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
        Map<Integer, List<String>> data = relation.getData();

        // check number of rows
        assertEquals(3, data.size());

        // check values in each row
        assertEquals(List.of("1", "2"), data.get(0));
        assertEquals(List.of("Anne", ""), data.get(1));
        assertEquals(new ArrayList<>(Arrays.asList("","24")), data.get(2));
    }

    @Test
    public void testReadCSVColumns_withDifferentDataTypes() throws IOException {
        // test data with mixed types
        CSVTool csvTool = new CSVTool();
        Relation relation = csvTool.readCSVColumns(mixedDataFile, true, ',', '"', '\\');

        // check schema and datatypes
        Map<Integer, Attribute> schema = relation.getSchema();
        assertEquals(Type.DOUBLE, schema.get(0).getDataType());
        assertEquals(Type.DOUBLE, schema.get(1).getDataType());
        assertEquals(Type.STRING, schema.get(2).getDataType());
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
        CSVTool csvTool = new CSVTool();

        // Write the relation to a CSV file
        csvTool.writeCSV(relation, outputPath, ',', '"',"None");

        // Read the output CSV file and check its content
        List<String> lines = Files.readAllLines(Paths.get(outputPath));

        assertEquals(4, lines.size());  // 1 line for header + 3 data lines
        assertEquals("id,name,age", lines.get(0)); // Check the header
        assertEquals("1,Anne,24", lines.get(1));  // Check data rows
        assertEquals("2,,30", lines.get(2));
        assertEquals("3,\"Jonathan,Leon\",22", lines.get(3));
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
        CSVTool csvTool = new CSVTool();

        // Write the relation to a CSV file
        csvTool.writeCSV(relation, outputPath, ',', '"', "None");

        // Read the output CSV file and check its content
        List<String> lines = Files.readAllLines(Paths.get(outputPath));

        assertEquals(3, lines.size());  // 3 data lines, no header
        assertEquals("1,Anne,24,1", lines.get(0));  // Check data rows
        assertEquals("2,,30,2", lines.get(1));
        assertEquals("3,Jonathan,22,3", lines.get(2));
    }

    @Test
    public void testWriteCSV_emptyDataset() throws IOException {
        // Prepare an empty relation
        Map<Integer, Attribute> schema = new HashMap<>();
        Map<Integer, List<String>> data = new HashMap<>();
        Relation relation = new Relation(schema, data);
        CSVTool csvTool = new CSVTool();

        // Test for empty data
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            csvTool.writeCSV(relation, outputPath, ',', '"', "None");
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
        CSVTool csvTool = new CSVTool();

        // Write the relation to a CSV file
        csvTool.writeCSV(relation, outputPath, ',', '"',"shuffleColumns");

        // Read the output CSV file and check its content
        List<String> lines = Files.readAllLines(Paths.get(outputPath));

        System.out.println(lines.get(0));
        System.out.println(lines.get(1));
        System.out.println(lines.get(2));
        System.out.println(lines.get(3));
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
        CSVTool csvTool = new CSVTool();

        // Write the relation to a CSV file
        csvTool.writeCSV(relation, outputPath, ',', '"',"shuffleRows");

        // Read the output CSV file and check its content
        List<String> lines = Files.readAllLines(Paths.get(outputPath));

        System.out.println(lines.get(0));
        System.out.println(lines.get(1));
        System.out.println(lines.get(2));
        System.out.println(lines.get(3));
        assertEquals(4, lines.size());  // 1 line for header + 3 data lines
    }

    @AfterEach
    public void cleanUp() throws IOException {
        // delete files after tests
        Files.deleteIfExists(Paths.get(outputPath));
    }
}