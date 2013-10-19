package ca.ubc.ctlt.BBLWebworkConverter.PGBuilder;

import ca.ubc.ctlt.BBLWebworkConverter.Assessment.Choice;
import ca.ubc.ctlt.BBLWebworkConverter.Assessment.MultiChoiceQuestion;
import ca.ubc.ctlt.BBLWebworkConverter.HtmlTexConverter;

/**
 * MultipleSelectQuestionAdapter the adapter to convert multiple select question to PG
 *
 * @see <a href="http://webwork.maa.org/wiki/MultipleSelectProblems">http://webwork.maa.org/wiki/MultipleSelectProblems</a>
 */
public class MultipleSelectQuestionAdapter implements QuestionAdapter {
    private MultiChoiceQuestion question;

    public MultipleSelectQuestionAdapter(MultiChoiceQuestion question) {
        this.question = question;
    }

    @Override
    public String convertSetup() {
        StringBuffer buffer = new StringBuffer();

        buffer.append("$mc = new_checkbox_multiple_choice()" + PGProblem.LF);
        //the first parameter is question text, we don't use it here as we put the question text in the PGML section
        buffer.append("$mc -> qa (\"\"," + PGProblem.LF);

        // add correct answers
        for (Choice c : question.getChoices()) {
            if (c.isCorrect()) {
                buffer.append("\"" + c.getText() + "\"" + PGProblem.LF);
            }
        }
        buffer.append(");");

        // adding wrong answers
        buffer.append("$mc -> extra("  + PGProblem.LF);
        for (Choice c : question.getChoices()) {
            if (!c.isCorrect()) {
                buffer.append("\"" + c.getText() + "\"" + PGProblem.LF);
            }
        }
        buffer.append(");");

        return buffer.toString();
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
        buffer.append("\\{ $mc -> print_a() \\}").append(PGProblem.LF);
        buffer.append("END_TEXT").append(PGProblem.LF);

        //answer evaluation
        buffer.append("install_problem_grader(~~&std_problem_grader);").append(PGProblem.LF);
        buffer.append("$showPartialCorrectAnswers = 0;").append(PGProblem.LF);
        buffer.append("ANS( checkbox_cmp( $mc->correct_ans() ) );").append(PGProblem.LF);

        return buffer.toString();
    }

    @Override
    public String convertSolution() {
        return null;
    }
}
