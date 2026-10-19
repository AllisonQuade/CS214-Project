
import java.util.ArrayList;

public abstract class util_CalculateMeanSDNR {

    private double runningMean = 0.0;
    private double standardDeviation = 0.0;
    //private ArrayList<Double> ratings = new ArrayList<>();

    private int count = 0;
    private double difference = 0.0;
    private double difference2 = 0.0;
    private double variance = 0.0;
    private double M2 = 0.0;

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

    public double getRunningMean() {
        return this.runningMean;
    }

    public double getStandardDeviation() {
        return this.standardDeviation;
    }

    public double getSongNormalizedRating(double rating) {
        if (this.standardDeviation != 0) {
            return (rating - this.runningMean) / this.standardDeviation;
        }
        return 0.0;
    }

    // //Searched up on google how to truncate
    // public static double truncate6(double value) {
    //     if (Double.isNaN(value)) { // Was getting error from populateRatingPlaceHolderData method about Character N is neither a decimal digit number. Fix
    //         return value;
    //     }
    //     return BigDecimal.valueOf(value)
    //             .setScale(6, RoundingMode.DOWN)
    //             .doubleValue();
    // }
}
