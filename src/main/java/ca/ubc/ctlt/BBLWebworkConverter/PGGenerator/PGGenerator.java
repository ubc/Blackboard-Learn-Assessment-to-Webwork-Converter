package ca.ubc.ctlt.BBLWebworkConverter.PGGenerator;

import ca.ubc.ctlt.BBLWebworkConverter.Assessment.Question;
import ca.ubc.ctlt.BBLWebworkConverter.Assessment.Variable;
import ca.ubc.ctlt.BBLWebworkConverter.HtmlTexConverter;

import java.util.*;

/**
 * Created by compass on 13-10-08.
 */
public class PGGenerator {
    private Properties tags;
    private List<String> macros;
    private String pgsetup = "";
    private String solution = "";

    private int showPartialCorrectAnswers = 1;
    private String pgContext = "Numeric";
    private double tolerance = 0.01;

    private Question question;
    private HtmlTexConverter converter = null;

    private StringBuffer pgText;
    private static final String LF = System.getProperty("line.separator");

    public PGGenerator(Question q) {
        tags = new Properties();
        macros = new ArrayList<String>();
        question = q;
    }

    public PGGenerator(Question q, HtmlTexConverter converter) {
        tags = new Properties();
        macros = new ArrayList<String>();
        question = q;
        this.converter = converter;
    }

    public String generate() {
        pgText = new StringBuffer();
        pgText.append("DOCUMENT();" + LF);

        // tag and description section
        pgText.append(printTags());
        pgText.append(LF);

        // macros section
        pgText.append(printMacors());
        pgText.append(LF);

        // set up
        pgText.append("$showPartialCorrectAnswers = " + showPartialCorrectAnswers + ";" + LF);
        pgText.append("Context(\"" + pgContext + "\");" + LF);
        pgText.append("Context()->flags->set(tolerance => " + tolerance + ");" + LF);
        pgText.append(printVariables());
        pgText.append(LF);

        if (!pgsetup.isEmpty()) {
            pgText.append(pgsetup + LF + LF);
        }

        if ("Multiple".equals(question.getType())) {
            pgText.append("$mc = RadioButtons()");
        }

        // problem text and answers
        pgText.append("TEXT(beginproblem());" + LF);
        pgText.append("#####################################" + LF);
        pgText.append("#TEXT(PGML::Format2(<<'END_PGML'));" + LF);
        pgText.append("BEGIN_PGML" + LF);

        pgText.append(replaceVariablesForText() + LF);
        pgText.append(LF);

        // print calculated question response field
        if ("Calculated".equals(question.getType())) {
            pgText.append("[_____________________________]");
            pgText.append("{\"" + question.getFormulaAscii() + "\"}" + LF);
        }

        pgText.append("END_PGML" + LF);
        pgText.append("#####################################" + LF);

        // if the question is multiple choice, we have to use old style, PGML don't support it.
        if ("Multiple".equals(question.getType())) {
            pgText.append("BEGIN_TEXT").append(LF);
            pgText.append("\\{ $mc->buttons() \\}").append(LF);
            pgText.append("END_TEXT").append(LF);
            pgText.append("ANS( $mc->cmp() );").append(LF);
        }

        // solution
        if (!solution.isEmpty()) {
            pgText.append(printSolution());
        }

        // end of the PG problem
        pgText.append("ENDDOCUMENT();" + LF);

        return pgText.toString();
    }

    private String replaceVariablesForText() {
        String ret = converter.convert(question.getText());
        for (Map.Entry<String, Variable> entry : question.getFormulaVars().entrySet()) {
            Variable var = entry.getValue();
            ret = ret.replace("[" + var.getName() + "]", "[$" + var.getName() + "]");
        }

        return ret;
    }

    private String printTags() {
        StringBuffer buffer = new StringBuffer();

        for (String name : tags.stringPropertyNames() ) {
            buffer.append(String.format("#%s($s);" + LF, name, tags.getProperty(name)));
        }

        return buffer.toString();
    }

    public String printMacors() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("loadMacros(").append(LF);
        for (String macro : macros) {
            buffer.append("\"").append(macro).append("\",").append(LF);
        }
        buffer.append(");" + LF);

        return buffer.toString();
    }

    public String printSolution() {
        StringBuffer buffer = new StringBuffer();

        buffer.append("BEGIN_PGML_SOLUTION" + LF);
        buffer.append(solution+LF);
        buffer.append("END_PGML_SOLUTION" + LF);

        return buffer.toString();
    }

    public String printVariables() {
        StringBuffer buffer = new StringBuffer();

        for (Map.Entry<String, Variable> entry : question.getFormulaVars().entrySet()) {
            Variable var = entry.getValue();
            buffer.append(String.format("$%s = random(%f, %f, %f);" + LF, var.getName(), var.getMin(), var.getMax(), Math.pow(0.1, var.getDecimalPlaces())));
        }

        return buffer.toString();
    }

    public Properties getTags() {
        return tags;
    }

    public void setTags(Properties tags) {
        this.tags = tags;
    }

    public List<String> getMacros() {
        return macros;
    }

    public void setMacros(List<String> macros) {
        this.macros = macros;
    }

    public void addMacor(String macro) {
        this.macros.add(macro);
    }

    public String getPgsetup() {
        return pgsetup;
    }

    public void setPgsetup(String pgsetup) {
        this.pgsetup = pgsetup;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public int getShowPartialCorrectAnswers() {
        return showPartialCorrectAnswers;
    }

    public void setShowPartialCorrectAnswers(int showPartialCorrectAnswers) {
        this.showPartialCorrectAnswers = showPartialCorrectAnswers;
    }

    public String getPgContext() {
        return pgContext;
    }

    public void setPgContext(String pgContext) {
        this.pgContext = pgContext;
    }

    public double getTolerance() {
        return tolerance;
    }

    public void setTolerance(double tolerance) {
        this.tolerance = tolerance;
    }
}
