package ca.ubc.ctlt.BBLWebworkConverter.BlackboardParser;

import nu.xom.Element;
import ca.ubc.ctlt.BBLWebworkConverter.Assessment.Question;

public abstract class QuestionParser
{
	protected Question question;
	protected Element questionRoot;
	
	public QuestionParser(Question question, Element questionRoot)
	{
		setQuestion(question);
		setQuestionRoot(questionRoot);
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

}
