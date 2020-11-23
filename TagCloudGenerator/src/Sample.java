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

    private static class wordComparator
            implements Comparator<Map.Pair<String, Integer>> {
        @Override
        public int compare(Map.Pair<String, Integer> one,
                Map.Pair<String, Integer> two) {

            return one.key().toLowerCase().compareTo(two.key().toLowerCase());
        }
    }

    private static class countComparator
            implements Comparator<Map.Pair<String, Integer>> {
        @Override
        public int compare(Map.Pair<String, Integer> one,
                Map.Pair<String, Integer> two) {
            return two.value().compareTo(one.value());
        }
    }

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

                char character = charLine[i];

                if (specialChar.indexOf(character) == -1) {
                    if (i == charLine.length - 1) {
                        word += character;
                        words.enqueue(word);
                    } else {
                        word += character;
                    }

                } else if (word.length() > 0) {
                    words.enqueue(word);
                    word = "";
                }

            }

        }

        in.close();

        return words;
    }

    public static Map<String, Integer> wordsCount(Queue<String> words) {

        Map<String, Integer> wordsMap = new Map1L<>();

        while (words.length() > 0) {

            String word = words.dequeue();

            word = word.toLowerCase();

            if (!wordsMap.hasKey(word)) {
                wordsMap.add(word, 1);
            } else {
                int currentCount = wordsMap.value(word);
                wordsMap.replaceValue(word, currentCount + 1);
            }

        }

        return wordsMap;
    }

    public static Queue<Integer> sortCount(Map<String, Integer> wordsMap) {

        Comparator<Pair<String, Integer>> countComparator = new countComparator();

        Map<String, Integer> sortMap = wordsMap.newInstance();

        Queue<Integer> maxAndMin = new Queue1L<>();

        SortingMachine<Map.Pair<String, Integer>> sortMachine = new SortingMachine1L<Pair<String, Integer>>(
                countComparator);

        for (Pair<String, Integer> pair : wordsMap) {
            sortMachine.add(pair);
        }
        sortMachine.changeToExtractionMode();

        for (int i = 0; i < 100; i++) {

            Pair<String, Integer> topPair = sortMachine.removeFirst();
            String topWord = topPair.key();
            int topCount = topPair.value();

            if (i == 0) {
                maxAndMin.enqueue(topCount);
            } else if (i == 99) {
                maxAndMin.enqueue(topCount);
            }

            sortMap.add(topWord, topCount);
        }
        wordsMap.transferFrom(sortMap);

        return maxAndMin;
    }

    public static SortingMachine<Map.Pair<String, Integer>> sortWord(
            Map<String, Integer> wordsMap) {

        Comparator<Pair<String, Integer>> countComparator = new wordComparator();

        SortingMachine<Map.Pair<String, Integer>> sortMachine = new SortingMachine1L<Pair<String, Integer>>(
                countComparator);

        for (Pair<String, Integer> pair : wordsMap) {
            sortMachine.add(pair);
        }
        sortMachine.changeToExtractionMode();

        return sortMachine;
    }

    public static void header(SimpleWriter out, String inputFile) {

        out.println("<html>");
        out.println("<head>");
        out.println("<title>Top 100 words in " + inputFile + "</title>");
        out.println("<link href=\"http://web.cse.ohio-state.edu/software/2231/"
                + "web-sw2/assignments/projects/tag-cloud-generator/data/"
                + "tagcloud.css\" rel=\"stylesheet\" type=\"text/css\">");
        out.println("</head>");

    }

    public static void body(SimpleWriter out, String inputFile,
            Queue<Integer> maxAndMin,
            SortingMachine<Map.Pair<String, Integer>> sortedWords) {

        int average = 0;
        int max = maxAndMin.dequeue();
        int min = maxAndMin.dequeue();
        int difference = max - min;
        for (Map.Pair<String, Integer> word : sortedWords) {
            average += word.value();
        }
        average /= 100;

        out.println("<body data-new-gr-c-s-check-loaded=\"14.984.0\" "
                + "data-gr-ext-installed>");
        out.println("<h2>Top 100 words in " + inputFile + "</h2>");
        out.println("<hr>");
        out.println("<div class=\"cdiv\">");
        out.println("<p class=\"cbox\">");

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

    public static void htmlGenerator(String outputFile, String inputFile,
            Queue<Integer> maxAndMin,
            SortingMachine<Map.Pair<String, Integer>> sortedWords) {

        SimpleWriter out = new SimpleWriter1L(outputFile);

        header(out, inputFile);
        body(out, inputFile, maxAndMin, sortedWords);

    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments; unused here
     */
    public static void main(String[] args) {
        SimpleWriter out = new SimpleWriter1L();

        String inputFile = "data/importance.txt";

        Queue<String> words = wordsInput(inputFile);

        Map<String, Integer> wordsMap = wordsCount(words);

        Queue<Integer> maxAndMin = sortCount(wordsMap);

        SortingMachine<Map.Pair<String, Integer>> sortedWords = sortWord(
                wordsMap);

        htmlGenerator("test.html", "data/importance.txt", maxAndMin,
                sortedWords);

        out.close();
    }

}
