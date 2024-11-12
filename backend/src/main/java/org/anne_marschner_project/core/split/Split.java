package org.anne_marschner_project.core.split;

import org.anne_marschner_project.core.data.Attribute;
import org.anne_marschner_project.core.data.Relation;

import java.util.*;

/**
 * The Split class provides functionality to split a {@link Relation} into two parts, either horizontally or vertically.
 * The split operation can be configured to include a specified percentage of row or column overlap between the resulting relations.
 */
public class Split {


    /**
     * Splits a given {@link Relation} based on the specified split type and overlap percentages.
     *
     * @param source               the source {@link Relation} to be split.
     * @param columnOverlapPercentage the percentage of columns to overlap if performing a vertical split (0-100).
     * @param rowOverlapPercentage the percentage of rows to overlap if performing a horizontal split (0-100).
     * @param splitType            the type of split to perform, either "Horizontal" or "Vertical".
     * @return a List of {@link Relation} objects, representing the split relation according to the specified parameters.
     */
    public List<Relation> splitRelation(Relation source, int columnOverlapPercentage, int rowOverlapPercentage, String splitType) {

        // Choose which type of split has to be applied
        if (splitType.equals("Horizontal")) {
            return splitHorizontally(source, rowOverlapPercentage);
        } else {
            return splitVertically(source, columnOverlapPercentage);
        }
    }

    /**
     * Splits the {@link Relation} horizontally into two relations with a specified row overlap.
     *
     * @param source The source {@link Relation} to be split.
     * @param overlapPercentage The percentage of rows to overlap between the two relations (0-100).
     *
     * @return A List of two {@link Relation} objects. The first contains the top portion
     *         with overlapping rows, the second contains the bottom portion with the same overlap.
     */
    public List<Relation> splitHorizontally(Relation source, int overlapPercentage) {

        // Get schema and data from Relation
        Map<Integer, Attribute> sourceSchema = source.getSchema();
        Map<Integer, List<String>> sourceData = source.getData();

        // Get number of rows in Relation
        int numRows = sourceData.get(0).size();

        // Calculate the number of rows that should overlap
        int numOverlapRows = (int) Math.round(numRows * (overlapPercentage / 100.0));
        int numNonOverlapRows = numRows - numOverlapRows;

        // Calculate how many of the non-overlapping rows go into left and right relations
        int numLeftNonOverlapRows = (int) Math.ceil((double) numNonOverlapRows / 2);
        int numLeftRows = numLeftNonOverlapRows + numOverlapRows;

        // Choose a random starting index for overlap within the range of rows
        Random random = new Random();
        int overlapStartIndex = random.nextInt(numRows - numOverlapRows + 1);
        System.out.println("Overlap Start Index: " + overlapStartIndex);
        System.out.println("Num of overlapping Rows: " + numOverlapRows);

        // Create new Maps for the data
        Map<Integer, List<String>> dataLeft = new HashMap<>();
        Map<Integer, List<String>> dataRight = new HashMap<>();

        // Fill both Relations
        for (Map.Entry<Integer, List<String>> column : sourceData.entrySet()) {
            // Get data from one Column
            List<String> fullColumn = column.getValue();

            // The rows starting at the chosen index are the overlapping ones and put into both Relations
            List<String> leftColumnData = new ArrayList<>(fullColumn.subList(overlapStartIndex, overlapStartIndex + numOverlapRows));
            List<String> rightColumnData = new ArrayList<>(fullColumn.subList(overlapStartIndex, overlapStartIndex + numOverlapRows));

            // The first half of the not overlapping Rows is put into leftColumndata
            // Try adding values from above the overlapping data
            int firstMissingLeftRows = numLeftRows - leftColumnData.size();
            if (firstMissingLeftRows > 0) {
                leftColumnData.addAll(fullColumn.subList(0, Math.min(overlapStartIndex, firstMissingLeftRows)));
            }
            // Try adding values from below the overlapping data if not enough have been added
            int secondMissingLeftRows = numLeftRows - leftColumnData.size();
            if (secondMissingLeftRows > 0) {
                leftColumnData.addAll(fullColumn.subList(overlapStartIndex + numOverlapRows, overlapStartIndex + numOverlapRows + secondMissingLeftRows));
            }

            // The second half of the not overlapping Rows is put into rightColumndata
            // Try adding values from above the overlapping data
            if (firstMissingLeftRows < overlapStartIndex) {
                rightColumnData.addAll(fullColumn.subList(firstMissingLeftRows, overlapStartIndex));
            }
            if (secondMissingLeftRows > 0) {
                rightColumnData.addAll(fullColumn.subList(overlapStartIndex + numOverlapRows + secondMissingLeftRows, numRows));
            } else {
                rightColumnData.addAll(fullColumn.subList(overlapStartIndex + numOverlapRows, numRows));
            }
            // add split columns to new Relations
            dataLeft.put(column.getKey(), leftColumnData);
            dataRight.put(column.getKey(), rightColumnData);
        }

        // Schema stays the same in both Relations (make deep copy)
        Map<Integer, Attribute> schemaLeft = new HashMap<>();
        Map<Integer, Attribute> schemaRight = new HashMap<>();
        for (Map.Entry<Integer, Attribute> entry : sourceSchema.entrySet()) {
            schemaLeft.put(entry.getKey(), new Attribute(entry.getValue().getColumnName(), entry.getValue().getDataType()));
            schemaRight.put(entry.getKey(), new Attribute(entry.getValue().getColumnName(), entry.getValue().getDataType()));
        }

        // Create new Relations and return as a pair
        Relation relationLeft = new Relation(schemaLeft, dataLeft, new ArrayList<>(source.getKeyIndices()), numOverlapRows);
        Relation relationRight = new Relation(schemaRight, dataRight, new ArrayList<>(source.getKeyIndices()), numOverlapRows);

        return new ArrayList<>(Arrays.asList(relationLeft, relationRight));
    }



