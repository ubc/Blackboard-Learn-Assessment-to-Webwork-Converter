package ca.ubc.ctlt.BBLWebworkConverter.PGBuilder;

import ca.ubc.ctlt.BBLWebworkConverter.Assessment.CalculatedQuestion;
import ca.ubc.ctlt.BBLWebworkConverter.Assessment.Variable;
import ca.ubc.ctlt.BBLWebworkConverter.HtmlTexConverter;

import java.util.Map;

/**
 * Created by compass on 13-10-18.
 */
public class CalculatedQuestionAdapter implements QuestionAdapter {
    private CalculatedQuestion question;

    public CalculatedQuestionAdapter(CalculatedQuestion question) {
        this.question = question;
    }

    @Override
    public String convertSetup() {
        StringBuffer buffer = new StringBuffer();

        for (Map.Entry<String, Variable> entry : question.getFormulaVars().entrySet()) {
            Variable var = entry.getValue();
            buffer.append(String.format("$%s = random(%f, %f, %f);" + PGProblem.LF, var.getName(), var.getMin(), var.getMax(), Math.pow(0.1, var.getDecimalPlaces())));
        }

        return buffer.toString();
    }

    @Override
    public String convertQuestion() {
        String questionText = HtmlTexConverter.convert(question.getText());

        return replaceVariables(questionText);
    }

    @Override
    public String convertAnswer() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[_____________________________]");
        buffer.append("{\"" + question.getFormulaAscii() + "\"}" + PGProblem.LF);

        return buffer.toString();
    }

    @Override
    public String convertAnswerNoPGML() {
        return null;
    }

    @Override
    public String convertSolution() {
        return null;
    }

    private String replaceVariables(String text) {
        String questionText = text;
        for (Map.Entry<String, Variable> entry : question.getFormulaVars().entrySet()) {
            Variable var = entry.getValue();
            questionText = questionText.replace("[" + var.getName() + "]", "[$" + var.getName() + "]");
        }

        return questionText;
    }
}
