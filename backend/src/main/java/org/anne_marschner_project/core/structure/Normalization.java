package org.anne_marschner_project.core.structure;

import de.metanome.algorithm_integration.AlgorithmExecutionException;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingFileInput;
import de.metanome.algorithm_integration.input.RelationalInputGenerator;
import de.metanome.algorithm_integration.results.Result;
import de.metanome.algorithms.normalize.Normi;
import de.metanome.backend.input.file.DefaultFileInputGenerator;
import de.metanome.backend.result_receiver.ResultCache;
import org.anne_marschner_project.core.csvTool.CSVTool;
import org.anne_marschner_project.core.data.Attribute;
import org.anne_marschner_project.core.data.Relation;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * The Normalization class provides functionality to normalize database relations to
 * Boyce-Codd Normal Form (BCNF). It uses the Normalize algorithm to identify the BCNF.
 */
public class Normalization {

    /**
     * Transforms a given {@link Relation} to BCNF by normalizing its data.
     *
     * @param relation        The original {@link Relation} to normalize.
     * @param separator       The column separator for the CSV file.
     * @return A list of {@link Relation} objects in BCNF.
     * @throws IOException                   If an I/O error occurs during file operations.
     * @throws AlgorithmExecutionException   If an error occurs in the Normalize algorithm execution.
     */
    public List<Relation> transformToBCNF(Relation relation, String separator) throws IOException, AlgorithmExecutionException {

        // create temporary csv file
        Path tempFilepath = Files.createTempFile("temp", ".csv");
        tempFilepath.toFile().deleteOnExit();

        // Write Data of Relation that should be normalized into CSV as Normalize needs CSV as Input
        new CSVTool().writeCSV(relation,String.valueOf(tempFilepath), false, separator,"No Change");

        // Apply Normalization to receive information about which indices should form which new relation
        List<IndexSummary> indicesBCNF = normalize(String.valueOf(tempFilepath), separator.charAt(0));

        // Translate indices of BCNF Relations into column indices of original relation
        List<IndexSummary> indicesRelations = getRelationIndices(indicesBCNF, relation);

        // Create the new relations and return them
        return createBCNFDatasets(relation, indicesRelations);
    }


    /**
     * Configures and executes the Normalize algorithm to identify BCNF-compliant groupings of columns.
     *
     * @param inputFilepath The file path of the input CSV file.
     * @param separator     The column separator for the CSV file.
     * @return A list of {@link IndexSummary} objects containing the indices of columns in each new BCNF relation.
     * @throws AlgorithmExecutionException If an error occurs during Normalize execution.
     * @throws FileNotFoundException       If the specified input file is not found.
     */
    public List<IndexSummary> normalize(String inputFilepath, char separator) throws AlgorithmExecutionException, FileNotFoundException {

        // Configuration for Normalize
        CustomNormiConfig conf = new CustomNormiConfig(inputFilepath, separator);

        Normi normi = new Normi();

        RelationalInputGenerator relationalInputGenerator = null;
        ResultCache resultReceiver = new ResultCache("MetanomeMock", null);

        relationalInputGenerator = new DefaultFileInputGenerator(new ConfigurationSettingFileInput(
                conf.inputFilepath, true,
                conf.inputFileSeparator, conf.inputFileQuotechar, conf.inputFileEscape, conf.inputFileStrictQuotes,
                conf.inputFileIgnoreLeadingWhiteSpace, conf.inputFileSkipLines, conf.inputFileHasHeader, conf.inputFileSkipDifferingLines, conf.inputFileNullString));

        normi.setRelationalInputConfigurationValue(Normi.Identifier.INPUT_GENERATOR.name(), relationalInputGenerator);
        normi.setResultReceiver(resultReceiver);
        normi.setIsHumanInTheLoop(conf.isHumanInTheLoop);

        normi.execute();

        // Get the result from Normalize
        List<Result> results = resultReceiver.fetchNewResults();
        return getBCNFIndices(results);
    }


