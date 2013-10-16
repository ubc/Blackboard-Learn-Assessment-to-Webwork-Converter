package ca.ubc.ctlt.BBLWebworkConverter.Assessment;

import java.util.HashMap;

public class Question {
	
	public static final String ANSWER_TOLERANCE_NUMERIC = "numeric";
	public static final String ANSWER_TOLERANCE_PERCENT = "percent";
	
	protected String type;
	protected String text;
	private String formulaLatex;
	private String formulaAscii;
	private String answerToleranceType; // this should be either the NUMERIC or PERCENT constants
	private double answerTolerance;
	private int answerDecimalPlaces;
	private HashMap<String, Variable> formulaVars = new HashMap<String, Variable>();

	public String getType() {
		return type;
	}
	public String getText() {
		return text;
	}
	public String getFormulaLatex() {
		return formulaLatex;
	}
	public String getFormulaAscii() {
		return formulaAscii;
	}
	public HashMap<String, Variable> getFormulaVars() {
		return formulaVars;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	public void setFormulaLatex(String formula)
	{
		this.formulaLatex = formula;
	}
	
	public void addVariable(Variable var)
	{
		formulaVars.put(var.getName(), var);
	}
	public String getAnswerToleranceType()
	{
		return answerToleranceType;
	}
	public void setAnswerToleranceType(String answerToleranceType)
	{
		this.answerToleranceType = answerToleranceType;
	}
	public double getAnswerTolerance()
	{
		return answerTolerance;
	}
	public void setAnswerTolerance(double answerTolerance)
	{
		this.answerTolerance = answerTolerance;
	}
	public int getAnswerDecimalPlaces()
	{
		return answerDecimalPlaces;
	}
	public void setAnswerDecimalPlaces(int answerDecimalPlaces)
	{
		this.answerDecimalPlaces = answerDecimalPlaces;
	}
	public void setFormulaAscii(String formulaAscii)
	{
		this.formulaAscii = formulaAscii;
	}

}
