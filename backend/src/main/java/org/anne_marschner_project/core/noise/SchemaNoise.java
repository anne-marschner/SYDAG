package org.anne_marschner_project.core.noise;

import java.util.*;

import org.anne_marschner_project.core.data.Attribute;
import org.anne_marschner_project.core.data.Relation;

/**
 * The SchemaNoise class provides methods for perturbing the schema of a {@link Relation} by modifying
 * column names according to user-selected noise methods.
 */
public class SchemaNoise extends Noise {

    // List of Methods chosen by th User
    private List<String> allSelectedMethods = new ArrayList<>();

    // List of Methods that have been chosen but not applied yet
    private List<String> availableMethods = new ArrayList<>();


    /**
     * Constructs a SchemaNoise object with the specified list of selected noise methods.
     *
     * @param allSelectedMethods a list of method names selected by the user for perturbing schema
     */
    public SchemaNoise (List<String> allSelectedMethods) {
        this.allSelectedMethods = allSelectedMethods;
        this.availableMethods = new ArrayList<>(allSelectedMethods);
    }


    /**
     * Constructs an empty SchemaNoise object.
     */
    public SchemaNoise () {
        this.allSelectedMethods = null;
        this.availableMethods = null;
    }



    /**
     * Perturbs the schema of the given relation by modifying the column names.
     * It selects columns from the relation based on the specified noise percentage
     * and applies the noise methods chosen by the user to perturb the column names.
     *
     * @param relation the {@link Relation} whose schema is to be perturbed
     * @param noisePercentage the percentage of columns to perturb
     * @param schemaNoiseInKeys whether the keys should be perturbed
     * @param deleteSchema whether headers should be deleted
     * @return a {@link Relation} object with the perturbed schema
     * @throws Exception if an error occurs during the schema perturbation
     */
    public Relation perturbSchema(Relation relation, int noisePercentage, boolean schemaNoiseInKeys, boolean deleteSchema) throws Exception {

        // Get schema and overlapping columns indices from Relation
        Map<Integer, Attribute> schema = relation.getSchema();
        List<Integer> overlappingColumnsIndices = relation.getOverlappingColumnsIndices();

        // If deleteSchema was selected return dataset without headers
        if (deleteSchema) {
            return deleteHeaders(relation);
        }

        // Determine the indices of the columns that are eligible for perturbation
        List<Integer> columnsToConsider;
        if (relation.getNumOfOverlappingRows() != null) { // overlappingColumnsIndices == null
            // Due to horizontal Splitting, all columns are eligible, since they are all duplicates
            columnsToConsider = new ArrayList<>(schema.keySet());
        } else {
            // Otherwise, only consider the overlapping columns from Vertical Splitting
            columnsToConsider = new ArrayList<>(overlappingColumnsIndices);
        }
        if (!schemaNoiseInKeys) {
            columnsToConsider.removeAll(relation.getKeyIndices());
            columnsToConsider.removeAll(relation.getForeignKeyIndices());
        }

        // Calculate number of columns to perturb based on the percentage
        int numColumnsToConsider = columnsToConsider.size();
        int numToPerturb = (int) Math.round((noisePercentage / 100.0) * numColumnsToConsider);

        // Shuffle the list of column indices to randomize which ones will be perturbed
        Collections.shuffle(columnsToConsider);

        // Perturb the column names for the selected columns
        for (int i = 0; i < numToPerturb; i++) {
            int colIndex = columnsToConsider.get(i);
            Attribute attribute = schema.get(colIndex);
            System.out.println("Column got Schema Noise: " + colIndex);

            // Perturb the column name
            String perturbedName = chooseNoise(attribute.getColumnName());

            // Update the schema with the new perturbed attribute name
            Attribute newAttribute = new Attribute(perturbedName, attribute.getDataType());
            schema.put(colIndex, newAttribute);
        }

        // Return the new relation with the perturbed schema
        relation.setSchema(schema);
        return relation;
    }


    /**
     * Deletes the headers of the relation by setting them to null.
     *
     * @param relation the {@link Relation} which headers should be removed.
     * @return the {@link Relation} withouut headers.
     */
    public Relation deleteHeaders(Relation relation) {
        for (Map.Entry<Integer, Attribute> entry: relation.getSchema().entrySet()) {
            relation.getSchema().get(entry.getKey()).setColumnName(null);
        }
        return relation;
    }


