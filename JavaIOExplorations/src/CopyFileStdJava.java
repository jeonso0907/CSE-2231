import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Program to copy a text file into another file.
 *
 * @author Put your name here
 *
 */
public final class CopyFileStdJava {

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private CopyFileStdJava() {
    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments: input-file-name output-file-name
     */
    public static void main(String[] args) {

        // TODO - fill in body
        BufferedReader input;
        PrintWriter output;

        try {
            input = new BufferedReader(new FileReader(args[0]));
            output = new PrintWriter(
                    new BufferedWriter(new FileWriter(args[1])));
        } catch (IOException e) {
            System.err.println("File Opening Error");
            return;
        }

        try {
            String line = input.readLine();
            while (line != null) {
                output.println(line);
                line = input.readLine();
            }
        } catch (IOException e) {
            System.err.println("Reading or Writing the File Error");
        }

        try {
            input.close();
            output.close();
        } catch (IOException e) {
            System.err.println("Closing Error with either Reading or Writing");
        }

    }

}
