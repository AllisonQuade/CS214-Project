 import java.io.IOException;
 import java.util.ArrayList;

 /**
  * 
  * Cs214Song
  */
 public class Cs214Song extends util_CalculateMeanSDNR{
    private String songName; //stores name of song
    private int songCount = 0;
    private final ArrayList<Double> ratings = new ArrayList<>(); //Puts rating of this song/user into an arraylist
    
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public Cs214Song(String newSongName){ //garuntees a song name      
      this.setSongName(newSongName);
    }

    
    public void setSongName(String newSongName){
      if (util_FormatOutput.isNullOrEmpty(newSongName))
      {
          throw new IllegalArgumentException("Song Name is required.");
      }

      this.songName = newSongName;
    }
    
    public String getSongName(){
      return this.songName;
    }
    
    //you can also create a method to increment numberSongsOccured instead of using setters and getters
    public void incrementsSongCount(){ //increments song counter by 1
      this.songCount++;
    }


    public int getNumberSongsOccured() {
      return this.songCount;
    }

    public void setNumberSongsOccured(int newNumberSongsOccured) throws IOException{
      if(newNumberSongsOccured < 0){
        throw new IllegalArgumentException("Number of Songs Occured can't be less than 0.");
      }
      this.songCount = newNumberSongsOccured;
    }


    public int getRatingsCount(){
      return ratings.size();
    }

    public ArrayList<Double> getRatings(){
      return ratings;
    }
    

    /**
     * Add rating and uses rating to calculate running mean and standard deviation.
     * @param newRating Rating added to ratings array.
     * @throws IOException
     */
    public void addRating(double newRating) throws IOException{
      if(newRating < 0 || newRating > 5){
        throw new IllegalArgumentException("The rating" + newRating + "is out of bounds.");
      }
      this.ratings.add(newRating);
      super.calculateRunningMeanAndStandardDeviation(ratings);
    }
  }