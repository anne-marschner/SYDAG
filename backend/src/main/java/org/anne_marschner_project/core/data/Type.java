package org.anne_marschner_project.core.data;

import java.util.List;

/**
 * Represents the data types for values in a column.
 * The two possible types are STRING for alphanumeric values and DOUBLE for numeric values.
 */
public enum Type {

    STRING, // All values that are alphanumeric
    DOUBLE; // All values that are numeric


    /**
     * Determines the Type of a column based on the values in that column.
     * The method analyzes a list of column values and returns either {@link Type#STRING} or {@link Type#DOUBLE}.
     * The decision is based on the ratio of numeric to non-numeric values in the list.
     *
     * @param columnValues the list of values from the column to be analyzed
     * @return {@link Type#DOUBLE} if at least 75% of the values are numeric, otherwise {@link Type#STRING}
     */
    public static Type determineType(List<String> columnValues) {

        // Counts for number of DOUBLE and STRING values
        int doubleCount = 0;
        int stringCount = 0;
        int totalCount = columnValues.size();

        // Loop through all column values
        for (String value : columnValues) {

            // Try to parse value to Double
            try {
                // Count if Double was parsed successfully
                Double.parseDouble(value);
                doubleCount++;

                // If at least 75 % are Double: return DOUBLE
                if (doubleCount >= Math.ceil(totalCount * 0.75)) {
                    return Type.DOUBLE;
                }
            } catch (NumberFormatException e) {
                // Count if Double was not parsed
                stringCount++;

                //  If more than 25 % are String: return String
                if (stringCount > totalCount * 0.25) {
                    return Type.STRING;
                }
            }
        }

        // Check if enough Double values were found
        if (doubleCount >= Math.ceil(totalCount * 0.75)) {
            return Type.DOUBLE;
        } else {
            return Type.STRING;
        }
    }
}
