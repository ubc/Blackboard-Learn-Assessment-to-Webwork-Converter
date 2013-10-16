package ca.ubc.ctlt.BBLWebworkConverter.Assessment;

public class Choice
{
	boolean correct;
	String text;
	String ident;

	public String getIdent()
	{
		return ident;
	}
	public void setIdent(String ident)
	{
		this.ident = ident;
	}
	public boolean isCorrect()
	{
		return correct;
	}
	public void setCorrect(boolean correct)
	{
		this.correct = correct;
	}
	public String getText()
	{
		return text;
	}
	public void setText(String text)
	{
		this.text = text;
	}

}
