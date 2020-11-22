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

    public static Queue<String> sortCount(Map<String, Integer> wordsMap) {

        Comparator countComparator = new countComparator();

        Map<String, Integer> sortMap = wordsMap.newInstance();

        Queue<String> sortCountWords = new Queue1L<>();

        SortingMachine<Map.Pair<String, Integer>> sortMachine = new SortingMachine1L(
                countComparator);

        for (Pair<String, Integer> pair : wordsMap) {
            sortMachine.add(pair);
        }
        sortMachine.changeToExtractionMode();

        for (int i = 0; i < 100; i++) {
            Pair<String, Integer> topPair = sortMachine.removeFirst();
            String topWord = topPair.key();
            int topCount = topPair.value();
            sortCountWords.enqueue(topWord);
            sortMap.add(topWord, topCount);
        }
        wordsMap.transferFrom(sortMap);

        return sortCountWords;
    }

    public static Queue<String> sortWord(Map<String, Integer> wordsMap) {

        Comparator countComparator = new wordComparator();

        Map<String, Integer> sortMap = wordsMap.newInstance();

        Queue<String> sortedWords = new Queue1L<>();

        SortingMachine<Map.Pair<String, Integer>> sortMachine = new SortingMachine1L(
                countComparator);

        for (Pair<String, Integer> pair : wordsMap) {
            sortMachine.add(pair);
        }
        sortMachine.changeToExtractionMode();

        for (Pair<String, Integer> pair : sortMachine) {

            String word = pair.key();
            int count = pair.value();
            sortedWords.enqueue(word);
            sortMap.add(word, count);
        }

        wordsMap.transferFrom(sortMap);

        return sortedWords;
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

        sortCount(wordsMap);

        Queue<String> sortedWords = sortWord(wordsMap);

        for (String s : sortedWords) {
            out.print(s + " : ");
            out.print(wordsMap.value(s) + ", ");
        }

        out.close();
    }

}
