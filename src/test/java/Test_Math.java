// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertThrows;

// import java.util.ArrayList;
// import java.util.List;

// import org.junit.jupiter.api.Test;

// public class Test_Math {
//     // Test for MathResult.java
//     @Test
//     public void test_MathResultConstructor() {
//         MathResult mathResult = new MathResult(5.0, 5.0, 0.0);
//         assertEquals(5.0, mathResult.getRunningMeanResult());
//         assertEquals(5.0, mathResult.getStandardDeviationResult());
//     }

//     // Tests for Cs214Math.java
//     @Test
//     public void test_NullRatingsListParameter() {
//         IllegalArgumentException e = assertThrows(
//                 IllegalArgumentException.class,
//                 () -> Cs214Math.calculateRunningMeanAndStandardDeviation(null),
//                 "test_NullRatingsListParameter did not return expected exception.");

//         MyUtilities.printError(e.getMessage());
//     }

//     @Test
//     public void test_RatingOOBPositive() {

//         IllegalArgumentException e = assertThrows(
//                 IllegalArgumentException.class,
//                 () -> Cs214Math.calculateRunningMeanAndStandardDeviation(new ArrayList<>(List.of(1.0, 5.0, 10.0))),
//                 "test_RatingOOBPositive did not return expected exception.");

//         MyUtilities.printError(e.getMessage());
//     }

//     @Test
//     public void test_RatingOOBZero() {

//         IllegalArgumentException e = assertThrows(
//                 IllegalArgumentException.class,
//                 () -> Cs214Math.calculateRunningMeanAndStandardDeviation(new ArrayList<>(List.of(0.0, 1.0, 5.0))),
//                 "test_RatingOOBZero did not return expected exception.");

//         MyUtilities.printError(e.getMessage());
//     }

//     @Test
//     public void test_RatingOOBNegative() {

//         IllegalArgumentException e = assertThrows(
//                 IllegalArgumentException.class,
//                 () -> Cs214Math.calculateRunningMeanAndStandardDeviation(new ArrayList<>(List.of(-8.0, 1.0, 5.0))),
//                 "test_RatingOOBNegative did not return expected exception.");

//         MyUtilities.printError(e.getMessage());
//     }

//     @Test
//     public void test_RatingSuccess() {

//         MathResult result = Cs214Math.calculateRunningMeanAndStandardDeviation(new ArrayList<>(List.of(1.0, 2.0, 3.0, 5.0)));
//         assertEquals(2.75, result.getRunningMeanResult());
//         assertEquals(1.479019945774904, result.getStandardDeviationResult());
//     }
// }
