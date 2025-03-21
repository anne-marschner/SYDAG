package org.anne_marschner_project.core.noise;

import org.anne_marschner_project.core.data.Attribute;
import org.anne_marschner_project.core.data.Relation;
import org.anne_marschner_project.core.data.Type;

import java.util.*;
import java.util.function.Predicate;

/**
 * The DataNoise class is responsible for introducing noise into data.
 * It can apply noise to rows or columns of a given {@link Relation}.
 */
public class DataNoise extends Noise {

    private List<String> selectedStringMethods;     // List of noise methods for alphanumeric entries selected by the user
    private List<String> selectedNumericMethods;    // List of noise methods for numeric entries selected by the user
    private final List<String> existingNumericMethods = new ArrayList<>(Arrays.asList("changeValue", "changeValueToOutlier"));
    Map<Integer, StatisticValues> statisticValuesOfColumns = new HashMap<>();     // Map to store precomputed statistical values for columns


    /**
     * Constructs a DataNoise instance with specific noise methods for string and numeric data.
     *
     * @param selectedStringMethods List of alphanumeric error methods to be used for noise.
     * @param selectedNumericMethods List of numeric error methods to be used for noise.
     */
    public DataNoise (List<String> selectedStringMethods, List<String> selectedNumericMethods) {
        this.selectedStringMethods = selectedStringMethods;
        this.selectedNumericMethods = selectedNumericMethods;
    }


    /**
     * Default constructor that initializes DataNoise with no selected noise methods.
     */
    public DataNoise () {
        this.selectedStringMethods = null;
        this.selectedNumericMethods = null;
    }


    /**
     * Determines the type of perturbation (row-based or column-based) and applies noise
     * to the Relation based on the type.
     *
     * @param relation The Relation to be perturbed.
     * @param noisePercentage The percentage of columns/ rows to apply noise to.
     * @param noiseInsidePercentage The percentage of entries within a column/ row to receive noise.
     * @param dataNoiseInKeys Whether keys in the data can receive noise.
     * @return The perturbed Relation.
     * @throws Exception If noise cannot be applied.
     */
    public Relation perturbData(Relation relation, Integer noisePercentage, Integer noiseInsidePercentage, boolean dataNoiseInKeys) throws Exception {

        // If noisePercentage is 0, return original relation
        if (noisePercentage == 0) {
            return relation;
        }

        // Determine whether rows or columns or both will de perturbed
        if (relation.getOverlappingColumnsIndices() == null) {
            // Due to horizontal splitting, the rows should be perturbed
            return perturbRowData(relation, noisePercentage, noiseInsidePercentage, dataNoiseInKeys, false);
        } else if (relation.getNumOfOverlappingRows() == null) {
            //  Due to vertical splitting, the columns should be perturbed
            return perturbColumnData(relation, noisePercentage, noiseInsidePercentage, dataNoiseInKeys);
        } else {
            // Both splittings have been applied, so we add noise to columns and rows
            Relation perturbedColumnRelation = perturbColumnData(relation, noisePercentage, noiseInsidePercentage, dataNoiseInKeys);
            return perturbRowData(perturbedColumnRelation, noisePercentage, noiseInsidePercentage, dataNoiseInKeys, true);
        }
    }


