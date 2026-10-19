import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;

import org.junit.jupiter.api.Test;

public class Test_Cs214Song {
    @Test
    public void test_ConstructorSuccess() {
        Cs214Song song = new Cs214Song("MySong");
        assertEquals(Cs214Song.class, song.getClass(),
                "test_ConstructorSuccess did not return expected exception.");
    }

    @Test
    public void Test_EmptySongNameInConstructor() {
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> new Cs214Song(""), "Test_EmptySongNameInConstructor did not return expected exception.");

        util_FormatOutput.printError(e.getMessage());
    }

    @Test
    public void Test_EmptySongNameInSetter() {
        Cs214Song song = new Cs214Song("Hello");

        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> song.setSongName(""), "Test_EmptySongNameInSetter did not return expected exception.");

        util_FormatOutput.printError(e.getMessage());
    }

    @Test
    public void test_NegativeNumberSongsCounted() {
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> new Cs214Song("Hello").setNumberSongsOccured(-1),
                "test_NegativeNumberSongsCounted did not return expected exception.");

        util_FormatOutput.printError(e.getMessage());
    }

    @Test
    public void test_NumberSongsCountedSuccess() throws IOException{
         Cs214Song song = new Cs214Song("MySong");
        song.setNumberSongsOccured(4);
        assertEquals(4, song.getNumberSongsOccured(),
                "test_NumberSongsCountedSuccess did not return expected exception.");
    }

    @Test
    public void test_RatingOOBPositive() {

        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> new Cs214Song("MySong").addRating(6.0),
                "test_RatingOOBPositive did not return expected exception.");

        util_FormatOutput.printError(e.getMessage());
    }

    @Test
    public void test_RatingOOBZero() {
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> new Cs214Song("MySong").addRating(0.0),
                "test_RatingOOBZero did not return expected exception.");

        util_FormatOutput.printError(e.getMessage());
    }

    @Test
    public void test_RatingOOBNegative() {
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> new Cs214Song("MySong").addRating(-1.0),
                "test_RatingOOBNegative did not return expected exception.");

        util_FormatOutput.printError(e.getMessage());
    }

    @Test
    public void test_RatingSuccess() throws IOException {
        Cs214Song song = new Cs214Song("MySong");
        song.addRating(3.0);
        assertEquals(3.0, song.getRatings().getFirst(), "test_RatingSuccess did not return expected exception.");
    }

    @Test
    public void test_RunningMeanSuccess() throws IOException {
        Cs214Song song = new Cs214Song("MySong");
        song.addRating(1.0);
        song.addRating(2.0);
        song.addRating(3.0);
        song.addRating(5.0);
        assertEquals(2.75, song.getRunningMean(), "test_RunningMeanSuccess did not return expected exception.");
    }

    @Test
    public void test_StandardDeviationSuccess() throws IOException {
        Cs214Song song = new Cs214Song("MySong");
        song.addRating(1.0);
        song.addRating(2.0);
        song.addRating(3.0);
        song.addRating(5.0);
        assertEquals(1.479019945774904, song.getStandardDeviation(),
                "test_StandardDeviationSuccess did not return expected exception.");
    }

    @Test
    public void test_RatingsCountSuccess() throws IOException {
        Cs214Song song = new Cs214Song("MySong");
        song.addRating(1.0);
        song.addRating(2.0);
        song.addRating(3.0);
        song.addRating(5.0);
        assertEquals(4, song.getRatingsCount(),
                "test_RatingsCountSuccess did not return expected exception.");
    }

    @Test
    public void test_GetSongNameSuccess() {
        Cs214Song song = new Cs214Song("MySong");
        assertEquals("MySong", song.getSongName(),
                "test_GetSongNameSuccess did not return expected exception.");
    }

    
     @Test
    public void test_NumberSongsOccuredSuccess() {
        Cs214Song song = new Cs214Song("MySong");
        song.incrementsSongCount();
        assertEquals(1, song.getNumberSongsOccured(),
                "test_NumberSongsOccuredSuccess did not return expected exception.");
    }

}
