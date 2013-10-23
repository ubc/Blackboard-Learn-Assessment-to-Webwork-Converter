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
import ca.ubc.ctlt.BBLWebworkConverter.Assessment.MultiChoiceQuestion;
import ca.ubc.ctlt.BBLWebworkConverter.Assessment.Question;
import ca.ubc.ctlt.BBLWebworkConverter.Assessment.QuestionTypes;
import ca.ubc.ctlt.BBLWebworkConverter.BlackboardParser.BlackboardParser;
import ca.ubc.ctlt.BBLWebworkConverter.PGBuilder.*;
import org.apache.commons.cli.*;

public class Converter
{

	public static void main(String[] args)
	{

        Options options = new Options();

        options.addOption("d", false, "Display debug information");
        options.addOption("h", false, "Print help");

        Option input = OptionBuilder.withArgName( "file" )
                .hasArg()
                .withDescription("input zip file exported from Blackboard Learn")
                .create( "i" );
        options.addOption(input);

        CommandLineParser cliparser = new BasicParser();
        CommandLine cmd = null;
        try {
            cmd = cliparser.parse(options, args);
        } catch (ParseException e) {
            System.err.println("Parsing failed.  Reason: " + e.getMessage());
            printHelp(options);
            return;
        }

        if (cmd.hasOption("h")) {
            printHelp(options);
            return;
        }

        // check if file argument exists
		if (!cmd.hasOption("i"))
		{
			System.out.println("Please pass in an xml export file as a parameter.");
			return;
		}
		String xmlPath = cmd.getOptionValue("i");
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
	    // cleanup 
	    try
		{
			zip.close();
		} catch (IOException e)
		{ // does't matter what exceptions is thrown at this point
			e.printStackTrace();
		}
	    // check that the parser likes the file we chose
	    if (parser == null || !parser.validate())
	    { // either no entries or couldn't find the correct question xml file
	    	System.out.println("Could not file questions xml in zip file.");
	    	return;
	    }

		// parse the file into intermediate Assessment data structure
		List<Question> questions = parser.getQuestions();
        QuestionAdapter adapter = null;

        if (cmd.hasOption("d")) {
            System.out.println("Title: " + parser.getTitle());
        }
        PGZipper zipper = new PGZipper();
		for (Question q : questions)
		{
			if (q.getType().equals(QuestionTypes.CALCULATED))
			{
				CalculatedQuestion cq = (CalculatedQuestion) q;
                if (cmd.hasOption("d")) {
                    System.out.println(cq.toString());
                }
                adapter = new CalculatedQuestionAdapter(cq);
			}
			else if (q.getType().equals(QuestionTypes.MULTIPLE_CHOICE))
			{
				MultiChoiceQuestion mcq = (MultiChoiceQuestion) q;
                if (cmd.hasOption("d")) {
                    System.out.println(mcq.toString());
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

            if (cmd.hasOption("d")) {
                System.out.println("----------------   begin of problem --------------");
                System.out.println(builder.getProblem().toString());
                System.out.println("----------------   end of problem --------------");
            }
            zipper.addProblem(builder.getProblem());
        }

        // WeBWorK doesn't like special characters in the name
        String setName = parser.getTitle().replace("#", "").replace(" ", "_");
        zipper.pack(setName);

        System.out.println("Done!");
    }

    private static void printHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("converter", options);
    }
}
