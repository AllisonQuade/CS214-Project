
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.csv.CSVRecord;
import org.apache.commons.math4.legacy.ml.distance.EuclideanDistance;

/**
 * 
 * SongUserServicePA3V2
 */
public class SongUserServicePA3V2 {

    private TreeMap<String, Cs214Song> songList;
    private TreeMap<String, Cs214User> userList;
    // private ArrayList<Double> userRatingList;
    private List<String> masterSongNameList;
    private Double[][] songSimilarityMatrix;

    /**
     * deafult constructor
     */
    // public SongUserServicePA3V2(){
    // }

    /**
     * Calls methods to load from CSV, clear data, populate placeholder data, and
     * calculate song similarity.
     * 
     * @param inputFile Input CSV file path.
     * @throws IOException If invalid input file.
     */
    public void loadAndProcessData(String inputFile) throws IOException { // for main
        loadSongsandUsersFromCSV(inputFile);
        dataCleaner();
        populateRatingPlaceHolderData();
        calcSimilarity();
    }

    /**
     * Parse through CSV input file.
     * 
     * @param inputFile Input CSV file path.
     * @return song list and user list.
     * @throws IOException If input file null or empty.
     */
    private LoadSongsandUsersFromCSV_Result loadSongsandUsersFromCSV(String inputFile) throws IOException {

        if (util_FormatOutput.isNullOrEmpty(inputFile)) {
            throw new IllegalArgumentException(
                    "Input file is required and must contain a file path and file name.");
        }

        this.songList = new TreeMap<>();
        this.userList = new TreeMap<>();
        // this.userRatingList = new ArrayList<>();

        this.masterSongNameList = new ArrayList<>();

        CSVParserService parserService = new CSVParserService(inputFile, false);
        for (CSVRecord record : parserService.getParser()) {
            if (record.size() != 3) {
                throw new IllegalArgumentException("Expected 3 fields but got " + record.size()
                        + " at record number " + record.getRecordNumber());
            }

            String songName = record.get(0); // Gets Song
            String userName = record.get(1); // Gets User
            double rating = Integer.parseInt(record.get(2));

            if (!masterSongNameList.contains(songName)) {
                masterSongNameList.add(songName);
            }

            processSongData(songName, rating);

            processUserData(songName, userName, rating);

            Collections.sort(masterSongNameList); // sorts final master list
        }

        // if (!parserService.equals(null)) { // handles the parserService object to
        // make sure it was created successfully
        parserService.closeCSVFile();
        // }

        return new LoadSongsandUsersFromCSV_Result(songList, userList);

    }

    /**
     * Process user data.
     * 
     * @param songName Song name of song for user.
     * @param userName User name for user.
     * @param rating   Numeric rating of users song.
     */
    private void processUserData(String songName, String userName, double rating) {
        Cs214User currentUser;
        // If the user list already contains that user grab the user
        if (userList.containsKey(userName)) {
            currentUser = userList.get(userName);
        } // Otherwise add user
        else {
            currentUser = new Cs214User(userName);

            userList.put(userName, currentUser);
        }

        currentUser.getSongList().put(songName, rating);

        currentUser.calculateRunningMeanAndStandardDeviation(new ArrayList<>(currentUser.getSongList().values()));

        currentUser.incrementsUserCount();
    }

    /**
     * Process song data.
     * 
     * @param songName Song name of song.
     * @param rating   Numeric rating of users song.
     */
    private void processSongData(String songName, double rating) throws IOException {
        Cs214Song currentSong;
        // If the song list already contains that song grab the song
        if (songList.containsKey(songName)) {
            currentSong = songList.get(songName);
        } // Otherwise add song
        else {
            currentSong = new Cs214Song(songName);
            songList.put(songName, currentSong);
        }

        currentSong.incrementsSongCount();
        currentSong.addRating(rating);
    }

    /**
     * Removes invalid users from data.
     * Removes invalid user song from master song lsit if song only belonged to
     * invalid user.
     * 
     * @throws IOException If unable to clean data.
     */
    public void dataCleaner() throws IOException {
        List<String> invalidUsers = new ArrayList<>();

        Set<String> validUserSongs = new TreeSet<>();
        Set<String> invalidUserSongs = new TreeSet<>();

        for (Map.Entry<String, Cs214User> userEntry : this.userList.entrySet()) { // stepthrough users
            TreeMap<String, Double> songNameRating = userEntry.getValue().getSongList();

            // this list keeps track of song names for current user
            Set<String> currentUserSongs = new TreeSet<>();

            Double firstRating = songNameRating.firstEntry().getValue(); // the first rating in the treeMap
            Boolean invalidUser = true;

            invalidUser = isInvalidUser(songNameRating, currentUserSongs, firstRating, invalidUser);

            if (invalidUser) { // invalid user
                invalidUserSongs.addAll(currentUserSongs); // merge all user songs into invalid user songs list
                invalidUsers.add(userEntry.getKey()); // add current invalid user name to invalidUsers list
            } else {
                validUserSongs.addAll(currentUserSongs);
            }
        }

        invalidUserSongs.removeAll(validUserSongs);
        masterSongNameList.removeAll(invalidUserSongs);

        for (String user : invalidUsers) {
            userList.remove(user);
        }
    }

