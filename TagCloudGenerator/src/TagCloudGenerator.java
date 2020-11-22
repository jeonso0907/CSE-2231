import java.util.ArrayList;
import java.util.Comparator;

import components.map.Map;
import components.map.Map.Pair;
import components.map.Map1L;
import components.queue.Queue;
import components.queue.Queue1L;
import components.set.Set;
import components.set.Set1L;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

/**
 * Generate a HTML page, which organize the words and their counts lexically.
 * Prompt the user to type the file to input and file name to output. Read each
 * Sentence from the input file and separate it with each word. Consider the
 * same words with different case as a same word, and list the words and their
 * counts in a table. Generate the final HTML and save it with a name given by
 * the user.
 *
 * @author Sooyoung Jeon and Kevin Lim
 */
public final class TagCloudGenerator {

    /**
     * Default constructor--private to prevent instantiation.
     */
    private TagCloudGenerator() {
        // no code needed here
    }

    /**
     *
     * @author Sooyoung Jeon and Kevin Lim
     *
     */
    private static class OrderValue
            implements Comparator<Map.Pair<String, Integer>> {

        @Override
        public int compare(Pair<String, Integer> o1, Pair<String, Integer> o2) {
            // Compare the words insensitively
            return (o1.key().compareToIgnoreCase(o2.key()));
        }

    }

    private static class OrderKey
            implements Comparator<Map.Pair<String, Integer>> {

        @Override
        public int compare(Pair<String, Integer> o1, Pair<String, Integer> o2) {
            // Compare the words insensitively
            return (o1.value().compareTo(o2.value()));
        }

    }

    /**
     * Generate the queue with words from each line from the input file given by
     * the user.
     *
     * @param inputFile
     *            input file given by the user to generate the words of queue
     * @return the words of queue from the input file
     */
    public static Queue<String> wordsInput(String inputFile) {
        Queue<String> wordsQueue = new Queue1L<>();
        SimpleReader in = new SimpleReader1L(inputFile);

        // Read each line from the input file
        while (!in.atEOS()) {
            String nextLine = in.nextLine();
            // Split the sentence with words by space, comma, period, and dash
            // Save these words in an array
            char[] lineCharArray = nextLine.toCharArray();
            String specialChar = " .,:;-_[]!?*\'\"1234567890";
            String s = "";
            int length = lineCharArray.length;
            for (int i = 0; i < length; i++) {
                if (specialChar.indexOf(lineCharArray[i]) == -1) {
                    s += lineCharArray[i];
                } else if (s.length() > 0 && length - 1 > i) {
                    wordsQueue.enqueue(s);
                    s = "";
                } else if (s.length() > 0 && i == length) {
                    wordsQueue.enqueue(s);
                }
            }
            wordsQueue.enqueue(s);
        }

        in.close();

        //Return the queue with the words from the input file
        return wordsQueue;
    }

    /**
     * Count the same words in the sorted queue and record them in the map.
     *
     * @param wordsQueue
     *            sorted queue of words
     * @return the map of the words with their counting
     */
    public static Map<String, Integer> wordCounter(Queue<String> wordsQueue) {
        Map<String, Integer> wordsMap = new Map1L<>();
        ArrayList<String> arrayList = new ArrayList<String>();

        // Loop until all the words in queue are checked
        while (wordsQueue.length() > 0) {
            String word = wordsQueue.dequeue();

            if (!word.equals("")) {

                // Convert the word with lower case to count the same word insensitively
                word = word.toLowerCase();

                // Add words in the list which do not overlap
                if (!arrayList.contains(word)) {
                    arrayList.add(word);
                }

                // Update the map by word and its count
                if (!wordsMap.hasKey(word)) {
                    wordsMap.add(word, 1);
                } else {
                    // If the word is already in the map
                    // add another count to the previous one
                    int currentCount = wordsMap.value(word);
                    wordsMap.replaceValue(word, currentCount + 1);
                }
            }

        }
        // Enqueue the words from a list
        for (String s : arrayList) {
            wordsQueue.enqueue(s);
        }

        // Return the updated map
        return wordsMap;
    }

