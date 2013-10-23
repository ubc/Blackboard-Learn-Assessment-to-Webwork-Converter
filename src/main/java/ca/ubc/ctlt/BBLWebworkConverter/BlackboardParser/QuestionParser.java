package ca.ubc.ctlt.BBLWebworkConverter.BlackboardParser;

import nu.xom.Element;
import ca.ubc.ctlt.BBLWebworkConverter.Assessment.Question;

public abstract class QuestionParser
{
	private Question question;
	protected Element questionRoot;
	
	public QuestionParser(Question question, Element questionRoot)
	{
		setQuestion(question);
		setQuestionRoot(questionRoot);
		parseQuestionTitle();
	}
	
	public abstract Question parse();

	public Question getQuestion()
	{
		return question;
	}

	public void setQuestion(Question question)
	{
		this.question = question;
	}

	public Element getQuestionRoot()
	{
		return questionRoot;
	}

	public void setQuestionRoot(Element questionRoot)
	{
		this.questionRoot = questionRoot;
	}

	protected void parseItemfeedback(Element itemfeedback)
	{
		if (itemfeedback.getChild(0).getChildCount() == 0)
		{ // no feedback given
			return;
		}
		// <flow_mat><flow_mat><material><mat_extension><mat_formattedtext>
		String msg = itemfeedback.getChild(0).getChild(0).getChild(0).getChild(0).getChild(0).getValue();
		if (itemfeedback.getAttributeValue("ident").equals("correct"))
		{
			getQuestion().setCorrectMessage(msg);
		}
		else if (itemfeedback.getAttributeValue("ident").equals("incorrect"))
		{
			getQuestion().setIncorrectMessage(msg);
		}
	}
	
	protected void parseQuestionTitle()
	{
		question.setTitle(getQuestionRoot().getAttributeValue("title"));
	}

	
}
