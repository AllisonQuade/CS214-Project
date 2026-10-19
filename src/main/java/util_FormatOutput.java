public class util_FormatOutput {
    public static boolean isNullOrEmpty(String str){
        return str == null || str.isEmpty();
    }

     public static void printError(String errorMessage){
        System.err.print("Error: " + errorMessage);
    }
}