    /**
     * Extracts column indices for BCNF relations from Normalize results.
     *
     * @param results The list of {@link Result} objects from Normalize.
     * @return A list of {@link IndexSummary} objects containing the indices for each BCNF relation.
     */
    public List<IndexSummary> getBCNFIndices(List<Result> results) {

        // Patterns to extract information
        Pattern bracketPattern = Pattern.compile("\\[(.*?)\\]");
        Pattern columnPattern = Pattern.compile("column(\\d+)");
        Pattern foreignKeyPattern = Pattern.compile("ForeignKey\\s+-\\s+(.*?);");
        Pattern primaryKeyPattern = Pattern.compile("PrimaryKey\\s+-\\s+(.*?)$");

        // Lists to save the column indices and key indices of each new Relation
        List<IndexSummary> indicesOfAllRelations = new ArrayList<>();

        for (Result result : results) {
            System.out.println(result.toString());
            // add new List for relation
            List<Integer> indicesOfRelation = new ArrayList<>();
            List<Integer> indicesOfForeignKeys = new ArrayList<>();
            List<Integer> indicesOfKeys = new ArrayList<>();

            // Extract indices from the part inside brackets
            Matcher bracketMatcher = bracketPattern.matcher(result.toString());
            if (bracketMatcher.find()) {
                // Get content of inner bracket
                String insideBrackets = bracketMatcher.group(1);
                Matcher columnMatcher = columnPattern.matcher(insideBrackets);

                // Get all indices and save as integer
                while (columnMatcher.find()) {
                    int number = Integer.parseInt(columnMatcher.group(1));
                    indicesOfRelation.add(number);
                }
            }
            // Extract indices from the ForeignKey section
            Matcher foreignKeyMatcher = foreignKeyPattern.matcher(result.toString());
            if (foreignKeyMatcher.find()) {
                // Get content after "ForeignKey - " and before ";"
                String foreignKeySection = foreignKeyMatcher.group(1);
                Matcher columnMatcher = columnPattern.matcher(foreignKeySection);

                // Get all indices and save as integer
                while (columnMatcher.find()) {
                    int number = Integer.parseInt(columnMatcher.group(1));
                    indicesOfForeignKeys.add(number);
                }
            }

            // Extract indices from the PrimaryKey section
            Matcher primaryKeyMatcher = primaryKeyPattern.matcher(result.toString());
            if (primaryKeyMatcher.find()) {
                // Get content after "PrimaryKey - "
                String primaryKeySection = primaryKeyMatcher.group(1);
                Matcher columnMatcher = columnPattern.matcher(primaryKeySection);

                // Get all indices and save as integer
                while (columnMatcher.find()) {
                    int number = Integer.parseInt(columnMatcher.group(1));
                    indicesOfKeys.add(number);
                }
            }

            indicesOfAllRelations.add(new IndexSummary(indicesOfRelation, indicesOfKeys, indicesOfForeignKeys));
        }
        return indicesOfAllRelations;
    }


    /**
     * Maps BCNF column indices back to their original relation indices.
     *
     * @param indicesBCNF The list of {@link IndexSummary} objects with BCNF indices.
     * @param relation    The original {@link Relation} object.
     * @return A list of {@link IndexSummary} objects with column indices for each new relation.
     */
    public List<IndexSummary> getRelationIndices(List<IndexSummary> indicesBCNF, Relation relation) {

        // Create List for the indices of the Columns in the original relation
        List<Integer> originalIndices = new ArrayList<>(relation.getSchema().keySet());
        Collections.sort(originalIndices);
        List<IndexSummary> indicesRelations = new ArrayList<>();

        // Create Map that connects BCNF indices with the real column indices of the original relation
        Map<Integer, Integer> translation = new HashMap<>();
        for (int i = 0; i < originalIndices.size(); i++) {
            translation.put(i + 1, originalIndices.get(i)); // +1 since BCNF indices start at 1 not at 0
        }

        // Loop all BCNF Relations and get the matching column indices from the original relation
        for (IndexSummary indexSummary: indicesBCNF) {
            List<Integer> columnIndices = new ArrayList<>();
            List<Integer> keyIndices = new ArrayList<>();
            List<Integer> foreignKeyIndices = new ArrayList<>();

            // Get the matching indices of all columns in relation
            for (Integer index: indexSummary.getColumnIndices()) {
                columnIndices.add(translation.get(index));
            }

            // Get the matching indices of the keys in relation
            for (Integer index: indexSummary.getKeyIndices()) {
                keyIndices.add(translation.get(index));
            }

            // Get the matching indices of the foreign keys in relation
            for (Integer index: indexSummary.getForeignKeys()) {
                foreignKeyIndices.add(translation.get(index));
            }

            // Add the found indices into new List
            indicesRelations.add(new IndexSummary(columnIndices, keyIndices, foreignKeyIndices));
        }
        return indicesRelations;
    }


