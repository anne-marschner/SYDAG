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
        char separator = formDataWrapper.getSeparator().charAt(0);
        char quoteChar = formDataWrapper.getQuoteChar();
        char escapeChar = formDataWrapper.getEscapeChar();
        String[] splitType = formDataWrapper.getSplitType();
        Integer columnOverlapPercentage = formDataWrapper.getColumnOverlapPercentage();
        Integer rowOverlapPercentage = formDataWrapper.getRowOverlapPercentage();
        Integer columnDistribution = formDataWrapper.getColumnDistribution();
        Integer rowDistribution = formDataWrapper.getRowDistribution();
        boolean mixedOverlap = formDataWrapper.getMixedOverlap();
        String dataset1StructureType = formDataWrapper.getDataset1StructureType();
        String dataset2StructureType = formDataWrapper.getDataset2StructureType();
        String dataset3StructureType = formDataWrapper.getDataset3StructureType();
        String dataset4StructureType = formDataWrapper.getDataset4StructureType();
        Integer joinPercentage1 = formDataWrapper.getJoinPercentage1();
        Integer joinPercentage2 = formDataWrapper.getJoinPercentage2();
        Integer joinPercentage3 = formDataWrapper.getJoinPercentage3();
        Integer joinPercentage4 = formDataWrapper.getJoinPercentage4();
        Integer normalizePercentage1 = formDataWrapper.getNormalizePercentage1();
        Integer normalizePercentage2 = formDataWrapper.getNormalizePercentage2();
        Integer normalizePercentage3 = formDataWrapper.getNormalizePercentage3();
        Integer normalizePercentage4 = formDataWrapper.getNormalizePercentage4();
        boolean dataset1SchemaNoise = formDataWrapper.getDataset1SchemaNoise();
        boolean dataset2SchemaNoise = formDataWrapper.getDataset2SchemaNoise();
        boolean dataset3SchemaNoise = formDataWrapper.getDataset3SchemaNoise();
        boolean dataset4SchemaNoise = formDataWrapper.getDataset4SchemaNoise();
        Integer dataset1SchemaNoiseValue = formDataWrapper.getDataset1SchemaNoiseValue();
        Integer dataset2SchemaNoiseValue = formDataWrapper.getDataset2SchemaNoiseValue();
        Integer dataset3SchemaNoiseValue = formDataWrapper.getDataset3SchemaNoiseValue();
        Integer dataset4SchemaNoiseValue = formDataWrapper.getDataset4SchemaNoiseValue();
        boolean dataset1SchemaKeyNoise = formDataWrapper.getDataset1SchemaKeyNoise();
        boolean dataset2SchemaKeyNoise = formDataWrapper.getDataset2SchemaKeyNoise();
        boolean dataset3SchemaKeyNoise = formDataWrapper.getDataset3SchemaKeyNoise();
        boolean dataset4SchemaKeyNoise = formDataWrapper.getDataset4SchemaKeyNoise();
        boolean deleteSchema1 = formDataWrapper.getDeleteSchema1();
        boolean deleteSchema2 = formDataWrapper.getDeleteSchema2();
        boolean deleteSchema3 = formDataWrapper.getDeleteSchema3();
        boolean deleteSchema4 = formDataWrapper.getDeleteSchema4();
        List<String> selectedSchemaMethods1 = formDataWrapper.getSelectedSchemaMethods1();
        List<String> selectedSchemaMethods2 = formDataWrapper.getSelectedSchemaMethods2();
        List<String> selectedSchemaMethods3 = formDataWrapper.getSelectedSchemaMethods3();
        List<String> selectedSchemaMethods4 = formDataWrapper.getSelectedSchemaMethods4();
        boolean dataset1DataNoise = formDataWrapper.getDataset1DataNoise();
        boolean dataset2DataNoise = formDataWrapper.getDataset2DataNoise();
        boolean dataset3DataNoise = formDataWrapper.getDataset3DataNoise();
        boolean dataset4DataNoise = formDataWrapper.getDataset4DataNoise();
        Integer dataset1DataNoiseValue = formDataWrapper.getDataset1DataNoiseValue();
        Integer dataset2DataNoiseValue = formDataWrapper.getDataset2DataNoiseValue();
        Integer dataset3DataNoiseValue = formDataWrapper.getDataset3DataNoiseValue();
        Integer dataset4DataNoiseValue = formDataWrapper.getDataset4DataNoiseValue();
        Integer dataset1NoiseInside = formDataWrapper.getDataset1NoiseInside();
        Integer dataset2NoiseInside = formDataWrapper.getDataset2NoiseInside();
        Integer dataset3NoiseInside = formDataWrapper.getDataset3NoiseInside();
        Integer dataset4NoiseInside = formDataWrapper.getDataset4NoiseInside();
        boolean dataset1DataKeyNoise = formDataWrapper.getDataset1DataKeyNoise();
        boolean dataset2DataKeyNoise = formDataWrapper.getDataset2DataKeyNoise();
        boolean dataset3DataKeyNoise = formDataWrapper.getDataset3DataKeyNoise();
        boolean dataset4DataKeyNoise = formDataWrapper.getDataset4DataKeyNoise();
        List<String> selectedStringMethods1 = formDataWrapper.getSelectedStringMethods1();
        List<String> selectedStringMethods2 = formDataWrapper.getSelectedStringMethods2();
        List<String> selectedStringMethods3 = formDataWrapper.getSelectedStringMethods3();
        List<String> selectedStringMethods4 = formDataWrapper.getSelectedStringMethods4();
        List<String> selectedNumericMethods1 = formDataWrapper.getSelectedNumericMethods1();
        List<String> selectedNumericMethods2 = formDataWrapper.getSelectedNumericMethods2();
        List<String> selectedNumericMethods3 = formDataWrapper.getSelectedNumericMethods3();
        List<String> selectedNumericMethods4 = formDataWrapper.getSelectedNumericMethods4();
        String dataset1ShuffleOption = formDataWrapper.getDataset1ShuffleOption();
        String dataset2ShuffleOption = formDataWrapper.getDataset2ShuffleOption();
        String dataset3ShuffleOption = formDataWrapper.getDataset3ShuffleOption();
        String dataset4ShuffleOption = formDataWrapper.getDataset4ShuffleOption();
        String filepathOutput = formDataWrapper.getFilepathOutput();

        // Set Arrays
        String[] structureTypes = {dataset1StructureType, dataset2StructureType, dataset3StructureType, dataset4StructureType};
        Integer[] normalizePercentages = {normalizePercentage1, normalizePercentage2, normalizePercentage3, normalizePercentage4};
        List<List<String>> selectedSchemaMethods = new ArrayList<>(Arrays.asList(selectedSchemaMethods1, selectedSchemaMethods2, selectedSchemaMethods3, selectedSchemaMethods4));
        Integer[] schemaNoisePercentages = {dataset1SchemaNoiseValue, dataset2SchemaNoiseValue, dataset3SchemaNoiseValue, dataset4SchemaNoiseValue};
        boolean[] schemaNoiseInKeys = {dataset1SchemaKeyNoise, dataset2SchemaKeyNoise, dataset3SchemaKeyNoise, dataset4SchemaKeyNoise};
        boolean[] schemaNoise = {dataset1SchemaNoise, dataset2SchemaNoise, dataset3SchemaNoise, dataset4SchemaNoise};
        boolean[] deleteSchema = {deleteSchema1, deleteSchema2, deleteSchema3, deleteSchema4};
        List<List<String>> selectedStringMethods = new ArrayList<>(Arrays.asList(selectedStringMethods1, selectedStringMethods2, selectedStringMethods3, selectedStringMethods4));
        List<List<String>> selectedNumericMethods = new ArrayList<>(Arrays.asList(selectedNumericMethods1, selectedNumericMethods2, selectedNumericMethods3, selectedNumericMethods4));
        Integer[] dataNoisePercentages = {dataset1DataNoiseValue, dataset2DataNoiseValue, dataset3DataNoiseValue, dataset4DataNoiseValue};
        Integer[] noiseInsidePercentage = {dataset1NoiseInside, dataset2NoiseInside, dataset3NoiseInside, dataset4NoiseInside};
        boolean[] dataNoiseInKeys = {dataset1DataKeyNoise, dataset2DataKeyNoise, dataset3DataKeyNoise, dataset4DataKeyNoise};
        boolean[] dataNoise = {dataset1DataNoise, dataset2DataNoise, dataset3DataNoise, dataset4DataNoise};
        Integer[] joinPercentages = {joinPercentage1, joinPercentage2, joinPercentage3, joinPercentage4};
        String[] shuffleTypes = {dataset1ShuffleOption, dataset2ShuffleOption, dataset3ShuffleOption, dataset4ShuffleOption};
        String[] identifier = {"A", "B", "C", "D"};

        // Begin processing
        // Read information from file into relation
        Relation inputRelation = readInput(csvFile, hasHeaders, separator, quoteChar, escapeChar);

        // Set key indices for the input relation
        setKeyIndices(inputRelation);

        // Split Relation according to selected direction
        List<Relation> splitDataset = splitRelation(inputRelation, columnOverlapPercentage, rowOverlapPercentage, columnDistribution, rowDistribution, splitType, mixedOverlap);

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

        // Write datasets to CSV
        CSVTool csvTool = new CSVTool();
        for (int i = 0; i < datasets.size(); i++) {
            writeDataset(csvTool, datasets.get(i), filepathOutput + "_" + identifier[i], separator, quoteChar, shuffleTypes[i], i, identifier[i]);
        }

        // Write Mapping
        csvTool.writeMapping(inputRelation, datasets, identifier,filepathOutput + "_Mapping.txt");
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
    private List<Relation> splitRelation(Relation relation, Integer columnOverlapPercentage, Integer rowOverlapPercentage, int columnDistribution, int rowDistribution, String[] splitType, boolean mixedOverlap) {

        // Check which split Types have been chosen
        boolean hasHorizontal = Arrays.asList(splitType).contains("Horizontal");
        boolean hasVertical = Arrays.asList(splitType).contains("Vertical");
        Split split = new Split();
        List<Relation> splitRelations = new ArrayList<>();

        // Apply horizontal split first
        if (hasHorizontal) {
            splitRelations = split.splitRelation(relation, columnOverlapPercentage, rowOverlapPercentage, columnDistribution, rowDistribution, "Horizontal", mixedOverlap);
        }

        // Apply vertical split next
        if (hasVertical) {
            List<Relation> tempRelations = new ArrayList<>();

            // Either use results from horizontal split or only apply vertical split (according to chosen type)
            for (Relation rel : splitRelations.isEmpty() ? List.of(relation) : splitRelations) {
                tempRelations.addAll(split.splitRelation(rel, columnOverlapPercentage, rowOverlapPercentage, columnDistribution, rowDistribution, "Vertical", mixedOverlap));
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

