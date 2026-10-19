import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;

import org.junit.jupiter.api.Test;

public class Test_User {
     @Test
    public void test_UserConstructorNullName() {
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> new Cs214User(null), "test_UserConstructorNullName did not return expected exception.");

        util_FormatOutput.printError(e.getMessage());
    }

       @Test
    public void test_UserConstructorEmptyName() {
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> new Cs214User(""), "test_UserConstructorEmptyName did not return expected exception.");

        util_FormatOutput.printError(e.getMessage());
    }

    @Test
    public void test_NullSongNameParameter() {
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> new Cs214User("AQ").addSongAndRating(null, 5.0), "test_NullSongNameParameter did not return expected exception.");

        util_FormatOutput.printError(e.getMessage());
    }

    @Test
    public void test_EmptySongNameParameter() {
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> new Cs214User("AQ").addSongAndRating("", 5.0), "test_EmptySongNameParameter did not return expected exception.");

        util_FormatOutput.printError(e.getMessage());
    }

    @Test
    public void test_SongNameDuplicate() throws IOException{
        Cs214User user = new Cs214User("Randy");
        user.addSongAndRating("Hello", 2.0);

        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> user.addSongAndRating("Hello",5.0), "test_SongNameDuplicate did not return expected exception.");

        util_FormatOutput.printError(e.getMessage());   
}

@Test
    public void test_UserRatingUpperOOB() {
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> new Cs214User("UserName").addSongAndRating("SongName", 6.0), "test_UserRatingUpperOOB did not return expected exception.");

        util_FormatOutput.printError(e.getMessage());
    }

    @Test
    public void test_UserRatingLowerOOB() {
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> new Cs214User("UserName").addSongAndRating("SongName", 0.0), "test_UserRatingLowerOOB did not return expected exception.");

        util_FormatOutput.printError(e.getMessage());
    }

    @Test
    public void test_UserNameValidate() {
        assertEquals("UserName", new Cs214User("UserName").getUserName(), "test_UserNameValidate did not return expected exception.");
    }

    @Test
    public void test_SongListValidate() throws IOException{
        Cs214User user = new Cs214User("UserName");
        user.addSongAndRating("Song1", 3.0);
        assertEquals("Song1", user.getSongList().firstKey(), "test_SongListValidate did not return expected exception.");
    }
}
