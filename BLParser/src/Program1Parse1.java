import components.map.Map;
import components.map.Map.Pair;
import components.program.Program;
import components.program.Program1;
import components.queue.Queue;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;
import components.statement.Statement;
import components.utilities.Reporter;
import components.utilities.Tokenizer;

/**
 * Layered implementation of secondary method {@code parse} for {@code Program}.
 *
 * @author Sooyoung Jeon and Kevin Lim
 *
 */
public final class Program1Parse1 extends Program1 {

    /*
     * Private members --------------------------------------------------------
     */

    /**
     * Parses a single BL instruction from {@code tokens} returning the
     * instruction name as the value of the function and the body of the
     * instruction in {@code body}.
     *
     * @param tokens
     *            the input tokens
     * @param body
     *            the instruction body
     * @return the instruction name
     * @replaces body
     * @updates tokens
     * @requires <pre>
     * [<"INSTRUCTION"> is a prefix of tokens]  and
     *  [<Tokenizer.END_OF_INPUT> is a suffix of tokens]
     * </pre>
     * @ensures <pre>
     * if [an instruction string is a proper prefix of #tokens]  and
     *    [the beginning name of this instruction equals its ending name]  and
     *    [the name of this instruction does not equal the name of a primitive
     *     instruction in the BL language] then
     *  parseInstruction = [name of instruction at start of #tokens]  and
     *  body = [Statement corresponding to statement string of body of
     *          instruction at start of #tokens]  and
     *  #tokens = [instruction string at start of #tokens] * tokens
     * else
     *  [report an appropriate error message to the console and terminate client]
     * </pre>
     */
    private static String parseInstruction(Queue<String> tokens,
            Statement body) {
        assert tokens != null : "Violation of: tokens is not null";
        assert body != null : "Violation of: body is not null";
        assert tokens.length() > 0 && tokens.front().equals("INSTRUCTION") : ""
                + "Violation of: <\"INSTRUCTION\"> is proper prefix of tokens";

        // Check for INSTRUCTION
        String instr = tokens.dequeue();
        Reporter.assertElseFatalError(instr.equals("INSTRUCTION"),
                "INSTRUCTION is not found");

        // Check if the name of instruction is unique
        String firstName = tokens.dequeue();
        Reporter.assertElseFatalError(Tokenizer.isIdentifier(firstName),
                "The name is not unique");

        // Check for IS
        String is = tokens.dequeue();
        Reporter.assertElseFatalError(is.equals("IS"), "IS is not found");

        body.parseBlock(tokens);

        // Check for END
        String end = tokens.dequeue();
        Reporter.assertElseFatalError(end.equals("END"), "END is not found");
        // Check if the names match
        String endName = tokens.dequeue();
        Reporter.assertElseFatalError(firstName.equals(endName),
                "The names do not match");

        // Return the name
        return endName;
    }

    /*
     * Constructors -----------------------------------------------------------
     */

    /**
     * No-argument constructor.
     */
    public Program1Parse1() {
        super();
    }

    /*
     * Public methods ---------------------------------------------------------
     */

    @Override
    public void parse(SimpleReader in) {
        assert in != null : "Violation of: in is not null";
        assert in.isOpen() : "Violation of: in.is_open";
        Queue<String> tokens = Tokenizer.tokens(in);
        this.parse(tokens);
    }

    @Override
    public void parse(Queue<String> tokens) {
        assert tokens != null : "Violation of: tokens is not null";
        assert tokens.length() > 0 : ""
                + "Violation of: Tokenizer.END_OF_INPUT is a suffix of tokens";

        // Check for "PROGRAM"
        String program = tokens.dequeue();
        Reporter.assertElseFatalError(program.equals("PROGRAM"),
                "PROGRAM is not found");

        // Check if the name is unique
        String programName = tokens.dequeue();
        Reporter.assertElseFatalError(Tokenizer.isIdentifier(programName),
                "The name is not unique");
        this.setName(programName);

        // Check for "IS"
        String is = tokens.dequeue();
        Reporter.assertElseFatalError(is.equals("IS"), "IS is not found");

        //Map contains all Instructions, could be empty.
        Map<String, Statement> ctxt = this.newContext();

        //Either Instruction or Begin
        String instr = tokens.front();

        // Check if the instruction is already in or not
        while (instr.equals("INSTRUCTION")) {
            Statement body = this.newBody();
            String startName = parseInstruction(tokens, body);
            for (Pair<String, Statement> x : ctxt) {
                Reporter.assertElseFatalError(!x.key().equals(startName),
                        "Instruction is already determined");
            }
            ctxt.add(startName, body);
            instr = tokens.front();
        }

        // Swaps the context
        this.swapContext(ctxt);

        // Check for "BEGIN"
        Reporter.assertElseFatalError(instr.equals("BEGIN"),
                "BEGIN is not found");

        // Swaps the body
        tokens.dequeue();
        Statement body = this.newBody();
        body.parseBlock(tokens);
        this.swapBody(body);

        // Check for "END"
        String end = tokens.dequeue();
        Reporter.assertElseFatalError(end.equals("END"), "END is not found");

        // Check if the names are equal
        String endProgramName = tokens.dequeue();
        Reporter.assertElseFatalError(endProgramName.equals(programName),
                "The names do not match");

        //Checks for end of program.
        String endOfInput = tokens.dequeue();
        Reporter.assertElseFatalError(endOfInput.equals("### END OF INPUT ###"),
                "END OF INPUT is not found");
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
        out.print("Enter valid BL program file name: ");
        String fileName = in.nextLine();
        /*
         * Parse input file
         */
        out.println("*** Parsing input file ***");
        Program p = new Program1Parse1();
        SimpleReader file = new SimpleReader1L(fileName);
        Queue<String> tokens = Tokenizer.tokens(file);
        file.close();
        p.parse(tokens);
        /*
         * Pretty print the program
         */
        out.println("*** Pretty print of parsed program ***");
        p.prettyPrint(out);

        in.close();
        out.close();
    }

}
