import java.io.IOException;

public abstract class PA {
    //find similariites of PA1 and PA2
    public abstract void loadSongsandUsersFromCSV(String inputFile) throws IOException;
    public abstract void outputUsersSongsRatingsCSV(String outputFile) throws IOException;
}
