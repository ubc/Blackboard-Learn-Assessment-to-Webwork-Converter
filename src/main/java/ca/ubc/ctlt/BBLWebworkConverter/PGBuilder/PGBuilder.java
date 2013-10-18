package ca.ubc.ctlt.BBLWebworkConverter.PGBuilder;

/**
 * Created by compass on 13-10-08.
 */
public class PGBuilder {
    private QuestionAdapter adapter = null;
    private PGProblem problem;

    public PGBuilder(QuestionAdapter adapter) {
        this.adapter = adapter;
        this.problem = new PGProblem();
        this.problem.setSetup(adapter.convertSetup());
        this.problem.setQuestion(adapter.convertQuestion());
        this.problem.setAnswer(adapter.convertAnswer());
        this.problem.setAnswerNoPGML(adapter.convertAnswerNoPGML());
        this.problem.setSolution(adapter.convertSolution());
    }

    public PGBuilder addMacro(String macro) {
        problem.addMacro(macro);

        return this;
    }

    public PGBuilder addTag(String key, String value) {
        problem.addTag(key, value);

        return this;
    }

    public PGProblem getProblem() {
        return problem;
    }

}
