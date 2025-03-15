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
     * @param dataType   the data type of the column, represented by the Type enum
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
     * Returns the data type of the attribute.
     *
     * @return the Type of the attribute
     */
    public Type getDataType() {
        return type;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }


    /**
     * Returns the data type that represents both attributes.
     *
     * @return STRING if one or both attributes have String Values, else return DOUBLE.
     */
    public Type getSharedType(Attribute attribute) {
        if ((this.type).equals(Type.STRING) || attribute.getDataType().equals(Type.STRING)) {
            return Type.STRING;
        } else {
            return Type.DOUBLE;
        }
    }
}

