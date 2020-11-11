import components.queue.Queue;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;
import components.statement.Statement;
import components.statement.Statement1;
import components.utilities.Reporter;
import components.utilities.Tokenizer;

/**
 * Layered implementation of secondary methods {@code parse} and
 * {@code parseBlock} for {@code Statement}.
 *
 * @author Put your name here
 *
 */
public final class Statement1Parse1 extends Statement1 {

    /*
     * Private members --------------------------------------------------------
     */
    /**
     * Converts {@code c} into the corresponding {@code Condition}.
     *
     * @param c
     *            the condition to convert
     * @return the {@code Condition} corresponding to {@code c}
     * @requires [c is a condition string]
     * @ensures parseCondition = [Condition corresponding to c]
     */
    private static Condition parseCondition(String c) {
        assert c != null : "Violation of: c is not null";
        assert Tokenizer
                .isCondition(c) : "Violation of: c is a condition string";
        return Condition.valueOf(c.replace('-', '_').toUpperCase());
    }

    /**
     * Parses an IF or IF_ELSE statement from {@code tokens} into {@code s}.
     *
     * @param tokens
     *            the input tokens
     * @param s
     *            the parsed statement
     * @replaces s
     * @updates tokens
     * @requires <pre>
     * [<"IF"> is a prefix of tokens]  and
     *  [<Tokenizer.END_OF_INPUT> is a suffix of tokens]
     * </pre>
     * @ensures <pre>
     * if [an if string is a proper prefix of #tokens] then
     *  s = [IF or IF_ELSE Statement corresponding to if string at start of #tokens]  and
     *  #tokens = [if string at start of #tokens] * tokens
     * else
     *  [reports an appropriate error message to the console and terminates client]
     * </pre>
     */
    private static void parseIf(Queue<String> tokens, Statement s) {
        assert tokens != null : "Violation of: tokens is not null";
        assert s != null : "Violation of: s is not null";
        assert tokens.length() > 0 && tokens.front().equals("IF") : ""
                + "Violation of: <\"IF\"> is proper prefix of tokens";

        // TODO - fill in body
        String tokenIf = tokens.dequeue();

        Reporter.assertElseFatalError(Tokenizer.isCondition(tokens.front()),
                "Error: Violation of condition after IF not a valid condition");
        Condition conditionIf = parseCondition(tokens.dequeue());

        Reporter.assertElseFatalError(tokens.front().equals("THEN"),
                "Error: Expected THEN, found: " + "\"" + tokens.front() + "\"");
        String thenToken = tokens.dequeue();

        Statement ifStatement = s.newInstance();
        ifStatement.parseBlock(tokens);

        Reporter.assertElseFatalError(
                tokens.front().equals("ELSE") || tokens.front().equals("END"),
                "Error: Expected ELSE or END, found " + tokens.front());

        if (tokens.front().equals("ELSE")) {

            String elseToken = tokens.dequeue();
            Statement elseStatement = s.newInstance();
            elseStatement.parseBlock(tokens);
            s.assembleIfElse(conditionIf, ifStatement, elseStatement);

            Reporter.assertElseFatalError(tokens.front().equals("END"),
                    "Error: Expected END, found: " + "\"" + tokens.front()
                            + "\"");
            String endToken = tokens.dequeue();

        } else {
            s.assembleIf(conditionIf, ifStatement);
            Reporter.assertElseFatalError(tokens.front().equals("END"),
                    "Error: Expected END, found: " + "\"" + tokens.front()
                            + "\"");
            String end = tokens.dequeue();
        }

        String endIfToken = tokens.dequeue();
        Reporter.assertElseFatalError(endIfToken.equals("IF"),
                "Error: Expected IF, found " + "\"" + endIfToken + "\"");
    }