    /**
     * Selects a noise method to perturb the given column name. The method chosen depends on the column's properties
     * and the available noise methods.
     *
     * @param columnName the original name of the column to be perturbed
     * @return a new column name generated by applying one of the noise methods
     * @throws Exception if an error occurs during noise generation
     */
    public String chooseNoise(String columnName) throws Exception {

        // If all methods have been used, refill List with selected methods
        refillMethodsIfNeeded();

        // Check if column Name consists of multiple words or vowels
        boolean containsMultipleWords = columnName.split("[\\s-_]+").length > 1;
        boolean containsVowels = !removeVowels(columnName).equals(columnName);
        boolean removedShuffleMethod = false;
        boolean removedVowelMethod = false;

        // Check if shuffleWords can be applied first since it can only be applied in certain cases
        if (availableMethods.contains("shuffleWords")) {
            if (containsMultipleWords) {
                availableMethods.remove("shuffleWords"); // Remove if it has been used
                return shuffleWords(columnName);
            } else {
                availableMethods.remove("shuffleWords"); // Will be added again after random Choice of other method
                removedShuffleMethod = true; // To remember that method needs to be added again
            }
        }

        // Add other optional Methods again, if availableMethods are empty because of removing "shuffleWords"
        if (availableMethods.isEmpty()) {
            availableMethods.addAll(new ArrayList<>(allSelectedMethods));
            // Remove shuffleWords because it cannot be used at the moment...
            availableMethods.removeAll(List.of("shuffleWords"));
            // ...but make sure to add shuffleWords again at the end
            if (allSelectedMethods.contains("shuffleWords")) {
                removedShuffleMethod = true;
            }
        }

        // Check removeVowels next since it cannot always be applied
        if (availableMethods.contains("removeVowels")) {
            if (containsVowels) {
                availableMethods.remove("removeVowels");
                if (removedShuffleMethod) {
                    availableMethods.add("shuffleWords");
                }
                return removeVowels(columnName);
            }
            availableMethods.remove("removeVowels"); // Will be added again after random Choice of other method
            removedVowelMethod = true; // To remember that method needs to be added again
        }

        // Add other optional Methods again, if availableMethods are empty because of removing "shuffleWords" or "removeVowels"
        if (availableMethods.isEmpty()) {
            availableMethods.addAll(new ArrayList<>(allSelectedMethods));
            // Remove removeVowels because it cannot be used at the moment...
            availableMethods.removeAll(List.of("removeVowels", "shuffleWords"));
            // ...but make sure to add removeVowels again at the end
            if (allSelectedMethods.contains("removeVowels")) {
                removedVowelMethod = true;
            }
            if (allSelectedMethods.contains("shuffleWords")) {
                removedShuffleMethod = true;
            }
        }

        // If shuffleWords and removeVowels don't provide change, choose random method
        String replacement = findReplacementFromAvailableMethods(columnName, removedShuffleMethod, removedVowelMethod);
        if (replacement != null) {
            return replacement;
        }

        // If none of the unused methods work: Test if "shuffleWords" or "removeVowels" might work again if they part of allselectedMethods
        if (allSelectedMethods.contains("shuffleWords") && containsMultipleWords) {
            if (removedVowelMethod) {
                availableMethods.add("removeVowels");
            }
            return shuffleWords(columnName);
        }
        if (allSelectedMethods.contains("removeVowels") && containsVowels) {
            if (removedShuffleMethod) {
                availableMethods.add("shuffleWords");
            }
            return removeVowels(columnName);
        }

        // Try other already used methods (case: there were other methods than the special ones, but they do not provide change)
        replacement = findReplacementFromUsedMethods(columnName, removedShuffleMethod, removedVowelMethod);
        if (replacement != null) {
            return replacement;
        }

        // If no method generated a change, use a fallback method that guarantees change
        return useFallbackMethod(columnName, removedShuffleMethod, removedVowelMethod);
    }


    /**
     * Refills the list of available methods if it has been exhausted.
     */
    private void refillMethodsIfNeeded() {
        if (availableMethods.isEmpty()) {
            availableMethods = new ArrayList<>(allSelectedMethods);
        }
    }


    /**
     * Shuffles the list of methods to randomize their order.
     *
     * @param methodList the list of methods to be shuffled
     * @return the shuffled list of methods
     */
    private List<String> shuffleMethodList(List<String> methodList) {
        if (!methodList.isEmpty()) {
            Collections.shuffle(methodList);
        }
        return methodList;
    }


    /**
     * Finds the methods that have already been used from the selected methods.
     *
     * @param availableMethods the list of available methods
     * @return a list of methods that have already been used
     */
    private List<String> findAlreadyUsedMethods (List<String> availableMethods) {
        List<String> alreadyUsedMethods = new ArrayList<>(allSelectedMethods);
        alreadyUsedMethods.removeAll(availableMethods);
        alreadyUsedMethods.removeAll(Arrays.asList("removeVowels", "shuffleWords"));
        return alreadyUsedMethods;
    }


