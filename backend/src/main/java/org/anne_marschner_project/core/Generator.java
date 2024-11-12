package org.anne_marschner_project.core;

import de.metanome.algorithm_integration.AlgorithmExecutionException;
import org.anne_marschner_project.core.csvTool.CSVTool;
import org.anne_marschner_project.core.data.Dataset;
import org.anne_marschner_project.core.data.Relation;
import org.anne_marschner_project.core.keys.KeyFinder;
import org.anne_marschner_project.core.noise.DataNoise;
import org.anne_marschner_project.core.noise.SchemaNoise;
import org.anne_marschner_project.core.split.Split;
import org.anne_marschner_project.core.structure.Join;
import org.anne_marschner_project.core.structure.Normalization;
import org.anne_marschner_project.api.FormDataWrapper;
import org.anne_marschner_project.api.GeneratorParameters;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

/**
 * The Generator class combines various processes, such as reading, splitting,
 * normalizing, and adding noise to data, to generate datasets that simulate real-world scenarios
 * with schema and data perturbations. The class also handles writing the final datasets to CSV files.
 */
@Service
public class Generator {

    /**
     * Executes the entire data generation process using the parameters provided.
     *
     * @param params The parameters encapsulated in a GeneratorParameters object.
     */
    public void execute(GeneratorParameters params) {

        // Extract parameters from GeneratorParameters
        MultipartFile csvFile = params.getCsvFile();
        FormDataWrapper formDataWrapper = params.getFormDataWrapper();

        // Extract fields from FormDataWrapper
        boolean hasHeaders = formDataWrapper.getHasHeaders();
        String separator = formDataWrapper.getSeparator();
        String splitType = formDataWrapper.getSplitType();
        Integer columnOverlapPercentage = formDataWrapper.getColumnOverlapPercentage();
        Integer rowOverlapPercentage = formDataWrapper.getRowOverlapPercentage();
        String dataset1StructureType = formDataWrapper.getDataset1StructureType();
        String dataset2StructureType = formDataWrapper.getDataset2StructureType();
        boolean dataset1SchemaNoise = formDataWrapper.getDataset1SchemaNoise();
        boolean dataset2SchemaNoise = formDataWrapper.getDataset2SchemaNoise();
        Integer dataset1SchemaNoiseValue = formDataWrapper.getDataset1SchemaNoiseValue();
        Integer dataset2SchemaNoiseValue = formDataWrapper.getDataset2SchemaNoiseValue();
        boolean dataset1SchemaKeyNoise = formDataWrapper.getDataset1SchemaKeyNoise();
        boolean dataset2SchemaKeyNoise = formDataWrapper.getDataset2SchemaKeyNoise();
        List<String> selectedSchemaMethods1 = formDataWrapper.getSelectedSchemaMethods1();
        List<String> selectedSchemaMethods2 = formDataWrapper.getSelectedSchemaMethods2();
        boolean dataset1DataNoise = formDataWrapper.getDataset1DataNoise();
        boolean dataset2DataNoise = formDataWrapper.getDataset2DataNoise();
        Integer dataset1DataNoiseValue = formDataWrapper.getDataset1DataNoiseValue();
        Integer dataset2DataNoiseValue = formDataWrapper.getDataset2DataNoiseValue();
        boolean dataset1DataKeyNoise = formDataWrapper.getDataset1DataKeyNoise();
        boolean dataset2DataKeyNoise = formDataWrapper.getDataset2DataKeyNoise();
        List<String> selectedStringMethods1 = formDataWrapper.getSelectedStringMethods1();
        List<String> selectedStringMethods2 = formDataWrapper.getSelectedStringMethods2();
        List<String> selectedNumericMethods1 = formDataWrapper.getSelectedNumericMethods1();
        List<String> selectedNumericMethods2 = formDataWrapper.getSelectedNumericMethods2();
        String dataset1ShuffleOption = formDataWrapper.getDataset1ShuffleOption();
        String dataset2ShuffleOption = formDataWrapper.getDataset2ShuffleOption();
        String filepathOutput = formDataWrapper.getFilepathOutput();

        // Begin processing
        // Read information from file into relation
        Relation inputRelation = readInput(csvFile, hasHeaders, separator);

        // Set key indices for the input relation
        setKeyIndices(inputRelation);

        // Split Relation according to selected direction
        List<Relation> splitDataset = splitRelation(inputRelation, columnOverlapPercentage, rowOverlapPercentage, splitType);
        Relation leftRelation = splitDataset.get(0);
        Relation rightRelation = splitDataset.get(1);

        // Apply Normalization or other structure modifications
        Dataset leftDataset = applyNormalization(leftRelation, dataset1StructureType, separator);
        Dataset rightDataset = applyNormalization(rightRelation, dataset2StructureType, separator);

        // Add Schema Noise to the datasets
        leftDataset = addSchemaNoise(leftDataset, hasHeaders, selectedSchemaMethods1, dataset1SchemaNoiseValue, dataset1SchemaKeyNoise, dataset1SchemaNoise);
        rightDataset = addSchemaNoise(rightDataset, hasHeaders, selectedSchemaMethods2, dataset2SchemaNoiseValue, dataset2SchemaKeyNoise, dataset2SchemaNoise);

        // Add Data Noise to datasets
        leftDataset = addDataNoise(leftDataset, selectedStringMethods1, selectedNumericMethods1, dataset1DataNoiseValue, splitType, dataset1DataKeyNoise, dataset1DataNoise);
        rightDataset = addDataNoise(rightDataset, selectedStringMethods2, selectedNumericMethods2, dataset2DataNoiseValue, splitType, dataset2DataKeyNoise, dataset2DataNoise);

        // Apply Join if necessary
        leftDataset = applyJoin(leftDataset, dataset1StructureType);
        rightDataset = applyJoin(rightDataset, dataset2StructureType);

        // Write Datasets to CSV files
        writeDataset(leftDataset, filepathOutput + "_left_", hasHeaders, separator, dataset1ShuffleOption, dataset1StructureType);
        writeDataset(rightDataset, filepathOutput + "_right_", hasHeaders, separator, dataset2ShuffleOption, dataset2StructureType);
    }


