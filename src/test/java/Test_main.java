import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;

public class Test_main {
    @Test
    public void test_mainPA1_success() throws IOException {
        String inputFileName = "database/files/testfiles/maintests/test_mainPA1_success_input.csv";
        String outputFileName = "database/files/testfiles/maintests/test_mainPA1_success_output.csv";

        File file = new File(outputFileName);
        if (file.exists()) {
            file.delete();
        }

        Cs214Project.main(new String[] { inputFileName, outputFileName});

        assertEquals(true, file.exists(), "test_mainPA1_success did not return expected exception.");
    }

    @Test
    public void test_mainPA2_success() throws IOException {
        String inputFileName = "database/files/testfiles/maintests/test_mainPA2_success_input.csv";
        String outputFileName = "database/files/testfiles/maintests/test_mainPA2_success_output.csv";

        File file = new File(outputFileName);
        if (file.exists()) {
            file.delete();
        }

        Cs214Project.main(new String[] { inputFileName, outputFileName, "-a"});

        assertEquals(true, file.exists(), "test_mainPA2_success did not return expected exception.");
    }

    @Test
    public void test_mainPA3_success() throws IOException {
        String inputFileName = "database/files/testfiles/maintests/test_mainPA3_success_input.csv";
        String outputFileName = "database/files/testfiles/maintests/test_mainPA3_success_output.csv";

        File file = new File(outputFileName);
        if (file.exists()) {
            file.delete();
        }

        Cs214Project.main(new String[] { inputFileName, outputFileName, "-u"});

        assertEquals(true, file.exists(), "test_mainPA3_success did not return expected exception.");
    }

        @Test
    public void test_mainFail() throws IOException {
        String inputFileName = "database/files/testfiles/maintests/test_mainPA1_success_input.txt";
        String outputFileName = "database/files/testfiles/maintests/test_mainPA1_success_output.csv";

        File file = new File(outputFileName);
        if (file.exists()) {
            file.delete();
        }

        Cs214Project.main(new String[] { inputFileName, outputFileName});
    }

    @Test
    public void test_MainArgumentLowerOOB() throws IOException {
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> Cs214Project.validateArguments(new String[] { "argument1" }),
                "test_MainArgumentLowerOOB did not return expected exception.");

            util_FormatOutput.printError(e.getMessage());
    }

    @Test
    public void test_MainArgumentUpperOOB() throws IOException {
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> Cs214Project
                        .validateArguments(new String[] { "argument1", "argument2", "argument3", "argument4" }),
                "test_MainArgumentUpperOOB did not return expected exception.");

        util_FormatOutput.printError(e.getMessage());
    }

    @Test
    public void test_MainArgument1Empty() throws IOException {
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> Cs214Project.validateArguments(new String[] { "", "argument2" }),
                "test_MainArgument1Empty did not return expected exception.");

        util_FormatOutput.printError(e.getMessage());
    }

    @Test
    public void test_MainArgument1Null() throws IOException {
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> Cs214Project.validateArguments(new String[] { null, "argument2" }),
                "test_MainArgument1Null did not return expected exception.");

        util_FormatOutput.printError(e.getMessage());
    }

    @Test
    public void test_MainArgument2Empty() throws IOException {
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> Cs214Project.validateArguments(new String[] { "argument1", "" }),
                "test_MainArgument2Empty did not return expected exception.");

        util_FormatOutput.printError(e.getMessage());
    }

    @Test
    public void test_MainArgument2Null() throws IOException {
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> Cs214Project.validateArguments(new String[] { "argument1", null }),
                "test_MainArgument2Null did not return expected exception.");

        util_FormatOutput.printError(e.getMessage());
    }

    @Test
    public void test_MainArgument3Invalid() throws IOException {
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> Cs214Project.validateArguments(new String[] { "argument1", "argument2", "-error" }),
                "test_MainArgument3Invalid did not return expected exception.");

        util_FormatOutput.printError(e.getMessage());
    }
}