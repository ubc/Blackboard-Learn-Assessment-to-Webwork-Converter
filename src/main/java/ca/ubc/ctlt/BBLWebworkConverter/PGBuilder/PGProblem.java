package ca.ubc.ctlt.BBLWebworkConverter.PGBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by compass on 13-10-18.
 */
public class PGProblem {
    private Properties tags;
    private List<String> macros;
    // different section text
    private String setup = null;
    private String question = null;
    private String answer = null;
    private String answerNoPGML = null;
    private String solution = null;

    private int showPartialCorrectAnswers = 1;
    private String pgContext = "Numeric";
    private double tolerance = 0.01;

    public static final String LF = System.getProperty("line.separator");

    public PGProblem() {
        tags = new Properties();
        macros = new ArrayList<String>();
    }

    public String toString() {
        StringBuffer pgText = new StringBuffer();
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
        pgText.append(LF);

        if (null != setup) {
            pgText.append(setup + LF + LF);
        }

        // problem text and answers
        pgText.append("TEXT(beginproblem());" + LF);
        pgText.append("#####################################" + LF);
        pgText.append("#TEXT(PGML::Format2(<<'END_PGML'));" + LF);
        pgText.append("BEGIN_PGML" + LF);

        pgText.append(question + LF);
        pgText.append(LF);

        if (null != answer) {
            pgText.append(answer + LF);
        }

        pgText.append("END_PGML" + LF);
        pgText.append("#####################################" + LF);

        if (null != answerNoPGML) {
            pgText.append(answerNoPGML + LF);
        }

        // solution
        if (null != solution) {
            pgText.append(printSolution());
        }

        // end of the PG problem
        pgText.append("ENDDOCUMENT();" + LF);

        return pgText.toString();
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

    public Properties getTags() {
        return tags;
    }

    public void setTags(Properties tags) {
        this.tags = tags;
    }

    public void addTag(String key, String value) {
        tags.setProperty(key, value);
    }

    public List<String> getMacros() {
        return macros;
    }

    public void setMacros(List<String> macros) {
        this.macros = macros;
    }

    public void addMacro(String macro) {
        this.macros.add(macro);
    }

    public String getSetup() {
        return setup;
    }

    public void setSetup(String setup) {
        this.setup = setup;
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

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setAnswerNoPGML(String answerNoPGML) {
        this.answerNoPGML = answerNoPGML;
    }
}