    /**
     * Applies the selected noise method to the given column name.
     *
     * @param selectedMethod the name of the noise method to be applied
     * @param columnName the column name to which the method will be applied
     * @return the perturbed column name
     * @throws Exception if an error occurs during method application
     */
    private String applyMethod(String selectedMethod, String columnName) throws Exception {
        return switch (selectedMethod) {
            case "generateRandomString" -> generateRandomString();
            case "abbreviateFirstLetters" -> abbreviateFirstLetters(columnName);
            case "abbreviateRandomLength" -> abbreviateRandomLength(columnName);
            case "addRandomPrefix" -> addRandomPrefix(columnName);
            case "shuffleLetters" -> shuffleLetters(columnName);
            case "replaceWithSynonyms" -> replaceWithSynonyms(columnName);
            case "replaceWithTranslation" -> replaceWithTranslation(columnName, "en", "de");
            default -> columnName;
        };
    }


    /**
     * Attempts to find a replacement for the column name using the available noise methods.
     * If no suitable method is found, it attempts to apply previously used methods or a fallback method.
     *
     * @param columnName the original column name to be perturbed
     * @param removedShuffleMethod indicates whether shuffleWords method was previously removed
     * @param removedVowelMethod indicates whether removeVowels method was previously removed
     * @return a perturbed column name if a method is found, otherwise null
     * @throws Exception if an error occurs during method application
     */
    private String findReplacementFromAvailableMethods(String columnName, boolean removedShuffleMethod, boolean removedVowelMethod) throws Exception {

        // Shuffle the available methods (could be empty if allSelectedMethods only contains "removeVowels" and "shuffleWords")
        availableMethods = shuffleMethodList(availableMethods);

        for (String selectedMethod : availableMethods) {
            String replacement = applyMethod(selectedMethod, columnName);

            // If a different column name is generated, return it
            if (!replacement.equals(columnName) && !replacement.equals(" ") && !replacement.equals("")) {

                // Remove used method so that it will not be used again if other methods are left tu be used
                availableMethods.remove(selectedMethod);

                // Add  the methods that were removed because they could not be applied on the given columnName
                if (removedShuffleMethod) {
                    availableMethods.add("shuffleWords");
                }
                if (removedVowelMethod) {
                    availableMethods.add("removeVowels");
                }
                return replacement;
            }
        }

        // Return null if no replacement was found
        return null;
    }


    /**
     * Attempts to find a replacement for the column name using methods that have already been used.
     *
     * @param columnName the original column name to be perturbed
     * @param removedShuffleMethod indicates whether shuffleWords method was previously removed
     * @param removedVowelMethod indicates whether removeVowels method was previously removed
     * @return a perturbed column name if a method is found, otherwise null
     * @throws Exception if an error occurs during method application
     */
    private String findReplacementFromUsedMethods(String columnName, boolean removedShuffleMethod, boolean removedVowelMethod) throws Exception {

        // Get all Methods that were already used
        List<String> alreadyUsedMethods = findAlreadyUsedMethods(availableMethods);

        // Shuffle for random selection
        alreadyUsedMethods = shuffleMethodList(alreadyUsedMethods);

        // Try each method in the shuffled list
        for (String selectedMethod : alreadyUsedMethods) {
            String replacement = applyMethod(selectedMethod, columnName);

            // If a different column name is generated, return it
            if (!replacement.equals(columnName) && !(replacement.isEmpty())) {

                // Add  the methods that were removed because they could not be applied on the given columnName
                if (removedShuffleMethod) {
                    availableMethods.add("shuffleWords");
                }
                if (removedVowelMethod) {
                    availableMethods.add("removeVowels");
                }
                return replacement;
            }
        }

        // Return null if no replacement was found
        return null;
    }


    /**
     * Uses a fallback method to generate a perturbed column name if no suitable method could be applied.
     *
     * @param columnName the original column name to be perturbed
     * @param removedShuffleMethod indicates whether shuffleWords method was previously removed
     * @param removedVowelMethod indicates whether removeVowels method was previously removed
     * @return a perturbed column name generated by a fallback method
     * @throws Exception if an error occurs during fallback method application
     */
    private String useFallbackMethod(String columnName, boolean removedShuffleMethod, boolean removedVowelMethod) throws Exception {

        // If no method generated a change, select a fallback method that guarantees change
        List<String> guaranteedChangeMethods = List.of("generateRandomString", "addRandomPrefix");
        String fallbackMethod = guaranteedChangeMethods.get(new Random().nextInt(guaranteedChangeMethods.size()));

        // Add  the methods that were removed because they could not be applied on the given columnName
        if (removedShuffleMethod) {
            availableMethods.add("shuffleWords");
        }
        if (removedVowelMethod) {
            availableMethods.add("removeVowels");
        }

        return applyMethod(fallbackMethod, columnName);
    }
}
