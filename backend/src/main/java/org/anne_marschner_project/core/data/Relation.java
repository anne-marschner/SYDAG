package org.anne_marschner_project.core.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents a relation, which is a collection of attributes and their corresponding data.
 * A relation can either be a source relation with no overlap or a result relation that has overlapping columns or rows.
 */
public class Relation {

    private Map<Integer, Attribute> schema; // A map of indices and their attributes
    private Map<Integer,List<String>> data; // A map of column indices (=keys) and Lists of column entries (=values)
    private List<Integer> keyIndices; // Holds the indices of the key columns
    private List<Integer> foreignKeyIndices; // Holds the indices of the foreign key columns
    private List<Integer> overlappingColumnsIndices; // Holds all Indices of the overlapping columns with other created Relations
    private Integer numOfOverlappingRows; // Holds the number of rows that overlap with other result Relation
    private List<Integer> keysBeforeNormalization = new ArrayList<>(); // Holds the keys of the original Relation that was normalized


    /**
     * Constructs a Source Relation where no overlap exists.
     *
     * @param schema a map of column indices and their attributes.
     * @param data a map of column indices and their lists of data entries.
     */
    public Relation(Map<Integer, Attribute> schema, Map<Integer,List<String>> data) {
        this.schema = schema;
        this.data = data;
        this.keyIndices = new ArrayList<>();
        this.foreignKeyIndices = new ArrayList<>();
        this.overlappingColumnsIndices = null;
        this.numOfOverlappingRows = null;
    }


    /**
     * Constructs a Result Relation with overlap in columns.
     *
     * @param schema a map of column indices and their attributes.
     * @param data a map of column indices and their lists of data entries.
     * @param keyIndices a list of indices that represent the key columns.
     * @param overlappingColumnsIndices a list of indices that represent overlapping columns with other relations.
     */
    public Relation(Map<Integer, Attribute> schema, Map<Integer,List<String>> data, List<Integer> keyIndices,
                    List<Integer> overlappingColumnsIndices) {
        this.schema = schema;
        this.data = data;
        this.keyIndices = keyIndices;
        this.foreignKeyIndices = new ArrayList<>();
        this.overlappingColumnsIndices = overlappingColumnsIndices;
        this.numOfOverlappingRows = null;
    }


    /**
     * Constructs a Result Relation with overlap in rows.
     *
     * @param schema a map of column indices and their attributes.
     * @param data a map of column indices and their lists of data entries.
     * @param keyIndices a list of indices that represent the key columns.
     * @param numOfOverlappingRows the number of rows that overlap with other result relations.
     */
    public Relation(Map<Integer, Attribute> schema, Map<Integer,List<String>> data, List<Integer> keyIndices,
                    Integer numOfOverlappingRows) {
        this.schema = schema;
        this.data = data;
        this.keyIndices = keyIndices;
        this.foreignKeyIndices = new ArrayList<>();
        this.overlappingColumnsIndices = null;
        this.numOfOverlappingRows = numOfOverlappingRows;
    }


    /**
     * Constructs a Result Relation with unknown type of overlap.
     *
     * @param schema a map of column indices and their attributes.
     * @param data a map of column indices and their lists of data entries.
     * @param keyIndices a list of indices that represent the key columns.
     * @param overlappingColumnsIndices a list of indices that represent overlapping columns with other relations.
     * @param numOfOverlappingRows the number of rows that overlap with other result relations.
     */
    public Relation(Map<Integer, Attribute> schema, Map<Integer,List<String>> data, List<Integer> keyIndices,
                    List<Integer> overlappingColumnsIndices, Integer numOfOverlappingRows) {
        this.schema = schema;
        this.data = data;
        this.keyIndices = keyIndices;
        this.foreignKeyIndices = new ArrayList<>();
        this.overlappingColumnsIndices = overlappingColumnsIndices;
        this.numOfOverlappingRows = numOfOverlappingRows;
    }


