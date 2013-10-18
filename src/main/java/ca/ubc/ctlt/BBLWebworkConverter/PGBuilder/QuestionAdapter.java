package ca.ubc.ctlt.BBLWebworkConverter.PGBuilder;

/**
 * Created by compass on 13-10-18.
 */
public interface QuestionAdapter {
    public String convertSetup();
    public String convertQuestion();
    public String convertAnswer();
    public String convertAnswerNoPGML();
    public String convertSolution();
}
