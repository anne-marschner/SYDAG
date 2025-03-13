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
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * The Generator class combines various processes, such as reading, splitting,
 * normalizing, and adding noise to data, to generate datasets that simulate real-world scenarios
 * with schema and data perturbations. The class also handles writing the final datasets to CSV files.
 */
@Service
public class Generator {

    private static final int THREAD_POOL_SIZE = Math.max(1, Runtime.getRuntime().availableProcessors() - 1);

    /**
     * Executes the entire data generation process using the parameters provided.
     *
     * @param params The parameters encapsulated in a GeneratorParameters object.
     * @param outputPath the path for the output datasets and metadata
     */
    public void execute(GeneratorParameters params, String outputPath) {

        // Extract parameters from GeneratorParameters
        MultipartFile csvFile = params.getCsvFile();
        FormDataWrapper formDataWrapper = params.getFormDataWrapper();
        // Extract fields from FormDataWrapper
        boolean hasHeaders = formDataWrapper.getHasHeaders();
        char separator = formDataWrapper.getSeparator().charAt(0);
        char quoteChar = formDataWrapper.getQuote().charAt(0);
        char escapeChar = formDataWrapper.getEscape().charAt(0);
        String splitType = formDataWrapper.getSplitType();
        Integer columnOverlapPercentage = formDataWrapper.getColumnOverlapPercentage();
        Integer rowOverlapPercentage = formDataWrapper.getRowOverlapPercentage();
        Integer columnDistribution = formDataWrapper.getColumnDistribution();
        Integer rowDistribution = formDataWrapper.getRowDistribution();
        String overlapType = formDataWrapper.getOverlapType();
        String datasetAStructureType = formDataWrapper.getDatasetAStructureType();
        String datasetBStructureType = formDataWrapper.getDatasetBStructureType();
        String datasetCStructureType = formDataWrapper.getDatasetCStructureType();
        String datasetDStructureType = formDataWrapper.getDatasetDStructureType();
        Integer joinPercentageA = formDataWrapper.getDatasetAJoinColumnsSliderValue();
        Integer joinPercentageB = formDataWrapper.getDatasetBJoinColumnsSliderValue();
        Integer joinPercentageC = formDataWrapper.getDatasetCJoinColumnsSliderValue();
        Integer joinPercentageD = formDataWrapper.getDatasetDJoinColumnsSliderValue();
        Integer normalizePercentageA = formDataWrapper.getDatasetABCNFSliderValue();
        Integer normalizePercentageB = formDataWrapper.getDatasetBBCNFSliderValue();
        Integer normalizePercentageC = formDataWrapper.getDatasetCBCNFSliderValue();
        Integer normalizePercentageD = formDataWrapper.getDatasetDBCNFSliderValue();
        boolean datasetASchemaNoise = formDataWrapper.getDatasetASchemaNoise();
        boolean datasetBSchemaNoise = formDataWrapper.getDatasetBSchemaNoise();
        boolean datasetCSchemaNoise = formDataWrapper.getDatasetCSchemaNoise();
        boolean datasetDSchemaNoise = formDataWrapper.getDatasetDSchemaNoise();
        Integer datasetASchemaNoiseValue = formDataWrapper.getDatasetASchemaNoiseValue();
        Integer datasetBSchemaNoiseValue = formDataWrapper.getDatasetBSchemaNoiseValue();
        Integer datasetCSchemaNoiseValue = formDataWrapper.getDatasetCSchemaNoiseValue();
        Integer datasetDSchemaNoiseValue = formDataWrapper.getDatasetDSchemaNoiseValue();
        boolean datasetASchemaKeyNoise = formDataWrapper.getDatasetASchemaKeyNoise();
        boolean datasetBSchemaKeyNoise = formDataWrapper.getDatasetBSchemaKeyNoise();
        boolean datasetCSchemaKeyNoise = formDataWrapper.getDatasetCSchemaKeyNoise();
        boolean datasetDSchemaKeyNoise = formDataWrapper.getDatasetDSchemaKeyNoise();
        boolean deleteSchemaA = formDataWrapper.getDatasetASchemaDeleteSchema();
        boolean deleteSchemaB = formDataWrapper.getDatasetBSchemaDeleteSchema();
        boolean deleteSchemaC = formDataWrapper.getDatasetCSchemaDeleteSchema();
        boolean deleteSchemaD = formDataWrapper.getDatasetDSchemaDeleteSchema();
        List<String> selectedSchemaMethodsA = formDataWrapper.getDatasetASchemaMultiselect();
        List<String> selectedSchemaMethodsB = formDataWrapper.getDatasetBSchemaMultiselect();
        List<String> selectedSchemaMethodsC = formDataWrapper.getDatasetCSchemaMultiselect();
        List<String> selectedSchemaMethodsD = formDataWrapper.getDatasetDSchemaMultiselect();
        boolean datasetADataNoise = formDataWrapper.getDatasetADataNoise();
        boolean datasetBDataNoise = formDataWrapper.getDatasetBDataNoise();
        boolean datasetCDataNoise = formDataWrapper.getDatasetCDataNoise();
        boolean datasetDDataNoise = formDataWrapper.getDatasetDDataNoise();
        Integer datasetADataNoiseValue = formDataWrapper.getDatasetADataNoiseValue();
        Integer datasetBDataNoiseValue = formDataWrapper.getDatasetBDataNoiseValue();
        Integer datasetCDataNoiseValue = formDataWrapper.getDatasetCDataNoiseValue();
        Integer datasetDDataNoiseValue = formDataWrapper.getDatasetDDataNoiseValue();
        Integer datasetANoiseInside = formDataWrapper.getDatasetADataNoiseInside();
        Integer datasetBNoiseInside = formDataWrapper.getDatasetBDataNoiseInside();
        Integer datasetCNoiseInside = formDataWrapper.getDatasetCDataNoiseInside();
        Integer datasetDNoiseInside = formDataWrapper.getDatasetDDataNoiseInside();
        boolean datasetADataKeyNoise = formDataWrapper.getDatasetADataKeyNoise();
        boolean datasetBDataKeyNoise = formDataWrapper.getDatasetBDataKeyNoise();
        boolean datasetCDataKeyNoise = formDataWrapper.getDatasetCDataKeyNoise();
        boolean datasetDDataKeyNoise = formDataWrapper.getDatasetDDataKeyNoise();
        List<String> selectedDataMethodsA = formDataWrapper.getDatasetADataMultiselect();
        List<String> selectedDataMethodsB = formDataWrapper.getDatasetBDataMultiselect();
        List<String> selectedDataMethodsC = formDataWrapper.getDatasetCDataMultiselect();
        List<String> selectedDataMethodsD = formDataWrapper.getDatasetDDataMultiselect();
        List<String> selectedStringMethodsA = extractStringMethods(selectedDataMethodsA);
        List<String> selectedStringMethodsB = extractStringMethods(selectedDataMethodsB);
        List<String> selectedStringMethodsC = extractStringMethods(selectedDataMethodsC);
        List<String> selectedStringMethodsD = extractStringMethods(selectedDataMethodsD);
        List<String> selectedNumericMethodsA = extractNumericMethods(selectedDataMethodsA);
        List<String> selectedNumericMethodsB = extractNumericMethods(selectedDataMethodsB);
        List<String> selectedNumericMethodsC = extractNumericMethods(selectedDataMethodsC);
        List<String> selectedNumericMethodsD = extractNumericMethods(selectedDataMethodsD);
        String datasetAShuffleOption = formDataWrapper.getDatasetAShuffleOption();
        String datasetBShuffleOption = formDataWrapper.getDatasetBShuffleOption();
        String datasetCShuffleOption = formDataWrapper.getDatasetCShuffleOption();
        String datasetDShuffleOption = formDataWrapper.getDatasetDShuffleOption();

        // Set Arrays
        String[] structureTypes = {datasetAStructureType, datasetBStructureType, datasetCStructureType, datasetDStructureType};
        Integer[] normalizePercentages = {normalizePercentageA, normalizePercentageB, normalizePercentageC, normalizePercentageD};
        List<List<String>> selectedSchemaMethods = new ArrayList<>(Arrays.asList(selectedSchemaMethodsA, selectedSchemaMethodsB, selectedSchemaMethodsC, selectedSchemaMethodsD));
        Integer[] schemaNoisePercentages = {datasetASchemaNoiseValue, datasetBSchemaNoiseValue, datasetCSchemaNoiseValue, datasetDSchemaNoiseValue};
        boolean[] schemaNoiseInKeys = {datasetASchemaKeyNoise, datasetBSchemaKeyNoise, datasetCSchemaKeyNoise, datasetDSchemaKeyNoise};
        boolean[] schemaNoise = {datasetASchemaNoise, datasetBSchemaNoise, datasetCSchemaNoise, datasetDSchemaNoise};
        boolean[] deleteSchema = {deleteSchemaA, deleteSchemaB, deleteSchemaC, deleteSchemaD};
        List<List<String>> selectedStringMethods = new ArrayList<>(Arrays.asList(selectedStringMethodsA, selectedStringMethodsB, selectedStringMethodsC, selectedStringMethodsD));
        List<List<String>> selectedNumericMethods = new ArrayList<>(Arrays.asList(selectedNumericMethodsA, selectedNumericMethodsB, selectedNumericMethodsC, selectedNumericMethodsD));
        Integer[] dataNoisePercentages = {datasetADataNoiseValue, datasetBDataNoiseValue, datasetCDataNoiseValue, datasetDDataNoiseValue};
        Integer[] noiseInsidePercentage = {datasetANoiseInside, datasetBNoiseInside, datasetCNoiseInside, datasetDNoiseInside};
        boolean[] dataNoiseInKeys = {datasetADataKeyNoise, datasetBDataKeyNoise, datasetCDataKeyNoise, datasetDDataKeyNoise};
        boolean[] dataNoise = {datasetADataNoise, datasetBDataNoise, datasetCDataNoise, datasetDDataNoise};
        Integer[] joinPercentages = {joinPercentageA, joinPercentageB, joinPercentageC, joinPercentageD};
        String[] shuffleTypes = {datasetAShuffleOption, datasetBShuffleOption, datasetCShuffleOption, datasetDShuffleOption};
        String[] identifier = {"A", "B", "C", "D"};

        // Begin processing
        // Read information from file into relation
        Relation inputRelation = readInput(csvFile, hasHeaders, separator, quoteChar, escapeChar);

        // Set key indices for the input relation
        setKeyIndices(inputRelation);

        // Split Relation according to selected direction
        List<Relation> splitDataset = splitRelation(inputRelation, columnOverlapPercentage, rowOverlapPercentage, columnDistribution, rowDistribution, splitType, overlapType);

        // Apply BCNF
        List<Dataset> datasets = new ArrayList<>();
        for (int i = 0; i < splitDataset.size(); i++) {
            datasets.add(applyNormalization(splitDataset.get(i), structureTypes[i], separator, quoteChar, normalizePercentages[i]));
        }

        // Add Schema Noise to the datasets
        for (int i = 0; i < datasets.size(); i++) {
            datasets.set(i, addSchemaNoise(datasets.get(i), hasHeaders, selectedSchemaMethods.get(i), schemaNoisePercentages[i],
                    schemaNoiseInKeys[i], schemaNoise[i], deleteSchema[i]));
        }

        // Add Data Noise to datasets
        for (int i = 0; i < datasets.size(); i++) {
            datasets.set(i, addDataNoise(datasets.get(i), selectedStringMethods.get(i), selectedNumericMethods.get(i),
                    dataNoisePercentages[i], noiseInsidePercentage[i], dataNoiseInKeys[i], dataNoise[i]));
        }

        // Apply Join
        for (int i = 0; i < datasets.size(); i++) {
            datasets.set(i, applyJoin(datasets.get(i), structureTypes[i], joinPercentages[i], separator));
        }

        // Use a fixed thread pool to write datasets concurrently
        CSVTool csvTool = new CSVTool();
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        List<Future<?>> futures = new ArrayList<>();

        for (int i = 0; i < datasets.size(); i++) {
            final int index = i;
            Future<?> future = executor.submit(() -> writeDataset(csvTool, datasets.get(index), outputPath + "_" + identifier[index],
                    separator, quoteChar, shuffleTypes[index], index, identifier[index]));
            futures.add(future);
        }

        // Wait for all tasks to finish
        for (Future<?> f : futures) {
            try {
                f.get();
            } catch (InterruptedException | ExecutionException e) {
                System.err.println("Error during dataset writing: " + e.getMessage());
            }
        }
        executor.shutdown();

        // Write mapping file.
        csvTool.writeMapping(inputRelation, datasets, identifier, outputPath + "_Mapping.txt");

        datasets.clear();
    }


