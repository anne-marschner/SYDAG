package org.anne_marschner_project.core.noise;

import org.anne_marschner_project.core.data.Relation;

/**
 * The StatisticValues class represents basic statistical values calculated for a column of a {@link Relation},
 * specifically the mean and standard deviation.
 */
public class StatisticValues {

    private final double mean;
    private final double standardDeviation;


    /**
     * Constructs a StatisticValues instance with specified mean and standard deviation values.
     *
     * @param mean the mean value of the column.
     * @param standardDeviation the standard deviation of the column.
     */
    public StatisticValues (double mean, double standardDeviation) {
        this.mean = mean;
        this.standardDeviation = standardDeviation;
    }


    /**
     * Returns the mean value of a column.
     *
     * @return the mean value.
     */
    public double getMean() {
        return mean;
    }


    /**
     * Returns the standard deviation of the column.
     *
     * @return the standard deviation value.
     */
    public double getStandardDeviation() {
        return standardDeviation;
    }
}