    /**
     * Reads input data from a CSV file and creates a {@link Relation} object.
     *
     * @param csvFile    The CSV file to read.
     * @param hasHeaders Indicates if the CSV file has headers.
     * @param separator  The separator used in the CSV file.
     * @return A {@link Relation} object representing the CSV data, or null if an error occurs.
     */
    public Relation readInput(MultipartFile csvFile, boolean hasHeaders, String separator) {
        try {
            CSVTool csvTool = new CSVTool();
            return csvTool.readCSVColumnsAPI(csvFile, hasHeaders, separator);
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
            return null;
        }
    }


    /**
     * Sets the primary key indices for a given {@link Relation}.
     *
     * @param relation The {@link Relation} for which key indices are determined.
     */
    private void setKeyIndices(Relation relation) {
        try {
            KeyFinder keyFinder = new KeyFinder();
            List<Integer> keyIndices = keyFinder.findKeyIndices(relation, "input");
            relation.setKeyIndices(keyIndices);
            System.out.println("Key indices: ");
            keyIndices.forEach(System.out::println);
        } catch (AlgorithmExecutionException e) {
            System.err.println("Error finding keys: " + e.getMessage());
        }
    }


    /**
     * Splits a {@link Relation} into two relations based on the specified overlap and split type.
     *
     * @param relation                The {@link Relation} to be split.
     * @param columnOverlapPercentage Percentage of columns to overlap in the split.
     * @param rowOverlapPercentage    Percentage of rows to overlap in the split.
     * @param splitType               The split type ("Vertical" or "Horizontal").
     * @return A List containing two {@link Relation} objects after the split.
     */
    private List<Relation> splitRelation(Relation relation, Integer columnOverlapPercentage, Integer rowOverlapPercentage, String splitType) {
        Split split = new Split();
        return split.splitRelation(relation, columnOverlapPercentage, rowOverlapPercentage, splitType);
    }


    /**
     * Applies BCNF normalization to a given {@link Relation} and stores the results in a {@link Dataset}.
     *
     * @param relation      The {@link Relation} to be normalized.
     * @param structureType The structure type ("BCNF", "Join Columns" or "No Change").
     * @param separator     The separator for the CSV file.
     * @return A {@link Dataset} containing the BCNF-normalized relations.
     */
    private Dataset applyNormalization(Relation relation, String structureType, String separator) {
        try {
            Normalization normalization = new Normalization();
            Dataset dataset = new Dataset();
            if (structureType.equals("BCNF")) {
                dataset.setRelations(normalization.transformToBCNF(relation, separator));
            } else {
                dataset.getRelations().add(relation);
            }
            return dataset;
        } catch (IOException | AlgorithmExecutionException e) {
            System.err.println("Error building BCNF: " + e.getMessage());
            return new Dataset(new ArrayList<>(Collections.singletonList(relation)));
        }
    }


