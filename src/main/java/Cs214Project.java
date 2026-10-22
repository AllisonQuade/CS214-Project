
import java.io.FileNotFoundException;
import java.io.IOException;

// ******************************************************************
// ******************************************************************
// TERMINAL COMMANDS TO RUN PROGRAM
// ******************************************************************
// gradle run -q --args="'database/files/file2.csv' 'output.csv'"gradle run -q --args="'database/files/file2.csv' 'output.csv'"
// ******************************************************************
public class Cs214Project {

    public static void validateArguments(String[] args) {
        validateNumberOfArguments(args);
        validateFirstAndSecondArguments(args);
        validateThirdArguments(args);
    }

    public static void validateNumberOfArguments(String[] args) {
        if (args.length < 2 || args.length > 3) {
            throw new IllegalArgumentException(args.length + " is out of bounds. Requires two or three arguments.");
        }
    }

    public static void validateFirstAndSecondArguments(String[] args) {
        if (util_FormatOutput.isNullOrEmpty(args[0])) {
            throw new IllegalArgumentException("Arguments 1 is required. It must include file input path.");
        }

        if (util_FormatOutput.isNullOrEmpty(args[1])) {
            throw new IllegalArgumentException("Arguments 2 is required. It must include output file.");
        }

    }

    public static void validateThirdArguments(String[] args) {
        if (args.length == 3 && !args[2].equals("-a") && !args[2].equals("-u") && !args[2].equals("")) {
            throw new IllegalArgumentException("\"-a\" or \"-u\" required for PA2 or PA3.");
        }

    }

    public static void main(String[] args) throws IOException, FileNotFoundException {
        // Open the project in Codio's integrated Visual Studio Code!
        // See Hints or README.md for more information
        try { // graceful exit (no red error meassage)
            validateArguments(args);

            String input = args[0]; // the input file
            String output = args[1]; // the output file

            String PASelector = args.length == 3 || args.length == 4 ? args[2] : ""; // Which PA?
            // factory
            switch (PASelector) {
                case "-a" -> {
                    // PA2 PA service factory
                    SongUserServicePA2 SongUserServicePA2 = new SongUserServicePA2();
                    SongUserServicePA2.loadAndProcessData(input);
                    SongUserServicePA2.outputUsersSongsRatingsCSV(output);
                }
                case "-3" -> { //????
                    //PA3
                    SongUserServicePA3V2 SongUserServicePA3 = new SongUserServicePA3V2();
                    SongUserServicePA3.loadAndProcessData(input);
                    SongUserServicePA3.outputUsersSongsRatingsCSV(output);
                }
                case "-u" -> {
                    //PA4
                    SongUserServicePA4 SongUserServicePA4 = new SongUserServicePA4();
                    SongUserServicePA4.loadAndProcessData(input);
                    SongUserServicePA4.outputUsersSongsRatingsCSV(output);
                }
                default -> {
                    // PA1
                    SongUserServicePA1 SongUserServicePA1 = new SongUserServicePA1();
                    LoadSongsandUsersFromCSV_Result songsAndUsers = SongUserServicePA1.loadSongsandUsersFromCSV(input);
                    SongUserServicePA1.outputUsersSongsRatingsCSV(output, songsAndUsers.songList);
                }
            }
        } catch (Exception e) {
            util_FormatOutput.printError(e.getMessage());
        }
    }
}
