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
 * Boyce-Codd Normal Form (BCNF). It uses the Normalize algorithm.
 */
public class Normalization {

    /**
     * Transforms a given Relation to BCNF by normalizing it.
     *
     * @param relation        The original Relation to normalize.
     * @param separator       The column separator for the CSV file.
     * @param quoteChar       The quote character for the CSV file.
     * @param normalizePercentage The percentage indicating the degree of normalization to apply.
     * @return A list of Relation objects.
     * @throws IOException                   If an I/O error occurs during file operations.
     * @throws AlgorithmExecutionException   If an error occurs in the Normalize algorithm execution.
     */
    public List<Relation> transformToBCNF(Relation relation, char separator, char quoteChar, Integer normalizePercentage) throws IOException, AlgorithmExecutionException {

        // Create temporary CSV file
        Path tempFilepath = Files.createTempFile("temp", ".csv");
        tempFilepath.toFile().deleteOnExit();

        // Write data of relation that should be normalized into CSV as Normalize needs CSV as Input
        new CSVTool().writeCSV(relation,String.valueOf(tempFilepath), separator, quoteChar, "No Change");

        // Apply Normalization to receive information about which indices form BCNF-compliant relations
        List<IndexSummary> indicesBCNF = normalize(String.valueOf(tempFilepath), separator);

        // Translate indices of BCNF relations into column indices of original relation
        List<IndexSummary> indicesRelations = getRelationIndices(indicesBCNF, relation);

        // Choose the number of decomposition steps that will be performed by using the given percentage
        List<IndexSummary> indicesPickedRelations = chooseDegreeOfNormalization(indicesRelations, normalizePercentage);

        // Create the new relations and return them
        return createBCNFDatasets(relation, indicesPickedRelations);
    }


    /**
     * Configures and executes the Normalize algorithm to identify BCNF-compliant groupings of columns.
     *
     * @param inputFilepath The file path of the input CSV file.
     * @param separator     The column separator for the CSV file.
     * @return A list of IndexSummary objects containing the column indices of BCNF-compliant relations.
     * @throws AlgorithmExecutionException If an error occurs during Normalize execution.
     * @throws FileNotFoundException       If the specified input file is not found.
     */
    public List<IndexSummary> normalize(String inputFilepath, char separator) throws AlgorithmExecutionException, FileNotFoundException {

        // Configure Normalize
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

        // Execute Normalize
        normi.execute();

        // Get the result from Normalize
        List<Result> results = resultReceiver.fetchNewResults();
        return getBCNFIndices(results);
    }


    /**
     * Extracts the column indices of BCNF-compliant relations from Normalize results.
     *
     * @param results The list of Result objects from Normalize.
     * @return A list of IndexSummary objects each containing the indices of a BCNF-compliant relation.
     */
    public List<IndexSummary> getBCNFIndices(List<Result> results) {

        // Set patterns to extract information
        Pattern bracketPattern = Pattern.compile("\\[(.*?)\\]");
        Pattern columnPattern = Pattern.compile("column(\\d+)");
        Pattern foreignKeyPattern = Pattern.compile("ForeignKey\\s+-\\s+(.*?);");
        Pattern primaryKeyPattern = Pattern.compile("PrimaryKey\\s+-\\s+(.*?);");

        // Set list to save the column indices and key indices of each relation
        List<IndexSummary> indicesOfAllRelations = new ArrayList<>();

        for (Result result : results) {
            // Create lists for indices of relation
            List<Integer> indicesOfRelation = new ArrayList<>();
            List<Integer> indicesOfForeignKeys = new ArrayList<>();
            List<Integer> indicesOfKeys = new ArrayList<>();

            // Extract all indices of the relation
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

            // Extract foreign key indices of the relation
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

            // Extract primary key indices of the relation
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

            // Create IndexSummary object out of all index lists of the relation
            indicesOfAllRelations.add(new IndexSummary(indicesOfRelation, indicesOfKeys, indicesOfForeignKeys));
        }
        return indicesOfAllRelations;
    }


    /**
     * Maps BCNF column indices back to their original relation indices.
     *
     * @param indicesBCNF The list of IndexSummary objects of BCNF-compliant relations.
     * @param relation    The original Relation object.
     * @return A list of IndexSummary objects with correct column indices for each new relation.
     */
    public List<IndexSummary> getRelationIndices(List<IndexSummary> indicesBCNF, Relation relation) {

        // Create List for the indices of the columns in the original relation
        List<Integer> originalIndices = new ArrayList<>(relation.getSchema().keySet());
        Collections.sort(originalIndices);
        List<IndexSummary> indicesRelations = new ArrayList<>();

        // Create Map that connects BCNF indices with the real column indices of the original relation
        Map<Integer, Integer> translation = new HashMap<>();
        for (int i = 0; i < originalIndices.size(); i++) {
            translation.put(i + 1, originalIndices.get(i)); // +1 since BCNF indices start at 1 not at 0
        }

        // Loop all BCNF-compliant relations
        for (IndexSummary indexSummary: indicesBCNF) {
            List<Integer> columnIndices = new ArrayList<>();
            List<Integer> keyIndices = new ArrayList<>();
            List<Integer> foreignKeyIndices = new ArrayList<>();

            // Get the matching indices of all columns in the relation
            for (Integer index: indexSummary.getColumnIndices()) {
                columnIndices.add(translation.get(index));
            }

            // Get the matching indices of the keys in the relation
            for (Integer index: indexSummary.getKeyIndices()) {
                keyIndices.add(translation.get(index));
            }

            // Get the matching indices of the foreign keys in the relation
            for (Integer index: indexSummary.getForeignKeys()) {
                foreignKeyIndices.add(translation.get(index));
            }

            // Add the found indices into an IndexSummary object
            indicesRelations.add(new IndexSummary(columnIndices, keyIndices, foreignKeyIndices));
        }
        return indicesRelations;
    }