    /**
     * Applies noise to random rows within a Relation.
     *
     * @param relation The Relation to be perturbed.
     * @param noisePercentage The percentage of rows to apply noise to.
     * @param noiseInsidePercentage The percentage of entries within a row to receive noise.
     * @param dataNoiseInKeys Whether keys in the data can receive noise.
     * @param columnPerturbation Whether the data already includes noise in columns.
     * @return The perturbed Relation.
     * @throws Exception If noise cannot be applied.
     */
    public Relation perturbRowData(Relation relation, int noisePercentage, int noiseInsidePercentage ,boolean dataNoiseInKeys, boolean columnPerturbation) throws Exception {

        // Get schema, data and number of row overlap from relation
        Map<Integer, Attribute> schema = relation.getSchema();
        Map<Integer, List<String>> data = relation.getData();
        Integer numOfOverlappingRows = relation.getNumOfOverlappingRows();

        // Calculate number of rows to perturb based on the percentage
        int numToPerturb = (int) Math.round((noisePercentage / 100.0) * numOfOverlappingRows);
        if (numToPerturb == 0) {
            return relation;
        }

        // Pick numToPerturb random indices from the first numOverlappingRows of the data
        Set<Integer> indicesToPerturb = pickUniqueRandomIndices(numOfOverlappingRows, numToPerturb);

        // Get the column indices of the entries that are allowed to be perturbed
        List<Integer> selectableIndices = new ArrayList<>(data.keySet());

        // Remove keys if they shall not receive noise
        if (!dataNoiseInKeys) {
            selectableIndices.removeAll(relation.getKeyIndices());
            selectableIndices.removeAll(relation.getForeignKeyIndices());
            selectableIndices.removeAll(relation.getKeysBeforeNormalization());
        }

        // Remove the overlappingColumnIndices if the relation already contains column noise
        if (columnPerturbation) {
            selectableIndices.removeAll(relation.getOverlappingColumnsIndices());
        }

        // Loop through all rows that should be perturbed
        for (int rowIndex : indicesToPerturb) {

            // Determine the number of values that can be perturbed
            int numOfValues = selectableIndices.size();

            // Calculate number of errors that will be added in row entries
            int numOfErrors = (int) Math.round((double) (numOfValues * noiseInsidePercentage) / 100);

            // Add noise into numOfErrors random entries
            Collections.shuffle(selectableIndices);
            for (int i = 0; i < numOfErrors; i++) {
                int columnIndex = selectableIndices.get(i);
                String entry = data.get(columnIndex).get(rowIndex);
                String replacement = chooseNoise(entry, schema.get(columnIndex), columnIndex, data.get(columnIndex));
                data.get(columnIndex).set(rowIndex, replacement);
            }
        }
        return relation;
    }


    /**
     * Applies noise to random columns within a Relation.
     *
     * @param relation The Relation to be perturbed.
     * @param noisePercentage The percentage of columns to apply noise to.
     * @param noiseInsidePercentage The percentage of entries within a column to receive noise.
     * @param dataNoiseInKeys Whether keys in the data can receive noise.
     * @return The perturbed Relation.
     * @throws Exception If noise cannot be applied.
     */
    public Relation perturbColumnData(Relation relation, int noisePercentage, int noiseInsidePercentage, boolean dataNoiseInKeys) throws Exception {

        // Get schema, data and overlapping columns indices from relation
        Map<Integer, Attribute> schema = relation.getSchema();
        Map<Integer, List<String>> data = relation.getData();
        List<Integer> candidateColumnsIndices = new ArrayList<>(relation.getOverlappingColumnsIndices());

        // Remove keys if they shall not receive noise
        if (!dataNoiseInKeys) {
            candidateColumnsIndices.removeAll(relation.getKeyIndices());
            candidateColumnsIndices.removeAll(relation.getForeignKeyIndices());
            candidateColumnsIndices.removeAll(relation.getKeysBeforeNormalization());
        }

        // Create Lists of indices of the types
        List<Integer> numericIndices = new ArrayList<>();
        List<Integer> stringIndices = new ArrayList<>();

        // Go through all indices from overlappingColumnsIndices and add to correct type list
        for (Integer index: candidateColumnsIndices) {
            Type attributeType = schema.get(index).getDataType();
            if (attributeType.equals(Type.DOUBLE)) {
                numericIndices.add(index);
            } else {
                stringIndices.add(index);
            }
        }

        // Calculate number of columns to perturb based on the percentage
        int numCandidateColumns = candidateColumnsIndices.size();
        int numToPerturb = (int) Math.round((noisePercentage / 100.0) * numCandidateColumns);
        if (numToPerturb == 0) {
            return relation;
        }
        int numNumericPerturbation = calculateNumOfNumericPerturbation(numToPerturb, numCandidateColumns, numericIndices.size(), stringIndices.size());
        int numStringPerturbation = numToPerturb - numNumericPerturbation;

        // Add Noise into data
        Map<Integer, List<String>> dataWithNumericErrors = perturbNumericColumnData(data, numNumericPerturbation, numericIndices, noiseInsidePercentage);
        Map<Integer, List<String>> dataWithAllErrors = perturbStringColumnData(dataWithNumericErrors, numStringPerturbation, stringIndices, noiseInsidePercentage);

        return new Relation(schema, dataWithAllErrors, relation.getKeyIndices(), relation.getForeignKeyIndices(), relation.getOverlappingColumnsIndices(), relation.getNumOfOverlappingRows());
    }


