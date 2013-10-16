package ca.ubc.ctlt.BBLWebworkConverter.Assessment;

import java.util.ArrayList;
import java.util.List;

public class MultiChoiceQuestion extends Question
{
	private List<Choice> choices = new ArrayList<Choice>();

	public List<Choice> getChoices()
	{
		return choices;
	}
	
	public void addChoice(Choice choice)
	{
		choices.add(choice);
	}
}