    /**
     * Constructs a Result Relation with foreign keys.
     *
     * @param schema a map of column indices and their attributes.
     * @param data a map of column indices and their lists of data entries.
     * @param keyIndices a list of indices that represent the key columns.
     * @param foreignKeyIndices a list of indices that represent the foreign key columns.
     * @param overlappingColumnsIndices a list of indices that represent overlapping columns with other relations.
     */
    public Relation(Map<Integer, Attribute> schema, Map<Integer,List<String>> data, List<Integer> keyIndices,
                    List<Integer> foreignKeyIndices, List<Integer> overlappingColumnsIndices) {
        this.schema = schema;
        this.data = data;
        this.keyIndices = keyIndices;
        this.foreignKeyIndices = foreignKeyIndices;
        this.overlappingColumnsIndices = overlappingColumnsIndices;
        this.numOfOverlappingRows = null;
    }


    /**
     * Constructs a Result Relation with foreign keys and unknown overlap.
     *
     * @param schema a map of column indices and their attributes.
     * @param data a map of column indices and their lists of data entries.
     * @param keyIndices a list of indices that represent the key columns.
     * @param foreignKeyIndices a list of indices that represent the foreign key columns.
     * @param overlappingColumnsIndices a list of indices that represent overlapping columns with other relations.
     * @param numOfOverlappingRows the number of rows that overlap with other result relations.
     */
    public Relation(Map<Integer, Attribute> schema, Map<Integer,List<String>> data, List<Integer> keyIndices,
                    List<Integer> foreignKeyIndices, List<Integer> overlappingColumnsIndices, Integer numOfOverlappingRows) {
        this.schema = schema;
        this.data = data;
        this.keyIndices = keyIndices;
        this.foreignKeyIndices = foreignKeyIndices;
        this.overlappingColumnsIndices = overlappingColumnsIndices;
        this.numOfOverlappingRows = numOfOverlappingRows;
    }


    /**
     * Returns the schema of this relation, which is a map of column indices and their attributes.
     *
     * @return a map of column indices and their Attribute objects.
     */
    public Map<Integer, Attribute> getSchema() {
        return schema;
    }


    /**
     * Returns the data of this relation, which is a map of column indices and their lists of data entries.
     *
     * @return a map of column indices and their lists of column data.
     */
    public Map<Integer,List<String>> getData() {
        return data;
    }


    /**
     * Returns the list of indices of overlapping columns.
     *
     * @return a list of column indices that overlap with other relations.
     */
    public List<Integer> getOverlappingColumnsIndices() {
        return overlappingColumnsIndices;
    }


    /**
     * Returns the number of overlapping rows with other result relations.
     *
     * @return the number of overlapping rows.
     */
    public Integer getNumOfOverlappingRows() {
        return numOfOverlappingRows;
    }


    /**
     * Sets the schema for this relation.
     *
     * @param schema the new schema to be set.
     */
    public void setSchema(Map<Integer, Attribute> schema) {
        this.schema = schema;
    }


    /**
     * Returns the list of indices that represent the key columns of this relation.
     *
     * @return a list of indices representing the key columns.
     */
    public List<Integer> getKeyIndices() {
        return keyIndices;
    }


    /**
     * Sets the list of key indices for this relation.
     *
     * @param keyIndices a list of indices representing the key columns.
     */
    public void setKeyIndices(List<Integer> keyIndices) {
        this.keyIndices = keyIndices;
    }


    /**
     * Returns the list of indices that represent the foreign key columns of this relation.
     *
     * @return a list of indices representing the foreign key columns.
     */
    public List<Integer> getForeignKeyIndices() {
        return foreignKeyIndices;
    }

    /**
     * Returns the list of key indices that represent the key columns of the original relation before Normalization.
     *
     * @return a list of indices representing the key columns.
     */
    public List<Integer> getKeysBeforeNormalization() {
        return keysBeforeNormalization;
    }

    /**
     * Sets the list of key indices that represent the key columns of the original relation before Normalization.
     *
     * @param keysBeforeNormalization a list of indices representing the key columns.
     */
    public void setKeysBeforeNormalization(List<Integer> keysBeforeNormalization) {
        this.keysBeforeNormalization = keysBeforeNormalization;
    }
}


