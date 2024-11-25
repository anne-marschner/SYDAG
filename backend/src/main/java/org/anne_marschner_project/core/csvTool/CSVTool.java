package org.anne_marschner_project.core.csvTool;

import org.anne_marschner_project.core.data.Attribute;
import org.anne_marschner_project.core.data.Dataset;
import org.anne_marschner_project.core.data.Relation;
import org.anne_marschner_project.core.data.Type;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * The CSVTool class provides methods to read and write CSV files.
 * It allows reading CSV files with optional headers and writing them with
 * various shuffling options for columns and rows.
 */
public class CSVTool {

    private Map<Integer, List<List<Integer>>> columnOrders = new HashMap<>();

    /**
     * Reads a CSV file and converts it into a Relation object.
     *
     * @param file  the CSV file to be read
     * @param hasHeader whether the CSV file contains a header row
     * @param separator the character used to separate values in the CSV file
     * @param quoteChar the character used for quoted values in the CSV file
     * @param escapeChar the escape character used in the CSV file
     * @return a Relation object containing the data and schema from the CSV file
     * @throws IOException if an I/O error occurs or if the file is empty
     */
    public Relation readCSVColumns(MultipartFile file, boolean hasHeader, char separator, char quoteChar, char escapeChar) throws IOException {

        // Check if the file is empty
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            if (reader.readLine() == null) {
                throw new IOException("The CSV file is empty.");
            }
        }

        Map<Integer, Attribute> schema = new HashMap<>();
        Map<Integer, List<String>> data = new HashMap<>();

        // Set up CSV format with custom separator, quote character, escape character, and if leading white space is ignored
        CSVFormat csvFormat = CSVFormat.DEFAULT
                .withDelimiter(separator)
                .withQuote(quoteChar)
                .withEscape(escapeChar)
                .withIgnoreEmptyLines(true)
                .withIgnoreSurroundingSpaces(true);

        // If there is a header, parse with headers
        if (hasHeader) {
            csvFormat = csvFormat.withFirstRecordAsHeader();
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
             CSVParser csvParser = csvFormat.parse(reader)) {

            // Determine the number of columns and headers if present
            int numberOfColumns;
            String[] headers = null;

            if (hasHeader) {
                numberOfColumns = csvParser.getHeaderMap().size();
                headers = csvParser.getHeaderNames().toArray(new String[0]);
            } else {
                // Get first record to determine number of columns when there's no header
                CSVRecord firstRecord = csvParser.iterator().next();
                numberOfColumns = firstRecord.size();

                // Initialize data with values from the first record
                for (int i = 0; i < numberOfColumns; i++) {
                    data.put(i, new ArrayList<>());
                    data.get(i).add(firstRecord.get(i)); // Add first record's values directly to data
                }
            }

            // Ensure all columns are initialized
            for (int i = 0; i < numberOfColumns; i++) {
                data.putIfAbsent(i, new ArrayList<>());
            }

            // Read remaining records and populate data
            for (CSVRecord record : csvParser) {
                for (int i = 0; i < numberOfColumns; i++) {
                    data.get(i).add(record.size() > i ? record.get(i) : ""); // Handle missing values with empty strings
                }
            }

            // Determine the type of each attribute and add to schema
            for (int i = 0; i < numberOfColumns; i++) {
                String columnName = hasHeader ? headers[i] : null;
                Type columnType = Type.determineType(data.get(i)); // Assuming this method checks data types
                schema.put(i, new Attribute(columnName, columnType));
            }
        }

