import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * JUnit test fixture for {@code ExpressionEvaluator}'s {@code valueOfExpr}
 * static method.
 *
 * @author Put your name here
 *
 */
public final class ExpressionEvaluatorTest {

    @Test
    public void testExample() {
        StringBuilder exp = new StringBuilder(
                "281/7/2-1-5*(15-(14-1))+((1))+20=30!");
        int value = ExpressionEvaluator.valueOfExpr(exp);
        assertEquals(30, value);
        assertEquals("=30!", exp.toString());
    }

    // TODO - add other (simpler) test cases to help with debugging
    //Digit-Seq
    @Test
    public void test1() {
        StringBuilder exp = new StringBuilder("0+24=24");
        int value = ExpressionEvaluator.valueOfExpr(exp);
        assertEquals(24, value);
        assertEquals("=24", exp.toString());
    }

    //Digit
    @Test
    public void test2() {
        StringBuilder exp = new StringBuilder("0+3=3");
        int value = ExpressionEvaluator.valueOfExpr(exp);
        assertEquals(3, value);
        assertEquals("=3", exp.toString());
    }

    //Term
    @Test
    public void test3() {
        StringBuilder exp = new StringBuilder("4*2=8");
        int value = ExpressionEvaluator.valueOfExpr(exp);
        assertEquals(8, value);
        assertEquals("=8", exp.toString());
    }

    //Expr
    @Test
    public void test4() {
        StringBuilder exp = new StringBuilder("3+4=7");
        int value = ExpressionEvaluator.valueOfExpr(exp);
        assertEquals(7, value);
        assertEquals("=7", exp.toString());
    }

    //Factor
    @Test
    public void test5() {
        StringBuilder exp = new StringBuilder("(3+4)*3=21");
        int value = ExpressionEvaluator.valueOfExpr(exp);
        assertEquals(21, value);
        assertEquals("=21", exp.toString());
    }
}
