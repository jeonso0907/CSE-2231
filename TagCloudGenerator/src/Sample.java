import java.util.Comparator;

import components.map.Map;
import components.map.Map.Pair;
import components.map.Map1L;
import components.queue.Queue;
import components.queue.Queue1L;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;
import components.sortingmachine.SortingMachine;
import components.sortingmachine.SortingMachine1L;

/**
 * Simple HelloWorld program (clear of Checkstyle and FindBugs warnings).
 *
 * @author P. Bucci
 */
public final class Sample {

    /**
     * Default constructor--private to prevent instantiation.
     */
    private Sample() {
        // no code needed here
    }

    /**
     * Comparator<Map.Pair<String, Integer>> implementation to be used to
     * compare the key. Compare {@code Map.Pair<String, Integer>}s in
     * lexicographic order.
     */
    private static class wordComparator
            implements Comparator<Map.Pair<String, Integer>> {
        @Override
        public int compare(Map.Pair<String, Integer> one,
                Map.Pair<String, Integer> two) {

            return one.key().toLowerCase().compareTo(two.key().toLowerCase());
        }
    }

    /**
     * Comparator<Map.Pair<String, Integer>> implementation to be used to
     * compare the value. Compare {@code Map.Pair<String, Integer>}s in
     * lexicographic order.
     */
    private static class countComparator
            implements Comparator<Map.Pair<String, Integer>> {
        @Override
        public int compare(Map.Pair<String, Integer> one,
                Map.Pair<String, Integer> two) {
            return two.value().compareTo(one.value());
        }
    }

    /**
     * Makes {@code Queue} that has all words without the separators
     *
     * @param String
     *            the input file
     * @return Queue
     * @requires inputFile != null
     * @ensures
     */
    public static Queue<String> wordsInput(String inputFile) {

        // Initialize a queue to store only words
        Queue<String> words = new Queue1L<>();

        // Read the input file
        SimpleReader in = new SimpleReader1L(inputFile);

        String specialChar = " .,:;-_[]!?*\'\"1234567890";

        // Get each line of the input file until it reaches to the end of the line
        while (!in.atEOS()) {

            // Get a line as a string
            String line = in.nextLine();

            // Get a line as a character array
            char[] charLine = line.toCharArray();

            // Initialize the word to store each character to form a valid word
            String word = "";

            // Check each character in the line
            for (int i = 0; i < charLine.length; i++) {

                // Get a character from charLine
                char character = charLine[i];

                // If the result is -1, add characters to form a word then add it to the new Queue
                if (specialChar.indexOf(character) == -1) {
                    if (i == charLine.length - 1) {
                        word += character;
                        words.enqueue(word);
                    }
                    // If not, add characters to form a word
                    else {
                        word += character;
                    }

                    // If length of Queue that has words without a special character is more than 0, add it to the new Queue
                } else if (word.length() > 0) {
                    words.enqueue(word);
                    word = "";
                }

            }

        }

        in.close();

        // Return the new Queue without special characters
        return words;
    }

    /**
     * Makes {@code Map} that counts every word from Queue
     *
     * @param String
     *            the input file
     * @return Queue
     * @requires inputFile != null
     * @ensures
     */
    public static Map<String, Integer> wordsCount(Queue<String> words) {

        // Initialize the new Map
        Map<String, Integer> wordsMap = new Map1L<>();

        // Check if the length of words is more than 0
        while (words.length() > 0) {

            // Remove a word from Queue
            String word = words.dequeue();

            // Make every character in a word into a lower case
            word = word.toLowerCase();

            // If a word is not in the Map, add a word into the Map
            if (!wordsMap.hasKey(word)) {
                wordsMap.add(word, 1);
            }
            // If a word is already in the Map, just add the value by one
            else {
                int currentCount = wordsMap.value(word);
                wordsMap.replaceValue(word, currentCount + 1);
            }

        }

        // Return the new Map
        return wordsMap;
    }

    /**
     * Makes {@code Queue} that has 100 words in lexicographic order
     *
     * @param String
     *            the input file
     * @return Queue
     * @requires inputFile != null
     * @ensures
     */
    public static Queue<Integer> sortCount(Map<String, Integer> wordsMap) {

        // Initialize the comparator
        Comparator<Pair<String, Integer>> countComparator = new countComparator();

        // Initialize the new Map
        Map<String, Integer> sortMap = wordsMap.newInstance();

        // Initialize the new Queue
        Queue<Integer> maxAndMin = new Queue1L<>();

        // Initialize the new SortingMachine
        SortingMachine<Map.Pair<String, Integer>> sortMachine = new SortingMachine1L<Pair<String, Integer>>(
                countComparator);

        // Check every Pair in wordsMap
        for (Pair<String, Integer> pair : wordsMap) {

            // Add Pair into SortingMachine
            sortMachine.add(pair);
        }

        // Change the SortingMachine into Extraction Mode and sort in increasing order
        sortMachine.changeToExtractionMode();

        // Check every Pair until 100
        for (int i = 0; i < 100; i++) {

            // Remove the first Pair from SortingMachine
            Pair<String, Integer> topPair = sortMachine.removeFirst();

            // Stores a key
            String topWord = topPair.key();

            // Stores a value
            int topCount = topPair.value();

            // Add the minimum value into the Queue
            if (i == 0) {
                maxAndMin.enqueue(topCount);
            }
            // Add the maximum value into the Queue
            else if (i == 99) {
                maxAndMin.enqueue(topCount);
            }

            // Add the Pair into the new Map
            sortMap.add(topWord, topCount);
        }

        // Transfer the new Map into the old Map
        wordsMap.transferFrom(sortMap);

        // Return the Queue
        return maxAndMin;
    }

