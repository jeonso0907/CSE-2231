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
 * @author Sooyoung Jeon and Kevin Lim
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

        // Check for the name of the condition and if it is true,
        // then make it into a condition
        tokens.dequeue();
        String con = tokens.dequeue();
        Reporter.assertElseFatalError(Tokenizer.isCondition(con),
                "Not a right name of a condition after IF");
        Condition ifCon = parseCondition(con);

        // Check for THEN
        String thenState = tokens.dequeue();
        Reporter.assertElseFatalError(thenState.equals("THEN"),
                "THEN is not found");

        // Make a new Statement for IF
        Statement newIf = s.newInstance();
        newIf.parseBlock(tokens);

        // Check for ELSE or END
        Reporter.assertElseFatalError(
                tokens.front().equals("ELSE") || tokens.front().equals("END"),
                "ELSE nor END is not found");

        // If ELSE is found, make IFELSE Statement, if not, make IF Statement
        if (tokens.front().equals("ELSE")) {
            tokens.dequeue();
            Statement newElse = s.newInstance();
            newElse.parseBlock(tokens);
            s.assembleIfElse(ifCon, newIf, newElse);
            String end = tokens.dequeue();
            Reporter.assertElseFatalError(end.equals("END"),
                    "END is not found");
        } else {
            s.assembleIf(ifCon, newIf);
            String end = tokens.dequeue();
            Reporter.assertElseFatalError(end.equals("END"),
                    "END is not found");
        }

        // Check for IF at the end
        String endIf = tokens.dequeue();
        Reporter.assertElseFatalError(endIf.equals("IF"),
                "IF at the end is not found");
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

        // Check for WHILE and if the name of condition is right, make a condition
        String startWhile = tokens.dequeue();
        String whileCon = tokens.dequeue();
        Reporter.assertElseFatalError(Tokenizer.isCondition(whileCon),
                "Not a right name of a condition after WHILE");
        Condition con = parseCondition(whileCon);

        // Check for DO
        String doState = tokens.dequeue();
        Reporter.assertElseFatalError(doState.equals("DO"), "DO is not found");

        // Make a new Statement for WHILE
        Statement newWhile = s.newInstance();
        newWhile.parseBlock(tokens);
        s.assembleWhile(con, newWhile);

        // Check for END
        String end = tokens.dequeue();
        Reporter.assertElseFatalError(end.equals("END"), "END is not found");

        // Check for WHILE at the end
        String endWhile = tokens.dequeue();
        Reporter.assertElseFatalError(endWhile.equals(startWhile),
                "WHILE at the end is not found");
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
        // Assemble the call
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

        // Get the identifier
        String identifer = tokens.front();

        // Check if the identifier is IF, WHILE, or something else
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

        // Make a new Statement
        Statement s = this.newInstance();

        // If the token is not END_OF_INPUT, END, nor ELSE, then add the block
        for (int c = 0; !tokens.front().equals(Tokenizer.END_OF_INPUT)
                && !tokens.front().equals("END")
                && !tokens.front().equals("ELSE"); c++) {
            s.parse(tokens);
            this.addToBlock(c, s);
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
