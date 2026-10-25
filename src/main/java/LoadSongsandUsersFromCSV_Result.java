import java.util.TreeMap;

/**
 * 
 * LoadSongsandUsersFromCSV_Result
 */
public class LoadSongsandUsersFromCSV_Result {
    public TreeMap<String, Cs214Song> songList;
    public TreeMap<String, Cs214User> userList;

    /**
     * Sets field variables to given song and user list.
     * @param newSongList Populated song list.
     * @param newUserList Populated user list.
     */
    public LoadSongsandUsersFromCSV_Result(TreeMap<String, Cs214Song> newSongList, TreeMap<String, Cs214User> newUserList ){
        this.songList = newSongList;
        this.userList = newUserList;
    }
}
