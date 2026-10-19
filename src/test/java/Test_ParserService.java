import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.csv.CSVParser;
import org.junit.jupiter.api.Test;

public class Test_ParserService {
    @Test
    public void test_CSVServiceConstructor() throws IOException {
        CSVParserService parserService = new CSVParserService("database/files/testfiles/file1_copy.csv", false);
        assertEquals(CSVParserService.class, parserService.getClass(),
                "test_CSVServiceConstructor did not return expected exception.");
    }

    @Test
    public void test_GetParser() throws IOException {
        CSVParserService parserService = new CSVParserService("database/files/testfiles/file1_copy.csv", false);
        assertEquals(CSVParser.class, parserService.getParser().getClass(),
                "test_GetParser did not return expected exception."); // checks class type comparison
    }

    @Test
    public void test_OpenNonExistingFile() {
        FileNotFoundException e = assertThrows(
                FileNotFoundException.class,
                () -> new CSVParserService("database/files/testfiles/file1_notFound.csv", false),
                "test_OpenNonExistingFile did not return expected exception.");

        util_FormatOutput.printError(e.getMessage());
    }

    @Test
    public void test_Argument1Empty() {
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> new CSVParserService("", false),
                "test_InvalidArgument1 did not return expected exception.");

        util_FormatOutput.printError(e.getMessage());
    }

    
    @Test
    public void test_Argument1Null() {
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> new CSVParserService(null, false),
                "test_Argument1Null did not return expected exception.");

        util_FormatOutput.printError(e.getMessage());
    }

    @Test
    public void test_OpenEmptyInputFile() {
        IllegalStateException e = assertThrows(
                IllegalStateException.class,
                () -> new CSVParserService("database/files/testfiles/fileEmpty.csv", false),
                "test_OpenEmptyInputFile did not return expected exception.");

        util_FormatOutput.printError(e.getMessage());
    }

    @Test
    public void test_OpenNonCSV() {
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> new CSVParserService("database/files/testfiles/noncsv_file.txt", false),
                "test_OpenNonCSV did not return expected exception."); // this message is for if the assertThrows
                                                                              // fails!

        util_FormatOutput.printError(e.getMessage());
    }
}
