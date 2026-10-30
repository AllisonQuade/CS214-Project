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

// extends PA 
/**
 * 
 * SongUserServicePA4
 * This service handles importing csv file and analyzing that data to display
 * user similarity user rating prediction
 */
public class SongUserServicePA4 {

    private TreeMap<String, Cs214Song> songList;
    private TreeMap<String, Cs214User> userList;
    // private ArrayList<Double> userRatingList;
    private List<String> masterSongNameList;
    private Double[][] userSimilarityMatrix;
    private TreeMap<Cs214User, TreeMap<Double, Cs214User>> similarUserList;
    private TreeMap<String, Double> userSongRatingPredictions;

    /**
     * deafult constructor
     */
    public SongUserServicePA4() {
    }

    /**
     * Calls methods to load data, clean data, populate placeholder data, and
     * calculate similarity.
     * 
     * @param inputFile Input CSV file path.
     * @throws IOException If load songlist.entrySet() and users from CSV fails.
     */
    public void loadAndProcessDataUserSimilarity(String inputFile) throws IOException {
        loadSongsandUsersFromCSV(inputFile);
        dataCleaner();
        populateRatingPlaceHolderData();
        calcSimilarity();
    }

    public void loadAndProcessDataPrediction(String inputFile) throws IOException{
        loadAndProcessDataUserSimilarity(inputFile);
        predictMissingUserSongRating();
    }

    /**
     * Parses through input file .
     * Populate and sort master songlist.entrySet() list with all unique songs.
     * 
     * @param inputFile Input CSV file path.
     * @return populated songlist.entrySet() list and user list.
     * @throws IOException
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
     * JavaDoc
     * Processes user data.
     * Calls methods from Cs214User to calculate mean and standard deviation.
     * 
     * @param songName Name of songlist.entrySet() for user
     * @param userName Name of user name for user.
     * @param rating   Numeric rating for users songlist.entrySet().
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
     * Process songlist.entrySet() data.
     * Calls Cs214Song methods to add songlist.entrySet() and rating.
     * 
     * @param songName Song name.
     * @param rating   Numeric rating of songlist.entrySet().
     * @throws IOException if add rating fail.
     */
    private void processSongData(String songName, double rating) throws IOException {
        Cs214Song currentSong;
        // If the songlist.entrySet() list already contains that songlist.entrySet()
        // grab the songlist.entrySet()
        if (songList.containsKey(songName)) {
            currentSong = songList.get(songName);
        } // Otherwise add songlist.entrySet()
        else {
            currentSong = new Cs214Song(songName);
            songList.put(songName, currentSong);
        }

        currentSong.incrementsSongCount();
        currentSong.addRating(rating);
    }