    /**
     * Determines if a user is invalid or valid.
     * 
     * @param songNameRating   Used to stepthrough user's songs and ratings.
     * @param currentUserSongs List of all current user songs.
     * @param firstRating      Users first rating.
     * @param invalidUser      Status if user invalid or valid.
     * @return true if invalid user, false if valid.
     */
    private Boolean isInvalidUser(TreeMap<String, Double> songNameRating, Set<String> currentUserSongs,
            Double firstRating,
            Boolean invalidUser) {
        for (Map.Entry<String, Double> songEntry : songNameRating.entrySet()) { // stepthrough user's songs and
            // ratings
            currentUserSongs.add(songEntry.getKey()); // list of all my current user songs

            // if(!firstRating.equals(songEntry.getValue())){ //valid user //This if way
            // works same as if below, gets value of Double and NOT object (memory space)
            if (firstRating.doubleValue() != songEntry.getValue().doubleValue()) {
                invalidUser = false;
            }
        }
        return invalidUser;
    }

    /**
     * Puts missing song from user into song list with NaN.
     */
    public void populateRatingPlaceHolderData() { // puts missing song from user into songList with NaN
        for (String currentSongName : this.masterSongNameList) {
            for (Map.Entry<String, Cs214User> userEntry : this.userList.entrySet()) {
                TreeMap<String, Double> songNameRating = userEntry.getValue().getSongList();
                if (!songNameRating.containsKey(currentSongName)) {
                    songNameRating.put(currentSongName, Double.NaN);
                }
            }
        }
    }

    // public Double[][] calcSimilarity() throws IOException {
    // //https://www.geeksforgeeks.org/java/how-to-convert-treemap-to-an-arraylist-in-java/
    // validateCooperativeUsers();
    // EuclideanDistance EuclideanDistance = new EuclideanDistance();
    // this.dimensions = new
    // Double[masterSongNameList.size()][masterSongNameList.size()];
    // double edResult;
    // for (int i = 0; i < this.masterSongNameList.size(); i++) {
    // for (int s = i + 1; s < this.masterSongNameList.size(); s++) {
    // double[] song1Rating = new double[userList.size()];
    // double[] song2Rating = new double[userList.size()];
    // boolean songsHaveSharedUser = false;
    // int userIndex = 0; //anytime need index tracker for a for each loop,
    // initilize counter above for each loop
    // for (Map.Entry<String, Cs214User> userEntry : this.userList.entrySet()) {
    // ArrayList<Double> songRatingList = getSongRatingArray(userEntry); //turns
    // treemap to array list
    // //get normalized ratings for euclidean distance
    // Double song1 =
    // userEntry.getValue().getSongNormalizedRating(songRatingList.get(i));
    // Double song2 =
    // userEntry.getValue().getSongNormalizedRating(songRatingList.get(s));
    // if (song1.equals(Double.NaN) || song2.equals(Double.NaN)) {
    // song1Rating[userIndex] = 0.0;
    // song2Rating[userIndex] = 0.0;
    // } else {
    // song1Rating[userIndex] = song1;
    // song2Rating[userIndex] = song2;
    // songsHaveSharedUser = true;
    // }
    // userIndex++;
    // }
    // if (songsHaveSharedUser) {
    // edResult = EuclideanDistance.compute(song1Rating, song2Rating);
    // } else {
    // edResult = Double.NaN;
    // }
    // dimensions[i][s] = edResult;
    // }
    // }
    // return dimensions;
    // }

    /**
     * Caclulates similarity of two songs for each user.
     * 
     * @return matrix of song similarity.
     * @throws IOException if validate cooperative users fail.
     */
    public Double[][] calcSimilarity() throws IOException {
        validateCooperativeUsers();

        int numSongs = this.masterSongNameList.size();
        this.songSimilarityMatrix = new Double[numSongs][numSongs];

        for (int firstSongIndex = 0; firstSongIndex < numSongs; firstSongIndex++) {
            for (int secondSongIndex = firstSongIndex + 1; secondSongIndex < numSongs; secondSongIndex++) {
                songSimilarityMatrix[firstSongIndex][secondSongIndex] = calcSongPairDistance(firstSongIndex,
                        secondSongIndex);
            }
        }
        return songSimilarityMatrix;
    }

