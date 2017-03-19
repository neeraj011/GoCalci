package project.speech;

import com.fathzer.soft.javaluator.DoubleEvaluator;
import com.fathzer.soft.javaluator.Function;
import com.fathzer.soft.javaluator.Parameters;
import java.util.ArrayList;
import java.util.Iterator;
public class Calculator {
    // The function has one argument and its name is "sqrt"
    Parameters params;
    DoubleEvaluator evaluator;
    private double previousSum = 0;
    private double currentSum = 0;
    private String currentDisplay = "";
    //private String expressionUsedForParsing ="";
    private boolean isRadians = false;
    public Calculator() {
        addFunctions();
        //Adds the functions to the evaluator
        evaluator = new DoubleEvaluator(params) {
            @Override
            protected Double evaluate(Function function, Iterator arguments, Object evaluationContext) {

                return super.evaluate(function, arguments, evaluationContext);
            }
        };
    }
    private int getFactorial(int n)    {
        int result;
        if(n==0 || n==1)
            return 1;
        result = getFactorial(n-1) * n;
        return result;
    }
    public void addFunctions() {
        params = DoubleEvaluator.getDefaultParameters();

    }
    public String getResult(String currentDisplay, String expressionUsedForParsing) {
        //Tries to parse the information as it is entered, if the parser can't handle it, the word error is shown on screen
        try {
            System.out.println("Displayed Output " + expressionUsedForParsing);
            currentSum = evaluator.evaluate(fixExpression(expressionUsedForParsing));
            currentSum = convertToRadians(currentSum);
            currentDisplay = String.valueOf(currentSum);
            //previousSum = currentSum;
        } catch (Exception e) {
            currentDisplay = "Error";
        }
        return currentDisplay;
    }
    public double convertToRadians(double sum){
        double newSum = sum;
        if(isRadians == true)
            newSum = Math.toRadians(sum);
        return newSum;
    }
    //Used to show display to user
    public String getCurrentDisplay() {
        return currentDisplay;
    }
    //Handles fixing the expression before parsing. Adding parens, making sure parens can multiply with each other,
    public String fixExpression(String exp) {
        int openParens = 0;
        int closeParens = 0;
        char openP = '(';
        char closeP = ')';
        String expr = exp;
        for (int i = 0; i < exp.length(); i++) {
            if (exp.charAt(i) == openP)
                openParens++;
            else if (exp.charAt(i) == closeP)
                closeParens++;
        }
        while (openParens > 0) {
            expr += closeP;
            openParens--;
        }
        while (closeParens > 0) {
            expr = openP + expr;
            closeParens--;
        }
        expr = multiplicationForParens(expr);
        return expr;
    }
    //Used to fix multiplication between parentheses
    public String multiplicationForParens(String s) {
        String fixed = "";
        for (int position = 0; position < s.length(); position++) {
            fixed += s.charAt(position);
            if (position == s.length() - 1)
                continue;
            if (s.charAt(position) == ')' && s.charAt(position + 1) == '(')
                fixed += '*';
            if (s.charAt(position) == '(' && s.charAt(position + 1) == ')')
                fixed += '1';
        }
        return fixed;
    }
}