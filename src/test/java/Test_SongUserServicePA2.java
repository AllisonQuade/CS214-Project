
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

public class Test_SongUserServicePA2 {

    @Test
    public void inputfileEmpty() throws IOException {
        SongUserServicePA2 songUserServicePA2 = new SongUserServicePA2();
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> songUserServicePA2.loadSongsandUsersKeysFromCSV(""));
        util_FormatOutput.printError(e.getMessage());
    }

    @Test
    public void inputfileNull() throws IOException {
        SongUserServicePA2 songUserServicePA2 = new SongUserServicePA2();
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> songUserServicePA2.loadSongsandUsersKeysFromCSV(null));
        util_FormatOutput.printError(e.getMessage());
    }

    @Test
    public void test_ExtraFieldsError() {
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> new SongUserServicePA2()
                        .loadSongsandUsersKeysFromCSV("database/files/testfiles/PA1tests/extrafields.csv"),
                "test_ExtraFieldsError did not return expected exception.");

        util_FormatOutput.printError(e.getMessage());
    }

    @Test
    public void test_LessFieldsError() {
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> new SongUserServicePA2()
                        .loadSongsandUsersKeysFromCSV("database/files/testfiles/PA1tests/lessfields.csv"),
                "test_LessFieldsError did not return expected exception.");

        util_FormatOutput.printError(e.getMessage());
    }

    @Test
    public void test_RatingNotInteger() throws IOException {
        NumberFormatException e = assertThrows(
                NumberFormatException.class,
                () -> new SongUserServicePA2().loadSongsandUsersKeysFromCSV("database/files/testfiles/PA2tests/rating_not_integer.csv"),
                "test_RatingNotInteger did not return expected exception.");

        util_FormatOutput.printError(e.getMessage());
    }

    @Test
    public void test_UserSongCountSuccess() throws IOException {
        SongUserServicePA2 songUserServicePA2 = new SongUserServicePA2();
        songUserServicePA2.loadSongsandUsersKeysFromCSV("database/files/testfiles/PA2tests/test_usersongcount_success.csv");
        //assertEquals(3, songUserServicePA2.getUsers().get("user1").size(), "test_UserSongCountSuccess did not return expected exception.");
        assertEquals(3, songUserServicePA2.getSongRatingsForUserName("user1").size(), "test_UserSongCountSuccess did not return expected exception.");
    }

    @Test
    public void test_UserCountSuccess() throws IOException {
        SongUserServicePA2 songUserServicePA2 = new SongUserServicePA2();
        songUserServicePA2.loadSongsandUsersKeysFromCSV("database/files/testfiles/PA2tests/test_usercount_success.csv");
        assertEquals(4, songUserServicePA2.getUsers().size(), "test_UserCountSuccess did not return expected exception.");
    }

    @Test
    public void test_UserCountAsDataStringSuccess() throws IOException {
        SongUserServicePA2 songUserServicePA2 = new SongUserServicePA2();
        // The "//" in the fileUserCountTest:  string continues on next line. NOT A COMMENT
        String fileUserCountTest = "song1,user1,4\r\n"
                + //
                "song1,user2,4\r\n"
                + //
                "song2,user3,1\r\n"
                + //
                "song2,user4,2\r\n"
                + //
                "song1,user3,2";
        
        songUserServicePA2.loadSongsandUsersKeysFromDataString(fileUserCountTest);
        assertEquals(4, songUserServicePA2.getUsers().size(), "test_UserCountAsDataStringSuccess did not return expected exception.");
    }

    //#region outputUsersSongsRatingsCSV
    @Test
    public void test_OutputFileSuccess() throws IOException {
        String outputFileName = "output.csv";
        File file = new File(outputFileName);
        if (file.exists()) { //Delete file if it exists
            file.delete();
        }
        SongUserServicePA2 songUserServicePA2 = new SongUserServicePA2();
        songUserServicePA2.loadSongsandUsersKeysFromCSV("database/files/testfiles/PA2tests/test_outputfile_success.csv");
        songUserServicePA2.dataCleaner();
        songUserServicePA2.outputUsersSongsRatingsCSV(outputFileName);
        assertEquals(true, file.exists(), "test_OutputFileSuccess did not return expected exception.");
    }

    // @Test
    // public void test_OutputFileNameParameterContainsBackSlash() throws IOException{
    //     IllegalArgumentException e = assertThrows(
    //             IllegalArgumentException.class,
    //             () -> new SongUserServicePA2().outputUsersSongsRatingsCSV("/output.csv"),
    //             "test_OutputFileNameParameterContainsBackSlash did not return expected exception.");
    //     MyUtilities.printError(e.getMessage());
    // }
    @Test
    public void test_OutputFileNameParameterNull() throws IOException {
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> new SongUserServicePA2().outputUsersSongsRatingsCSV(null),
                "test_OutputFileNameParameterNull did not return expected exception.");

        util_FormatOutput.printError(e.getMessage());
    }

    @Test
    public void test_OutputFileNameParameterEmpty() throws IOException {
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> new SongUserServicePA2().outputUsersSongsRatingsCSV(""),
                "test_OutputFileNameParameterEmpty did not return expected exception.");

        util_FormatOutput.printError(e.getMessage());
    }


////#endregion
}
