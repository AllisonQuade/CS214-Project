
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class CSVPrinterService {

    private Writer writer = null;
    private CSVPrinter printer = null;

    public CSVPrinterService(String outputFile) throws IOException { // No headers constructor
        this(outputFile, null); // null disables header
    }

    public CSVPrinterService(String outputFile, String[] headers) throws IOException {
        validateOutputFileName(outputFile);
        validateOutputFolder(outputFile);
        validateCsvExtension(outputFile);

        CSVFormat outputFormat = buildOutputFormat(headers);

        this.writer = new FileWriter(outputFile);
        this.printer = new CSVPrinter(writer, outputFormat);
    }

    private static void validateOutputFileName(String outputFile) {
        if (util_FormatOutput.isNullOrEmpty(outputFile)) {
            throw new IllegalArgumentException("Output file is required.");
        }
    }

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

    private static void validateCsvExtension(String outputFile) {
        if (!outputFile.endsWith("csv")) {
            throw new IllegalStateException("The parameter \"" + outputFile + "\" must have `.csv` extension");
        }
    }

    private static CSVFormat buildOutputFormat(String[] headers) {
        return CSVFormat.DEFAULT.builder()
                .setHeader(headers)
                .build();
    }

    public void printRecord(Object[] record) throws IOException { // record = row of data
        if (record == null) {
            throw new IllegalArgumentException("The parameter \"record\" is required.");
        }

        this.printer.printRecord(record);
    }

    public void closeCSVFile() throws IOException {
        if (this.writer != null) {
            this.writer.close();
        }

        if (this.printer != null) {
            this.printer.close();
        }
    }
}
