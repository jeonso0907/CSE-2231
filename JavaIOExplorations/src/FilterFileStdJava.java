import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Program to copy a text file into another file.
 *
 * @author Put your name here
 *
 */
public final class FilterFileStdJava {

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private FilterFileStdJava() {
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
        BufferedReader inputFilter;
        PrintWriter output;
        String inputFile = args[0];
        String outputFile = args[1];
        String filterFile = args[2];

        Set<String> set = new HashSet<>();

        try {
            input = new BufferedReader(new FileReader(inputFile));
            inputFilter = new BufferedReader(new FileReader(filterFile));
            output = new PrintWriter(
                    new BufferedWriter(new FileWriter(outputFile)));
        } catch (IOException e) {
            System.err.println("File Opening Error");
            return;
        }

        try {
            String line = input.readLine();
            String filterLine = inputFilter.readLine();

            while (filterLine != null) {
                set.add(filterLine);
                filterLine = inputFilter.readLine();
            }

            while (line != null) {
                boolean contains = false;
                Iterator<String> it = set.iterator();
                while (it.hasNext() && !contains) {
                    if (line.contains(it.next())) {
                        contains = true;
                        output.println(line);
                        line = input.readLine();
                    }
                }
            }

        } catch (IOException e) {
            System.err.println("Reading or Writing the File Error");
        }

        try {
            input.close();
            output.close();
            inputFilter.close();
        } catch (IOException e) {
            System.err.println("Closing Error with either Reading or Writing");
        }

    }

}
