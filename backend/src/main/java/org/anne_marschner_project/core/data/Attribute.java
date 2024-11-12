package org.anne_marschner_project.core.data;


/**
 * Represents an attribute of a {@link Relation}, including its column name and data type.
 * This class is used to define metadata for columns within a {@link Relation}.
 */
public class Attribute {
    private String columnName;
    private Type type;


    /**
     * Constructs an Attribute with a specified column name and data type.
     *
     * @param columnName the name of the column represented by this attribute
     * @param dataType   the data type of the column, represented by the {@link Type} enum
     */
    public Attribute(String columnName, Type dataType) {
        this.columnName = columnName;
        this.type = dataType;
    }

    /**
     * Returns the name of the column represented by this attribute.
     *
     * @return the column name as a String
     */
    public String getColumnName() {
        return columnName;
    }


    /**
     * Returns the data type of the column.
     *
     * @return the {@link Type} of the column
     */
    public Type getDataType() {
        return type;
    }
}

