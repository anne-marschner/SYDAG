package org.anne_marschner_project.core.csvTool;

import org.anne_marschner_project.core.data.Attribute;
import org.anne_marschner_project.core.data.Dataset;
import org.anne_marschner_project.core.data.Relation;
import org.anne_marschner_project.core.data.Type;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * The CSVTool class provides methods to read and write CSV files.
 * It allows reading CSV files with optional headers and writing them with
 * various shuffling options for columns and rows.
 */
public class CSVTool {


    /**
     * Reads a CSV file and converts it into a Relation object.
     *
     * @param filePath  the path to the CSV file to be read
     * @param hasHeader whether the CSV file contains a header row
     * @param separator the character used to separate values in the CSV file
     * @return a Relation object containing the data and schema from the CSV file
     * @throws IOException if an I/O error occurs or if the file is empty
     */
    public Relation readCSVColumns(String filePath, boolean hasHeader, String separator) throws IOException {

        Map<Integer, Attribute> schema = new HashMap<>();
        Map<Integer, List<String>> data = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String firstLine = reader.readLine();
            if (firstLine == null) {
                throw new IOException("Empty File");
            }

            String[] headers = null;
            int numberOfColumns;

            if (hasHeader) {
                // Read the header line
                headers = firstLine.split(separator);
                numberOfColumns = headers.length;
            } else {
                // Treat the first line as data if no header is present
                numberOfColumns = firstLine.split(separator).length;
            }

            // Initialize empty list for each column
            for (int i = 0; i < numberOfColumns; i++) {
                data.put(i, new ArrayList<>());
            }

            // If no header, we need to process the first line as data
            if (!hasHeader) {
                String[] values = firstLine.split(separator);
                for (int i = 0; i < values.length; i++) {
                    data.get(i).add(values[i]);
                }
            }

            // Read data rows
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(separator);
                for (int i = 0; i < values.length; i++) {
                    data.get(i).add(values[i]); // Save value of the row
                }
            }

            // Determine the type of each attribute
            for (int i = 0; i < numberOfColumns; i++) {
                String columnName = hasHeader ? headers[i] : null;
                Type columnType = Type.determineType(data.get(i));
                schema.put(i, new Attribute(columnName, columnType));
            }
        }
        return new Relation(schema, data);
    }


    public Relation readCSVColumnsAPI(MultipartFile file, boolean hasHeader, String separator) throws IOException {

        if (file == null || file.isEmpty()) {
            throw new IOException("Empty File");
        }

        Map<Integer, Attribute> schema = new HashMap<>();
        Map<Integer, List<String>> data = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String firstLine = reader.readLine();
            if (firstLine == null) {
                throw new IOException("Empty File");
            }

            String[] headers = null;
            int numberOfColumns;

            if (hasHeader) {
                // Read the header line
                headers = firstLine.split(separator);
                numberOfColumns = headers.length;
            } else {
                // Treat the first line as data if no header is present
                numberOfColumns = firstLine.split(separator).length;
            }

            // Initialize empty list for each column
            for (int i = 0; i < numberOfColumns; i++) {
                data.put(i, new ArrayList<>());
            }

            // If no header, process the first line as data
            if (!hasHeader) {
                String[] values = firstLine.split(separator);
                for (int i = 0; i < values.length; i++) {
                    data.get(i).add(values[i]);
                }
            }

            // Read data rows
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(separator);
                for (int i = 0; i < numberOfColumns; i++) {
                    if (i < values.length) {
                        data.get(i).add(values[i]);
                    } else {
                        data.get(i).add(""); // Handle missing values by adding an empty string
                    }
                }
            }

            // Determine the type of each attribute
            for (int i = 0; i < numberOfColumns; i++) {
                String columnName = hasHeader ? headers[i] : null;
                Type columnType = Type.determineType(data.get(i));
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
     * @param hasHeaders  whether to write headers in the CSV file
     * @param separator   the character used to separate values in the CSV file
     * @param shuffleType the type of shuffling to apply ("Shuffle Columns", "Shuffle Rows", or none)
     * @return A List of the column indices in correct order
     * @throws IOException if an I/O error occurs
     * @throws IllegalArgumentException if the relation, schema, or data is null or empty
     */
    public List<Integer> writeCSV(Relation relation, String filePath, boolean hasHeaders, String separator, String shuffleType) throws IOException {

        // Check that Relation cannot be null in order to write it in CSV
        if (relation == null || relation.getData() == null || relation.getSchema() == null
                || relation.getData().isEmpty() || relation.getSchema().isEmpty()) {
            throw new IllegalArgumentException("Relation, schema, or data cannot be null.");
        }

        // Choose shuffle Type and apply it while writing
        return switch (shuffleType) {
            case "Shuffle Columns" -> writeShuffledColumns(relation, filePath, hasHeaders, separator);
            case "Shuffle Rows" -> writeShuffledRows(relation, filePath, hasHeaders, separator);
            default -> writeInOrder(relation, filePath, hasHeaders, separator);
        };
    }


    /**
     * Writes a CSV file with columns shuffled.
     *
     * @param relation   the Relation object containing data and schema to be written
     * @param filePath   the path to the CSV file to be created
     * @param hasHeaders whether to write headers in the CSV file
     * @param separator  the character used to separate values in the CSV file
     * @return A List of the column indices in correct order
     * @throws IOException if an I/O error occurs
     */
    public List<Integer> writeShuffledColumns(Relation relation, String filePath, boolean hasHeaders, String separator) throws IOException {

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
            if (hasHeaders) {
                for (int listIndex = 0; listIndex < columnIndices.size(); listIndex++) {
                    int colIndex = columnIndices.get(listIndex);
                    Attribute attribute = schema.get(colIndex);
                    String columnName = attribute.getColumnName();
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
     * @param hasHeaders whether to write headers in the CSV file
     * @param separator  the character used to separate values in the CSV file
     * @return A List of the column indices in correct order
     * @throws IOException if an I/O error occurs
     */
    public List<Integer> writeShuffledRows(Relation relation, String filePath, boolean hasHeaders, String separator) throws IOException {

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
            if (hasHeaders) {
                for (int listIndex = 0; listIndex < columnIndices.size(); listIndex++) {
                    int colIndex = columnIndices.get(listIndex);
                    Attribute attribute = schema.get(colIndex);
                    String columnName = attribute.getColumnName();
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
     * @param hasHeaders whether to write headers in the CSV file
     * @param separator  the character used to separate values in the CSV file
     * @return A List of the column indices in correct order
     * @throws IOException if an I/O error occurs
     */
    public List<Integer> writeInOrder(Relation relation, String filePath, boolean hasHeaders, String separator) throws IOException {

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
            if (hasHeaders) {
                for (int listIndex = 0; listIndex < columnIndices.size(); listIndex++) {
                    int colIndex = columnIndices.get(listIndex);
                    Attribute attribute = schema.get(colIndex);
                    String columnName = attribute.getColumnName();
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
    public void writeKeyFile(Dataset dataset, List<List<Integer>> columnOrder, String filepath) {

        // Write Information of Relations in a File
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath))) {
            for (int j = 1; j <= dataset.getRelations().size(); j++) {

                // Write Index of Relation:
                writer.write("Relation " + j + ": \n");
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
            }
        } catch (IOException e) {
            System.err.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }
}
