import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

public class CSVParserService {
  private String inputFile;
  private CSVParser parser = null;
  private Reader reader = null;

  public enum fileOrDataInput {
    FILEPATH, DATASTRING 
  }

  public CSVParserService(String newInputFile, boolean skipHeader)throws IOException, FileNotFoundException { // create new file object for each file

    if (util_FormatOutput.isNullOrEmpty(newInputFile)) {
      throw new IllegalArgumentException("newInputFile is required and must contain a file path and file name.");
    }

    boolean fileEnd = newInputFile.endsWith("csv");
    if (!fileEnd) {
      throw new IllegalArgumentException("The parameter \"newInputFile\" must have `.csv` extension");
    }

    this.inputFile = newInputFile;

    CSVFormat inputFormat = CSVFormat.DEFAULT.builder() // open file
        .setSkipHeaderRecord(skipHeader)
        .build();

    this.reader = new FileReader(this.inputFile); // opening text file
    this.parser = inputFormat.parse(reader); // parse through reader

    if (!this.parser.iterator().hasNext()) { // error handle input file empty
      throw new IllegalStateException("File data empty.");
    }

  }


public CSVParserService(String inputFilePathOrDataString, fileOrDataInput inputFileOrString, Boolean skipHeader) throws IOException{ // create new file object for each file
  if (util_FormatOutput.isNullOrEmpty(inputFilePathOrDataString)) {
      throw new IllegalArgumentException("inputFilePathOrDataString is required.");
  }
  switch(inputFileOrString) //-> rule switch
  {
    case FILEPATH -> {
        boolean fileEnd = inputFilePathOrDataString.endsWith("csv");
        if (!fileEnd) {
            throw new IllegalStateException("The parameter \"newInputFile\" must have `.csv` extension");
        }
        this.inputFile = inputFilePathOrDataString;
        
        this.reader = new FileReader(this.inputFile); // opening text file
          }

    case DATASTRING -> {
        this.reader = new StringReader(inputFilePathOrDataString); // opening text file
          }
  }
  
    CSVFormat inputFormat = CSVFormat.DEFAULT.builder() // open file
        .setSkipHeaderRecord(skipHeader)
        .build();

    this.parser = inputFormat.parse(reader); // parse through reader

    if (!this.parser.iterator().hasNext()) { // error handle input file empty
      throw new IllegalStateException("File data empty.");
    }

  }


  public CSVParser getParser() {
    return this.parser;
  }

  public void closeCSVFile() throws IOException {
    if (this.parser != null) { // handles parser instance variable in THIS parser service
      this.parser.close();
    }

    if (this.reader != null) {
      this.reader.close();
    }
  }

}