    /**
     * Creates a list of new {@link Relation} objects for each BCNF-compliant grouping of attributes.
     *
     * @param relation      The original {@link Relation} to be normalized.
     * @param indicesRelations   A list of {@link IndexSummary} objects containing indices for each new BCNF relation.
     * @return A list of {@link Relation} objects in BCNF.
     */
    public List<Relation> createBCNFDatasets(Relation relation, List<IndexSummary> indicesRelations) {

        // Create List to save new relations
        List<Relation> relations = new ArrayList<>();

        // create new relations for each new relation that Normalize found
        for (IndexSummary indicesOfRelation: indicesRelations) {

            // Create new data and schema
            Map<Integer, Attribute> schema = new HashMap<>();
            Map<Integer, List<String>> data = new HashMap<>();

            // Add Attribute and Column at each index of relation
            for (Integer index: indicesOfRelation.getColumnIndices()) {
                schema.put(index, new Attribute(relation.getSchema().get(index).getColumnName(), relation.getSchema().get(index).getDataType()));
                data.put(index, new ArrayList<>(relation.getData().get(index)));
            }

            // Get key Indices of new Relation
            List<Integer> keyIndices = indicesOfRelation.getKeyIndices();

            // Get foreign key indices of new Relation
            List<Integer> foreignKeyIndices = indicesOfRelation.getForeignKeys();

            // Get remaining overlapping columns
            List<Integer> overlappingColumnIndices = null;
            if (relation.getOverlappingColumnsIndices() != null) {
                overlappingColumnIndices = new ArrayList<>(relation.getOverlappingColumnsIndices());
                overlappingColumnIndices.retainAll(indicesOfRelation.getColumnIndices());
            }
            relations.add(new Relation(schema, data, keyIndices, foreignKeyIndices, overlappingColumnIndices, relation.getNumOfOverlappingRows()));
        }

        // Clean each new relation/ relation (remove redundant Records)
        List<Relation> cleanedRelations = new ArrayList<>();
        for (Relation relationToClean : relations) {
            cleanedRelations.add(removeDuplicateRows(relationToClean));
        }

        return cleanedRelations;
    }


    /**
     * Removes duplicate rows from a given {@link Relation} object, keeping only unique rows.
     *
     * @param relation The {@link Relation} object from which duplicate rows are removed.
     * @return A new {@link Relation} object with unique rows only.
     */
    public Relation removeDuplicateRows(Relation relation) {

        // Determine the number of rows
        int numRows = relation.getData().values().iterator().next().size();

        // Set to store unique rows as strings for quick comparison
        Set<String> uniqueRows = new HashSet<>();
        List<Integer> rowsToKeep = new ArrayList<>();

        // Counter for remaining overlapping rows
        Integer remainingOverlappingRows = (relation.getNumOfOverlappingRows() != null) ? 0 : null;

        // Iterate over each row
        for (int rowIndex = 0; rowIndex < numRows; rowIndex++) {
            // Build a unique representation of the row as a string
            StringBuilder rowRepresentation = new StringBuilder();
            for (List<String> column : relation.getData().values()) {
                rowRepresentation.append(column.get(rowIndex)).append(",");
            }
            String rowKey = rowRepresentation.toString();

            // If this row representation is not in uniqueRows, it is unique
            if (uniqueRows.add(rowKey)) { // add returns false if element is already present
                rowsToKeep.add(rowIndex); // Keep this row

                // Check if the row is within the overlapping rows range
                if (remainingOverlappingRows != null && rowIndex < relation.getNumOfOverlappingRows()) {
                    remainingOverlappingRows++;
                }
            }
        }

        // Create new data for the resulting Relation without duplicate rows
        Map<Integer, List<String>> newData = new HashMap<>();
        for (Map.Entry<Integer, List<String>> entry : relation.getData().entrySet()) {
            Integer columnIndex = entry.getKey();
            List<String> originalColumnValues = entry.getValue();
            List<String> filteredColumnValues = new ArrayList<>();

            // Only add rows that are in rowsToKeep
            for (Integer rowIndex : rowsToKeep) {
                filteredColumnValues.add(originalColumnValues.get(rowIndex));
            }
            newData.put(columnIndex, filteredColumnValues);
        }

        return new Relation(relation.getSchema(), newData, relation.getKeyIndices(), relation.getForeignKeyIndices() ,relation.getOverlappingColumnsIndices(), remainingOverlappingRows);
    }
}