package ca.ubc.ctlt.BBLWebworkConverter.BlackboardParser.Calculated;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ca.ubc.ctlt.BBLWebworkConverter.BlackboardParser.StringSection;

/**
 * Prefix variables in the formula with $
 */
public class FormulaVariableParser
{
	private static final char VARPADCHAR = ';';
	private static final char LATEXPADCHAR = '@';
	
	private String var;
	private String originalFormula;
	private List<StringSection> latexCmdLoc = new ArrayList<StringSection>();
	private String varPad; // what the variable was replaced with
	

	public FormulaVariableParser(String var, String formula)
	{
		super();
		this.var = var;
		this.originalFormula = formula;
	}
	
	/**
	 * The biggest problem here is that math functions like sin() can contain characters
	 * that may be used as variables. So we'll replace them with characters that cannot
	 * be used as variables and then restore them after we've safely prepended $ in front 
	 * of the actual variables.
	 * @return
	 */
	public String parse()
	{
		// remove latex commands so they won't be mistaken for variables
		String noLatexFormula = removeLatexCommands(originalFormula);
		varPad = pad(var.length(), VARPADCHAR);
		// replace the variable occurrences with a known padded value
		noLatexFormula = noLatexFormula.replace(var, varPad);
		// restore latex commands
		String ret = restoreLatexCommands(noLatexFormula);
		// substitute variable known padded value with $var
		ret = ret.replace(varPad, "$" + var + " ");

		return ret;
	}

	private String restoreLatexCommands(String formula)
	{
		for (StringSection section : latexCmdLoc)
		{
			formula = FormulaParser.replaceSection(section.getStart(), section.getEnd(), formula, 
					originalFormula.substring(section.getStart(), section.getEnd()));

		}
		return formula;
	}

	private String removeLatexCommands(String formula)
	{
		List<String> allMappings = new ArrayList<String>();
		allMappings.addAll(Arrays.asList(FormulaParser.DIRECTMAP));
		allMappings.addAll(Arrays.asList(FormulaParser.NONDIRECTMAP));
		
		for (String command : allMappings)
		{
			String regex = "\\\\" + command;
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(formula);
			while (matcher.find())
			{
				latexCmdLoc.add(new StringSection(matcher.start(), matcher.end()));
			}
			String toBeReplaced = "\\" + command;
			formula = formula.replace(toBeReplaced, pad(toBeReplaced.length(), LATEXPADCHAR));
		}
		
		return formula;
	}

	private String pad(int length, char charToFill) 
	{
		if (length > 0) 
		{
			char[] array = new char[length];
			Arrays.fill(array, charToFill);
			return new String(array);
		}
		return "";
	}
	

}
