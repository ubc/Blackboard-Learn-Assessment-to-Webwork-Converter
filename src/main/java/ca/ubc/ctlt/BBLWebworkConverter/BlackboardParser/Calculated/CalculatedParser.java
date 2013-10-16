package ca.ubc.ctlt.BBLWebworkConverter.BlackboardParser.Calculated;

import org.apache.commons.lang3.StringEscapeUtils;

import nu.xom.Element;
import ca.ubc.ctlt.BBLWebworkConverter.Assessment.CalculatedQuestion;
import ca.ubc.ctlt.BBLWebworkConverter.Assessment.Question;
import ca.ubc.ctlt.BBLWebworkConverter.Assessment.Variable;
import ca.ubc.ctlt.BBLWebworkConverter.BlackboardParser.QuestionParser;
import fmath.conversion.ConvertFromMathMLToLatex;

public class CalculatedParser extends QuestionParser
{

	public CalculatedParser(CalculatedQuestion question, Element questionRoot)
	{
		super(question, questionRoot);
	}

	@Override
	public Question parse()
	{
		for (int j = 0; j < questionRoot.getChildCount(); j++)
		{
			Element tmpElem = (Element) questionRoot.getChild(j);
			String elemName = tmpElem.getQualifiedName();
			if (elemName.equals("presentation"))
			{
				parsePresentation(tmpElem);
			}
			else if (elemName.equals("itemproc_extension"))
			{
				parseItemprocExtension(tmpElem);
			}
		}
		return null;
	}

	/**
	 * Get the question text
	 * @param question
	 * @param presentation
	 */
	private void parsePresentation(Element presentation)
	{
		// need to get <mat_formattedtext> which is under <flow><flow><flow><material><mat_extension>
		Element formattedtext = (Element) presentation.getChild(0).getChild(0).getChild(0).getChild(0).getChild(0).getChild(0);
		getQuestion().setText(formattedtext.getValue());
	}
	
	/**
	 * Get the formula and variables
	 * @param question
	 * @param presentation
	 */
	private void parseItemprocExtension(Element presentation)
	{
		Element calculated = (Element) presentation.getChild(0);
		for (int i = 0; i < calculated.getChildCount(); i++)
		{
			Element child = (Element) calculated.getChild(i);
			String name = child.getQualifiedName();
			if (name.equals("formula"))
			{
				String formula = child.getValue();
				formula = formula.replace(" xmlns=\"http://www.w3.org/1998/Math/MathML\"", "");	// fmath converter doesn't like xmlns
				formula = StringEscapeUtils.unescapeHtml4(formula); // convert html entities back into regular characters
				formula = ConvertFromMathMLToLatex.convertToLatex(formula);
				getQuestion().setFormulaLatex(formula);
			}
			else if (name.equals("vars"))
			{
				parseVars(child);
				getQuestion().setFormulaAscii(FormulaParser.parseToAsciiMath(getQuestion()));
			}
			else if (name.equals("answer_tolerance"))
			{
				String toleranceType = child.getAttributeValue("type");
				if (toleranceType.equals(Question.ANSWER_TOLERANCE_NUMERIC))
				{
					getQuestion().setAnswerToleranceType(Question.ANSWER_TOLERANCE_NUMERIC);
				}
				else if (toleranceType.equals(Question.ANSWER_TOLERANCE_PERCENT))
				{
					getQuestion().setAnswerToleranceType(Question.ANSWER_TOLERANCE_PERCENT);
				}
				else
				{
					System.out.println("Warning: Unknown answer tolerance type");
				}
				getQuestion().setAnswerTolerance(Double.parseDouble(child.getValue()));
			}
			else if (name.equals("answer_scale"))
			{
				int decimalPlace = Integer.parseInt(child.getValue());
				getQuestion().setAnswerDecimalPlaces(decimalPlace);
			}
		}
	}
	
	private void parseVars(Element vars)
	{
		// get the variables being used in the formula and the range that they're to be generated in
		for (int i = 0; i < vars.getChildCount(); i++)
		{
			Element var = (Element) vars.getChild(i);
			// variable information
			String varName = var.getAttributeValue("name");
			int varScale = Integer.parseInt(var.getAttributeValue("scale")); // AKA significant digit
			// variable range
			Element min = null;
			Element max = null;
			for (int j = 0; j < var.getChildCount(); j++)
			{
				Element tmp = (Element) var.getChild(j);
				String name = tmp.getQualifiedName();
				if (name.equals("min"))
				{
					min = tmp;
				}
				else if (name.equals("max"))
				{
					max = tmp;
				}
				else
				{
					System.out.println("Warning: Did not find variable min or max");
				}
			}
			// create and add the variable to question
			Variable variable = new Variable();
			variable.setName(varName);
			variable.setDecimalPlaces(varScale);
			variable.setMax(Double.parseDouble(max.getValue()));
			variable.setMin(Double.parseDouble(min.getValue()));
			getQuestion().addVariable(variable);
		}
	}
	
	@Override
	public CalculatedQuestion getQuestion()
	{
		return (CalculatedQuestion) super.getQuestion();
	}
	
}
