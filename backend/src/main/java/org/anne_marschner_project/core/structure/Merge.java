package org.anne_marschner_project.core.structure;

import org.anne_marschner_project.core.data.Attribute;
import org.anne_marschner_project.core.data.Relation;
import org.anne_marschner_project.core.data.Type;

import java.util.*;

/**
 * The Merge class provides functionality to merge columns within a given {@link Relation}.
 */
public class Merge {


    /**
     * Merges specified columns of a Relation based on the given percentage of columns to merge.
     *
     * @param relation       The Relation object containing the schema and data.
     * @param mergePercentage The percentage of columns to merge from the overlapping columns.
     * @param separator      The separator dividing the relation.
     * @return A new Relation with the specified columns merged.
     */
    public Relation mergeColumns(Relation relation, Integer mergePercentage, char separator) {

        // Get schema and data from relation
        Map<Integer, Attribute> schema = relation.getSchema();

        // Determine the indices of the columns that are eligible for merging
        List<Integer> columnsToConsider;
        if (relation.getOverlappingColumnsIndices() == null) {
            // Due to horizontal Splitting, all columns are eligible, since they are all duplicates
            columnsToConsider = new ArrayList<>(schema.keySet());
        } else {
            // Otherwise, only consider the overlapping columns from Vertical Splitting
            columnsToConsider = new ArrayList<>(relation.getOverlappingColumnsIndices());
        }

        // At least two columns are needed
        if (columnsToConsider.size() < 2) {
            return relation;
        }

        // Choose how many of the overlapping columns should be merged into a multivalued attribute
        int numOfColumnsToMerge = (int) Math.ceil((mergePercentage / 100.0) * columnsToConsider.size());

        // Choose separator to use for multivalued object
        char mergeSeparator = chooseMergeSeparator(separator);

        // Execute the decided number of merges
        return executeMerge(relation, numOfColumnsToMerge, columnsToConsider, mergeSeparator);
    }


    /**
     * Executes the merging process on the Relation object by merging a specified number
     * of columns.
     *
     * @param relation          The Relation to perform merges on.
     * @param numOfColumnsToMerge The number of columns to merge based on the percentage.
     * @param columnsToConsider  A list of column indices to consider for merging.
     * @param mergeSeparator      The separator to use when merging column values.
     * @return A Relation object with the merged columns.
     */
    public Relation executeMerge(Relation relation, Integer numOfColumnsToMerge, List<Integer> columnsToConsider, char mergeSeparator){

        Map<Integer, Attribute> schema = relation.getSchema();
        Map<Integer, List<String>> data = relation.getData();
        List<Integer> keys = relation.getKeyIndices();
        Set<Integer> mergedColumnIndices = new HashSet<>();

        // Merge until the desired number of merged columns in achieved
        while (mergedColumnIndices.size() < numOfColumnsToMerge) {
            int[] indicesToMerge = findBestMerge(relation, columnsToConsider, mergedColumnIndices);
            Integer firstColumnIndex = indicesToMerge[0];
            Integer secondColumnIndex = indicesToMerge[1];

            // Create multivalued attribute from the two columns
            Attribute mergedAttribute;
            if (relation.getSchema().entrySet().iterator().next().getValue().getColumnName() != null) {
                String firstAttributeName = schema.get(firstColumnIndex).getColumnName();
                String secondAttributeName = schema.get(secondColumnIndex).getColumnName();
                String mergedAttributeName = firstAttributeName + mergeSeparator + secondAttributeName;
                mergedAttribute = new Attribute(mergedAttributeName, schema.get(firstColumnIndex).getSharedType(schema.get(secondColumnIndex)));
            } else {
                mergedAttribute = new Attribute(null, Type.STRING);
            }

            // Merge the column entries
            List<String> firstColumn = data.get(firstColumnIndex);
            List<String> secondColumn = data.get(secondColumnIndex);
            List<String> mergedColumn = new ArrayList<>();

            // Iterate through both columns and combine entries
            for (int i = 0; i < Math.min(firstColumn.size(), secondColumn.size()); i++) {
                String firstEntry = firstColumn.get(i);
                String secondEntry = secondColumn.get(i);
                String mergedEntry = firstEntry + mergeSeparator + secondEntry;
                mergedColumn.add(mergedEntry);
            }

            // Set the merged column and attribute at firstColumnIndex and remove the ones at secondColumnIndex
            data.put(firstColumnIndex, mergedColumn);
            data.remove(secondColumnIndex);
            schema.put(firstColumnIndex, mergedAttribute);
            schema.remove(secondColumnIndex);
            if (keys.contains(secondColumnIndex)) {
                if (!keys.contains(firstColumnIndex)) {
                    keys.add(firstColumnIndex);
                    Collections.sort(keys);
                }
                keys.remove(secondColumnIndex);
            }

            // Update values to keep track
            mergedColumnIndices.add(firstColumnIndex);
            mergedColumnIndices.add(secondColumnIndex);
            columnsToConsider.remove(secondColumnIndex);
        }

        // Return the modified relation
        return new Relation(schema, data, keys, relation.getOverlappingColumnsIndices(),
                relation.getNumOfOverlappingRows());
    }


    /**
     * Finds the best pair of columns to merge based on data type, distance, and whether
     * columns are already part of a merge.
     *
     * @param relation            The Relation containing schema and data.
     * @param columnsToConsider   List of indices of columns that can be merged.
     * @param mergedColumnIndices Set of indices for columns already part of a merge.
     * @return An array of two integers representing the best pair of column indices to merge.
     */
    public int[] findBestMerge(Relation relation, List<Integer> columnsToConsider, Set<Integer> mergedColumnIndices) {

        Map<Integer, Attribute> schema = relation.getSchema();
        int bestIndex1 = -1;
        int bestIndex2 = -1;
        double bestScore = Double.NEGATIVE_INFINITY;

        // Calculate max distance for weighting factor
        int maxDistance = columnsToConsider.stream().max(Integer::compare).orElse(0) -
                columnsToConsider.stream().min(Integer::compare).orElse(0);

        // Loop all pairs in columnsToConsider
        for (int i = 0; i < columnsToConsider.size(); i++) {
            for (int j = i + 1; j < columnsToConsider.size(); j++) {
                int index1 = columnsToConsider.get(i);
                int index2 = columnsToConsider.get(j);
                Attribute attr1 = schema.get(index1);
                Attribute attr2 = schema.get(index2);

                // Calculate Score
                double score = 0;
                if (attr1.getDataType().equals(attr2.getDataType())) { // Check equal types
                    score += 1;
                }
                double normalizedDistance = (double) Math.abs(index1 - index2) / maxDistance;  // check distance
                score -= normalizedDistance;
                if (mergedColumnIndices.contains(index1) || mergedColumnIndices.contains(index2)) { // check if already part of merge
                    score -= 0.5;
                }

                if (score > bestScore) {
                    bestIndex1 = index1;
                    bestIndex2 = index2;
                    bestScore = score;
                }
            }
        }

        // return best combination
        return (bestIndex1 != -1 && bestIndex2 != -1) ? new int[]{bestIndex1, bestIndex2} : null;
    }


    /**
     * Determines the separator to use for merging column values based on the input.
     *
     * @param separator The initial separator of the csv.
     * @return Returns ";" if the initial separator is ","; otherwise, returns ",".
     */
    public char chooseMergeSeparator(char separator) {
        if (separator == ',') {
            return ';';
        } else {
            return ',';
        }
    }
}
