package ca.ubc.ctlt.BBLWebworkConverter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import ca.ubc.ctlt.BBLWebworkConverter.Assessment.CalculatedQuestion;
import ca.ubc.ctlt.BBLWebworkConverter.Assessment.Choice;
import ca.ubc.ctlt.BBLWebworkConverter.Assessment.MultiChoiceQuestion;
import ca.ubc.ctlt.BBLWebworkConverter.Assessment.Question;
import ca.ubc.ctlt.BBLWebworkConverter.Assessment.QuestionTypes;
import ca.ubc.ctlt.BBLWebworkConverter.Assessment.Variable;
import ca.ubc.ctlt.BBLWebworkConverter.BlackboardParser.BlackboardParser;
import ca.ubc.ctlt.BBLWebworkConverter.PGBuilder.CalculatedQuestionAdapter;
import ca.ubc.ctlt.BBLWebworkConverter.PGBuilder.MultipleChoiceQuestionAdapter;
import ca.ubc.ctlt.BBLWebworkConverter.PGBuilder.PGBuilder;
import ca.ubc.ctlt.BBLWebworkConverter.PGBuilder.QuestionAdapter;

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
		// the export should comes in a zip file
		ZipFile zip;
		try
		{
			zip = new ZipFile(file);
		} catch (ZipException e)
		{
			System.out.println("Zip format error. " + e.getMessage());
			return;
		} catch (IOException e)
		{
			System.out.println("File i/o error. " + e.getMessage());
			return;
		}
		// look for the xml file with the questions
	    Enumeration<? extends ZipEntry> entries = zip.entries();
		BlackboardParser parser = null;
	    while (entries.hasMoreElements())
	    {
	        ZipEntry entry = entries.nextElement();
	        String name = entry.getName();
	        if (!name.substring(name.length() - 3).equalsIgnoreCase("dat"))
	        { // skip files that don't have a dat extension
	        	continue;
	        }
	        InputStream stream;
			try
			{
				stream = zip.getInputStream(entry);
			} catch (IOException e)
			{
				System.out.println("Error reading file in zip: " + name);
				continue;
			}
			// ask the parser if this is the file we're looking for
	        parser = new BlackboardParser(stream);
	        if (parser.validate()) break; // this is the file we're looking for
	    }
	    if (parser == null || !parser.validate())
	    { // either no entries or couldn't find the correct question xml file
	    	System.out.println("Could not file questions xml in zip file.");
	    	return;
	    }
	    // cleanup 
	    try
		{
			zip.close();
		} catch (IOException e)
		{ // does't matter what exceptions is thrown at this point
			e.printStackTrace();
		}

		// parse the file into intermediate Assessment data structure
		List<Question> questions = parser.getQuestions();
        QuestionAdapter adapter = null;
		for (Question q : questions)
		{
			System.out.println("Question: ");
			System.out.println(" Text: " + q.getText());
			System.out.println(" Type: " + q.getType());
			System.out.println(" Correct: " + q.getCorrectMessage());
			System.out.println(" Incorrect: " + q.getIncorrectMessage());
			if (q.getType().equals(QuestionTypes.CALCULATED))
			{
				CalculatedQuestion cq = (CalculatedQuestion) q;
				System.out.println(" Latex Formula: " + cq.getFormulaLatex());
				System.out.println(" Ascii Formula: " + cq.getFormulaAscii());
				System.out.println(" Answer Tolerance: " + cq.getAnswerToleranceType() + " " + cq.getAnswerTolerance());
				System.out.println(" Answer Decimal Place: " + cq.getAnswerDecimalPlaces());
				System.out.println(" Variables: ");
				for (Variable v : cq.getFormulaVars().values())
				{
					System.out.println("  " + v.getName() + " - Max: " + v.getMax() + " Min: " + v.getMin() + " Decimal Place: " + v.getDecimalPlaces());
				}
                adapter = new CalculatedQuestionAdapter(cq);
			}
			else if (q.getType().equals(QuestionTypes.MULTIPLE_CHOICE))
			{
				MultiChoiceQuestion mcq = (MultiChoiceQuestion) q;
				System.out.println(" Choices: ");
				for (Choice c : mcq.getChoices())
				{
					System.out.println("  " + c.getIdent() + " " + c.isCorrect() + " " + c.getText());
				}
                adapter = new MultipleChoiceQuestionAdapter(mcq);
			} else {
                throw new RuntimeException("Unknown question type!");
            }

            // generate question
            PGBuilder builder = new PGBuilder(adapter);
            builder.addMacro("PGstandard.pl")
                    .addMacro("MathObjects.pl")
                    .addMacro("PGML.pl")
                    .addMacro("PGcourse.pl")
                    .addMacro("PGcourse.pl")
                    .addMacro("parserRadioButtons.pl");

            System.out.println("----------------   begin of problem --------------");
            System.out.println(builder.getProblem().toString());
            System.out.println("----------------   end of problem --------------");
        }
	}
}
