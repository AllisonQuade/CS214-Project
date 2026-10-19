import java.util.TreeMap;

public class LoadSongsandUsersFromCSV_Result {
    public TreeMap<String, Cs214Song> songList;
    public TreeMap<String, Cs214User> userList;

    public LoadSongsandUsersFromCSV_Result(TreeMap<String, Cs214Song> newSongList, TreeMap<String, Cs214User> newUserList ){
        this.songList = newSongList;
        this.userList = newUserList;
    }
}
