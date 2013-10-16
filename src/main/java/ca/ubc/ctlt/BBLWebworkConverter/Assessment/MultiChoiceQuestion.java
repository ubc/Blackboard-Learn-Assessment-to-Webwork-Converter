package ca.ubc.ctlt.BBLWebworkConverter.Assessment;

import java.util.ArrayList;
import java.util.List;

public class MultiChoiceQuestion extends Question
{
	private List<Choice> choices = new ArrayList<Choice>();
	private boolean randomize = false; // whether to present the choices in random order

	public List<Choice> getChoices()
	{
		return choices;
	}
	
	public void addChoice(Choice choice)
	{
		choices.add(choice);
	}

	public boolean isRandomize()
	{
		return randomize;
	}

	public void setRandomize(boolean randomize)
	{
		this.randomize = randomize;
	}
}
