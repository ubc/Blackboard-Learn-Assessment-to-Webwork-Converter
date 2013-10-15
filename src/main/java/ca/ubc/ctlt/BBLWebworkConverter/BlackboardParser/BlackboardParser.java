package ca.ubc.ctlt.BBLWebworkConverter.BlackboardParser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ca.ubc.ctlt.BBLWebworkConverter.Assessment.Question;
import ca.ubc.ctlt.BBLWebworkConverter.Assessment.QuestionTypes;
import ca.ubc.ctlt.BBLWebworkConverter.BlackboardParser.Calculated.CalculatedParser;
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
			
			// parsing each question
			for (int j = 0; j < item.getChildCount(); j++)
			{
				Element tmpElem = (Element) item.getChild(j);
				String elemName = tmpElem.getQualifiedName();
				if (elemName.equals("itemmetadata"))
				{ // parse the question type
					parseItemMetadata(question, tmpElem);
				}
				QuestionParser parser;
				String type = question.getType();
				// need to use different parsers depending on the question type
				if (type.equals(QuestionTypes.CALCULATED))
				{
					parser = new CalculatedParser(question, item);
					parser.parse();
				}
				else if (type.equals(QuestionTypes.MULTIPLE_CHOICE))
				{
				}
				else
				{
					System.out.println("Warning, unrecognized question type: " + type);
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
	
}
