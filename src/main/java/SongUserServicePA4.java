
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
public class SongUserServicePA4 {

    private TreeMap<String, Cs214Song> songList;
    private TreeMap<String, Cs214User> userList;
    //private ArrayList<Double> userRatingList;
    private List<String> masterSongNameList;
    private Double[][] dimensions;

    // #region PA1
    public void loadAndProcessData(String inputFile) throws IOException {
        loadSongsandUsersFromCSV(inputFile);
        dataCleaner();
        populateRatingPlaceHolderData();
        calcSimilarity();
    }

    private LoadSongsandUsersFromCSV_Result loadSongsandUsersFromCSV(String inputFile) throws IOException {

        if (util_FormatOutput.isNullOrEmpty(inputFile)) {
            throw new IllegalArgumentException(
                    "Input file is required and must contain a file path and file name.");
        }

        this.songList = new TreeMap<>();
        this.userList = new TreeMap<>();
        //this.userRatingList = new ArrayList<>();

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

        //if (!parserService.equals(null)) { // handles the parserService object to make sure it was created successfully
        parserService.closeCSVFile();
        //}

        return new LoadSongsandUsersFromCSV_Result(songList, userList);

    }

    /** JavaDoc
     * @param songName
     * @param userName
     * @param rating
     * @return void
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

    private Boolean isInvalidUser(TreeMap<String, Double> songNameRating, Set<String> currentUserSongs, Double firstRating,
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

    public void populateRatingPlaceHolderData() { //puts missing song from user into songList with NaN
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
    //     //https://www.geeksforgeeks.org/java/how-to-convert-treemap-to-an-arraylist-in-java/
    //     validateCooperativeUsers();
    //     EuclideanDistance EuclideanDistance = new EuclideanDistance();
    //     this.dimensions = new Double[masterSongNameList.size()][masterSongNameList.size()];
    //     double edResult;
    //     for (int i = 0; i < this.masterSongNameList.size(); i++) {
    //         for (int s = i + 1; s < this.masterSongNameList.size(); s++) {
    //             double[] song1Rating = new double[userList.size()];
    //             double[] song2Rating = new double[userList.size()];
    //             boolean songsHaveSharedUser = false;
    //             int userIndex = 0; //anytime need index tracker for a for each loop, initilize counter above for each loop
    //             for (Map.Entry<String, Cs214User> userEntry : this.userList.entrySet()) {
    //                 ArrayList<Double> songRatingList = getSongRatingArray(userEntry); //turns treemap to array list
    //                 //get normalized ratings for euclidean distance
    //                 Double song1 = userEntry.getValue().getSongNormalizedRating(songRatingList.get(i));
    //                 Double song2 = userEntry.getValue().getSongNormalizedRating(songRatingList.get(s));
    //                 if (song1.equals(Double.NaN) || song2.equals(Double.NaN)) {
    //                     song1Rating[userIndex] = 0.0;
    //                     song2Rating[userIndex] = 0.0;
    //                 } else {
    //                     song1Rating[userIndex] = song1;
    //                     song2Rating[userIndex] = song2;
    //                     songsHaveSharedUser = true;
    //                 }
    //                 userIndex++;
    //             }
    //             if (songsHaveSharedUser) {
    //                 edResult = EuclideanDistance.compute(song1Rating, song2Rating);
    //             } else {
    //                 edResult = Double.NaN;
    //             }
    //             dimensions[i][s] = edResult;
    //         }
    //     }
    //     return dimensions;
    // }
    public Double[][] calcSimilarity() throws IOException {
        validateCooperativeUsers();
 
        int numUsers = this.userList.size();
        this.dimensions = new Double[numUsers][numUsers];

        //https://www.tutorialspoint.com/article/how-to-get-treemap-key-or-value-using-index-in-java
        List<Map.Entry<String, Cs214User>> users = new ArrayList<>(this.userList.entrySet());

        for (int firstUserIndex = 0; firstUserIndex < numUsers; firstUserIndex++)  {
            for (int secondUserIndex = firstUserIndex + 1; secondUserIndex < numUsers; secondUserIndex++) {
                dimensions[firstUserIndex][secondUserIndex] = calcSongPairDistance(users.get(firstUserIndex), users.get(secondUserIndex));
            }
        }
        return dimensions;
    }

// Distance between user firstUserIndex and user secondUserIndex across all songs they both rated (NaN if no both users didn't rate the song)
    private double calcSongPairDistance(Map.Entry<String, Cs214User> firstUser, Map.Entry<String, Cs214User> secondUser) {
        double[] song1Ratings = new double[masterSongNameList.size()];
        double[] song2Ratings = new double[masterSongNameList.size()];
        boolean usersHaveSharedSong = false;

        ArrayList<Double> user1Ratings = getSongRatingArray(firstUser);
        ArrayList<Double> user2Ratings = getSongRatingArray(secondUser);

        for (int songIndex = 0; songIndex < user1Ratings.size(); songIndex++) {
            if(fillUserRatings(firstUser, secondUser, songIndex, song1Ratings, song2Ratings)){
                usersHaveSharedSong = true;
            }
        }

        if (!usersHaveSharedSong) {
            return Double.NaN;
        }
        return new EuclideanDistance().compute(song1Ratings, song2Ratings);
    }

    private boolean fillUserRatings(Map.Entry<String, Cs214User> userEntry1, Map.Entry<String, Cs214User> userEntry2, int songIndex, double[] song1Rating, double[] song2Rating) {
        ArrayList<Double> songRatingList1 = getSongRatingArray(userEntry1); // turns treemap to array list
         ArrayList<Double> songRatingList2 = getSongRatingArray(userEntry2);

        // get normalized ratings for euclidean distance
        Double song1 = userEntry1.getValue().getSongNormalizedRating(songRatingList1.get(songIndex));
        Double song2 = userEntry2.getValue().getSongNormalizedRating(songRatingList2.get(songIndex));

        if (song1.isNaN() || song2.isNaN()) {
            song1Rating[songIndex] = 0.0;
            song2Rating[songIndex] = 0.0;
            return false;
        }

        song1Rating[songIndex] = song1;
        song2Rating[songIndex] = song2;
        return true;
    }

    private void validateCooperativeUsers() {
        if (this.userList.size() < 2) {
            throw new IllegalArgumentException("at least two cooperative users are required for song similarity");
        }
    }

    private ArrayList<Double> getSongRatingArray(Map.Entry<String, Cs214User> userEntry) {
        TreeMap<String, Double> songNameRating = userEntry.getValue().getSongList();
        ArrayList<Double> songRating = new ArrayList<>(songNameRating.values());
        return songRating;
    }

    public void validateArguments(String outputFile) {
        if (util_FormatOutput.isNullOrEmpty(outputFile)) {
            throw new IllegalArgumentException("Output file is required and must contain a file name.");
        }

        if (this.userList == null) {
            throw new IllegalArgumentException("songs parameter required.");
        }
    }

    public void outputUsersSongsRatingsCSV(String outputFile) throws IOException {
        validateArguments(outputFile);

        CSVPrinterService printerService = null;

        try {
            String[] headers = new String[]{"name1", "name2", "similarity"};

            printerService = new CSVPrinterService(outputFile, headers);

            List<Map.Entry<String, Cs214User>> usersArray = new ArrayList<>(this.userList.entrySet());

            for (int i = 0; i < dimensions.length; i++) {
                for (int j = 0; j < dimensions.length; j++) {
                    printRecord(printerService, i, j, usersArray.get(i).getKey(), usersArray.get(j).getKey());
                }
            }

        } finally {
            if (printerService != null) {
                printerService.closeCSVFile();
            }
        }
    }
    // #endregion PA1

    private void printRecord(CSVPrinterService printerService, int i, int j, String userName1, String userName2) throws IOException {
        if (this.dimensions[i][j] != null && (i != j && this.dimensions[i][j] > 0.0 || this.dimensions[i][j].equals(Double.NaN))) {
            Object[] values = {userName1, userName2, dimensions[i][j]};
            printerService.printRecord(values);
        }
    }
}