    /**
     * Makes {@code SortingMachine} that has all words in increasing order
     *
     * @param Map
     *            the input file
     * @return Queue
     * @requires inputFile != null
     * @ensures
     */
    public static SortingMachine<Map.Pair<String, Integer>> sortWord(
            Map<String, Integer> wordsMap) {

        // Initialize the comparator
        Comparator<Pair<String, Integer>> countComparator = new wordComparator();

        // Initialize the new SortingMachine
        SortingMachine<Map.Pair<String, Integer>> sortMachine = new SortingMachine1L<Pair<String, Integer>>(
                countComparator);

        // Check every Pair in wordsMap
        for (Pair<String, Integer> pair : wordsMap) {

            // Add the Pair into the SortingMachine
            sortMachine.add(pair);
        }

        // Change the SortingMachine into Extraction Mode and sort
        sortMachine.changeToExtractionMode();

        // Return the new SortingMachine
        return sortMachine;
    }

    /**
     * Generates the header of HTML
     *
     * @param out
     *            outputs the code
     * @param inputFile
     *            the {@code String} that gets the input file
     */
    public static void header(SimpleWriter out, String inputFile) {

        out.println("<html>");
        out.println("<head>");
        out.println("<title>Top 100 words in " + inputFile + "</title>");
        out.println("<link href=\"http://web.cse.ohio-state.edu/software/2231/"
                + "web-sw2/assignments/projects/tag-cloud-generator/data/"
                + "tagcloud.css\" rel=\"stylesheet\" type=\"text/css\">");
        out.println("</head>");

    }

    /**
     * Generates the body of HTML
     *
     * @param out
     *            outputs the code
     * @param inputFile
     *            the {@code String} that gets the input file
     * @param maxAndMin
     *            the {@code Queue} that gets the minimum and the maximum
     * @param sortedWords
     *            the {@code SortingMachine} that gets sorted 100 Pairs
     */
    public static void body(SimpleWriter out, String inputFile,
            Queue<Integer> maxAndMin,
            SortingMachine<Map.Pair<String, Integer>> sortedWords) {

        // Initialize the average
        int average = 0;

        // Remove the maximum from Queue
        int max = maxAndMin.dequeue();

        // Remove the minimum from Queue
        int min = maxAndMin.dequeue();

        // Subtract the maximum and the minimum to find the difference
        int difference = max - min;

        // Add every value into the average
        for (Map.Pair<String, Integer> word : sortedWords) {
            average += word.value();
        }

        // Divide the average by the length of the SortingMachine
        average /= 100;

        out.println("<body data-new-gr-c-s-check-loaded=\"14.984.0\" "
                + "data-gr-ext-installed>");
        out.println("<h2>Top 100 words in " + inputFile + "</h2>");
        out.println("<hr>");
        out.println("<div class=\"cdiv\">");
        out.println("<p class=\"cbox\">");

        // Calculate the right font size for each Pair
        for (Map.Pair<String, Integer> wordAndCount : sortedWords) {
            String word = wordAndCount.key();
            int count = wordAndCount.value();

            int fontSize = 38 * (count - min);
            fontSize /= difference;
            fontSize += 11;

//            double fontSize = (double) (count * 5) / average;
//            fontSize += 11;
//            if (fontSize < 11) {
//                fontSize = 11;
//            } else if (fontSize > 48) {
//                fontSize = 48;
//            }

            out.println("<span style=\"cursor:default\"" + "class=\"f"
                    + fontSize + "\"" + "title=\"count: " + count + "\">" + word
                    + "</span>");
        }

        out.println("</p>");
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");

    }

    /**
     * Generates the HTML
     *
     * @param outputFile
     *            the {@code String} that gets the output file
     * @param inputFile
     *            the {@code String} that gets the input file
     * @param maxAndMin
     *            the {@code Queue} that gets the minimum and the maximum
     * @param sortedWords
     *            the {@code SortingMachine} that gets sorted 100 Pairs
     */
    public static void htmlGenerator(String outputFile, String inputFile,
            Queue<Integer> maxAndMin,
            SortingMachine<Map.Pair<String, Integer>> sortedWords) {

        // Initialize
        SimpleWriter out = new SimpleWriter1L(outputFile);

        // Calls the method "header"
        header(out, inputFile);

        // Calls the method "body"
        body(out, inputFile, maxAndMin, sortedWords);

    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments; unused here
     */
    public static void main(String[] args) {
        // Initialize the output stream
        SimpleWriter out = new SimpleWriter1L();

        String inputFile = "data/importance.txt";

        // Generate the Queue from the input file
        Queue<String> words = wordsInput(inputFile);

        // Generate the Map from words
        Map<String, Integer> wordsMap = wordsCount(words);

        // Generate the Queue from wordsMap
        Queue<Integer> maxAndMin = sortCount(wordsMap);

        // Generate the SortingMachine from wordsMap
        SortingMachine<Map.Pair<String, Integer>> sortedWords = sortWord(
                wordsMap);

        // Generate the HTML
        htmlGenerator("test.html", "data/importance.txt", maxAndMin,
                sortedWords);

        /*
         * Close output stream
         */
        out.close();
    }

}
