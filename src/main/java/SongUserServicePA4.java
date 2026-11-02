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

    private TreeMap<String, Cs214User> userList;
    private List<String> masterSongNameList;
    private Double[][] userSimilarityMatrix;
    private TreeMap<Cs214User, TreeMap<Double, Cs214User>> similarUserList;
    private TreeMap<String, Double> userSongRatingPredictions;

    private CSVPrinterService printerService = null;

    /**
     * deafult constructor
     */
    // public SongUserServicePA4() {
    // }

    /**
     * Calls methods to load data, clean data, populate placeholder data, and
     * calculate similarity.
     * 
     * @param inputFile Input CSV file path.
     * @throws IOException If load songlist.entrySet() and users from CSV fails.
     */
    public void loadAndProcessDataUserSimilarity(String inputFile) throws IOException {
        loadUsersFromCSV(inputFile);
        dataCleaner();
        populateRatingPlaceHolderData();
        calcSimilarity();
    }

    public void loadAndProcessDataPrediction(String inputFile) throws IOException {
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
    private TreeMap<String, Cs214User> loadUsersFromCSV(String inputFile) throws IOException {

        if (util_FormatOutput.isNullOrEmpty(inputFile)) {
            throw new IllegalArgumentException(
                    "Input file is required and must contain a file path and file name.");
        }

        this.userList = new TreeMap<>();

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

            processUserData(songName, userName, rating);

            Collections.sort(masterSongNameList); // sorts final master list
        }

        parserService.closeCSVFile();

        return userList;

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

        initilizeSimilarUserListKey();

        int firstUserIndex = 0;
        for (Map.Entry<String, Cs214User> userEntryRow : this.userList.entrySet()) {
            int secondUserIndex = 0;
            for (Map.Entry<String, Cs214User> userEntryColumn : this.userList.entrySet()) {
                double calcSongPair = calcSongPairDistance(userEntryRow.getValue(), userEntryColumn.getValue());
                userSimilarityMatrix[firstUserIndex][secondUserIndex] = calcSongPair;
                // userSimilarityMatrix[secondUserIndex][firstUserIndex] = calcSongPair;

                populateUserSimilarityMap(userEntryRow.getValue(), userEntryColumn.getValue(), calcSongPair);

                secondUserIndex++;
            }
            firstUserIndex++;
        }
    }

    private void initilizeSimilarUserListKey() {
        this.similarUserList = new TreeMap<>();
        for (Map.Entry<String, Cs214User> userEntry : userList.entrySet()) { // populate similarUserList treemap for all
                                                                             // users
            this.similarUserList.put(userEntry.getValue(), new TreeMap<Double, Cs214User>());
        }
    }

    private void populateUserSimilarityMap(Cs214User userEntryRow, Cs214User userEntryColumn, Double similarityValue) {
        if (userEntryRow != userEntryColumn && !similarityValue.isNaN()) {
            // can use key to get user
            TreeMap<Double, Cs214User> user1NestedTreeMap = similarUserList.get(userEntryRow); // get user key from the
                                                                                               // treemap
            user1NestedTreeMap.put(similarityValue, userEntryColumn);
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
    private double calcSongPairDistance(Cs214User firstUser, Cs214User secondUser) {

        if (firstUser == secondUser) {
            return 0.0;
        }

        double[] song1Ratings = new double[masterSongNameList.size()];
        double[] song2Ratings = new double[masterSongNameList.size()];
        boolean usersHaveSharedSong = false;

        TreeMap<String, Double> secondUserSongList = secondUser.getSongList();
        int index = 0;

        for (Map.Entry<String, Double> songNameRatingEntry : firstUser.getSongList().entrySet()) {
            Double song1 = firstUser.getSongNormalizedRating(songNameRatingEntry.getValue());
            Double song2 = secondUser.getSongNormalizedRating(secondUserSongList.get(songNameRatingEntry.getKey()));

            if (!Double.isFinite(song1) || !Double.isFinite(song2)) { // isFinite returns true if its not infinity or
                                                                      // NaN
                song1Ratings[index] = 0.0;
                song2Ratings[index] = 0.0;
            } else {
                song1Ratings[index] = song1;
                song2Ratings[index] = song2;
                usersHaveSharedSong = true;
            }
            index++;
        }

        if (!usersHaveSharedSong) {
            return Double.NaN;
        }

        return new EuclideanDistance().compute(song1Ratings, song2Ratings);
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
     * Predict a target user's empty rating based on the most similar users rating
     */
    public void predictMissingUserSongRating() {
        this.userSongRatingPredictions = new TreeMap<>();
        // targetUser: User that is missing rating data.
        // otherUser: user that is the closest in similarity to targetUser.

        // Loop through all users as targets
        for (Map.Entry<Cs214User, TreeMap<Double, Cs214User>> userEntry : this.similarUserList.entrySet()) {
            TreeMap<String, Double> songListUser1 = userEntry.getKey().getSongList();

            predictTargetUserSongRatings(userEntry, songListUser1);

            if (userSongRatingPredictions.size() == 0) {
                throw new IllegalArgumentException("All users rated all songs. No predictions needed.");
            }
        }
    }

    private void predictTargetUserSongRatings(Map.Entry<Cs214User, TreeMap<Double, Cs214User>> userEntry,
            TreeMap<String, Double> songListUser1) {
        // Loop through target users songs
        for (Map.Entry<String, Double> songEntry : songListUser1.entrySet()) {
            if (songEntry.getValue().equals(Double.NaN)) {
                String key = songEntry.getKey() + "," + userEntry.getKey().getUserName();

                double predictedRating = calculatePredictedRating(userEntry, songEntry);

                this.userSongRatingPredictions.put(key, predictedRating);
            }
        }
    }

    private double calculatePredictedRating(Map.Entry<Cs214User, TreeMap<Double, Cs214User>> userEntry,
            Map.Entry<String, Double> songEntry) {
        double predictedRating = Double.NaN;

        // Loop through target user similar user list
        for (Map.Entry<Double, Cs214User> similarUserEntry : userEntry.getValue().entrySet()) {
            // get most similar user (other user) and check to see if they rated
            // targetuser's song
            TreeMap<String, Double> songListUser2 = similarUserEntry.getValue().getSongList();
            Double user2SongRating = songListUser2.get(songEntry.getKey());

            if (!user2SongRating.isNaN()) { // checks if valid rating
                double otherUserNormalizedRating = similarUserEntry.getValue().getSongNormalizedRating(user2SongRating);

                // de-normalizing predicted rating
                predictedRating = Math.round(otherUserNormalizedRating * userEntry.getKey().getStandardDeviation()
                        + userEntry.getKey().getRunningMean());

                Math.max(1.0, Math.min(5.0, predictedRating));
                break;
            }
        }

        return predictedRating;
    }

    /**
     * Outputs user1 name, user2 name, and similarity between users.
     * 
     * @param outputFile Output CSV file path.
     * @throws IOException If printer service fail.
     */
    public void outputUsersSongsRatingsCSV(String outputFile) throws IOException {
        validateArguments(outputFile);

        try {
            String[] headers = new String[] { "name1", "name2", "similarity" };

            printerService = new CSVPrinterService(outputFile, headers);

            List<Map.Entry<String, Cs214User>> usersArray = new ArrayList<>(this.userList.entrySet());

            for (int rowIndex = 0; rowIndex < userSimilarityMatrix.length; rowIndex++) {
                printDataRow(usersArray, rowIndex);
            }
        } finally {
            if (printerService != null) {
                printerService.closeCSVFile();
            }
        }
    }

    private void printDataRow(List<Map.Entry<String, Cs214User>> usersArray, int rowIndex) throws IOException {
        for (int colIndex = rowIndex + 1; colIndex < userSimilarityMatrix.length; colIndex++) {
            Double similariyValue = this.userSimilarityMatrix[rowIndex][colIndex];

            if (similariyValue != null && rowIndex != colIndex
                    && (similariyValue >= 0.0 || similariyValue.equals(Double.NaN))) {
                Object[] values = { usersArray.get(rowIndex).getKey(), usersArray.get(colIndex).getKey(),
                        similariyValue };
                printerService.printRecord(values);
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
}
