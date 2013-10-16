package ca.ubc.ctlt.BBLWebworkConverter.Assessment;

public class Question {
	
	public static final String ANSWER_TOLERANCE_NUMERIC = "numeric";
	public static final String ANSWER_TOLERANCE_PERCENT = "percent";
	
	protected String type;
	protected String text;
	
	// messages to show when the user gets the answer correct or incorrect
	protected String incorrectMessage;
	protected String correctMessage;
	
	public String getType() {
		return type;
	}
	public String getText() {
		return text;
	}
	public void setType(String type)
	{
		this.type = type;
	}

	public void setText(String text)
	{
		this.text = text;
	}
	public String getIncorrectMessage()
	{
		return incorrectMessage;
	}
	public void setIncorrectMessage(String incorrectMessage)
	{
		this.incorrectMessage = incorrectMessage;
	}
	public String getCorrectMessage()
	{
		return correctMessage;
	}
	public void setCorrectMessage(String correctMessage)
	{
		this.correctMessage = correctMessage;
	}

	
}
