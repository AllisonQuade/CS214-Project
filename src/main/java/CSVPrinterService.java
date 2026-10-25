
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

/**
 * 
 * CSVPrinterService
 */
public class CSVPrinterService {

    private Writer writer = null;
    private CSVPrinter printer = null;

    /**
     * Constructor that sets output file and no headers.
     * @param outputFile Output path file. 
     * @throws IOException If invalid output file.
     */
    public CSVPrinterService(String outputFile) throws IOException { // No headers constructor
        this(outputFile, null); // null disables header
    }

    /**
     * Consturctor with output file and set headers.
     * @param outputFile Output file CSV path.
     * @param headers Given string array of headers for output.
     * @throws IOException If FileWriter or CSVPrinter fails.
     */
    public CSVPrinterService(String outputFile, String[] headers) throws IOException {
        validateOutputFileName(outputFile);
        validateOutputFolder(outputFile);
        validateCsvExtension(outputFile);

        CSVFormat outputFormat = buildOutputFormat(headers);

        this.writer = new FileWriter(outputFile);
        this.printer = new CSVPrinter(writer, outputFormat);
    }

    /**
     * Validates that an output file is required.
     * @param outputFile Output CSV file path.
     */
    private static void validateOutputFileName(String outputFile) {
        if (util_FormatOutput.isNullOrEmpty(outputFile)) {
            throw new IllegalArgumentException("Output file is required.");
        }
    }

    /**
     * Validates valid output path.
     * @param outputFile Output CSV file path.
     */
    private static void validateOutputFolder(String outputFile) {
        // e.g. "database/files/testfiles/outputfiles/test_OutputContainsBackSlashSuccess.csv"
        String directoryPath = new File(outputFile).getParent();

        if (directoryPath != null) {
            File folder = new File(directoryPath);

            if (!folder.exists() || !folder.isDirectory()) {
                throw new IllegalArgumentException("Folder \"" + directoryPath + "\" not found.");
            }
        }
    }

    /**
     * Validates that output path is written as a CSV.
     * @param outputFile Output CSV file path.
     */
    private static void validateCsvExtension(String outputFile) {
        if (!outputFile.endsWith("csv")) {
            throw new IllegalStateException("The parameter \"" + outputFile + "\" must have `.csv` extension");
        }
    }

    /**
     * Builds output format.
     * @param headers Given headers.
     * @return CSV Format
     */
    private static CSVFormat buildOutputFormat(String[] headers) {
        return CSVFormat.DEFAULT.builder()
                .setHeader(headers)
                .build();
    }

    /**
     * Prints data.
     * @param record Row of data
     * @throws IOException If parameter record is null.
     */
    public void printRecord(Object[] record) throws IOException {
        if (record == null) {
            throw new IllegalArgumentException("The parameter \"record\" is required.");
        }

        this.printer.printRecord(record);
    }

    /**
     * Closes CSV file.
     * @throws IOException If wrtier or printer failed to close.
     */
    public void closeCSVFile() throws IOException {
        if (this.writer != null) {
            this.writer.close();
        }

        if (this.printer != null) {
            this.printer.close();
        }
    }
}