    /**
     * Selects a subset of decomposed relations up to the specified degree of normalization.
     *
     * @param indicesRelations    A list of IndexSummary objects representing the indices of all decomposed relations.
     * @param normalizePercentage The percentage indicating the degree of normalization to apply.
     * @return A list of IndexSummary objects of the chosen decomposed relations.
     */
    public  List<IndexSummary> chooseDegreeOfNormalization(List<IndexSummary> indicesRelations, Integer normalizePercentage) {

        // Find number of decomposition steps to apply by calculating the given percentage of the possible decomposition steps
        int numOfSteps = (int) Math.round((normalizePercentage / 100.0) * (indicesRelations.size() - 1));
        if (numOfSteps == indicesRelations.size() - 1) {
            return indicesRelations;
        }

        List<IndexSummary> chosenRelations = new ArrayList<>();

        // Add the first numOfSteps of IndexSummarys
        for (int i = 0; i < numOfSteps; i++) {
            chosenRelations.add(indicesRelations.get(i));
        }

        // Combine the remaining entries in a shared IndexSummary
        IndexSummary mergedSummary = indicesRelations.get(numOfSteps);
        for (int i = numOfSteps + 1; i < indicesRelations.size(); i++) {
            mergedSummary.addColumnIndices(indicesRelations.get(i).getColumnIndices());
            mergedSummary.setForeignKeyIndices(new ArrayList<>());
        }
        Collections.sort(mergedSummary.getColumnIndices());
        chosenRelations.add(mergedSummary);

        return chosenRelations;
    }


    /**
     * Creates a list of new Relation objects out of the given IndexSummary objects.
     *
     * @param relation      The original Relation to be normalized.
     * @param indicesRelations   A list of IndexSummary objects containing indices of each BCNF-compliant relation that should be created.
     * @return A list of Relation objects.
     */
    public List<Relation> createBCNFDatasets(Relation relation, List<IndexSummary> indicesRelations) {

        // Create List to save new relations
        List<Relation> relations = new ArrayList<>();

        // Create new relations out of each IndexSummary object, which describes a relation
        for (IndexSummary indicesOfRelation: indicesRelations) {

            // Create new data and schema
            Map<Integer, Attribute> schema = new HashMap<>();
            Map<Integer, List<String>> data = new HashMap<>();

            // Add attribute and column at each index of relation
            for (Integer index: indicesOfRelation.getColumnIndices()) {
                schema.put(index, new Attribute(relation.getSchema().get(index).getColumnName(), relation.getSchema().get(index).getDataType()));
                data.put(index, new ArrayList<>(relation.getData().get(index)));
            }

            // Get key Indices of new relation
            List<Integer> keyIndices = indicesOfRelation.getKeyIndices();

            // Get foreign key indices of new relation
            List<Integer> foreignKeyIndices = indicesOfRelation.getForeignKeys();

            // Get remaining overlapping columns
            List<Integer> overlappingColumnIndices = null;
            if (relation.getOverlappingColumnsIndices() != null) {
                overlappingColumnIndices = new ArrayList<>(relation.getOverlappingColumnsIndices());
                overlappingColumnIndices.retainAll(indicesOfRelation.getColumnIndices());
            }
            Relation createdRelation = new Relation(schema, data, keyIndices, foreignKeyIndices, overlappingColumnIndices, relation.getNumOfOverlappingRows());
            createdRelation.setKeysBeforeNormalization(relation.getKeyIndices());
            relations.add(createdRelation);
        }

        // Clean each new relation (remove redundant records)
        List<Relation> cleanedRelations = new ArrayList<>();
        for (Relation relationToClean : relations) {
            cleanedRelations.add(removeDuplicateRows(relationToClean));
        }

        return cleanedRelations;
    }


    /**
     * Removes duplicate rows from a given Relation object, keeping only unique rows.
     *
     * @param relation The Relation object from which duplicate rows are removed.
     * @return A new Relation object with unique rows only.
     */
    public Relation removeDuplicateRows(Relation relation) {

        // Determine the number of rows
        int numRows = relation.getData().values().iterator().next().size();

        // Create Set to store unique rows as strings
        Set<String> uniqueRows = new HashSet<>();
        List<Integer> rowsToKeep = new ArrayList<>();

        // Set counter for remaining overlapping rows
        Integer remainingOverlappingRows = (relation.getNumOfOverlappingRows() != null) ? 0 : null;

        // Iterate over each row
        for (int rowIndex = 0; rowIndex < numRows; rowIndex++) {
            // Build a unique representation of the row as a string
            StringBuilder rowRepresentation = new StringBuilder();
            for (List<String> column : relation.getData().values()) {
                rowRepresentation.append(column.get(rowIndex)).append(",");
            }
            String rowKey = rowRepresentation.toString();

            // If the row representation is not in uniqueRows, it is unique
            if (uniqueRows.add(rowKey)) {
                rowsToKeep.add(rowIndex); // Keep this row

                // Check if the row is within the overlapping rows range
                if (remainingOverlappingRows != null && rowIndex < relation.getNumOfOverlappingRows()) {
                    remainingOverlappingRows++;
                }
            }
        }

        // Create new data for the resulting relation without duplicate rows
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

        Relation cleanedRelation = new Relation(relation.getSchema(), newData, relation.getKeyIndices(), relation.getForeignKeyIndices() ,relation.getOverlappingColumnsIndices(), remainingOverlappingRows);
        cleanedRelation.setKeysBeforeNormalization(relation.getKeysBeforeNormalization());
        return cleanedRelation;
    }
}