    /**
     * Applies numeric noise to selected numeric columns.
     *
     * @param data The data to perturb.
     * @param numNumericPerturbation The number of numeric columns to perturb.
     * @param numericIndices Indices of the columns containing numeric data.
     * @param noiseInsidePercentage The percentage of entries within a column to receive noise.
     * @return Data with numeric noise applied.
     */
    public Map<Integer, List<String>> perturbNumericColumnData(Map<Integer, List<String>> data, int numNumericPerturbation, List<Integer> numericIndices, int noiseInsidePercentage) {

        // Shuffle the lists of column indices to randomize which ones will be perturbed
        Collections.shuffle(numericIndices);

        // Add noise in correct number of columns (numNumericPerturbation)
        for (int i = 0; i < numNumericPerturbation; i++) {

            // Get the first column that will be perturbed
            int colIndex = numericIndices.get(i);
            List<String> column = data.get(colIndex);

            // Calculate mean, standard deviation and number of errors that will be added in column entries
            List<Double> numericColumn = createListOfDouble(column);
            double mean = calculateMean(numericColumn);
            double standardDeviation = calculateStandardDeviation(numericColumn, mean);
            int numOfErrors = (int) Math.round((double) (numericColumn.size() * noiseInsidePercentage) / 100);

            // Filter indices of Double values
            List<Integer> doubleIndices = new ArrayList<>();
            for (int rowIndex = 0; rowIndex < column.size(); rowIndex++) {
                if (isDouble(column.get(rowIndex))) {
                    doubleIndices.add(rowIndex);
                }
            }

            // Shuffle and pick the first numOfErrors indices to perturb
            Collections.shuffle(doubleIndices);
            List<Integer> indicesToModify = doubleIndices.subList(0, Math.min(numOfErrors, doubleIndices.size()));

            // Add noise to selected indices
            for (int rowIndex : indicesToModify) {
                String replacement = chooseNumericNoise(column.get(rowIndex), mean, standardDeviation);
                column.set(rowIndex, replacement);
            }
        }
        return data;
    }


    /**
     * Applies alphanumeric-based noise to selected alphanumeric columns.
     *
     * @param data The data to perturb.
     * @param numStringPerturbation The number of alphanumeric columns to perturb.
     * @param stringIndices Indices of columns containing alphanumeric data.
     * @param noiseInsidePercentage The percentage of entries within a column to receive noise.
     * @return Data with string noise applied.
     * @throws Exception If noise cannot be applied.
     */
    public Map<Integer, List<String>> perturbStringColumnData(Map<Integer, List<String>> data, int numStringPerturbation, List<Integer> stringIndices, int noiseInsidePercentage) throws Exception {

        // Shuffle the lists of column indices to randomize which ones will be perturbed
        Collections.shuffle(stringIndices);

        // Add Noise in correct number of Columns (numStringPerturbation)
        for (int i = 0; i < numStringPerturbation; i++) {

            // Get the first column that will be perturbed
            int colIndex = stringIndices.get(i);
            List<String> column = data.get(colIndex);

            // Randomly decide whether mapColumns will be applied (only method that is applied to a column, not to an entry)
            if (Math.random() < 1.0 / selectedStringMethods.size() && selectedStringMethods.contains("mapColumn")) {
                data.put(colIndex, mapColumn(column));
                continue; // move to the next column after mapColumn is applied
            }

            // Calculate number of errors that will be added in column entries
            int numOfErrors = (int) Math.round((double) (column.size() * noiseInsidePercentage) / 100);

            // Choose numOfErrors unique indices from the column to receive noise
            Set<Integer> indicesToModify = pickUniqueRandomIndices(column.size(), numOfErrors);

            // Add noise to selected indices
            for (int rowIndex : indicesToModify) {
                // Check which methods can be applied for entry
                String entry = column.get(rowIndex);
                List<String> applicableMethods = findApplicableMethods(entry);
                String replacement = chooseStringNoise(entry, applicableMethods);
                column.set(rowIndex, replacement);
            }
        }
        return data;
    }