    /**
     * Extracts the list of methods related to string operations from the provided list.
     *
     * @param allChosenMethods the list of all selected methods
     * @return a list of methods that are categorized as string-related methods
     */
    public List<String> extractStringMethods(List<String> allChosenMethods) {

        // Define the String Methods
        Set<String> stringMethods = Set.of(
                "replaceWithSynonyms", "addRandomPrefix", "replaceWithTranslation", "shuffleWords", "generateMissingValue",
                "generatePhoneticError", "generateOCRError", "abbreviateDataEntry", "changeFormat", "generateTypingError",
                "generateRandomString", "mapColumn"
        );
        // Filter the entries that are String methods
        return allChosenMethods.stream()
                .filter(stringMethods::contains)
                .collect(Collectors.toList());
    }


    /**
     * Extracts a list of methods related to numeric operations from the provided list.
     *
     * @param allChosenMethods the list of all selected methods
     * @return a list of methods that are categorized as numeric-related methods
     */
    public List<String> extractNumericMethods(List<String> allChosenMethods) {

        // Define the String Methods
        Set<String> stringMethods = Set.of(
                "changeValue", "changeValueToOutlier"
        );
        // Filter the entries that are String methods
        return allChosenMethods.stream()
                .filter(stringMethods::contains)
                .collect(Collectors.toList());
    }