    /**
     * Calculates euclidean distance for each song.
     * 
     * @param firstSongIndex
     * @param secondSongIndex
     * @return Euclidean distance.
     */
    // Distance between song i and song s across all users (NaN if no user rated both)
    private double calcSongPairDistance(int firstSongIndex, int secondSongIndex) {
        double[] song1Rating = new double[userList.size()];
        double[] song2Rating = new double[userList.size()];
        boolean songsHaveSharedUser = false;
        int userIndex = 0; // index tracker for the for-each loop

        for (Map.Entry<String, Cs214User> userEntry : this.userList.entrySet()) {
            double[] normalizedRatingPair = fillUserRatings(userEntry, firstSongIndex, secondSongIndex);
            if (normalizedRatingPair != null) {
                song1Rating[userIndex] = normalizedRatingPair[0];
                song2Rating[userIndex] = normalizedRatingPair[1];
                songsHaveSharedUser = true;
            }
            userIndex++;
        }

        if (!songsHaveSharedUser) {
            return Double.NaN;
        }
        return new EuclideanDistance().compute(song1Rating, song2Rating);
    }

    /**
     * Gets one user's normalized ratings for two songs.
     * 
     * @param userEntry User map entry.
     * @param firstSongIndex  Index of first song for user.
     * @param secondSongIndex Index of second song for user.
     * @return {song1, song2} normalized ratings, or null if either rating is NaN.
     */
    private double[] fillUserRatings(Map.Entry<String, Cs214User> userEntry, int firstSongIndex, int secondSongIndex) {
        ArrayList<Double> songRatingList = getSongRatingArray(userEntry); // turns treemap to array list

        Double song1 = userEntry.getValue().getSongNormalizedRating(songRatingList.get(firstSongIndex));
        Double song2 = userEntry.getValue().getSongNormalizedRating(songRatingList.get(secondSongIndex));

        if (song1.isNaN() || song2.isNaN()) {
            return null;
        }
        return new double[] {song1, song2};
    }

    /**
     * Used to require two cooperative user to compute user similarity.
     */
    private void validateCooperativeUsers() {
        if (this.userList.size() <= 1) {
            throw new IllegalArgumentException("at least two cooperative users are required for song similarity");
        }
    }

    /**
     * Transforms song map into array.
     * 
     * @param userEntry Used to get song list.
     * @return Arraylist of ratings.
     */
    private ArrayList<Double> getSongRatingArray(Map.Entry<String, Cs214User> userEntry) {
        TreeMap<String, Double> songNameRating = userEntry.getValue().getSongList();
        ArrayList<Double> songRating = new ArrayList<>(songNameRating.values());
        return songRating;
    }

    /**
     * Validate output arguments.
     * 
     * @param outputFile Output CSV file path.
     */
    public void validateArguments(String outputFile) {
        if (util_FormatOutput.isNullOrEmpty(outputFile)) {
            throw new IllegalArgumentException("Output file is required and must contain a file name.");
        }

        if (this.userList == null) {
            throw new IllegalArgumentException("songs parameter required.");
        }
    }

    /**
     * Outputs song1 name, song2 name, and similarity between songs.
     * 
     * @param outputFile Output CSV file path.
     * @throws IOException If printer service fail.
     */
    public void outputUsersSongsRatingsCSV(String outputFile) throws IOException {
        validateArguments(outputFile);

        CSVPrinterService printerService = null;

        try {
            String[] headers = new String[] { "name1", "name2", "similarity" };

            printerService = new CSVPrinterService(outputFile, headers);

            for (int i = 0; i < songSimilarityMatrix.length; i++) {
                for (int j = 0; j < songSimilarityMatrix.length; j++) {
                    printRecord(printerService, i, j);
                }
            }

        } finally {
            if (printerService != null) {
                printerService.closeCSVFile();
            }
        }
    }

    /**
     * Outputs values objects array with song 1 name, song 2 name, and dimensions
     * matrix.
     * 
     * @param printerService CSV printer used to print record.
     * @param i              Index 1 of matrix.
     * @param j              Index 2 of matrix.
     * @throws IOException if printer fails.
     */
    private void printRecord(CSVPrinterService printerService, int i, int j) throws IOException {
        if (this.songSimilarityMatrix[i][j] != null && (i != j && this.songSimilarityMatrix[i][j] > 0.0
                || this.songSimilarityMatrix[i][j].equals(Double.NaN))) {
            Object[] values = { masterSongNameList.get(i), masterSongNameList.get(j), songSimilarityMatrix[i][j] };
            printerService.printRecord(values);
        }
    }
}
