package org.anne_marschner_project.core.structure;

import org.anne_marschner_project.core.data.Attribute;
import org.anne_marschner_project.core.data.Relation;
import org.anne_marschner_project.core.data.Type;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.*;

/**
 * The Join class provides functionality to join columns within a given {@link Relation}.
 */
public class Join {


    /**
     * Joins specified columns of a Relation based on the given percentage of columns to join.
     *
     * @param relation       The Relation object containing the schema and data.
     * @param joinPercentage The percentage of columns to join from the overlapping columns.
     * @param separator      The separator dividing the relation.
     * @return A new Relation with the specified columns joined.
     */
    public Relation joinColumns(Relation relation, Integer joinPercentage, char separator) {

        // Get schema and data from relation
        Map<Integer, Attribute> schema = relation.getSchema();

        // Determine the indices of the columns that are eligible for joining
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

        // Choose how many of the overlapping columns should be joined into a multivalued attribute
        int numOfColumnsToJoin = (int) Math.ceil((joinPercentage / 100.0) * columnsToConsider.size());

        // Choose separator to use for multivalued object
        char joinSeparator = chooseJoinSeparator(separator);

        // Execute the decided number of joins
        return executeJoins(relation, numOfColumnsToJoin, columnsToConsider, joinSeparator);
    }


    /**
     * Executes the joining process on the Relation object by joining a specified number
     * of columns.
     *
     * @param relation          The Relation to perform joins on.
     * @param numOfColumnsToJoin The number of columns to join based on the percentage.
     * @param columnsToConsider  A list of column indices to consider for joining.
     * @param joinSeparator      The separator to use when joining column values.
     * @return A Relation object with the joined columns.
     */
    public Relation executeJoins(Relation relation, Integer numOfColumnsToJoin, List<Integer> columnsToConsider, char joinSeparator){

        Map<Integer, Attribute> schema = relation.getSchema();
        Map<Integer, List<String>> data = relation.getData();
        List<Integer> keys = relation.getKeyIndices();
        Set<Integer> joinedColumnIndices = new HashSet<>();

        // Join until the desired number of joined columns in achieved
        while (joinedColumnIndices.size() < numOfColumnsToJoin) {
            int[] indicesToJoin = findBestJoin(relation, columnsToConsider, joinedColumnIndices);
            Integer firstColumnIndex = indicesToJoin[0];
            Integer secondColumnIndex = indicesToJoin[1];

            // Create multivalued attribute from the two columns
            Attribute joinedAttribute;
            if (relation.getSchema().entrySet().iterator().next().getValue().getColumnName() != null) {
                String firstAttributeName = schema.get(firstColumnIndex).getColumnName();
                String secondAttributeName = schema.get(secondColumnIndex).getColumnName();
                String joinedAttributeName = firstAttributeName + joinSeparator + secondAttributeName;
                joinedAttribute = new Attribute(joinedAttributeName, schema.get(firstColumnIndex).getSharedType(schema.get(secondColumnIndex)));
            } else {
                joinedAttribute = new Attribute(null, Type.STRING);
            }

            // Join the column entries
            List<String> firstColumn = data.get(firstColumnIndex);
            List<String> secondColumn = data.get(secondColumnIndex);
            List<String> joinedColumn = new ArrayList<>();

            // Iterate through both columns and combine entries
            for (int i = 0; i < Math.min(firstColumn.size(), secondColumn.size()); i++) {
                String firstEntry = firstColumn.get(i);
                String secondEntry = secondColumn.get(i);
                String joinedEntry = firstEntry + joinSeparator + secondEntry;
                joinedColumn.add(joinedEntry);
            }

            // Set the joined column and attribute at firstColumnIndex and remove the ones at secondColumnIndex
            data.put(firstColumnIndex, joinedColumn);
            data.remove(secondColumnIndex);
            schema.put(firstColumnIndex, joinedAttribute);
            schema.remove(secondColumnIndex);
            if (keys.contains(secondColumnIndex)) {
                if (!keys.contains(firstColumnIndex)) {
                    keys.add(firstColumnIndex);
                    Collections.sort(keys);
                }
                keys.remove(secondColumnIndex);
            }

            // Update values to keep track
            joinedColumnIndices.add(firstColumnIndex);
            joinedColumnIndices.add(secondColumnIndex);
            columnsToConsider.remove(secondColumnIndex);
        }

        // Return the modified relation
        return new Relation(schema, data, keys, relation.getOverlappingColumnsIndices(),
                relation.getNumOfOverlappingRows());
    }


    /**
     * Finds the best pair of columns to join based on data type, distance, and whether
     * columns are already part of a join.
     *
     * @param relation            The Relation containing schema and data.
     * @param columnsToConsider   List of indices of columns that can be joined.
     * @param joinedColumnIndices Set of indices for columns already part of a join.
     * @return An array of two integers representing the best pair of column indices to join.
     */
    public int[] findBestJoin(Relation relation, List<Integer> columnsToConsider, Set<Integer> joinedColumnIndices) {

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
                if (joinedColumnIndices.contains(index1) || joinedColumnIndices.contains(index2)) { // check if already part of join
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
     * Determines the separator to use for joining column values based on the input.
     *
     * @param separator The initial separator of the csv.
     * @return Returns ";" if the initial separator is ","; otherwise, returns ",".
     */
    public char chooseJoinSeparator(char separator) {
        if (separator == ',') {
            return ';';
        } else {
            return ',';
        }
    }
}
