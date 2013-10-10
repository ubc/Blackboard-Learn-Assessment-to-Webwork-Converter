package ca.ubc.ctlt.BBLWebworkConverter.BlackboardParser;

public class StringSection
{
	private int start;
	private int end;
	
	public StringSection(int start, int end)
	{
		this.start = start;
		this.end = end;
	}

	public int getStart()
	{
		return start;
	}

	public int getEnd()
	{
		return end;
	}
}
