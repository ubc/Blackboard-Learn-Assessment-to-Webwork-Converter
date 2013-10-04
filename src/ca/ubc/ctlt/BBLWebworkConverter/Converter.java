package ca.ubc.ctlt.BBLWebworkConverter;

import org.apache.commons.lang3.StringEscapeUtils;

import fmath.conversion.ConvertFromMathMLToLatex;

public class Converter
{

	public static void main(String[] args)
	{
		String input = "&lt;math xmlns=\"http://www.w3.org/1998/Math/MathML\"&gt;&lt;mi&gt;n&lt;/mi&gt;&lt;mo&gt;*&lt;/mo&gt;&lt;mn&gt;8&lt;/mn&gt;&lt;mo&gt;.&lt;/mo&gt;&lt;mn&gt;314472&lt;/mn&gt;&lt;mo&gt;*&lt;/mo&gt;&lt;mo&gt;(&lt;/mo&gt;&lt;mi&gt;T&lt;/mi&gt;&lt;mo&gt;+&lt;/mo&gt;&lt;mn&gt;273&lt;/mn&gt;&lt;mo&gt;)&lt;/mo&gt;&lt;mo&gt;*&lt;/mo&gt;&lt;mi&gt;ln&lt;/mi&gt;&lt;mfenced&gt;&lt;mfrac&gt;&lt;mi&gt;F&lt;/mi&gt;&lt;mi&gt;I&lt;/mi&gt;&lt;/mfrac&gt;&lt;/mfenced&gt;&lt;/math&gt;";
		input = input.replace(" xmlns=\"http://www.w3.org/1998/Math/MathML\"", ""); // remove xmlns as the fmath converter doesn't like it
		input = StringEscapeUtils.unescapeHtml4(input);
		System.out.println(input);
		String ret = ConvertFromMathMLToLatex.convertToLatex(input);
		System.out.println(ret);
	}
}
