package ca.ubc.ctlt.BBLWebworkConverter.BlackboardParser.Calculated;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ca.ubc.ctlt.BBLWebworkConverter.Assessment.CalculatedQuestion;

public class FormulaParser
{
	/**
	 * functions that have a direct mapping from latex to ascii
	 */
	public final static String[] DIRECTMAP = {"sin", "cos", "csc", "sec", "cot", "log", "exp", "ln", "tan", "asin", "acos","atan"};
	/**
	 * latex commands that don't have a direct mapping from latex to ascii
	 */
	public final static String[] NONDIRECTMAP = {"frac", "sqrt", "left", "right"};
	/**
	 * unsupported functions, no equivalent in webwork
	 */
	public final static String[] UNSUPPORTED = {"round"};
	
	
	/**
	 * Given a formula in latex format, convert it into the ascii math format
	 * that Webwork's MathObjects uses.
	 * 
	 * @param formula
	 * @return
	 */
	public static String parseToAsciiMath(CalculatedQuestion question)
	{
		String formula = question.getFormulaLatex();
		// prepend $ to variables, this must be done before replaceDirectMapping
		// since the latex command prefix \ is how we know functions like sin
		// isn't the variables s, i, and n
		formula = parseVars(question, formula);

		// replace functions with direct mappings
		formula = replaceDirectMapping(formula);

		// convert pi symbol to 'pi'
		formula = formula.replace("\\#960", "pi");

		// convert the dot operator to '*'
		formula = formula.replace("\\#183", "*");
		// convert the x operator to '*'
		formula = formula.replace("\\#215", "*");
		// convert the division operator to '/'
		formula = formula.replace("\\#247", "/");

		// convert \left. to ( and \right. to )
		formula = formula.replace("\\left.", "(");
		formula = formula.replace("\\right.", ")");
		
		// convert \#160, the non-breaking space character, to regular space
		formula = formula.replace("\\#160", " ");

		// convert fraction
		formula = parseFrac(formula);

		// convert sqrt and roots with other bases
		formula = parseSqrt(formula);
		
		// remove extra spacing from decimal numbers
		formula = removeExtraSpacingFromDecimals(formula);
		
		// check for unsupported functions
		for (String func : UNSUPPORTED)
		{
			if (formula.contains(func))
			{
				System.out.println("Unsupported function " + func + "() found, cannot convert.");
				throw new RuntimeException("Unsupported function " + func + "() found in formula, cannot convert.");
			}
		}

		return formula;
	}
	
	/**
	 * Decimal numbers, e.g.: 5.1, gets additional spaces after conversion, so they look more like "5 . 1", this method
	 * will remove the extra spaces.
	 * 
	 * @return
	 */
	private static String removeExtraSpacingFromDecimals(String formula)
	{
		String regex = "(\\d+? \\. \\d+?)";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(formula);
		if (matcher.find())
		{
			String decimal = matcher.group(1);
			decimal = decimal.replace(" ", "");
			formula = replaceSection(matcher.start(), matcher.end(), formula, decimal);
			formula = removeExtraSpacingFromDecimals(formula);
		} 
		return formula;
	}
	
	private static String parseVars(CalculatedQuestion question, String formula)
	{
		for (String var : question.getFormulaVars().keySet())
		{
			FormulaVariableParser parser = new FormulaVariableParser(var, formula);
			formula = parser.parse();
		}
		return formula;
	}
	
	private static String parseFrac(String formula)
	{
		String regex = "\\\\frac\\{([^{]+?)}\\{([^{]+?)}";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(formula);
		if (matcher.find())
		{
			String numerator = matcher.group(1);
			String denominator = matcher.group(2);
			// put parens around numerator and denominator if there's an operator in the expression
			// to be more explicit about the order of operations
			if (numerator.matches(".*[^\\w()].*"))
			{
				numerator = "(" + numerator + ")";
			}
			if (denominator.matches(".*[^\\w()].*"))
			{
				denominator = "(" + denominator + ")";
			}
			formula = replaceSection(matcher.start(), matcher.end(), formula,
					"(" + numerator + "/" + denominator + ")");
			return parseFrac(formula);
		} else
		{
			return formula;
		}
	}

	private static String parseSqrt(String formula)
	{
		// sqrt may not always be base 2, e.g.: \sqrt[5]{3} is 3^(1/5)
		// Webwork only has sqrt base 2, so roots with other bases needs to be converted
		// to the exponent form
		// root base 2 case
		String regex = "\\\\sqrt\\{([^{]+?)}";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcherSqrt = pattern.matcher(formula);
		// root base >2 case
		regex = "\\\\sqrt\\[([^{]+?)]\\{([^{]+?)}";
		pattern = Pattern.compile(regex);	
		Matcher matcherGenRoot = pattern.matcher(formula); // general roots matcher
		if (matcherSqrt.find())
		{
			String operand = matcherSqrt.group(1);
			formula = replaceSection(matcherSqrt.start(), matcherSqrt.end(), formula, "sqrt(" + operand + ")");
			return parseSqrt(formula);
		}
		else if (matcherGenRoot.find())
		{
			String base = matcherGenRoot.group(1);
			String operand = matcherGenRoot.group(2);
			formula = replaceSection(matcherGenRoot.start(), matcherGenRoot.end(), formula, operand + "^(1/" + base + ")");
			return parseSqrt(formula);
		}
		else
		{
			return formula;
		}
	}

	/**
	 * Delete the specified section of the string and insert the replacement in
	 * its place.
	 * 
	 * @param start
	 * @param end
	 * @param original
	 * @param replacement
	 * @return
	 */
	public static String replaceSection(int start, int end, String original, String replacement)
	{
		String firstHalf = original.substring(0, start);
		String secondHalf = original.substring(end);
		return firstHalf + replacement + secondHalf;
	}
	
	/**
	 * Some latex functions have a direct mapping where we just need to remove the
	 * latex command prefix \, this function takes care of those
	 * @param formula
	 * @return
	 */
	private static String replaceDirectMapping(String formula)
	{
		for (String func : DIRECTMAP)
		{
			formula = formula.replace("\\" + func, func);
		}

		return formula;
	}
}
