/**
 * 
 * util_FormatOutput
 */
public class util_FormatOutput {
    /**
     * deafult constructor
     */
    public util_FormatOutput(){
    }

    /**
     * Determines if an input string is null or empty.
     * @param str Input string.
     * @return true if string in null or empty.
     */
    public static boolean isNullOrEmpty(String str){
        return str == null || str.isEmpty();
    }

    /**
     * Adds "Error:" in front of error messages.
     * @param errorMessage The error message from test.
     */
     public static void printError(String errorMessage){
        System.err.print("Error: " + errorMessage);
    }
}
