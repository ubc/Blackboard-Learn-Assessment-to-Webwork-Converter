package ca.ubc.ctlt.BBLWebworkConverter.Assessment;

import java.util.HashMap;

public class CalculatedQuestion extends Question
{
	private String formulaLatex;
	private String formulaAscii;
	private String answerToleranceType; // this should be either the NUMERIC or PERCENT constants
	private double answerTolerance;
	private int answerDecimalPlaces;
	private HashMap<String, Variable> formulaVars = new HashMap<String, Variable>();
	private String unit; // if answers come with units, what unit to expect, e.g.: kg, m, s, etc.
	
	public String getFormulaLatex() {
		return formulaLatex;
	}
	public String getFormulaAscii() {
		return formulaAscii;
	}
	public HashMap<String, Variable> getFormulaVars() {
		return formulaVars;
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
	public String getUnit()
	{
		return unit;
	}
	public void setUnit(String unit)
	{
		this.unit = unit;
	}

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        for (Variable v : this.getFormulaVars().values()) {
            buffer.append(v.getName() + " - Max: " + v.getMax() + " Min: " + v.getMin() + " Decimal Place: " + v.getDecimalPlaces() + ", ");
        }
        return "CalculatedQuestion{" +
                "Latex Formula='" + formulaLatex + '\'' +
                ", Ascii Formula='" + formulaAscii + '\'' +
                ", Answer Tolerance Type='" + answerToleranceType + '\'' +
                ", Answer Tolerance=" + answerTolerance +
                ", Answer Decimal Place=" + answerDecimalPlaces +
                ", unit='" + unit + '\'' +
                ", formulaVars=" + buffer.toString() +
                "} " + super.toString();
    }
}