    /**
     * Parses a WHILE statement from {@code tokens} into {@code s}.
     *
     * @param tokens
     *            the input tokens
     * @param s
     *            the parsed statement
     * @replaces s
     * @updates tokens
     * @requires <pre>
     * [<"WHILE"> is a prefix of tokens]  and
     *  [<Tokenizer.END_OF_INPUT> is a suffix of tokens]
     * </pre>
     * @ensures <pre>
     * if [a while string is a proper prefix of #tokens] then
     *  s = [WHILE Statement corresponding to while string at start of #tokens]  and
     *  #tokens = [while string at start of #tokens] * tokens
     * else
     *  [reports an appropriate error message to the console and terminates client]
     * </pre>
     */
    private static void parseWhile(Queue<String> tokens, Statement s) {
        assert tokens != null : "Violation of: tokens is not null";
        assert s != null : "Violation of: s is not null";
        assert tokens.length() > 0 && tokens.front().equals("WHILE") : ""
                + "Violation of: <\"WHILE\"> is proper prefix of tokens";

        // TODO - fill in body
        String whileToken = tokens.dequeue();

        Reporter.assertElseFatalError(Tokenizer.isCondition(tokens.front()),
                "Error: Violation of condition after WHILE is not a vlid condition");

        Condition whileCondition = parseCondition(tokens.dequeue());

        Reporter.assertElseFatalError(tokens.front().equals("DO"),
                "Error: Expected DO, found: " + tokens.front());

        String doTokenString = tokens.dequeue();

        Statement whileStatement = s.newInstance();
        whileStatement.parseBlock(tokens);
        s.assembleWhile(whileCondition, whileStatement);

        Reporter.assertElseFatalError(tokens.front().equals("END"),
                "Error: Expected END, found: " + "\"" + tokens.front() + "\"");

        String endWhile = tokens.dequeue();

        Reporter.assertElseFatalError(whileToken.equals("WHILE"),
                "Error: Does not contain While after END");

        whileToken = tokens.dequeue();
    }

    /**
     * Parses a CALL statement from {@code tokens} into {@code s}.
     *
     * @param tokens
     *            the input tokens
     * @param s
     *            the parsed statement
     * @replaces s
     * @updates tokens
     * @requires [identifier string is a proper prefix of tokens]
     * @ensures <pre>
     * s =
     *   [CALL Statement corresponding to identifier string at start of #tokens]  and
     *  #tokens = [identifier string at start of #tokens] * tokens
     * </pre>
     */
    private static void parseCall(Queue<String> tokens, Statement s) {
        assert tokens != null : "Violation of: tokens is not null";
        assert s != null : "Violation of: s is not null";
        assert tokens.length() > 0
                && Tokenizer.isIdentifier(tokens.front()) : ""
                        + "Violation of: identifier string is proper prefix of tokens";

        // TODO - fill in body
        String call = tokens.dequeue();
        s.assembleCall(call);

    }

    /*
     * Constructors -----------------------------------------------------------
     */

    /**
     * No-argument constructor.
     */
    public Statement1Parse1() {
        super();
    }

    /*
     * Public methods ---------------------------------------------------------
     */

    @Override
    public void parse(Queue<String> tokens) {
        assert tokens != null : "Violation of: tokens is not null";
        assert tokens.length() > 0 : ""
                + "Violation of: Tokenizer.END_OF_INPUT is a suffix of tokens";

        // TODO - fill in body
        String identifer = tokens.front();

        if (identifer.equals("IF")) {
            parseIf(tokens, this);
        } else if (identifer.equals("WHILE")) {
            parseWhile(tokens, this);
        } else {
            Reporter.assertElseFatalError(Tokenizer.isIdentifier(identifer),
                    "");
            parseCall(tokens, this);
        }

    }

    @Override
    public void parseBlock(Queue<String> tokens) {
        assert tokens != null : "Violation of: tokens is not null";
        assert tokens.length() > 0 : ""
                + "Violation of: Tokenizer.END_OF_INPUT is a suffix of tokens";

        // TODO - fill in body
        Statement s = this.newInstance();

        for (int i = 0; !tokens.front().equals("END")
                && !tokens.front().equals("ELSE")
                && !tokens.front().equals(Tokenizer.END_OF_INPUT); i++) {

            s.parse(tokens);
            this.addToBlock(i, s);

        }

    }

    /*
     * Main test method -------------------------------------------------------
     */

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) {
        SimpleReader in = new SimpleReader1L();
        SimpleWriter out = new SimpleWriter1L();
        /*
         * Get input file name
         */
        out.print("Enter valid BL statement(s) file name: ");
        String fileName = in.nextLine();
        /*
         * Parse input file
         */
        out.println("*** Parsing input file ***");
        Statement s = new Statement1Parse1();
        SimpleReader file = new SimpleReader1L(fileName);
        Queue<String> tokens = Tokenizer.tokens(file);
        file.close();
        s.parse(tokens); // replace with parseBlock to test other method
        /*
         * Pretty print the statement(s)
         */
        out.println("*** Pretty print of parsed statement(s) ***");
        s.prettyPrint(out, 0);

        in.close();
        out.close();
    }

}
