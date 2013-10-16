package ca.ubc.ctlt.BBLWebworkConverter.BlackboardParser.MultiChoice;

import nu.xom.Element;
import ca.ubc.ctlt.BBLWebworkConverter.Assessment.Choice;
import ca.ubc.ctlt.BBLWebworkConverter.Assessment.MultiChoiceQuestion;
import ca.ubc.ctlt.BBLWebworkConverter.Assessment.Question;
import ca.ubc.ctlt.BBLWebworkConverter.BlackboardParser.QuestionParser;

public class MultiChoiceParser extends QuestionParser
{

	public MultiChoiceParser(MultiChoiceQuestion question, Element questionRoot)
	{
		super(question, questionRoot);
	}

	@Override
	public Question parse()
	{
		for (int i = 0; i < questionRoot.getChildCount(); i++)
		{
			Element tmpElem = (Element) questionRoot.getChild(i);
			String elemName = tmpElem.getQualifiedName();
			if (elemName.equals("presentation"))
			{ // parse the question text and choices
				parsePresentation(tmpElem);
			}
			else if (elemName.equals("resprocessing"))
			{ // parse which choice is correct
				parseResprocessing(tmpElem);
			}
		}
		return null;
	}

	private void parseResprocessing(Element resprocessing)
	{
		for (int i = 0; i < resprocessing.getChildCount(); i++)
		{
			Element respcondition = (Element) resprocessing.getChild(i);
			String title = respcondition.getAttributeValue("title");
			if (title != null && title.equals("correct"))
			{
				parseCorrectChoice(respcondition);
			}
		}
		
	}
	
	private void parseCorrectChoice(Element respcondition)
	{
		for (int j = 0; j < respcondition.getChildCount(); j++)
		{
			Element conditionVar = (Element) respcondition.getChild(j);
			if (!conditionVar.getQualifiedName().equals("conditionvar")) continue;
			String correctIdent = conditionVar.getChild(0).getValue();
			for (Choice choice : getQuestion().getChoices())
			{
				if (choice.getIdent().equals(correctIdent))
				{
					choice.setCorrect(true);
				}
			}
		}
	}

	private void parsePresentation(Element presentation)
	{
		// expecting the input to be in the form of
		// <flow class="Block"><flow class="QUESTION_BLOCK">...</flow><flow class="RESPONSE_BLOCK">...</flow></flow>
		// we want to parse the question block for the question text and the response block for the choices
		Element block = (Element) presentation.getChild(0);
		for (int i = 0; i < block.getChildCount(); i++)
		{
			Element child = (Element) block.getChild(i);

			if (child.getAttribute("class").getValue().equals("QUESTION_BLOCK"))
			{ // get question text by going down the xml tree
				// <flow class="FORMATTED_TEXT_BLOCK"><material><mat_extension><mat_formattedtext>
				String text = child.getChild(0).getChild(0).getChild(0).getChild(0).getValue();
				getQuestion().setText(text);
			}
			else if (child.getAttribute("class").getValue().equals("RESPONSE_BLOCK"))
			{
				parseResponses(child);
			}
		}
	}

	private void parseResponses(Element responses)
	{
		// go past <response_lid><render_choice> for the list of <flow> describing each choice
		Element blocks = (Element) responses.getChild(0).getChild(0);
		// should now go through a list of <flow_label>, each of which describes a choice
		for (int i = 0; i < blocks.getChildCount(); i++)
		{
			Element fl = (Element) blocks.getChild(i);
			Choice choice = new Choice();
			// <response_label>
			Element rl = (Element) fl.getChild(0);
			choice.setIdent(rl.getAttributeValue("ident"));
			// <flow_mat><material><mat_extension><mat_formattedtext>
			String choiceText = rl.getChild(0).getChild(0).getChild(0).getChild(0).getValue();
			choice.setText(choiceText);
			choice.setCorrect(false);
			getQuestion().addChoice(choice);
		}
	}

	@Override
	public MultiChoiceQuestion getQuestion()
	{
		return (MultiChoiceQuestion) super.getQuestion();
	}

}