        return new Relation(schema, data);
    }


    /**
     * Writes a Relation object to a CSV file with optional column or row shuffling.
     *
     * @param relation    the Relation object containing data and schema to be written
     * @param filePath    the path to the CSV file to be created
     * @param separator   the character used to separate values in the CSV file
     * @param shuffleType the type of shuffling to apply ("Shuffle Columns", "Shuffle Rows", or none)
     * @return A List of the column indices in correct order
     * @throws IOException if an I/O error occurs
     * @throws IllegalArgumentException if the relation, schema, or data is null or empty
     */
    public List<Integer> writeCSV(Relation relation, String filePath, char separator, char quoteChar, String shuffleType) throws IOException {

        // Check that Relation cannot be null in order to write it in CSV
        if (relation == null || relation.getData() == null || relation.getSchema() == null
                || relation.getData().isEmpty() || relation.getSchema().isEmpty()) {
            throw new IllegalArgumentException("Relation, schema, or data cannot be null.");
        }

        // Choose shuffle Type and apply it while writing
        return switch (shuffleType) {
            case "Shuffle Columns" -> writeShuffledColumns(relation, filePath, separator, quoteChar);
            case "Shuffle Rows" -> writeShuffledRows(relation, filePath, separator, quoteChar);
            default -> writeInOrder(relation, filePath, separator, quoteChar);
        };
    }


    /**
     * Writes a CSV file with columns shuffled.
     *
     * @param relation   the Relation object containing data and schema to be written
     * @param filePath   the path to the CSV file to be created
     * @param separator  the character used to separate values in the CSV file
     * @param quoteChar  the character used to quote values in the CSV file
     * @return A List of the column indices in correct order
     * @throws IOException if an I/O error occurs
     */
    public List<Integer> writeShuffledColumns(Relation relation, String filePath, char separator, char quoteChar) throws IOException {

        // Get schema and data from Relation
        Map<Integer, Attribute> schema = relation.getSchema();
        Map<Integer, List<String>> data = relation.getData();

        // Get the number of rows from first column in the relation (assuming all columns have the same row count)
        int numberOfRows = data.values().iterator().next().size();

        // Collect the column indices from the data map and shuffle them
        List<Integer> columnIndices = new ArrayList<>(data.keySet());
        Collections.shuffle(columnIndices);
        System.out.println(columnIndices);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {

            // If Header exists write them in the first row of the CSV
            if (relation.getSchema().entrySet().iterator().next().getValue().getColumnName() != null) {
                for (int listIndex = 0; listIndex < columnIndices.size(); listIndex++) {
                    int colIndex = columnIndices.get(listIndex);
                    Attribute attribute = schema.get(colIndex);
                    String columnName = attribute.getColumnName();
                    if (columnName != null && columnName.indexOf(separator) >= 0) {
                        columnName = quoteChar + columnName + quoteChar;
                    }
                    writer.write(columnName != null ? columnName : "");
                    if (listIndex < columnIndices.size() - 1) {
                        writer.write(separator); // divide columns
                    }
                }
                writer.newLine(); // New line after header row
            }

            // Write data in CSV File
            for (int rowIndex = 0; rowIndex < numberOfRows; rowIndex++) {
                for (int listIndex = 0; listIndex < columnIndices.size(); listIndex++) {
                    int colIndex = columnIndices.get(listIndex);
                    String value = data.get(colIndex).get(rowIndex);
                    if (value != null && value.indexOf(separator) >= 0) {
                        value = quoteChar + value + quoteChar;
                    }
                    writer.write(value != null ? value : "");
                    if (listIndex < columnIndices.size() - 1) {
                        writer.write(separator); // divide columns
                    }
                }
                writer.newLine(); // new line after each row
            }
        }
        return columnIndices;
    }


    /**
     * Writes a CSV file with rows shuffled.
     *
     * @param relation   the Relation object containing data and schema to be written
     * @param filePath   the path to the CSV file to be created
     * @param separator  the character used to separate values in the CSV file
     * @param quoteChar  the character used to quote values in the CSV file
     * @return A List of the column indices in correct order
     * @throws IOException if an I/O error occurs
     */
    public List<Integer> writeShuffledRows(Relation relation, String filePath, char separator, char quoteChar) throws IOException {

        // Get schema and data from Relation
        Map<Integer, Attribute> schema = relation.getSchema();
        Map<Integer, List<String>> data = relation.getData();

        // Get the number of rows from first column in the relation (assuming all columns have the same row count)
        int numberOfRows = data.values().iterator().next().size();

        // Collect row indices and shuffle them
        List<Integer> rowIndices = IntStream.range(0, numberOfRows).boxed().collect(Collectors.toList());
        Collections.shuffle(rowIndices);

        // Collect the column indices
        List<Integer> columnIndices = new ArrayList<>(data.keySet());
        System.out.println(columnIndices);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {

            // If Header exists write them in the first row of the CSV
            if (relation.getSchema().entrySet().iterator().next().getValue().getColumnName() != null) {
                for (int listIndex = 0; listIndex < columnIndices.size(); listIndex++) {
                    int colIndex = columnIndices.get(listIndex);
                    Attribute attribute = schema.get(colIndex);
                    String columnName = attribute.getColumnName();
                    if (columnName != null && columnName.indexOf(separator) >= 0) {
                        columnName = quoteChar + columnName + quoteChar;
                    }
                    writer.write(columnName != null ? columnName : "");
                    if (listIndex < columnIndices.size() - 1) {
                        writer.write(separator); // divide columns
                    }
                }
                writer.newLine(); // New line after header row
            }

            // Write data in CSV File
            for (int rowIndex : rowIndices) {
                for (int listIndex = 0; listIndex < columnIndices.size(); listIndex++) {
                    int colIndex = columnIndices.get(listIndex);
                    String value = data.get(colIndex).get(rowIndex);
                    if (value != null && value.indexOf(separator) >= 0) {
                        value = quoteChar + value + quoteChar;
                    }
                    writer.write(value != null ? value : "");
                    if (listIndex < columnIndices.size() - 1) {
                        writer.write(separator); // divide columns
                    }
                }
                writer.newLine(); // new line after each row
            }
        }
        return columnIndices;
    }


    /**
     * Writes a CSV file in the original order of rows and columns.
     *
     * @param relation   the Relation object containing data and schema to be written
     * @param filePath   the path to the CSV file to be created
     * @param separator  the character used to separate values in the CSV file
     * @param quoteChar  the character used to quote values in the CSV file
     * @return A List of the column indices in correct order
     * @throws IOException if an I/O error occurs
     */
    public List<Integer> writeInOrder(Relation relation, String filePath, char separator, char quoteChar) throws IOException {

        // Get schema and data from Relation
        Map<Integer, Attribute> schema = relation.getSchema();
        Map<Integer, List<String>> data = relation.getData();

        // Get the number of rows from first column in the relation (assuming all columns have the same row count)
        int numberOfRows = data.values().iterator().next().size();

        // Collect the column indices from the data map
        List<Integer> columnIndices = new ArrayList<>(data.keySet());
        System.out.println(columnIndices);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {

            // If Header exists write them in the first row of the CSV
            if (relation.getSchema().entrySet().iterator().next().getValue().getColumnName() != null) {
                for (int listIndex = 0; listIndex < columnIndices.size(); listIndex++) {
                    int colIndex = columnIndices.get(listIndex);
                    Attribute attribute = schema.get(colIndex);
                    String columnName = attribute.getColumnName();
                    if (columnName != null && columnName.indexOf(separator) >= 0) {
                        columnName = quoteChar + columnName + quoteChar;
                    }
                    writer.write(columnName != null ? columnName : "");
                    if (listIndex < columnIndices.size() - 1) {
                        writer.write(separator); // divide columns
                    }
                }
                writer.newLine(); // New line after header row
            }

            // Write data in CSV File
            for (int rowIndex = 0; rowIndex < numberOfRows; rowIndex++) {
                for (int listIndex = 0; listIndex < columnIndices.size(); listIndex++) {
                    int colIndex = columnIndices.get(listIndex);
                    String value = data.get(colIndex).get(rowIndex);
                    if (value != null && value.indexOf(separator) >= 0) {
                        value = quoteChar + value + quoteChar;
                    }
                    writer.write(value != null ? value : "");
                    if (listIndex < columnIndices.size() - 1) {
                        writer.write(separator); // divide columns
                    }
                }
                writer.newLine(); // new line after each row
            }
        }
        return columnIndices;
    }


    /**
     * Writes information about BCNF relations, including foreign and primary keys, to an output file.
     *
     * @param dataset     The list of Relation objects in a dataset representing BCNF relations.
     * @param columnOrder The list of lists of the indices representing the current order of the columns.
     * @param filepath    The file path where the information will be saved.
     */
    public void writeKeyFile(Dataset dataset, List<List<Integer>> columnOrder, String identifier, String filepath) {

        // Write Information of Relations in a File
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath))) {
            for (int j = 1; j <= dataset.getRelations().size(); j++) {

                // Write Index of Relation:
                writer.write("Relation " + identifier + j + ": \n");
                Relation relation = dataset.getRelations().get(j-1);

                // Create Map to access according Column index (index summary still refers to all columns of relation)
                Map<Integer, Integer> translation = new HashMap<>();
                for(int i = 1; i <= relation.getData().keySet().size(); i++) {
                    translation.put(columnOrder.get(j - 1).get(i - 1), i);
                }

                // Write Foreign Key
                List<Integer> foreignKeys = relation.getForeignKeyIndices();
                if (!foreignKeys.isEmpty()) {
                    writer.write("Foreign Key: ");
                    for (int i = 0; i < foreignKeys.size(); i++) {
                        writer.write("Column " + translation.get(foreignKeys.get(i)));
                        if (i < foreignKeys.size() - 1) {
                            writer.write(", ");
                        }
                    }
                    writer.write(";\n");
                }

                // write Primary Key
                List<Integer> primaryKeys = relation.getKeyIndices();
                if (!primaryKeys.isEmpty()) {
                    writer.write("Primary Key: ");
                    for (int i = 0; i < primaryKeys.size(); i++) {
                        writer.write("Column " + translation.get(primaryKeys.get(i)));
                        if (i < primaryKeys.size() - 1) {
                            writer.write(", ");
                        }
                    }
                    writer.write(";\n");
                }
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }


    /**
     * Writes a mapping of input relation columns to their corresponding columns in multiple datasets to a text file.
     *
     * @param inputRelation the {@code Relation} object representing the input relation whose columns are being mapped.
     * @param datasets      a list of {@code Dataset} objects containing relations to which the input relation is mapped.
     * @param identifier    an array of unique identifiers for the datasets, used in the mapping to distinguish datasets.
     * @param filepath      the path to the file where the generated mapping will be written.
     *
     * @throws IOException if an error occurs while writing the mapping to the specified file.
     */
    public void writeMapping(Relation inputRelation, List<Dataset> datasets, String[] identifier, String filepath) {

        // Create a Map that holds the original columns as key and their Mapping as value
        Map<Integer, String> mapping = new HashMap<>();
        for (Map.Entry<Integer, Attribute> entry : inputRelation.getSchema().entrySet()) {
            int columnNumber = entry.getKey() + 1;
            Attribute attribute = entry.getValue();
            StringBuilder columnMapping = new StringBuilder("Input_column").append(columnNumber);
            if (attribute.getColumnName() != null) {
                columnMapping.append("_\"").append(attribute.getColumnName()).append("\"");
            }
            columnMapping.append(" corresponds to: \n");
            mapping.put(entry.getKey(), columnMapping.toString());
        }

        // Loop each relation in each dataset
        for (int i = 0; i < datasets.size(); i++) {
            for (int j = 0; j < datasets.get(i).getRelations().size(); j++) {

                // Get relation and its accurate column order
                List<Integer> columnOrder = this.columnOrders.get(i).get(j);
                Relation relation = datasets.get(i).getRelations().get(j);
                List<Integer> columnIndices = new ArrayList<>(relation.getSchema().keySet());

                // Create Translation to actual column number
                Map<Integer, Integer> translation = new HashMap<>();
                for (int k = 1; k <= columnIndices.size(); k++) {
                    translation.put(columnOrder.get(k - 1), k);
                }

                // Add number and name of column to Mapping
                for (int k = 0; k < columnIndices.size(); k++) {
                    Integer originalColumnIndex = columnIndices.get(k);
                    Integer newColumnIndex = translation.get(originalColumnIndex);
                    int relationNumber = j + 1;

                    // Build the addition string
                    StringBuilder addition = new StringBuilder().append(identifier[i]).append(relationNumber).append("_column").append(newColumnIndex);
                    String columnName = relation.getSchema().get(originalColumnIndex).getColumnName();
                    if (columnName != null) {
                        addition.append("_\"").append(columnName).append("\"");
                    }
                    addition.append("; ");

                    // Append the addition to the existing value in the mapping
                    mapping.put(originalColumnIndex, mapping.get(originalColumnIndex) + addition);
                }
            }
        }

        // Write Mapping to txt
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath))) {
            // Write the value of each entry in one row of a txt
            for (String value : mapping.values()) {
                writer.write(value + System.lineSeparator() + System.lineSeparator());
            }
        } catch (IOException e){
            System.err.println("Error writing Mapping to txt file: " + e.getMessage());
        }
    }

    public Map<Integer, List<List<Integer>>> getColumnOrders() {
        return columnOrders;
    }
}