    /**
     * Reads input data from a CSV file and creates a {@link Relation} object.
     *
     * @param csvFile    The CSV file to read.
     * @param hasHeaders Indicates if the CSV file has headers.
     * @param separator  The separator used in the CSV file.
     * @param quoteChar  The quote Character used in the CSV file.
     * @param escapeChar  The escape Character used in the CSV file.
     * @return A {@link Relation} object representing the CSV data, or null if an error occurs.
     */
    public Relation readInput(MultipartFile csvFile, boolean hasHeaders, char separator, char quoteChar, char escapeChar) {
        try {
            CSVTool csvTool = new CSVTool();
            return csvTool.readCSVColumns(csvFile, hasHeaders, separator, quoteChar, escapeChar);
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
     * @param relation               The {@link Relation} to be split.
     * @param columnOverlapPercentage Percentage of columns to overlap in the split.
     * @param rowOverlapPercentage    Percentage of rows to overlap in the split.
     * @param splitType               The split type ("Vertical" or "Horizontal").
     * @return A list of {@link Relation} objects after the split.
     */
    private List<Relation> splitRelation(Relation relation, Integer columnOverlapPercentage, Integer rowOverlapPercentage, Integer columnDistribution, Integer rowDistribution, String splitType, String overlapType) {

        Split split = new Split();
        List<Relation> splitRelations = new ArrayList<>();

        // Apply horizontal split first
        if (splitType.equals("Horizontal") || splitType.equals("VerticalHorizontal")) {
            splitRelations = split.splitRelation(relation, columnOverlapPercentage, rowOverlapPercentage, columnDistribution, rowDistribution, "Horizontal", overlapType);
        }

        // Apply vertical split next
        if (splitType.equals("Vertical") || splitType.equals("VerticalHorizontal")) {
            List<Relation> tempRelations = new ArrayList<>();

            // Either use results from horizontal split or only apply vertical split (according to chosen type)
            for (Relation rel : splitRelations.isEmpty() ? List.of(relation) : splitRelations) {
                tempRelations.addAll(split.splitRelation(rel, columnOverlapPercentage, rowOverlapPercentage, columnDistribution, rowDistribution, "Vertical", overlapType));
            }
            splitRelations = tempRelations;
        }
        return splitRelations;
    }


    /**
     * Applies BCNF normalization to a given {@link Relation} and stores the results in a {@link Dataset}.
     *
     * @param relation        The {@link Relation} to be normalized.
     * @param structureType   The structure type ("BCNF", "Join Columns" or "No Change").
     * @param separator       The separator for the CSV file.
     * @param quoteChar       The quote character for the CSV file.
     * @return A {@link Dataset} containing the BCNF-normalized relations.
     */
    private Dataset applyNormalization(Relation relation, String structureType, char separator, char quoteChar, Integer normalizePercentage) {
        try {
            Normalization normalization = new Normalization();
            Dataset dataset = new Dataset();
            if (structureType.equals("BCNF")) {
                dataset.setRelations(normalization.transformToBCNF(relation, separator, quoteChar, normalizePercentage));
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
     * @param dataset              The dataset to which schema noise is added.
     * @param hasHeaders           Indicates if the dataset has headers.
     * @param selectedSchemaMethods The list of schema noise methods to use.
     * @param schemaNoisePercentage The percentage of schema noise to apply.
     * @param schemaNoiseInKeys    If true, applies schema noise to key columns.
     * @param schemaNoise          If true, enables schema noise addition.
     * @param deleteSchema         If true, schema is deleted.
     * @return A new {@link Dataset} with schema noise added.
     */
    private Dataset addSchemaNoise(Dataset dataset, boolean hasHeaders, List<String> selectedSchemaMethods, Integer schemaNoisePercentage,
                                   boolean schemaNoiseInKeys, boolean schemaNoise, boolean deleteSchema) {
        if (!schemaNoise || !hasHeaders) {
            return dataset;
        }

        try {
            Dataset noisyDataset = new Dataset();
            for (Relation relation : dataset.getRelations()) {
                SchemaNoise schemaNoiseLeft = new SchemaNoise(selectedSchemaMethods);
                noisyDataset.getRelations().add(schemaNoiseLeft.perturbSchema(relation, schemaNoisePercentage, schemaNoiseInKeys, deleteSchema));
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
     * @param dataset              The dataset to which data noise is added.
     * @param selectedStringMethods The list of string manipulation methods for data noise.
     * @param selectedNumericMethods The list of numeric manipulation methods for data noise.
     * @param dataNoisePercentage   The percentage of data noise to apply.
     * @param dataNoiseInKeys       If true, applies data noise to key columns.
     * @param dataNoise             If true, enables data noise addition.
     * @return A new {@link Dataset} with data noise added.
     */
    private Dataset addDataNoise(Dataset dataset, List<String> selectedStringMethods, List<String> selectedNumericMethods,
                                 Integer dataNoisePercentage, Integer noiseInsidePercentage, boolean dataNoiseInKeys, boolean dataNoise) {
        if (!dataNoise) {
            return dataset;
        }

        try {
            Dataset noisyDataset = new Dataset();
            for (Relation relation : dataset.getRelations()) {
                DataNoise dataNoiseObject = new DataNoise(selectedStringMethods, selectedNumericMethods);
                noisyDataset.getRelations().add(dataNoiseObject.perturbData(relation, dataNoisePercentage, noiseInsidePercentage,dataNoiseInKeys));
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
     * @param dataset        The dataset to which the join is applied.
     * @param structureType  The structure type ("BCNF", "Join Columns" or "No Change").
     * @return A new {@link Dataset} after the join is applied.
     */
    private Dataset applyJoin(Dataset dataset, String structureType, Integer joinPercentage, char separator) {
        Dataset joinedDataset = new Dataset();
        if (structureType.equals("Join Columns")) {
            Join join = new Join();
            joinedDataset.getRelations().add(join.joinColumns(dataset.getRelations().get(0), joinPercentage, separator));
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
     * @param separator      The separator used in the CSV files.
     * @param quoteChar      The quote character used in the CSV files.
     * @param shuffleType    The type of shuffling to apply ("Shuffle Rows", "Shuffle Columns" or "No Change").
     * @param identifier     The Letter that identifies the created Dataset.
     */
    private void writeDataset(CSVTool csvTool, Dataset dataset, String filepathOutput, char separator, char quoteChar, String shuffleType, int datasetNumber, String identifier) {
        try {
            // Save order of Column Indices
            List<List<Integer>> columnOrder = new ArrayList<>();

            // Write Dataset in CSV
            for (int i = 1; i <= dataset.getRelations().size(); i++) {
                String outputFilepath = filepathOutput + i + ".csv";
                columnOrder.add(csvTool.writeCSV(dataset.getRelations().get(i - 1), outputFilepath, separator, quoteChar, shuffleType));
            }

            // Write Keys for dataset in txt file
            csvTool.writeKeyFile(dataset, columnOrder, identifier, filepathOutput + "_keys.txt");
            csvTool.getColumnOrders().put(datasetNumber, columnOrder);
        } catch (IOException e) {
            System.err.println("Error writing dataset to CSV file: " + e.getMessage());
        }
    }
}