    /**
     * Adds schema noise to a dataset, optionally targeting key columns.
     *
     * @param dataset               The dataset to which schema noise is added.
     * @param hasHeaders            Indicates if the dataset has headers.
     * @param selectedSchemaMethods The list of schema noise methods to use.
     * @param schemaNoisePercentage The percentage of schema noise to apply.
     * @param schemaNoiseInKeys     If true, applies schema noise to key columns.
     * @param schemaNoise           If true, enables schema noise addition.
     * @return A new {@link Dataset} with schema noise added.
     */
    private Dataset addSchemaNoise(Dataset dataset, boolean hasHeaders, List<String> selectedSchemaMethods, Integer schemaNoisePercentage, boolean schemaNoiseInKeys, boolean schemaNoise) {
        if (!schemaNoise || !hasHeaders) {
            return dataset;
        }

        try {
            Dataset noisyDataset = new Dataset();
            for (Relation relation : dataset.getRelations()) {
                SchemaNoise schemaNoiseLeft = new SchemaNoise(selectedSchemaMethods);
                noisyDataset.getRelations().add(schemaNoiseLeft.perturbSchema(relation, schemaNoisePercentage, schemaNoiseInKeys));
            }
            return noisyDataset;
        } catch (Exception e) {
            System.err.println("Error perturbing schema: " + e.getMessage());
            return dataset;
        }
    }


    /**
     * Adds data noise to a dataset, optionally targeting key columns, using specified string and numeric methods.
     *
     * @param dataset                The dataset to which data noise is added.
     * @param selectedStringMethods  The list of string manipulation methods for data noise.
     * @param selectedNumericMethods The list of numeric manipulation methods for data noise.
     * @param dataNoisePercentage    The percentage of data noise to apply.
     * @param splitType              The split type ("Vertical" or "Horizontal").
     * @param dataNoiseInKeys        If true, applies data noise to key columns.
     * @param dataNoise              If true, enables data noise addition.
     * @return A new {@link Dataset} with data noise added.
     */
    private Dataset addDataNoise(Dataset dataset, List<String> selectedStringMethods, List<String> selectedNumericMethods, Integer dataNoisePercentage, String splitType, boolean dataNoiseInKeys, boolean dataNoise) {
        if (!dataNoise) {
            return dataset;
        }

        try {
            Dataset noisyDataset = new Dataset();
            for (Relation relation : dataset.getRelations()) {
                DataNoise dataNoiseLeft = new DataNoise(selectedStringMethods, selectedNumericMethods);
                noisyDataset.getRelations().add(dataNoiseLeft.perturbData(relation, dataNoisePercentage, splitType, dataNoiseInKeys));
            }
            return noisyDataset;
        } catch (Exception e) {
            System.err.println("Error perturbing data: " + e.getMessage());
            return dataset;
        }
    }


    /**
     * Applies a join operation to the dataset if specified by structure type.
     *
     * @param dataset       The dataset to which the join is applied.
     * @param structureType The structure type ("BCNF", "Join Columns" or "No Change").
     * @return A new {@link Dataset} after the join is applied.
     */
    private Dataset applyJoin(Dataset dataset, String structureType) {
        Dataset joinedDataset = new Dataset();
        if (structureType.equals("Join Columns")) {
            Join join = new Join();
            joinedDataset.getRelations().add(join.joinColumns(dataset.getRelations().get(0)));
        } else {
            joinedDataset = dataset;
        }
        return joinedDataset;
    }


    /**
     * Writes a {@link Dataset} to one or more CSV files and applies specified shuffling.
     *
     * @param dataset        The dataset to write to CSV.
     * @param filepathOutput The base path for the output files.
     * @param hasHeaders     Indicates if headers should be included in the CSV.
     * @param separator      The separator used in the CSV files.
     * @param shuffleType    The type of shuffling to apply ("Shuffle Rows", "Shuffle Columns" or "No Change").
     * @param structureType  The type of structure change.
     */
    private void writeDataset(Dataset dataset, String filepathOutput, boolean hasHeaders, String separator, String shuffleType, String structureType) {
        try {
            // Save order of Column Indices
            List<List<Integer>> columnOrder = new ArrayList<>();

            // Write Dataset in CSV
            CSVTool csvTool = new CSVTool();
            for (int i = 1; i <= dataset.getRelations().size(); i++) {
                String outputFilepath = filepathOutput + i + ".csv";
                columnOrder.add(csvTool.writeCSV(dataset.getRelations().get(i - 1), outputFilepath, hasHeaders, separator, shuffleType));
            }
            if (structureType.equals("BCNF")) {
                csvTool.writeKeyFile(dataset, columnOrder, filepathOutput + "keys.txt");
            }
        } catch (IOException e) {
            System.err.println("Error writing dataset to CSV file: " + e.getMessage());
        }
    }
}

