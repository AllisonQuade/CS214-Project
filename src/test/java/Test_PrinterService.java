

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;

public class Test_PrinterService {

    @Test
    public void test_OutputParameterNull() {
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> new CSVPrinterService(null), "test_OutputNull did not return expected exception.");

        util_FormatOutput.printError(e.getMessage());
    }

    @Test
    public void test_OutputParameterEmpty() {
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> new CSVPrinterService(""), "test_OutputNull did not return expected exception.");

        util_FormatOutput.printError(e.getMessage());
    }

    @Test
    public void test_OutputContainsBackSlashSuccess() throws IOException {
        String outputFileName = "database/files/testfiles/outputfiles/test_OutputContainsBackSlashSuccess.csv";

        File file = new File(outputFileName);

        if (file.exists()) { // Delete file if it exists
            file.delete();
        }

        CSVPrinterService printerService = new CSVPrinterService(outputFileName, new String[]{"a","b","c","d"});

        printerService.printRecord(new Object[] {"song1", "rating1", 1, 2 });
        printerService.printRecord(new Object[] {"song2", "rating2", 2, 3 });
        printerService.printRecord(new Object[] {"song3", "rating3", 3, 4 });

        assertEquals(true, file.exists(),
                "test_OutputContainsBackSlashSuccess did not return expected exception.");
    }


    @Test
    public void test_OutputFolderNotFound() throws IOException {
        String outputFileName = "database/files/testfilesXXX/outputfiles/test_OutputContainsBackSlashSuccess.csv";
       IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> new CSVPrinterService(outputFileName),
                "test_OutputFolderNotFound did not return expected exception.");

        util_FormatOutput.printError(e.getMessage());
    }

    @Test
    public void test_OutputNonCSV() {
        IllegalStateException e = assertThrows(
                IllegalStateException.class,
                () -> new CSVPrinterService("nonoutput.txt"),
                "test_OutputNonCSV did not return expected exception.");

        util_FormatOutput.printError(e.getMessage());
    }

    @Test
    public void test_NullRecordParameter() {
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> new CSVPrinterService("file1.csv").printRecord(null),
                "test_NullValueParameter did not return expected exception.");

        util_FormatOutput.printError(e.getMessage());
    }


}
