package ca.ubc.ctlt.BBLWebworkConverter.Assessment;

public class Question {
	
	public static final String ANSWER_TOLERANCE_NUMERIC = "numeric";
	public static final String ANSWER_TOLERANCE_PERCENT = "percent";
	
	protected String type;
	protected String text;
	
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

	
}
