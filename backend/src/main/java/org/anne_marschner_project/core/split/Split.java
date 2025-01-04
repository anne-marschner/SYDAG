package org.anne_marschner_project.core.split;

import org.anne_marschner_project.core.data.Attribute;
import org.anne_marschner_project.core.data.Relation;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
     * @param rowDistribution      the percentage of remaining rows that will be put in one dataset (0-100).
     * @param columnDistribution   the percentage of remaining columns that will be put in one dataset (0-100).
     * @param splitType            the type of split to perform, either "Horizontal" or "Vertical".
     * @return a List of {@link Relation} objects, representing the split relation according to the specified parameters.
     */
    // Idea: add distribution of data (remainingRowDistribution, remainingColumnDistribution)
    public List<Relation> splitRelation(Relation source, Integer columnOverlapPercentage, Integer rowOverlapPercentage, Integer columnDistribution, Integer rowDistribution, String splitType, String overlapType) {

        // Choose which type of split has to be applied
        if (splitType.equals("Horizontal")) {
            return overlapType.equals("Mixed Overlap")
                    ? splitHorizontallyWithMixedOverlap(source, rowOverlapPercentage, rowDistribution)
                    : splitHorizontally(source, rowOverlapPercentage, rowDistribution);
        } else {
            return splitVertically(source, columnOverlapPercentage, columnDistribution);
        }
    }


    /**
     * Splits the {@link Relation} horizontally into two relations with a specified row overlap.
     *
     * @param source The source {@link Relation} to be split.
     * @param overlapPercentage The percentage of rows to overlap between the two relations (0-100).
     * @param rowDistribution   The percentage of remaining rows that will be put in top dataset (0-100).
     *
     * @return A List of two new {@link Relation} objects. The first contains the top portion
     *         with overlapping rows, the second contains the bottom portion with the same overlap.
     */
    public List<Relation> splitHorizontally(Relation source, Integer overlapPercentage, Integer rowDistribution) {

        // Get schema and data from Relation
        Map<Integer, Attribute> sourceSchema = source.getSchema();
        Map<Integer, List<String>> sourceData = source.getData();

        // Get number of rows in Relation
        int numRows = sourceData.get(0).size();

        // Calculate the number of rows that should overlap
        int numOverlapRows = (int) Math.round(numRows * (overlapPercentage / 100.0));
        int numNonOverlapRows = numRows - numOverlapRows;

        // Calculate how many of the non-overlapping rows go into left and right relations
        int numLeftNonOverlapRows = (int) Math.round((double) numNonOverlapRows * ((double) rowDistribution / 100));
        int numLeftRows = numLeftNonOverlapRows + numOverlapRows;

        // Choose a random starting index for overlap within the range of rows
        Random random = new Random();
        int overlapStartIndex = random.nextInt(numRows - numOverlapRows + 1);
        System.out.println("Overlap Start Index: " + overlapStartIndex);
        System.out.println("Num of overlapping Rows: " + numOverlapRows);

        // Create new Maps for the data
        Map<Integer, List<String>> dataTop = new HashMap<>();
        Map<Integer, List<String>> dataBottom = new HashMap<>();

        // Fill both Relations
        for (Map.Entry<Integer, List<String>> column : sourceData.entrySet()) {
            // Get data from one Column
            List<String> fullColumn = column.getValue();

            // The rows starting at the chosen index are the overlapping ones and put into both Relations
            List<String> topColumnData = new ArrayList<>(fullColumn.subList(overlapStartIndex, overlapStartIndex + numOverlapRows));
            List<String> bottomColumnData = new ArrayList<>(fullColumn.subList(overlapStartIndex, overlapStartIndex + numOverlapRows));

            // The first half of the not overlapping Rows is put into leftColumndata
            // Try adding values from above the overlapping data
            int firstMissingLeftRows = numLeftRows - topColumnData.size();
            if (firstMissingLeftRows > 0) {
                topColumnData.addAll(fullColumn.subList(0, Math.min(overlapStartIndex, firstMissingLeftRows)));
            }
            // Try adding values from below the overlapping data if not enough have been added
            int secondMissingLeftRows = numLeftRows - topColumnData.size();
            if (secondMissingLeftRows > 0) {
                topColumnData.addAll(fullColumn.subList(overlapStartIndex + numOverlapRows, overlapStartIndex + numOverlapRows + secondMissingLeftRows));
            }

            // The second half of the not overlapping Rows is put into rightColumndata
            // Try adding values from above the overlapping data
            if (firstMissingLeftRows < overlapStartIndex) {
                bottomColumnData.addAll(fullColumn.subList(firstMissingLeftRows, overlapStartIndex));
            }
            if (secondMissingLeftRows > 0) {
                bottomColumnData.addAll(fullColumn.subList(overlapStartIndex + numOverlapRows + secondMissingLeftRows, numRows));
            } else {
                bottomColumnData.addAll(fullColumn.subList(overlapStartIndex + numOverlapRows, numRows));
            }
            // add split columns to new Relations
            dataTop.put(column.getKey(), topColumnData);
            dataBottom.put(column.getKey(), bottomColumnData);
        }

        // Schema stays the same in both Relations (make deep copy)
        Map<Integer, Attribute> schemaTop = new HashMap<>();
        Map<Integer, Attribute> schemaBottom = new HashMap<>();
        for (Map.Entry<Integer, Attribute> entry : sourceSchema.entrySet()) {
            schemaTop.put(entry.getKey(), new Attribute(entry.getValue().getColumnName(), entry.getValue().getDataType()));
            schemaBottom.put(entry.getKey(), new Attribute(entry.getValue().getColumnName(), entry.getValue().getDataType()));
        }

        // Create new Relations and return as a pair
        Relation relationTop = new Relation(schemaTop, dataTop, new ArrayList<>(source.getKeyIndices()), numOverlapRows);
        Relation relationBottom = new Relation(schemaBottom, dataBottom, new ArrayList<>(source.getKeyIndices()), numOverlapRows);

        return new ArrayList<>(Arrays.asList(relationTop, relationBottom));
    }


    /**
     * Splits the {@link Relation} horizontally into two relations with random overlapping rows.
     *
     * @param source The source {@link Relation} to be split.
     * @param overlapPercentage The percentage of rows to overlap between the two relations (0-100).
     * @param rowDistribution   The percentage of remaining rows that will be put in the top dataset (0-100).
     *
     * @return A List of two new {@link Relation} objects. The first contains the top portion
     *         with overlapping rows, the second contains the bottom portion with the same overlap.
     */
    public List<Relation> splitHorizontallyWithMixedOverlap(Relation source, Integer overlapPercentage, Integer rowDistribution) {

        // Get schema and data from Relation
        Map<Integer, Attribute> sourceSchema = source.getSchema();
        Map<Integer, List<String>> sourceData = source.getData();

        // Get number of rows in Relation
        int numRows = sourceData.get(0).size(); // data.values().iterator().next().size();

        // Calculate the number of rows that should overlap
        int numOverlapRows = (int) Math.round(numRows * (overlapPercentage / 100.0));
        int numNonOverlapRows = numRows - numOverlapRows;

        // Randomly select indices for overlapping rows
        List<Integer> allIndices = IntStream.range(0, numRows).boxed().collect(Collectors.toList());
        Collections.shuffle(allIndices);
        List<Integer> overlapIndices = allIndices.subList(0, numOverlapRows).stream().sorted().toList(); // new ArrayList<>(allIndices.subList(0, numOverlapRows));

        // Remaining indices after overlap
        List<Integer> nonOverlapIndices = new ArrayList<>(allIndices.subList(numOverlapRows, allIndices.size()));

        // Randomly divide remaining rows based on rowDistribution
        int numLeftNonOverlapRows = (int) Math.round(numNonOverlapRows * (rowDistribution / 100.0));

        // For Top Indices first add overlapping indices, then add the sorted
        List<Integer> topIndices = new ArrayList<>(overlapIndices); // Start with overlap
        topIndices.addAll(nonOverlapIndices.subList(0, numLeftNonOverlapRows).stream().sorted().toList());  // topIndices.addAll(nonOverlapIndices.subList(0, numLeftNonOverlapRows));
        List<Integer> bottomIndices = new ArrayList<>(overlapIndices); // Start with overlap
        bottomIndices.addAll(nonOverlapIndices.subList(numLeftNonOverlapRows, nonOverlapIndices.size()).stream().sorted().toList());

        // Create new Maps for the data
        Map<Integer, List<String>> dataTop = new HashMap<>();
        Map<Integer, List<String>> dataBottom = new HashMap<>();

        // Fill both Relations
        for (Map.Entry<Integer, List<String>> column : sourceData.entrySet()) {
            List<String> fullColumn = column.getValue();
            List<String> topColumnData = topIndices.stream().map(fullColumn::get).collect(Collectors.toList());
            List<String> bottomColumnData = bottomIndices.stream().map(fullColumn::get).collect(Collectors.toList());

            dataTop.put(column.getKey(), topColumnData);
            dataBottom.put(column.getKey(), bottomColumnData);
        }

        // Schema stays the same in both Relations (make deep copy)
        Map<Integer, Attribute> schemaTop = new HashMap<>();
        Map<Integer, Attribute> schemaBottom = new HashMap<>();
        for (Map.Entry<Integer, Attribute> entry : sourceSchema.entrySet()) {
            schemaTop.put(entry.getKey(), new Attribute(entry.getValue().getColumnName(), entry.getValue().getDataType()));
            schemaBottom.put(entry.getKey(), new Attribute(entry.getValue().getColumnName(), entry.getValue().getDataType()));
        }

        // Create new Relations and return as a pair
        Relation relationTop = new Relation(schemaTop, dataTop, new ArrayList<>(source.getKeyIndices()), numOverlapRows);
        Relation relationBottom = new Relation(schemaBottom, dataBottom, new ArrayList<>(source.getKeyIndices()), numOverlapRows);

        return new ArrayList<>(Arrays.asList(relationTop, relationBottom));
    }



    /**
     * Splits the {@link Relation} vertically into two relations with a specified column overlap.
     *
     * @param source The source {@link Relation} to split.
     * @param overlapPercentage Percentage of non-key columns to overlap (0-100).
     * @param columnDistribution the percentage of remaining columns that will be put in left dataset (0-100).
     *
     * @return A list of two new {@link Relation} objects, both containing key columns and some overlapping non-key columns.
     */
    public List<Relation> splitVertically(Relation source, Integer overlapPercentage, Integer columnDistribution) {

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
        List<Integer> overlapColumnIndices = new ArrayList<>(nonKeyColumnIndices.subList(0, numOverlapColumns)); // randomly chooses first entries

        // Include key indices in the overlap columns to ensure they are in both relations
        overlapColumnIndices.addAll(keyIndices);
        System.out.println("Overlapping Columns: " + overlapColumnIndices);

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

        // Get the Indices of the remaining columns that need to be perturbed
        List<Integer> nonOverlapColumnIndices = nonKeyColumnIndices.subList(numOverlapColumns, nonKeyColumnIndices.size()); // rest of the list

        // Calculate the number of columns to distribute based on columnDistribution percentage
        int numColumnsForLeft = (int) Math.round((columnDistribution / 100.0) * nonOverlapColumnIndices.size());

        // Add the specified percentage of columns to the left relation (happens randomly since non-key column indices were shuffled)
        for (int i = 0; i < nonOverlapColumnIndices.size(); i++) {
            Integer nonOverlapIndex = nonOverlapColumnIndices.get(i);
            if (i < numColumnsForLeft) {
                schemaLeft.put(nonOverlapIndex, sourceSchema.get(nonOverlapIndex));
                dataLeft.put(nonOverlapIndex, sourceData.get(nonOverlapIndex));
            } else {
                schemaRight.put(nonOverlapIndex, sourceSchema.get(nonOverlapIndex));
                dataRight.put(nonOverlapIndex, sourceData.get(nonOverlapIndex));
            }
        }

        // Create the new relations and return as a pair
        Relation leftRelation = new Relation(schemaLeft, dataLeft, new ArrayList<>(keyIndices), new ArrayList<>(overlapColumnIndices), source.getNumOfOverlappingRows());
        Relation rightRelation = new Relation(schemaRight, dataRight, new ArrayList<>(keyIndices), new ArrayList<>(overlapColumnIndices), source.getNumOfOverlappingRows());

        return new ArrayList<>(Arrays.asList(leftRelation, rightRelation));
    }
}