    /**
     * Determines the type of noise to apply based on the data type and selected methods for row perturbation.
     *
     * @param entry The data entry to be perturbed.
     * @param attribute The attribute of the entry's column.
     * @param columnIndex The column index of the entry.
     * @param columnValues All values in the entry's column.
     * @return The perturbed data entry.
     * @throws Exception If noise cannot be applied.
     */
    public String chooseNoise(String entry, Attribute attribute, int columnIndex, List<String> columnValues) throws Exception {

        // Determine which Type of entry it is
        if (attribute.getDataType().equals(Type.DOUBLE)) {

            // Calculate mean and standardDeviation, but only if they have not been calculated yet
            double mean;
            double standardDeviation;

            if (statisticValuesOfColumns.containsKey(columnIndex)) {
                mean = statisticValuesOfColumns.get(columnIndex).getMean();
                standardDeviation = statisticValuesOfColumns.get(columnIndex).getStandardDeviation();
            } else {
                List<Double> doubleValues = createListOfDouble(columnValues);
                mean = calculateMean(doubleValues);
                standardDeviation = calculateStandardDeviation(doubleValues, mean);
                statisticValuesOfColumns.put(columnIndex, new StatisticValues(mean, standardDeviation));
            }

            // Check if it really is a Double value (since DOUBLE columns can include some alphanumeric values)
            try {
                Double.parseDouble(entry);
                return chooseNumericNoise(entry, mean, standardDeviation);
            } catch (NumberFormatException e) {
                return chooseNumericNoise("0", mean, standardDeviation);
            }
        } else {
            // For alphanumeric entry: find applicable Methods and apply one
            List<String> applicableMethods = findApplicableMethods(entry);
            return chooseStringNoise(entry, applicableMethods);
        }
    }


    /**
     * Randomly selects a numeric noise method and applies it to a given entry.
     *
     * @param entry The numeric data entry.
     * @param mean The mean value of the column for noise calculation.
     * @param standardDeviation The standard deviation of the column for noise calculation.
     * @return The perturbed numeric entry.
     */
    public String chooseNumericNoise(String entry, double mean, double standardDeviation) {

        // Choose random available method
        Random random = new Random();
        String selectedMethod;

        // If no specific method was chosen, pick from all existing ones, else pick one of the chosen ones
        if (selectedNumericMethods.isEmpty()) {
            selectedMethod = existingNumericMethods.get(random.nextInt(existingNumericMethods.size()));
        } else {
            selectedMethod = selectedNumericMethods.get(random.nextInt(selectedNumericMethods.size()));
        }

        // Apply method
        return applyNumericMethod(selectedMethod, entry, mean, standardDeviation);
    }


    /**
     * Randomly selects an alphanumeric noise method and applies it to a given entry.
     *
     * @param entry The alphanumeric data entry.
     * @param applicableMethods List of applicable alphanumeric noise methods.
     * @return The perturbed alphanumeric entry.
     * @throws Exception If noise cannot be applied.
     */
    public String chooseStringNoise(String entry, List<String> applicableMethods) throws Exception {

        // Get the intersection of applicableMethods and allSelectedStringMethods
        List<String> selectedApplicableMethods = new ArrayList<>(applicableMethods);
        selectedApplicableMethods.retainAll(selectedStringMethods);

        // Try the methods in the intersection
        String replacement = tryMethods(entry, selectedApplicableMethods);
        if (replacement != null) {
            return replacement; // Return if a valid replacement was found
        }

        // Get the remaining methods from applicableMethods
        List<String> remainingMethods = new ArrayList<>(applicableMethods);
        remainingMethods.removeAll(selectedApplicableMethods);

        // Try the remaining methods
        replacement = tryMethods(entry, remainingMethods);
        return Objects.requireNonNullElse(replacement, "");
    }


    /**
     * Applies a selected numeric noise method to a numeric entry.
     *
     * @param selectedMethod The numeric noise method to apply.
     * @param entry The numeric data entry to modify.
     * @param mean The mean value of the column, used in noise calculations.
     * @param standardDeviation The standard deviation of the column, used in noise calculations.
     * @return The perturbed numeric entry as a String.
     */
    private String applyNumericMethod(String selectedMethod, String entry, double mean, double standardDeviation) {
        return switch (selectedMethod) {
            case "changeValue" -> changeValue(entry, mean, standardDeviation);
            case "changeValueToOutlier" -> changeValueToOutlier(entry, mean, standardDeviation);
            default -> entry;
        };
    }


