package ca.ubc.ctlt.BBLWebworkConverter;

import java.io.File;
import java.util.List;

import ca.ubc.ctlt.BBLWebworkConverter.Assessment.Question;
import ca.ubc.ctlt.BBLWebworkConverter.Assessment.Variable;
import ca.ubc.ctlt.BBLWebworkConverter.BlackboardParser.BlackboardParser;

public class Converter
{

	public static void main(String[] args)
	{
		// check if file argument exists
		if (args.length != 1)
		{
			System.out.println("Please pass in an xml export file as a parameter.");
			return;
		}
		String xmlPath = args[0];
		// check if file exists
		File file = new File(xmlPath);
		if (!file.exists())
		{
			System.out.println("Could not find file: " + file.getPath());
			return;
		}
		// parse the file into intermediate Assessment data structure
		BlackboardParser parser = new BlackboardParser(file);
		List<Question> questions = parser.getQuestions();
		for (Question q : questions)
		{
			System.out.println("Question: ");
			System.out.println(" Text: " + q.getText());
			System.out.println(" Type: " + q.getType());
			System.out.println(" Formula: " + q.getFormula());
			System.out.println(" Answer Tolerance: " + q.getAnswerToleranceType() + " " + q.getAnswerTolerance());
			System.out.println(" Variables: ");
			for (Variable v : q.getFormulaVars().values())
			{
				System.out.println("  " + v.getName() + " - Max: " + v.getMax() + " Min: " + v.getMin() + " Sig: " + v.getSignificantDigit());
			}
		}
	}
}