    /**
     * Removes invalid users from data.
     * Removes invalid user songlist.entrySet() from master songlist.entrySet() lsit
     * if songlist.entrySet() only belonged to invalid user.
     */
    public void dataCleaner() {
        List<String> invalidUsers = new ArrayList<>();

        Set<String> validUserSongs = new TreeSet<>();
        Set<String> invalidUserSongs = new TreeSet<>();

        for (Map.Entry<String, Cs214User> userEntry : this.userList.entrySet()) { // stepthrough users
            TreeMap<String, Double> songNameRating = userEntry.getValue().getSongList();

            // this list keeps track of songlist.entrySet() names for current user
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
     * Puts missing songlist.entrySet() from user into songlist.entrySet() list with
     * NaN.
     */
    public void populateRatingPlaceHolderData() {
        for (String currentSongName : this.masterSongNameList) {
            for (Map.Entry<String, Cs214User> userEntry : this.userList.entrySet()) {
                TreeMap<String, Double> songNameRating = userEntry.getValue().getSongList();
                if (!songNameRating.containsKey(currentSongName)) {
                    songNameRating.put(currentSongName, Double.NaN);
                }
            }
        }
    }
    // #region OGSimilarityLogic
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
    // #endregion OGSimilarityLogic

    /**
     * Calculates similarity between two users
     * 
     * @return Matrix of similarity for first user and second user.
     */
    public void calcSimilarity() {
        validateCooperativeUsers();

        int numUsers = this.userList.size();
        this.userSimilarityMatrix = new Double[numUsers][numUsers];

        // https://www.tutorialspoint.com/article/how-to-get-treemap-key-or-value-using-index-in-java
        List<Map.Entry<String, Cs214User>> users = new ArrayList<>(this.userList.entrySet()); // list of map entries
        this.similarUserList = new TreeMap<>();

        for (int firstUserIndex = 0; firstUserIndex < numUsers; firstUserIndex++) {
            for (int secondUserIndex = 0; secondUserIndex < numUsers; secondUserIndex++) { // firstUserIndex
                // + 1
                double calcSongPair = calcSongPairDistance(users.get(firstUserIndex), users.get(secondUserIndex));
                userSimilarityMatrix[firstUserIndex][secondUserIndex] = calcSongPair;
                userSimilarityMatrix[secondUserIndex][firstUserIndex] = calcSongPair;

            }
        }

        // populate user similarity nested treemap
        for (int rowIndex = 0; rowIndex < numUsers; rowIndex++) {
            TreeMap<Double, Cs214User> similarityAndSimilarUser = new TreeMap<>();
            for (int columnIndex = 0; columnIndex < numUsers; columnIndex++) {
                if (rowIndex != columnIndex && !userSimilarityMatrix[rowIndex][columnIndex].isNaN()) {
                    similarityAndSimilarUser.put(userSimilarityMatrix[rowIndex][columnIndex],
                            users.get(columnIndex).getValue());
                }
            }

            this.similarUserList.put(users.get(rowIndex).getValue(), similarityAndSimilarUser);
        }
    }

    /**
     * Distance between user firstUserIndex and user secondUserIndex across all
     * songs they both rated (NaN if no both users didn't rate the
     * songlist.entrySet())
     * 
     * @param firstUser
     * @param secondUser
     * @return Euclidean distance between songlist.entrySet() of each user.
     */
    private double calcSongPairDistance(Map.Entry<String, Cs214User> firstUser,
            Map.Entry<String, Cs214User> secondUser) {

        if (firstUser == secondUser) {
            return 0.0;
        }

        double[] song1Ratings = new double[masterSongNameList.size()];
        double[] song2Ratings = new double[masterSongNameList.size()];
        boolean usersHaveSharedSong = false;

        ArrayList<Double> songRatingList1 = getSongRatingArray(firstUser); // turns treemap to array list
        ArrayList<Double> songRatingList2 = getSongRatingArray(secondUser);

        for (int songIndex = 0; songIndex < masterSongNameList.size(); songIndex++) {
            // get normalized ratings for euclidean distance
            Double song1 = firstUser.getValue().getSongNormalizedRating(songRatingList1.get(songIndex));
            Double song2 = secondUser.getValue().getSongNormalizedRating(songRatingList2.get(songIndex));

            if (fillUserRatings(songIndex, song1, song2, song1Ratings, song2Ratings)) {
                usersHaveSharedSong = true;
            }
        }

        if (!usersHaveSharedSong) {
            return Double.NaN;
        }
        return new EuclideanDistance().compute(song1Ratings, song2Ratings);
    }

    /**
     * Populates matrix with normalized rating for both songs of each user.
     * Normalize ratings for euclidean distance.
     * 
     * @param userEntry1   User 1 map.
     * @param userEntry2   User 2 map.
     * @param songIndex    Index of songlist.entrySet() used to compare users.
     * @param song1Ratings Dimension 1 of matrix.
     * @param song2Ratings Dimension 2 of matrix.
     * @return True if , false if song1 or songlist.entrySet() 2 rating is NaN.
     */
    private boolean fillUserRatings(int songIndex, Double song1, Double song2, double[] song1Ratings,
            double[] song2Ratings) {

        if (song1.isNaN() || song2.isNaN()) {
            song1Ratings[songIndex] = 0.0;
            song2Ratings[songIndex] = 0.0;
            return false;
        }

        song1Ratings[songIndex] = song1;
        song2Ratings[songIndex] = song2;
        return true;
    }

    /**
     * Used to require two cooperative user to compute user similarity.
     */
    private void validateCooperativeUsers() {
        if (this.userList.size() < 2) {
            throw new IllegalArgumentException("at least two cooperative users are required for user similarity");
        }
    }

    /**
     * Transforms songlist.entrySet() map into array.
     * 
     * @param userEntry Used to get songlist.entrySet() list.
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

    public void predictMissingUserSongRating() {
        this.userSongRatingPredictions = new TreeMap<>();
        // targetUser: User that is missing rating data.
        // otherUser: user that is the closest in similarity to targetUser.

        //Loop through all users as targets
        for (Map.Entry<Cs214User, TreeMap<Double, Cs214User>> userEntry : this.similarUserList.entrySet()) {
            Cs214User targetUser = userEntry.getKey();
            TreeMap<String, Double> songListUser1 = targetUser.getSongList();

            // Loop through target users songs
            for (Map.Entry<String, Double> songEntry : songListUser1.entrySet()) {  
                if (songEntry.getValue().equals(Double.NaN)) {
                    double predictedRating = Double.NaN;
                    String key = songEntry.getKey() + "," + userEntry.getKey().getUserName();

                    //Loop through target user similar user list
                    for (Map.Entry<Double, Cs214User> similarUserEntry : userEntry.getValue().entrySet()) { 
                        // get most similar user (other user) and check to see if they rated
                        // targetuser's song
                        TreeMap<String, Double> songListUser2 = similarUserEntry.getValue().getSongList();
                        Double user2SongRating = songListUser2.get(songEntry.getKey());
                        if (!user2SongRating.isNaN()) { // checks if valid rating
                            double otherUserNormalizedRating = similarUserEntry.getValue().getSongNormalizedRating(user2SongRating);

                            //de-normalizing predicted rating
                            predictedRating = Math.round(otherUserNormalizedRating * targetUser.getStandardDeviation() + targetUser.getRunningMean());
                            if(predictedRating < 1.0){
                                predictedRating = 1.0;
                            }
                            else if(predictedRating > 5.0){
                                predictedRating = 5.0;
                            }

                            break;
                        }
                    }
                    this.userSongRatingPredictions.put(key, predictedRating);
                }
            }
            if(userSongRatingPredictions.size() == 0){
                throw new IllegalArgumentException("All users rated all songs. No predictions needed.");
            }
        }
    }

    /**
     * Outputs user1 name, user2 name, and similarity between users.
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

            List<Map.Entry<String, Cs214User>> usersArray = new ArrayList<>(this.userList.entrySet());

            for (int i = 0; i < userSimilarityMatrix.length; i++) {
                for (int j = i+1; j < userSimilarityMatrix.length; j++) {
                    printRecord(printerService, i, j, usersArray.get(i).getKey(), usersArray.get(j).getKey());
                }
            }
        } finally {
            if (printerService != null) {
                printerService.closeCSVFile();
            }
        }
    }

    public void outputUsersSongsRatingPredictionsCSV(String outputFile) throws IOException {
        validateArguments(outputFile);

        CSVPrinterService printerService = null;

        try {
            String[] headers = new String[] { "song", "user", "predicted rating" };

            printerService = new CSVPrinterService(outputFile, headers);

            for (Map.Entry<String, Double> predictionEntry : this.userSongRatingPredictions.entrySet()) {
                String[] keys = predictionEntry.getKey().split(",");
                String valuePredictionEntry = null;
                if (predictionEntry.getValue().isNaN()) {
                    valuePredictionEntry = predictionEntry.getValue().toString();
                } else {
                    valuePredictionEntry = Integer.toString(predictionEntry.getValue().intValue());
                }
                Object[] values = { keys[0], keys[1], valuePredictionEntry };
                printerService.printRecord(values);
            }

        } finally {
            if (printerService != null) {
                printerService.closeCSVFile();
            }
        }
    }

    /**
     * Outputs values objects array with user 1 name, user2 name, and dimensions
     * matrix.
     * 
     * @param printerService CSV printer used to print record.
     * @param i              Index 1 of matrix.
     * @param j              Index 2 of matrix.
     * @param userName1      User 1's name.
     * @param userName2      User 2's name.
     * @throws IOException If printer fails.
     */
    private void printRecord(CSVPrinterService printerService, int i, int j, String userName1, String userName2)
            throws IOException {
        if (this.userSimilarityMatrix[i][j] != null && (i != j && this.userSimilarityMatrix[i][j] >= 0.0
                || this.userSimilarityMatrix[i][j].equals(Double.NaN))) {
            Object[] values = { userName1, userName2, userSimilarityMatrix[i][j] };
            printerService.printRecord(values);
        }
    }
}
