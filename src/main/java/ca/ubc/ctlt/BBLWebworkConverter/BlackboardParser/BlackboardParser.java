package ca.ubc.ctlt.BBLWebworkConverter.BlackboardParser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ca.ubc.ctlt.BBLWebworkConverter.Assessment.CalculatedQuestion;
import ca.ubc.ctlt.BBLWebworkConverter.Assessment.MultiChoiceQuestion;
import ca.ubc.ctlt.BBLWebworkConverter.Assessment.Question;
import ca.ubc.ctlt.BBLWebworkConverter.Assessment.QuestionTypes;
import ca.ubc.ctlt.BBLWebworkConverter.BlackboardParser.Calculated.CalculatedParser;
import ca.ubc.ctlt.BBLWebworkConverter.BlackboardParser.MultiChoice.MultiChoiceParser;
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
			e.printStackTrace();
		} catch (ParsingException e) {
			e.printStackTrace();
		} catch (IOException e) {
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
			Question question = null;
			
			// parsing each question
			for (int j = 0; j < item.getChildCount(); j++)
			{
				Element tmpElem = (Element) item.getChild(j);
				String elemName = tmpElem.getQualifiedName();
				String type = "";
				if (elemName.equals("itemmetadata"))
				{ // parse the question type
					type = getQuestionType(tmpElem);
					if (type == null) throw new RuntimeException("Could not find question type");
				}
				else
				{
					continue;
				}
				QuestionParser parser;
				// need to use different parsers depending on the question type
				if (type.equals(QuestionTypes.CALCULATED))
				{
					CalculatedQuestion cq = new CalculatedQuestion();
					cq.setType(type);
					parser = new CalculatedParser(cq, item);
					parser.parse();
					question = cq;
				}
				else if (type.equals(QuestionTypes.MULTIPLE_CHOICE))
				{
					MultiChoiceQuestion mcq = new MultiChoiceQuestion();
					mcq.setType(type);
					parser = new MultiChoiceParser(mcq, item);
					parser.parse();
					question = mcq;
				}
				else
				{
					System.out.println("Warning, unrecognized question type: " + type);
				}
			}
			
			if (question != null) questions.add(question);
		}
		
		return questions;
	}
	
	/**
	 * Get the question type
	 * @param question
	 * @param presentation
	 */
	private String getQuestionType(Element presentation)
	{
		for (int i = 0; i < presentation.getChildCount(); i++)
		{
			Element elem = (Element) presentation.getChild(i);
			if (elem.getQualifiedName().equals("bbmd_questiontype"))
			{
				return elem.getValue();
			}
		}
		return null;
	}
	
}
