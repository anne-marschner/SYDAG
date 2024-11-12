package org.anne_marschner_project.core.noise;

/**
 * The StatisticValues class represents basic statistical values calculated for a data set,
 * specifically the mean and standard deviation.
 */
public class StatisticValues {

    private final double mean;
    private final double standardDeviation;


    /**
     * Constructs a StatisticValues instance with specified mean and standard deviation values.
     *
     * @param mean the mean value of the data set
     * @param standardDeviation the standard deviation of the data set
     */
    public StatisticValues (double mean, double standardDeviation) {
        this.mean = mean;
        this.standardDeviation = standardDeviation;
    }


    /**
     * Returns the mean value of the data set.
     *
     * @return the mean value
     */
    public double getMean() {
        return mean;
    }


    /**
     * Returns the standard deviation of the data set.
     *
     * @return the standard deviation value
     */
    public double getStandardDeviation() {
        return standardDeviation;
    }
}
