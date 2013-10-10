package ca.ubc.ctlt.BBLWebworkConverter.BlackboardParser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;

import ca.ubc.ctlt.BBLWebworkConverter.Assessment.Question;
import ca.ubc.ctlt.BBLWebworkConverter.Assessment.Variable;
import fmath.conversion.ConvertFromMathMLToLatex;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.ParsingException;
import nu.xom.ValidityException;

public class BlackboardParser 
{
	private Document doc;
	
	public BlackboardParser(File file)
	{		
		Builder builder = new Builder();
		try {
			doc = builder.build(file);
		} catch (ValidityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParsingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public List<Question> getQuestions()
	{
		ArrayList<Question> questions = new ArrayList<Question>();
		Element root = doc.getRootElement(); // this should be <questestinterop>

		if (root.getChildCount() != 1)
		{ // we found more than or less than 1 <assessment>, just error out
			System.out.println("Unexpected number of assessments in data.");
			return questions;
		}
		
		Element assessment = (Element) root.getChild(0);
		Element section = null;
		// now need to get to <section>
		for (int i = 0; i < assessment.getChildCount(); i++)
		{
			Element tmpElem = (Element) assessment.getChild(i);
			if (tmpElem.getQualifiedName().equals("section"))
			{
				section = tmpElem;
			}
		}
		if (section == null)
		{
			System.out.println("Unable to find section in assessment.");
			return questions;
		}
		
		// now go through each <item> in <section>, <item> represents a question
		for (int i = 0; i < section.getChildCount(); i++)
		{
			Element item = (Element) section.getChild(i);
			if (!item.getQualifiedName().equals("item"))
			{ // don't need to worry about none <item> elements
				continue;
			}
			Question question = new Question();
			
			for (int j = 0; j < item.getChildCount(); j++)
			{
				Element tmpElem = (Element) item.getChild(j);
				String elemName = tmpElem.getQualifiedName();
				if (elemName.equals("itemmetadata"))
				{
					parseItemMetadata(question, tmpElem);
				}
				else if (elemName.equals("presentation"))
				{
					parsePresentation(question, tmpElem);
				}
				else if (elemName.equals("itemproc_extension"))
				{
					parseItemprocExtension(question, tmpElem);
				}
			}
			
			questions.add(question);
		}
		
		return questions;
	}
	
	/**
	 * Get the question type
	 * @param question
	 * @param presentation
	 */
	private void parseItemMetadata(Question question, Element presentation)
	{
		for (int i = 0; i < presentation.getChildCount(); i++)
		{
			Element elem = (Element) presentation.getChild(i);
			if (elem.getQualifiedName().equals("bbmd_questiontype"))
			{
				question.setType(elem.getValue());
				break;
			}
		}
	}
	
	/**
	 * Get the question text
	 * @param question
	 * @param presentation
	 */
	private void parsePresentation(Question question, Element presentation)
	{
		// need to get <mat_formattedtext> which is under <flow><flow><flow><material><mat_extension>
		Element formattedtext = (Element) presentation.getChild(0).getChild(0).getChild(0).getChild(0).getChild(0).getChild(0);
		question.setText(formattedtext.getValue());
	}
	
	/**
	 * Get the formula and variables
	 * @param question
	 * @param presentation
	 */
	private void parseItemprocExtension(Question question, Element presentation)
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
				question.setFormulaLatex(formula);
			}
			else if (name.equals("vars"))
			{
				parseVars(question, child);
				question.setFormulaAscii(FormulaParser.parseToAsciiMath(question));
			}
			else if (name.equals("answer_tolerance"))
			{
				String toleranceType = child.getAttributeValue("type");
				if (toleranceType.equals(Question.ANSWER_TOLERANCE_NUMERIC))
				{
					question.setAnswerToleranceType(Question.ANSWER_TOLERANCE_NUMERIC);
				}
				else if (toleranceType.equals(Question.ANSWER_TOLERANCE_PERCENT))
				{
					question.setAnswerToleranceType(Question.ANSWER_TOLERANCE_PERCENT);
				}
				else
				{
					System.out.println("Warning: Unknown answer tolerance type");
				}
				question.setAnswerTolerance(Double.parseDouble(child.getValue()));
			}
			else if (name.equals("answer_scale"))
			{
				int decimalPlace = Integer.parseInt(child.getValue());
				question.setAnswerDecimalPlaces(decimalPlace);
			}
		}
	}
	
	private void parseVars(Question question, Element vars)
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
			question.addVariable(variable);
		}
	}
}
