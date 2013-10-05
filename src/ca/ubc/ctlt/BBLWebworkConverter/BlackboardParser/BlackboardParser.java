package ca.ubc.ctlt.BBLWebworkConverter.BlackboardParser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;

import ca.ubc.ctlt.BBLWebworkConverter.Assessment.Question;

import fmath.conversion.ConvertFromMathMLToLatex;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.ParsingException;
import nu.xom.ValidityException;

public class BlackboardParser 
{
	private Document doc;
	
	public BlackboardParser(File file)
	{		
		Builder builder = new Builder();
		try {
			doc = builder.build(file);
			System.out.println("Doc " + doc.getRootElement().getQualifiedName());
		} catch (ValidityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParsingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		



		String input = "&lt;math xmlns=\"http://www.w3.org/1998/Math/MathML\"&gt;&lt;mi&gt;n&lt;/mi&gt;&lt;mo&gt;*&lt;/mo&gt;&lt;mn&gt;8&lt;/mn&gt;&lt;mo&gt;.&lt;/mo&gt;&lt;mn&gt;314472&lt;/mn&gt;&lt;mo&gt;*&lt;/mo&gt;&lt;mo&gt;(&lt;/mo&gt;&lt;mi&gt;T&lt;/mi&gt;&lt;mo&gt;+&lt;/mo&gt;&lt;mn&gt;273&lt;/mn&gt;&lt;mo&gt;)&lt;/mo&gt;&lt;mo&gt;*&lt;/mo&gt;&lt;mi&gt;ln&lt;/mi&gt;&lt;mfenced&gt;&lt;mfrac&gt;&lt;mi&gt;F&lt;/mi&gt;&lt;mi&gt;I&lt;/mi&gt;&lt;/mfrac&gt;&lt;/mfenced&gt;&lt;/math&gt;";
		input = input.replace(" xmlns=\"http://www.w3.org/1998/Math/MathML\"", ""); // remove xmlns as the fmath converter doesn't like it
		input = StringEscapeUtils.unescapeHtml4(input);
		System.out.println(input);
		String ret = ConvertFromMathMLToLatex.convertToLatex(input);
		System.out.println(ret);
	}
	
	public List<Question> getQuestions()
	{
		ArrayList<Question> questions = new ArrayList<Question>();
		return questions;
	}
}
