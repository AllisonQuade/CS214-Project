import java.util.TreeMap;

public class Cs214User extends util_CalculateMeanSDNR {

    private String userName;
    private int userCount = 0;
    private final TreeMap<String, Double> songList = new TreeMap<>();
    //private ArrayList<Double> ratingList = new ArrayList<>(); // PA3 I ADDED

    public Cs214User(String newUserName) {
        if (util_FormatOutput.isNullOrEmpty(newUserName)) {
            throw new IllegalArgumentException("The prarameter \"newUserName\" is required.");
        }
        this.userName = newUserName;
    }

    public void incrementsUserCount() {
        this.userCount++;
    }

    public String getUserName() {
        return this.userName;
    }

    public int getuserCount() {
        return this.userCount;
    }

    public TreeMap<String, Double> getSongList() {
        return this.songList;
    }

    public void addSongAndRating(String songName, Double newRating) {
        validateSongName(songName);
        validateRating(newRating);
        this.songList.put(songName, newRating);
    }

    private void validateSongName(String songName) {
        if (util_FormatOutput.isNullOrEmpty(songName)) {
            throw new IllegalArgumentException("The parameter \"songName\" is required.");
        }
        if (this.songList.containsKey(songName)) {
            throw new IllegalArgumentException("Song name '" + songName + "' already exists");
        }
    }

    private void validateRating(Double rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating " + rating + " is not between 1 and 5.");
        }
    }
}
