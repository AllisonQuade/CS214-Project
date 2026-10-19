// import java.io.IOException;
// import java.util.ArrayList;
// import java.util.Map;
// import java.util.TreeMap;
// import java.util.Set;
// import java.util.TreeSet;
// import java.util.List;
// import java.util.Collections;

// import org.apache.commons.csv.CSVRecord;
// //DLLs ^

// public class NOUSESongUserServicePA3 {
//     // https://www.geeksforgeeks.org/java/sorting-hashmap-according-key-value-java/
//     // use object to return song and users so you don't have to read file multiple
//     // times

//     private TreeMap<String, TreeMap<String, TreeMap<String, Double>>> users;
//     private List<String> masterSongNameList; // master = I have everything (all unique song names)

//     public TreeMap<String, TreeMap<String, Double>> getUsers() {
//         return this.users;
//     }

//     public TreeMap<String, Double> getSongRatingsForUserName(String userName) {
//         return users.get(userName);
//     }

//     // #region PA2
//     public void loadSongsandUsersKeysFromCSV(String inputFile) throws IOException {
//         if (MyUtilities.isNullOrEmpty(inputFile)) {
//             throw new IllegalArgumentException(
//                     "Input file is required and must contain a file path and file name.");
//         }

//         this.users = new TreeMap<>(); // Nested TreeMap<UserName,
//                                       // TreeMap<SongName, Rating>>

//         this.masterSongNameList = new ArrayList<>(); // Keeps a list of ALL songs in dataset

//         CSVParserService parserService = null;

//         // try {
//         parserService = new CSVParserService(inputFile, false);

//         for (CSVRecord record : parserService.getParser()) {
//             if (record.size() != 3) {
//                 throw new IllegalArgumentException("Expected 3 fields but got " + record.size()
//                         + " at record number " + record.getRecordNumber());
//             }

//             String song = record.get(0); // Gets Song
//             String user = record.get(1); // Gets User
//             double rating = Integer.parseInt(record.get(2)); // Gets Rating

//             if (!masterSongNameList.contains(song)) {
//                 masterSongNameList.add(song);
//             }


//             TreeMap<Song,TreeMap<String,Double>> songsProperties;
//             TreeMap<String, Double> ratings;

//             if (this.users.containsKey(user)) {
//                 TreeMap<String, TreeMap<String, Double>> usersSongsRatings;
//                 //new key
//                 //new tree
//                 //new key hardcode Rating, with value of rating

//                 userSongsRatings = this.users.get(user); // getting value for user key
//                 songsRatings.put(song, rating);
//             }

//             // Otherwise add user
//             else {
//                 songsProperties = new TreeMap<>(); // new treemap needs to be in else so each user has their own
//                 ratings = new TreeMap<>();
//                 ratings.put("Rating",rating); //inner most treemap (ratings name and rating)
                


//                 songsProperties.put(song,ratings); //CHANGE TO: put(song, new TreeMap<String,Double>),

//                 this.users.put(user, songsProperties);
//             }
//         }

//         PA3Calculations calculate = new PA3Calculations();
//         calculate.calculateRunningMeanAndStandardDeviationAndNormalization(updatedUsers);

//         Collections.sort(masterSongNameList); // sorts final master list

//         if (parserService != null) { // handles the parserService object to make sure it was created successfully
//             parserService.closeCSVFile();
//         }
//     }

//     public void dataCleaner() throws IOException {
//         List<String> invalidUsers = new ArrayList<>();

//         // Unique sets list
//         // https://codemia.io/knowledge-hub/path/best_way_to_merge_and_remove_duplicates_from_multiple_lists_in_java
//         Set<String> validUserSongs = new TreeSet<>();
//         Set<String> invalidUserSongs = new TreeSet<>();

//         for (Map.Entry<String, TreeMap<String, Double>> userEntry : this.users.entrySet()) { // stepthrough users
//             TreeMap<String, Double> songNameRating = userEntry.getValue();

//             // this list keeps track of song names for current user
//             Set<String> currentUserSongs = new TreeSet<>();

//             Double firstRating = songNameRating.firstEntry().getValue(); // the first rating in the treeMap
//             Boolean invalidUser = true;

//             for (Map.Entry<String, Double> songEntry : songNameRating.entrySet()) { // stepthrough user's songs and
//                                                                                     // ratings
//                 currentUserSongs.add(songEntry.getKey()); // list of all my current user songs

//                 // if(!firstRating.equals(songEntry.getValue())){ //valid user //This if way
//                 // works same as if below, gets value of Double and NOT object (memory space)
//                 if (firstRating.doubleValue() != songEntry.getValue().doubleValue()) {
//                     invalidUser = false;
//                 }
//             }

//             if (invalidUser) { // invalid user
//                 invalidUserSongs.addAll(currentUserSongs); // merge all user songs into invalid user songs list
//                 invalidUsers.add(userEntry.getKey()); // add current invalid user name to invalidUsers list
//             } else {
//                 validUserSongs.addAll(currentUserSongs);
//             }
//         }

//         invalidUserSongs.removeAll(validUserSongs);
//         masterSongNameList.removeAll(invalidUserSongs);

//         for (String user : invalidUsers) {
//             users.remove(user);
//         }
//     }

//     public void outputUsersSongsRatingsCSV(String outputFile) throws IOException {
//         if (MyUtilities.isNullOrEmpty(outputFile)) {
//             throw new IllegalArgumentException("Output file is required and must contain a file name.");
//         }

//         // if(outputFile.contains("/")){
//         // throw new IllegalArgumentException("System only outputs to current working
//         // folder.");
//         // }

//         CSVPrinterService printerService = null;

//         try {
//             String[] headers = new String[] { "username", "song", "rating" };

//             printerService = new CSVPrinterService(outputFile, headers);
//             // Step through each user
//             for (Map.Entry<String, TreeMap<String, Double>> entry : users.entrySet()) {

//                 String userName = entry.getKey();
//                 TreeMap<String, Double> songNameRating = entry.getValue();

//                 // Step through each song/rating for this user
//                 Double rating;
//                 Object[] record;
//                 for (String songName : this.masterSongNameList) {
//                     if (songNameRating.containsKey(songName)) {
//                         rating = songNameRating.get(songName);
//                         Integer intRating = rating.intValue();
//                         record = new Object[] { userName, songName, intRating };
//                     } else {
//                         rating = Double.NaN;
//                         record = new Object[] { userName, songName, rating };
//                     }
//                     printerService.printRecord(record);
//                 }
//             }

//             if (printerService != null) {
//                 printerService.closeCSVFile();
//             }
//         } finally {
//         }
//         ;
//     }
//     // #endregion PA2
// }
