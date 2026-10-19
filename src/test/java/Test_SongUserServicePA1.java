import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.TreeMap;

import org.junit.jupiter.api.Test;

public class Test_SongUserServicePA1 {

//#region loadSongsandUsersFromCSV
    @Test
    public void test_inputfileNotFound() throws IOException {
        SongUserServicePA1 songUserServicePA1 = new SongUserServicePA1();
        FileNotFoundException e = assertThrows(
                FileNotFoundException.class,
                () -> songUserServicePA1.loadSongsandUsersFromCSV("database/files/testfiles/PA1tests/unkown.csv"), "test_inputfileNotFound did not return expected exception.");
        util_FormatOutput.printError(e.getMessage());
    }

    @Test
    public void test_inputfileEmpty() throws IOException {
        SongUserServicePA1 songUserServicePA1 = new SongUserServicePA1();
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> songUserServicePA1.loadSongsandUsersFromCSV(""), "test_inputfileEmpty did not return expected exception.");
        util_FormatOutput.printError(e.getMessage());
    }

    @Test
    public void test_inputfileNull() throws IOException {
        SongUserServicePA1 songUserServicePA1 = new SongUserServicePA1();
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> songUserServicePA1.loadSongsandUsersFromCSV(null), "test_inputfileNull did not return expected exception.");
        util_FormatOutput.printError(e.getMessage());
    }

    @Test
    public void test_ExtraFieldsError() {
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> new SongUserServicePA1()
                        .loadSongsandUsersFromCSV("database/files/testfiles/PA1tests/extrafields.csv"),
                "test_ExtraFieldsError did not return expected exception.");

        util_FormatOutput.printError(e.getMessage());
    }

    @Test
    public void test_LessFieldsError() {
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> new SongUserServicePA1()
                        .loadSongsandUsersFromCSV("database/files/testfiles/PA1tests/lessfields.csv"),
                "test_LessFieldsError did not return expected exception.");

        util_FormatOutput.printError(e.getMessage());
    }

    @Test
    public void test_RatingTypeInvalid() throws IOException {
        NumberFormatException e = assertThrows(
                NumberFormatException.class,
                () -> new SongUserServicePA1()
                        .loadSongsandUsersFromCSV("database/files/testfiles/PA1tests/ratingdatatype_invalid.csv"),
                "test_RatingTypeInvalid did not return expected exception.");

        util_FormatOutput.printError(e.getMessage());
    }

    @Test
    public void test_NullRatingType() throws IOException {
        NumberFormatException e = assertThrows(
                NumberFormatException.class,
                () -> new SongUserServicePA1()
                        .loadSongsandUsersFromCSV("database/files/testfiles/PA1tests/nullratingfield.csv"),
                "test_NullRatingType did not return expected exception.");

        util_FormatOutput.printError(e.getMessage());
    }

    @Test
    public void test_EmptyUserName() throws IOException {
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> new SongUserServicePA1()
                        .loadSongsandUsersFromCSV("database/files/testfiles/PA1tests/empty_username.csv"),
                "test_EmptyUserName did not return expected exception.");

        util_FormatOutput.printError(e.getMessage());
    }

    @Test
    public void test_EmptySongName() throws IOException {
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> new SongUserServicePA1()
                        .loadSongsandUsersFromCSV("database/files/testfiles/PA1tests/empty_songname.csv"),
                "test_EmptySongName did not return expected exception.");

        util_FormatOutput.printError(e.getMessage());
    }

    @Test
    public void test_SameSongMultipleTimesSuccess() throws IOException {
        SongUserServicePA1 songUserServicePA1 = new SongUserServicePA1();
        LoadSongsandUsersFromCSV_Result result = songUserServicePA1.loadSongsandUsersFromCSV("database/files/testfiles/PA1tests/samesong_multiple.csv");
        assertEquals(2, result.songList.get("song1").getNumberSongsOccured(), "test_SameSongMultipleTimesSuccess did not return expected exception.");
    }

    @Test
    public void test_SameUserMultipleTimesSuccess() throws IOException {
        SongUserServicePA1 songUserServicePA1 = new SongUserServicePA1();
        LoadSongsandUsersFromCSV_Result result = songUserServicePA1.loadSongsandUsersFromCSV("database/files/testfiles/PA1tests/sameuser_multiple.csv");
        assertEquals(2, result.userList.get("user1").getuserCount(), "test_SameUserMultipleTimesSuccess did not return expected exception.");
        assertEquals(3, result.userList.size(), "test_SameUserMultipleTimesSuccess did not return expected exception.");
    }

    @Test
    public void test_UserKeyCountSuccess() throws IOException {
        SongUserServicePA1 songUserServicePA1 = new SongUserServicePA1();
        LoadSongsandUsersFromCSV_Result result = songUserServicePA1.loadSongsandUsersFromCSV("database/files/testfiles/PA1tests/sameuser_multiple.csv");
        assertEquals(3, result.userList.size(), "test_UserKeyCountSuccess did not return expected exception.");
    }
    //#endregion

//#region writeBasicSongStats

    @Test
    public void test_OutputFileSuccess() throws IOException{
        String outputFileName = "output.csv";
        File file = new File(outputFileName);
        if(file.exists()){ //Delete file if it exists
                file.delete();
        }
        SongUserServicePA1 songUserServicePA1 = new SongUserServicePA1();
         LoadSongsandUsersFromCSV_Result result = songUserServicePA1.loadSongsandUsersFromCSV("database/files/testfiles/PA1tests/outputfile_success.csv");
        songUserServicePA1.outputUsersSongsRatingsCSV(outputFileName, result.songList);
        assertEquals(true, file.exists(), "test_OutputFileSuccess did not return expected exception.");
    }

    @Test
    public void test_RatingNotInteger() throws IOException{
        NumberFormatException e = assertThrows(
                NumberFormatException.class,
                () -> new SongUserServicePA1().loadSongsandUsersFromCSV("database/files/testfiles/PA1tests/rating_not_integer.csv"),
                "test_RatingNotInteger did not return expected exception.");

        util_FormatOutput.printError(e.getMessage());
    }
    

//     @Test
//     public void test_OutputFileNameParameterContainsBackSlash() throws IOException{
//         IllegalArgumentException e = assertThrows(
//                 IllegalArgumentException.class,
//                 () -> new SongUserServicePA1().outputUsersSongsRatingsCSV("/output.csv", new TreeMap<String ,Cs214Song>()),
//                 "test_OutputFileNameParameterContainsBackSlash did not return expected exception.");

//         MyUtilities.printError(e.getMessage());
//     }

     @Test
    public void test_OutputFileNameParameterNull() throws IOException{
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> new SongUserServicePA1().outputUsersSongsRatingsCSV(null, new TreeMap<String ,Cs214Song>()),
                "test_OutputFileNameParameterNull did not return expected exception.");

        util_FormatOutput.printError(e.getMessage());
    }

      @Test
    public void test_OutputFileNameParameterEmpty() throws IOException{
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> new SongUserServicePA1().outputUsersSongsRatingsCSV("", new TreeMap<String ,Cs214Song>()),
                "test_OutputFileNameParameterEmpty did not return expected exception.");

        util_FormatOutput.printError(e.getMessage());
    }

     @Test
    public void test_SongsParameterNull() throws IOException{
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> new SongUserServicePA1().outputUsersSongsRatingsCSV("myoutput.csv", null),
                "test_OutputFileNameParameterEmpty did not return expected exception.");

        util_FormatOutput.printError(e.getMessage());
    }
    //#endregion
}
