package ca.ubc.ctlt.BBLWebworkConverter.PGBuilder;

import ca.ubc.ctlt.BBLWebworkConverter.Assessment.Choice;
import ca.ubc.ctlt.BBLWebworkConverter.Assessment.MultiChoiceQuestion;
import ca.ubc.ctlt.BBLWebworkConverter.HtmlTexConverter;

/**
 * MultipleChoiceQuestionAdapter the adapter to convert multiple choice question to PG
 *
 * @see <a href="http://webwork.maa.org/wiki/MultipleChoiceProblems">http://webwork.maa.org/wiki/MultipleChoiceProblems</a>
 */
public class MultipleChoiceQuestionAdapter implements QuestionAdapter {
    private MultiChoiceQuestion question;

    public MultipleChoiceQuestionAdapter(MultiChoiceQuestion question) {
        this.question = question;
    }

    @Override
    public String convertSetup() {
        StringBuffer buffer = new StringBuffer();

        buffer.append("$mc = RadioButtons([").append(PGProblem.LF);
        Choice correct = null;
        for (Choice c : question.getChoices()) {
            buffer.append("\"" + c.getText().replaceAll("^<p>", "").replaceAll("</p>$", "") + "\",").append(PGProblem.LF);
            if (c.isCorrect()) {
                correct = c;
            }
        }
        buffer.append("],").append(PGProblem.LF);

        if (correct == null) {
            throw new RuntimeException("No valid correct answer for the question!");
        }

        // correct answer
        buffer.append("\"" + correct.getText().replaceAll("^<p>", "").replaceAll("</p>$", "") + "\",").append(PGProblem.LF);

        buffer.append(");");

        return  buffer.toString();
    }

    @Override
    public String convertQuestion() {
        return HtmlTexConverter.convert(question.getText());
    }

    @Override
    public String convertAnswer() {
        return null;
    }

    @Override
    public String convertAnswerNoPGML() {
        //we have to use old style, PGML don't support it.
        StringBuffer buffer = new StringBuffer();
        buffer.append("BEGIN_TEXT").append(PGProblem.LF);
        buffer.append("\\{ $mc->buttons() \\}").append(PGProblem.LF);
        buffer.append("END_TEXT").append(PGProblem.LF);

        //answer evaluation
        buffer.append("$showPartialCorrectAnswers = 0;").append(PGProblem.LF);
        buffer.append("ANS( $mc->cmp() );").append(PGProblem.LF);

        return buffer.toString();
    }

    @Override
    public String convertSolution() {
        return null;
    }
}