    public static Map<String, Integer> topWords(Map<String, Integer> words) {

        Set<String> topWords = new Set1L<>();

        while (topWords.size() <= 50) { // fix the while loop
            Pair<String, Integer> p = words.removeAny();
            int count = p.value();
            if (topWords.size() < 50) {
                topWords.add(p.key());
            } else {
                for (String s : topWords) {
                    if (words.value(s) > count) {
                        words.remove(s);

                    }
                }
            }
        }

        return null;
    }

    /**
     * Header of the HTML. Set the structure of the head part of the HTML based
     * on the input file given by the user.
     *
     * @param fileName
     *            input file name given by the user
     * @param out
     *            SimpleWriter to output the structure of the HTML
     */
    public static void header(String fileName, SimpleWriter out) {
        // Set the title of the HTML by the input file name given by the user
        String title = "Words Counted in " + fileName;

        //Head of HTML
        out.println("<html>");
        out.println("<head>");

        //Title of the HTML
        out.println("<title>" + title + "</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h2>" + title + "</h2>");
        out.println("<hr>");
    }

    /**
     * Body of the HTML. Based on the sorted queue words, output the word with
     * its count from the map.
     *
     * @param wordsMap
     *            map of the words and each count
     * @param wordsQueue
     *            sorted queue of words
     * @param out
     *            SimpleWriter to output the structure of the HTML
     */
    public static void body(Map<String, Integer> wordsMap,
            Queue<String> wordsQueue, SimpleWriter out) {

        // Set a temporary queue to restore the original queue
        Queue<String> tempQueue = wordsQueue.newInstance();

        // Body of the HTML
        out.println("<table border = \"1\">");
        out.println("<tbody>");

        // Title of the table
        out.println("<tr>");
        out.println("<th>Words</th>");
        out.println("<th>Counts</th>");
        out.println("</tr>");

        // Print out all the words based on the lexically sorted queue words
        // Load the count data from the map to print the word and its count data
        for (int i = 0; i < wordsMap.size(); i++) {
            String word = wordsQueue.dequeue();
            int count = wordsMap.value(word);
            // Save the word in the temporary queue
            tempQueue.enqueue(word);

            out.println("<tr>");

            // Print out the word and its count in the table
            out.println("<th>" + word + "</th>");
            out.println("<th>" + count + " </th>");

            out.println("</tr>");
        }
        // Restore the original queue
        wordsQueue.transferFrom(tempQueue);

        // End the HTML body
        out.println("</tbody>");
        out.println("</table>");
        out.println("</body>");
        out.println("</html>");

    }

    /**
     * Gather all the information of the HTML and add them as a one HTML file.
     * Out put the HTML file by the named given by the user.
     *
     * @param fileName
     *            input file name given by the user
     * @param wordsQueue
     *            sorted queue of words
     * @param wordsMap
     *            map of the words and each count
     * @param outFile
     *            output file name given by the user
     */
    public static void htmlGenerator(String fileName, Queue<String> wordsQueue,
            Map<String, Integer> wordsMap, String outFile) {

        // Generate the out put file with the name given by the user
        SimpleWriter out = new SimpleWriter1L(outFile);

        //Generate the head and body part of the HTML
        header(fileName, out);
        body(wordsMap, wordsQueue, out);
    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments; unused here
     */
    public static void main(String[] args) {
        SimpleReader in = new SimpleReader1L();
        SimpleWriter out = new SimpleWriter1L();
        Comparator<String> cs = new StringLT();

        // Prompt the user to type the input file
        out.print("Enter the name of the input file: ");
        String fileName = in.nextLine();

        // Prompt the user to type the name of the output file
        out.print("Enter the name of the file to print out: ");
        String outFile = in.nextLine();

        // Generate the queue words from the input file
        Queue<String> wordsQueue = wordsInput(fileName);

        // Sort the words in queue lexically and insensitively
        wordsQueue.sort(cs);

        // Generate the map with the word and its count
        // Count the same words with different case as a same word
        Map<String, Integer> wordsMap = wordCounter(wordsQueue);

        // Get the top 100 words from the map

        // Generate the final HTML file with a name given by the user
        htmlGenerator(fileName, wordsQueue, wordsMap, outFile);

        in.close();
        out.close();
    }

}
