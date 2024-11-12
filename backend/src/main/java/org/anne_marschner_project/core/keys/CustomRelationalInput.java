package org.anne_marschner_project.core.keys;


import de.metanome.algorithm_integration.input.InputIterationException;
import de.metanome.algorithm_integration.input.RelationalInput;
import org.anne_marschner_project.core.data.Relation;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides a custom implementation of the {@link RelationalInput} interface,
 * enabling iteration over rows of a given {@link Relation} object.
 * This class is used to provide relational data to HyUCC.
 */
public class CustomRelationalInput implements RelationalInput {
    private Relation relation;
    private int currentRowIndex;
    private int numRows;
    private String relationName;
    private List<String> columnNames;


    /**
     * Constructs a CustomRelationalObject with a specified relation and relation name.
     * Automatically retrieves column names from the schema and calculates the row count.
     *
     * @param relation The {@link Relation} object representing the data to iterate over
     * @param relationName The name of the relation as a string
     */
    public CustomRelationalInput(Relation relation, String relationName) {
        this.relation = relation;
        this.relationName = relationName;
        this.currentRowIndex = 0;
        this.numRows = relation.getData().values().iterator().next().size(); // Annahme: alle Spalten sind gleich lang

        // Use indices as column names
        this.columnNames = relation.getSchema().keySet().stream()
                .map(Object::toString)
                .toList();
    }


    /**
     * Checks if there are more rows to iterate over in the relation.
     *
     * @return {@code true} if there are additional rows and {@code false} otherwise
     */
    @Override
    public boolean hasNext() {
        return currentRowIndex < numRows;
    }


    /**
     * Retrieves the next row in the relation as a list of strings.
     *
     * @return A list of strings representing the values of the next row
     * @throws InputIterationException if there are no more rows to retrieve
     */
    @Override
    public List<String> next() throws InputIterationException {

        if (!hasNext()) {
            throw new InputIterationException("No more rows.");
        }

        List<String> row = new ArrayList<>();
        for (Integer colIndex : relation.getSchema().keySet()) {
            String cellValue = relation.getData().get(colIndex).get(currentRowIndex);
            row.add(cellValue);
        }

        currentRowIndex++;
        return row;
    }


    /**
     * Returns the number of columns in the relation.
     *
     * @return The number of columns as an integer
     */
    @Override
    public int numberOfColumns() {
        return columnNames.size();
    }


    /**
     * Returns the name of the relation.
     *
     * @return The relation name as a string
     */
    @Override
    public String relationName() {
        return relationName;
    }


    /**
     * Provides the list of column names in the relation.
     *
     * @return A list of strings representing the column names
     */
    @Override
    public List<String> columnNames() {
        return columnNames;
    }


    /**
     * Closes any resources associated with this input.
     * In this implementation, no specific resources need to be released.
     */
    @Override
    public void close() {}
}
