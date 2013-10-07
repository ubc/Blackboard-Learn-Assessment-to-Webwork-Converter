package ca.ubc.ctlt.BBLWebworkConverter.Assessment;

import java.util.HashMap;

public class Question {
	
	private String type;
	private String text;
	private String formula;
	private HashMap<String, Variable> formulaVars = new HashMap<String, Variable>();

	public String getType() {
		return type;
	}
	public String getText() {
		return text;
	}
	public String getFormula() {
		return formula;
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

	public void setFormula(String formula)
	{
		this.formula = formula;
	}

}