    /**
     * Splits the {@link Relation} vertically into two relations with a specified column overlap.
     *
     * @param source The source {@link Relation} to split.
     * @param overlapPercentage Percentage of non-key columns to overlap (0-100).
     *
     * @return A List of two {@link Relation} objects, both containing key columns and some overlapping non-key columns.
     */
    public List<Relation> splitVertically(Relation source, int overlapPercentage) {

        // Get schema, data and keys from Relation
        Map<Integer, Attribute> sourceSchema = source.getSchema();
        Map<Integer, List<String>> sourceData = source.getData();
        List<Integer> keyIndices = source.getKeyIndices();

        // Get total number of Columns
        int numColumns = sourceSchema.size();

        // Calculate the number of additional columns that should overlap based on the columnOverlap percentage
        int numNonKeyColumns = numColumns - keyIndices.size();
        int numOverlapColumns = (int) Math.round((overlapPercentage / 100.0) * numNonKeyColumns);

        // Create a list of non-key columns (for random selection of overlap columns)
        List<Integer> nonKeyColumnIndices = new ArrayList<>();
        for (int i = 0; i < numColumns; i++) {
            if (!keyIndices.contains(i)) {
                nonKeyColumnIndices.add(i);
            }
        }

        // Randomly select overlap columns
        Collections.shuffle(nonKeyColumnIndices);
        List<Integer> overlapColumnIndices = nonKeyColumnIndices.subList(0, numOverlapColumns); // randomly chooses first entries

        // Include key indices in the overlap columns to ensure they are in both relations
        overlapColumnIndices.addAll(keyIndices);

        // Initialize schema and data for the two new relations
        Map<Integer, Attribute> schemaLeft = new HashMap<>();
        Map<Integer, Attribute> schemaRight = new HashMap<>();
        Map<Integer, List<String>> dataLeft = new HashMap<>();
        Map<Integer, List<String>> dataRight = new HashMap<>();

        // Add key and overlap columns to both relations (deep copy since the columns are used by both relations)
        for (Integer overlapIndex : overlapColumnIndices) {
            schemaLeft.put(overlapIndex, new Attribute(sourceSchema.get(overlapIndex).getColumnName(), sourceSchema.get(overlapIndex).getDataType()));
            schemaRight.put(overlapIndex, new Attribute(sourceSchema.get(overlapIndex).getColumnName(), sourceSchema.get(overlapIndex).getDataType()));
            dataLeft.put(overlapIndex, new ArrayList<>(sourceData.get(overlapIndex)));
            dataRight.put(overlapIndex, new ArrayList<>(sourceData.get(overlapIndex)));
        }

        // Distribute the remaining non-overlap columns (happens randomly since non-key column indices were shuffled)) (no deep copy needed since it only belongs to one relation)
        boolean addToLeft = true;
        List<Integer> nonOverlapColumnIndices = nonKeyColumnIndices.subList(numOverlapColumns, nonKeyColumnIndices.size()); // rest of the List
        for (Integer nonOverlapIndex : nonOverlapColumnIndices) {
            if (addToLeft) {
                schemaLeft.put(nonOverlapIndex, sourceSchema.get(nonOverlapIndex));
                dataLeft.put(nonOverlapIndex, sourceData.get(nonOverlapIndex));
            } else {
                schemaRight.put(nonOverlapIndex, sourceSchema.get(nonOverlapIndex));
                dataRight.put(nonOverlapIndex, sourceData.get(nonOverlapIndex));
            }
            // Alternate between left and right
            addToLeft = !addToLeft;
        }

        // Create the new relations and return as a pair
        Relation leftRelation = new Relation(schemaLeft, dataLeft, new ArrayList<>(keyIndices), new ArrayList<>(overlapColumnIndices));
        Relation rightRelation = new Relation(schemaRight, dataRight, new ArrayList<>(keyIndices), new ArrayList<>(overlapColumnIndices));

        return new ArrayList<>(Arrays.asList(leftRelation, rightRelation));
    }
}
