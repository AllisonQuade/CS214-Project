// import java.util.ArrayList;

// public class Cs214Math {
  
//   public static MathResult calculateRunningMeanAndStandardDeviation(ArrayList<Double> ratings){
//     if(ratings == null){
//       throw new IllegalArgumentException("The parameter \"ratings\" is required.");
//     }
    
//     double normalizedRating = 0.0;

//     double runningMean = 0.0;
//     double standardDeviation = 0.0;
//     int count = 0;
//     double difference;
//     double difference2;
//     double variance;
//     double M2 = 0.0;

//     for (double rating : ratings) {
//       if(rating < 1 || rating > 5){
//         throw new IllegalArgumentException("Rating " + count + " in the ratings list has a value of " + rating + " which is not between 1 and 5");
//       }

//       count++;
//       difference = rating - runningMean;
//       runningMean += difference / count;
//       difference2 = rating - runningMean;

//       M2 += difference * difference2;
//       variance = M2 / count;
//       standardDeviation = Math.sqrt(variance);
//       normalizedRating = (rating - runningMean) / (standardDeviation);
//     }

//     return new MathResult(runningMean, standardDeviation, normalizedRating); //return more than 1 data type? Create object!
//   }
// }
