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

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        for (Choice c : this.getChoices()) {
            buffer.append(c.getIdent() + " " + c.isCorrect() + " " + c.getText() + ", ");
        }
        return "MultiChoiceQuestion{" +
                "choices=" + buffer.toString() +
                ", randomize=" + randomize +
                "} " + super.toString();
    }
}
