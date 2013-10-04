package ca.ubc.ctlt.BBLWebworkConverter.Assessment;

import java.util.HashMap;

public class Question {
	
	private String type;
	private String text;
	private String formula;
	private HashMap<String, Variable> formulaVars;

	public Question(String type, String text, String formula,
			HashMap<String, Variable> formulaVars) {
		super();
		this.type = type;
		this.text = text;
		this.formula = formula;
		this.formulaVars = formulaVars;
	}

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

}