    /**
     * Applies a selected alphanumeric noise method to an alphanumeric entry.
     *
     * @param selectedMethod The alphanumeric noise method to apply.
     * @param entry The alphanumeric data entry to modify.
     * @return The perturbed alphanumeric entry.
     * @throws Exception If the selected method encounters an error.
     */
    private String applyStringMethod(String selectedMethod, String entry) throws Exception {
        return switch (selectedMethod) {
            case "replaceWithSynonyms" -> replaceWithSynonyms(entry);
            case "addRandomPrefix" -> addRandomPrefix(entry);
            case "replaceWithTranslation" -> replaceWithTranslation(entry, "en", "de");
            case "shuffleWords" -> shuffleWords(entry);
            case "generateMissingValue" -> generateMissingValue(entry);
            case "generatePhoneticError" -> generatePhoneticError(entry);
            case "generateOCRError" -> generateOCRError(entry);
            case "abbreviateDataEntry" -> abbreviateDataEntry(entry);
            case "changeFormat" -> changeFormat(entry);
            case "generateTypingError" -> generateTypingError(entry);
            case "generateRandomString" -> generateRandomString();
            default -> entry;
        };
    }


    /**
     * Calculates the number of numeric columns to perturb based on selected methods and column types.
     *
     * @param numToPerturb The total number of columns to perturb.
     * @param numOverlappingColumns The number of overlapping columns.
     * @param numOfNumericColumns The number of numeric columns in the data.
     * @param numOfStringColumns The number of alphanumeric columns in the data.
     * @return The number of numeric columns to perturb.
     */
    public int calculateNumOfNumericPerturbation(int numToPerturb ,int numOverlappingColumns, int numOfNumericColumns, int numOfStringColumns) {

        // Get number of alphanumeric and numeric Methods
        int numOfStringMethods = selectedStringMethods.size();
        int numOfNumericMethods = selectedNumericMethods.size();

        // If only numeric methods were selected, choose as many numeric columns as possible
        if (numOfStringMethods == 0) {
            return Math.min(numOfNumericColumns, numToPerturb);
        }

        // If only alphanumeric methods were selected, choose as little numeric columns as possible
        if (numOfNumericMethods == 0) {
            return numToPerturb - Math.min(numOfStringColumns, numToPerturb);
        }

        // Calculate the ratio of numeric Columns in the overlapping columns
        double percentageOfNumericColumns = (double) numOfNumericColumns / numOverlappingColumns;

        // Calculate the proposed ratio of numeric columns in the noisy columns
        int proposedNumericPerturbation = (int) Math.ceil(numToPerturb * percentageOfNumericColumns);

        // Adjust ratio if needed to make sure each numeric method can be applied
        if (proposedNumericPerturbation < numOfNumericMethods) {
            proposedNumericPerturbation = Math.min(numOfNumericColumns, numOfNumericMethods);
        }

        // Calculate the ratio of alphanumeric columns in the noisy columns
        int proposedStringPerturbation = numToPerturb - proposedNumericPerturbation;

        // Adjust ratio if needed to make sure each alphanumeric method can be applied
        if (proposedStringPerturbation < numOfStringMethods) {
            proposedStringPerturbation = Math.min(numOfStringColumns, numOfStringMethods);

            // Make sure to keep the numeric methods
            proposedNumericPerturbation = Math.max(numToPerturb - proposedStringPerturbation, Math.min(numOfNumericColumns, numOfNumericMethods));

            // If only two rows will be perturbed choose one of each Type if possible and if methods for both were selected
            if (numToPerturb == 2) {
                if (numOfStringColumns == 0) {
                    return numToPerturb;
                } else {
                    proposedNumericPerturbation = Math.min(1, proposedNumericPerturbation);
                }
            }
        }

        return Math.min(proposedNumericPerturbation, numOfNumericColumns);
    }


