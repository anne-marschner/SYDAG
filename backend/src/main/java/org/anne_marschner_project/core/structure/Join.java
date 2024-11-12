package org.anne_marschner_project.core.structure;

import org.anne_marschner_project.core.data.Attribute;
import org.anne_marschner_project.core.data.Relation;
import org.anne_marschner_project.core.data.Type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * The Join class provides functionality to join columns within a given {@link Relation}.
 * This class is useful for joining data by combining columns into a multivalued attribute.
 */
public class Join {


    /**
     * Joins two columns within the specified {@link Relation} into a multivalued attribute.
     *
     * @param relation The {@link Relation} object whose columns will be joined.
     * @return A modified {@link Relation} object where two columns have been combined into a multivalued attribute.
     */
    public Relation joinColumns(Relation relation) {

        // Get schema and data from Relation
        Map<Integer, Attribute> schema = relation.getSchema();
        Map<Integer, List<String>> data = relation.getData();

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

        // Choose two random column indices from columnsToConsider
        Collections.shuffle(columnsToConsider);
        int firstColumnIndex = columnsToConsider.get(0);
        int secondColumnIndex = columnsToConsider.get(1);

        // Create multivalued attribute from the two columns
        String firstAttributeName = schema.get(firstColumnIndex).getColumnName();
        String secondAttributeName = schema.get(secondColumnIndex).getColumnName();
        String joinedAttributeName = "{" + firstAttributeName + "_" + secondAttributeName + "}";
        Attribute joinedAttribute = new Attribute(joinedAttributeName, Type.STRING);

        // Join the column entries
        List<String> firstColumn = data.get(firstColumnIndex);
        List<String> secondColumn = data.get(secondColumnIndex);
        List<String> joinedColumn = new ArrayList<>();

        // Iterate through both columns and combine entries
        for (int i = 0; i < Math.min(firstColumn.size(), secondColumn.size()); i++) {
            String firstEntry = firstColumn.get(i);
            String secondEntry = secondColumn.get(i);
            String joinedEntry = "{" + firstEntry + "_" + secondEntry + "}";
            joinedColumn.add(joinedEntry);
        }

        // Set the joined column and attribute at firstColumnIndex and remove the ones at secondColumnIndex
        data.put(firstColumnIndex, joinedColumn);
        data.remove(secondColumnIndex);
        schema.put(firstColumnIndex, joinedAttribute);
        schema.remove(secondColumnIndex);

        // Return the modified relation
        return new Relation(schema, data, relation.getKeyIndices(), relation.getOverlappingColumnsIndices(),
                relation.getNumOfOverlappingRows());
    }
}
