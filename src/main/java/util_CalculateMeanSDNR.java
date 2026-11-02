
import java.util.ArrayList;

/**
 * 
 * util_CalculateMeanSDNR
 * Abstract class
 */
public abstract class util_CalculateMeanSDNR {

    private double runningMean = 0.0;
    private double standardDeviation = 0.0;
    //private ArrayList<Double> ratings = new ArrayList<>();

    private int count = 0;
    private double difference = 0.0;
    private double difference2 = 0.0;
    private double variance = 0.0;
    private double M2 = 0.0;

    /**
     * default constructor
     */
    // public util_CalculateMeanSDNR(){
    // }

    /**
     * Calculate running mean and standard deviation for current user
     * @param ratings Ratings array list used for calculations.
     */
    public void calculateRunningMeanAndStandardDeviation(ArrayList<Double> ratings) {
        if (ratings == null) {
            throw new IllegalArgumentException("The parameter \"ratings\" is required.");
        }

        //reset values
        runningMean = 0.0;
        standardDeviation = 0.0;
        count = 0;
        variance = 0.0;
        M2 = 0.0;

        for (double rating : ratings) {
            if (rating < 1 || rating > 5) {
                throw new IllegalArgumentException("Rating " + count + " in the ratings list has a value of " + rating
                        + " which is not between 1 and 5");
            }

            count++;
            difference = rating - runningMean;
            this.runningMean += difference / count;
            difference2 = rating - runningMean;

            M2 += difference * difference2;
            variance = M2 / count;
            this.standardDeviation = Math.sqrt(variance);
        }
    }

    /**
     * 
     * @return Running mean calculated.
     */
    public double getRunningMean() {
        return this.runningMean;
    }

    /**
     * @return Standard deviation calculated.
     */
    public double getStandardDeviation() {
        return this.standardDeviation;
    }

    /**
     * Calculates normalized rating using rating, running mean, and standard deviation.
     * @param rating Rating of song used for calculation.
     * @return Normalized rating.
     */
    public double getSongNormalizedRating(double rating) {
        if (this.standardDeviation != 0) {
            return (rating - this.runningMean) / this.standardDeviation;
        }
        return 0.0;
    }
}
