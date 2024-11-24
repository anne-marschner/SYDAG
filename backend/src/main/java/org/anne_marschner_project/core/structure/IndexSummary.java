package org.anne_marschner_project.core.structure;

import java.util.Collections;
import java.util.List;

/**
 * The IndexSummary class summarizes key metadata about a relation's structure.
 * Specifically which columns serve as keys and foreign keys, as well as which columns are included in the relation.
 */
public class IndexSummary {

    private List<Integer> columnIndices; // Holds the indices of the columns that are part of the relation
    private List<Integer> keyIndices; // Holds the indices of columns that are the keys
    private List<Integer> foreignKeyIndices; // Holds the indices of columns that are the foreign keys


    /**
     * Constructs an IndexSummary object with specified column indices and key indices.
     *
     * @param columnIndices the list of column indices that are part of the relation.
     * @param keyIndices the list of indices representing the primary key columns.
     */
    public IndexSummary(List<Integer> columnIndices, List<Integer> keyIndices) {
        this.columnIndices = columnIndices;
        this.keyIndices = keyIndices;
    }


    /**
     * Constructs an IndexSummary object with specified column indices, key indices,
     * and foreign key indices.
     *
     * @param columnIndices the list of column indices that are part of the relation.
     * @param keyIndices the list of indices representing the primary key columns.
     * @param foreignKeyIndices the list of indices representing the foreign key columns.
     */
    public IndexSummary(List<Integer> columnIndices, List<Integer> keyIndices, List<Integer> foreignKeyIndices) {
        this.columnIndices = columnIndices;
        this.keyIndices = keyIndices;
        this.foreignKeyIndices = foreignKeyIndices;
    }


    /**
     * Returns the list of primary key column indices.
     *
     * @return a list of integers representing the indices of primary key columns.
     */
    public List<Integer> getKeyIndices() {
        return keyIndices;
    }


    /**
     * Returns the list of all column indices that are part of the relation.
     *
     * @return a list of integers representing the indices of columns in the relation.
     */
    public List<Integer> getColumnIndices() {
        return columnIndices;
    }


    /**
     * Returns the list of foreign key column indices.
     *
     * @return a list of integers representing the indices of foreign key columns.
     */
    public List<Integer> getForeignKeys() {
        return foreignKeyIndices;
    }

    /**
     * Sets the list of foreign key column indices.
     *
     * @param foreignKeyIndices the list of foreign key indices.
     */
    public void setForeignKeyIndices(List<Integer> foreignKeyIndices) {
        this.foreignKeyIndices = foreignKeyIndices;
    }

    /**
     * Adds the list of column indices.
     *
     * @param indices the list of indices to add.
     */
    public void addColumnIndices(List<Integer> indices) {
        for (Integer index : indices) {
            if (!this.columnIndices.contains(index)) {
                this.columnIndices.add(index);
            }
        }
    }
}

