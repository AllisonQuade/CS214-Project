import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.csv.CSVRecord;
//DLLs ^

/**
 * 
 * SongUserServicePA1
 */
public class SongUserServicePA1 {
    // https://www.geeksforgeeks.org/java/sorting-hashmap-according-key-value-java/
    // use object to return song and users so you don't have to read file multiple
    // times
    // private LoadSongsandUsersFromCSV_Result result;

    // #region PA1
    
    /**
     * deafult constructor
     */
    public SongUserServicePA1(){
    }


    /**
     * Loads in input file path. 
     * Uses CSVParserService to parse through inputFile. 
     * @param inputFile A CSV input file path.
     * @return songList and userList.
     * @throws IOException If input file is null or empty.
     */
    public LoadSongsandUsersFromCSV_Result loadSongsandUsersFromCSV(String inputFile) throws IOException {

        if (util_FormatOutput.isNullOrEmpty(inputFile)) {
            throw new IllegalArgumentException(
                    "Input file is required and must contain a file path and file name.");
        }

        TreeMap<String, Cs214Song> songList = new TreeMap<>();
        TreeMap<String, Cs214User> userList = new TreeMap<>();

        CSVParserService parserService = null;

        parserService = new CSVParserService(inputFile, false);
        for (CSVRecord record : parserService.getParser()) {
            if (record.size() != 3) {
                throw new IllegalArgumentException("Expected 3 fields but got " + record.size()
                        + " at record number " + record.getRecordNumber());
            }

            String songName = record.get(0); // Gets Song
            String userName = record.get(1); // Gets User
            double rating = Integer.parseInt(record.get(2));
          

            processSongData(songList, songName, rating);

            processUserData(userList, songName, userName, rating);

        }


        if (parserService != null) { // handles the parserService object to make sure it was created successfully
            parserService.closeCSVFile();
        }

        return new LoadSongsandUsersFromCSV_Result(songList, userList);

    }

    /**
     * Populates user list wth user contents from input file.
     * @param userList The user list being populated.
     * @param songName Name of song for current user.
     * @param userName Name of user for current user.
     * @param rating Numeric rating from the user.
     */
    private void processUserData(TreeMap<String, Cs214User> userList, String songName, String userName, double rating) {
        Cs214User currentUser;
        // If the user list already contains that user grab the user
        if (userList.containsKey(userName)) {
            currentUser = userList.get(userName);
        }
        // Otherwise add user
        else {
            currentUser = new Cs214User(userName);
            currentUser.addSongAndRating(songName, rating);
            userList.put(userName, currentUser);
        }
        currentUser.incrementsUserCount();
    }

    /**
     * Populates song list with sing content from input file.
     * @param songList The song list being populated.
     * @param songName The name of the current song.
     * @param rating The numeric rating to the current song.
     * @throws IOException If invalid rating.
     */
    private void processSongData(TreeMap<String, Cs214Song> songList, String songName, double rating) throws IOException {
        Cs214Song currentSong;
        // If the song list already contains that song grab the song
        if (songList.containsKey(songName)) {
            currentSong = songList.get(songName);
        }
        // Otherwise add song
        else {
            currentSong = new Cs214Song(songName);
            songList.put(songName, currentSong);
        }

        currentSong.incrementsSongCount();
        currentSong.addRating(rating);
    }

    /**
     * Output song name, number of ratings for song, mean, and standard deviation.
     * @param outputFile Output CSV file path created by user to output data to.
     * @param songs Song list used to populate output.
     * @throws IOException If output file is null or empty and if song map is null.
     */
    public void outputUsersSongsRatingsCSV(String outputFile, TreeMap<String, Cs214Song> songs) throws IOException {
        // TreeMap<String, Cs214Song> songs = result.songList;
        if (util_FormatOutput.isNullOrEmpty(outputFile)) {
            throw new IllegalArgumentException("Output file is required and must contain a file name.");
        }

        // if (outputFile.contains("/")) {
        //     throw new IllegalArgumentException("System only outputs to current working folder.");
        // }

        if (songs == null) {
            throw new IllegalArgumentException("songs parameter required.");
        }

        CSVPrinterService printerService = null;

        try {
            String[] headers = new String[] { "song", "number of ratings", "mean", "standard deviation"};

            printerService = new CSVPrinterService(outputFile, headers);
            for (Map.Entry<String, Cs214Song> entry : songs.entrySet()) {
                Cs214Song song = entry.getValue();
                Object[] values = new Object[] { song.getSongName(), song.getRatingsCount(), song.getRunningMean(),
                        song.getStandardDeviation()};

                printerService.printRecord(values);
            }

        } finally {
            if (printerService != null) {
                printerService.closeCSVFile();
            }
        }
    }
    // #endregion PA1
}