    /**
     * Checks if a given String is a Double value.
     *
     * @param string The object to check.
     * @return True if the object is a double, otherwise false.
     */
    public boolean isDouble(String string) {
        try {
            Double.valueOf(string);         // Try to convert to Double
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }


    /**
     * Selects a unique set of random indices.
     *
     * @param N The range of indices.
     * @param k The number of unique indices to select.
     * @return A set of unique random indices.
     */
    public Set<Integer> pickUniqueRandomIndices(int N, int k) {
        Set<Integer> selectedIndices = new HashSet<>();
        Random random = new Random();

        for (int i = N - k; i < N; i++) {
            int t = random.nextInt(i + 1);
            if (selectedIndices.contains(t)) {
                selectedIndices.add(i);
            } else {
                selectedIndices.add(t);
            }
        }
        return selectedIndices;
    }


    /**
     * Finds applicable noise methods for an alphanumeric entry based on its characteristics.
     *
     * @param entry The alphanumeric entry.
     * @return A list of applicable noise methods.
     */
    public List<String> findApplicableMethods(String entry) {

        // Check if entry is empty string
        if (entry.isEmpty()) {
            return Arrays.asList("generateMissingValue", "generateRandomString");  // generateMissingValue returns: "-" in case of empty input String
        }

        // Add the methods that can always be applied
        List<String> applicableMethods = new ArrayList<>(Arrays.asList("abbreviateDataEntry", "generateMissingValue",
                "addRandomPrefix", "replaceWithTranslation", "replaceWithSynonyms"));

        // Add the specific methods if they can be applied
        if (entry.split("[\\s-_]+").length > 1) {
            applicableMethods.add("shuffleWords");
        }
        if (containsPhoneticSimilarity(entry, phoneticSimilarities)) {
            applicableMethods.add("generatePhoneticError");
        }
        if (containsOCRSimilarity(entry, ocrSimilarities)) {
            applicableMethods.add("generateOCRError");
        }
        if (containsFormat(entry, Arrays.asList(replaceableSymbols))) {
            applicableMethods.add("changeFormat");
        }
        if (containsKeyboardSimilarity(entry, keyboardSimilarities)) {
            applicableMethods.add("generateTypingError");
        }
        return applicableMethods;
    }


    /**
     * Checks if any character in an alphanumeric entry matches a given condition.
     *
     * @param entry The alphanumeric data entry to check.
     * @param condition The condition to test each character against.
     * @return True if at least one character in the entry matches the condition, otherwise false.
     */
    private boolean containsMatchingCondition(String entry, Predicate<Character> condition) {

        // Iterate each character
        for (char c : entry.toCharArray()) {

            // Check if condition is fulfilled
            if (condition.test(c)) {
                return true;
            }
        }
        return false;
    }


    /**
     * Checks if an alphanumeric entry contains characters that match any keyboard similarity defined above.
     *
     * @param entry The alphanumeric data entry to check.
     * @param map The map of keyboard similarity pairs.
     * @return True if the entry contains characters matching keyboard similarities, otherwise false.
     */
    public boolean containsKeyboardSimilarity(String entry, Map<Character, List<Character>> map) {
        return containsMatchingCondition(entry.toLowerCase(), map::containsKey);
    }


    /**
     * Checks if an alphanumeric entry contains characters that match any phonetic similarity defined above.
     *
     * @param entry The alphanumeric data entry to check.
     * @param map The map of phonetic similarity pairs.
     * @return True if the entry contains characters matching phonetic similarities, otherwise false.
     */
    public boolean containsPhoneticSimilarity(String entry, Map<Character, Character> map) {
        return containsMatchingCondition(entry.toLowerCase(), map::containsKey);
    }


    /**
     * Checks if an alphanumeric entry contains characters that match any OCR similarity defined above.
     *
     * @param entry The alphanumeric data entry to check.
     * @param map The map of OCR similarity pairs.
     * @return True if the entry contains characters matching OCR similarities, otherwise false.
     */
    public boolean containsOCRSimilarity(String entry, Map<Character, Character> map) {
        return containsMatchingCondition(entry, map::containsKey);
    }


    /**
     * Checks if an alphanumeric entry contains any format characters listed in a format list above.
     *
     * @param entry The alphanumeric data entry to check.
     * @param list The list of format-related characters to match.
     * @return True if the entry contains characters from the list, otherwise false.
     */
    public boolean containsFormat(String entry, List<String> list) {
        return containsMatchingCondition(entry, c -> list.contains(String.valueOf(c)));
    }


    /**
     * Attempts to apply a list of alphanumeric noise methods to an entry until a valid replacement is found.
     *
     * @param entry The alphanumeric data entry to perturb.
     * @param methods A list of alphanumeric noise methods to attempt.
     * @return A perturbed version of the entry, or null if no valid replacement is found.
     * @throws Exception If any method in the list encounters an error.
     */
    private String tryMethods(String entry, List<String> methods) throws Exception {
        Random random = new Random();
        String replacement;

        // Try each method until a valid replacement is found or the list is empty
        while (!methods.isEmpty()) {

            // Pick a random method from the list and apply it
            String selectedMethod = methods.get(random.nextInt(methods.size()));
            replacement = applyStringMethod(selectedMethod, entry);

            if ((!replacement.equals(entry) && !replacement.isEmpty())
                    || (replacement.isEmpty() && selectedMethod.equals("generateMissingValue"))) {
                return replacement;
            } else {
                methods.remove(selectedMethod);
            }
        }

        // Return null if no valid replacement was found
        return null;
    }
}


