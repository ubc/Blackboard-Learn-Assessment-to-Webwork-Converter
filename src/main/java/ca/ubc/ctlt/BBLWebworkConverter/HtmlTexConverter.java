package ca.ubc.ctlt.BBLWebworkConverter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import java.util.List;

/**
 * Created by compass on 13-10-08.
 */
public class HtmlTexConverter {
    public static String superscript(String text) {
        text = text.replaceAll("<sup> +</sup>", " ");
        return text.replaceAll("<sup.*?>(.*?)</sup>", "[`^{$1}`]");
    }

    public static String subscript(String text) {
        return text.replaceAll("<sub.*?>(.*?)</sub>", "[`_{$1}`]");
    }

    public static String strip(String text) {
//        text = text.replaceAll("<span style=\"color: #000000; font-family: Verdana, Arial, Helvetica, sans-serif; font-size: 11.818181991577148px; font-style: normal; font-variant: normal; font-weight: normal; letter-spacing: normal; line-height: normal; orphans: auto; text-align: start; text-indent: 0px; text-transform: none; white-space: normal; widows: auto; word-spacing: 0px; -webkit-text-stroke-width: 0px; background-color: #ffffff; display: inline !important; float: none;\">(.*?)</span>", "$1");
//        text = text.replaceAll("<span style=\"color: #000000; font-family: Verdana, Arial, Helvetica, sans-serif; font-style: normal; font-variant: normal; font-weight: normal; letter-spacing: normal; line-height: normal; orphans: auto; text-align: start; text-indent: 0px; text-transform: none; white-space: normal; widows: auto; word-spacing: 0px; -webkit-text-stroke-width: 0px; font-size: 11.818181991577148px; background-color: #ffffff; display: inline !important; float: none;\">(.*?)</span>", "$1");
//        text = text.replaceAll("<span style=\"color: #000000; font-family: Verdana, Arial, Helvetica, sans-serif; font-size: 11.818181991577148px; font-style: normal; font-variant: normal; font-weight: normal; letter-spacing: normal; line-height: normal; orphans: auto; text-align: start; text-indent: 0px; text-transform: none; white-space: normal; widows: auto; word-spacing: 0px; -webkit-text-stroke-width: 0px; background-color: #ffffff; display: inline !important; float: none;\">(.*?)</span>", "$1");
//        text = text.replaceAll("<span class=\"Apple-converted-space\">(.*?)</span>", "$1");
//
//        return text;
        return text.replaceAll("<span.*?>(.*?)</span>", "$1").replaceAll("<span.*?>(.*?)</span>", "$1");
    }

    public static String convert(String html) {
        Document doc = Jsoup.parseBodyFragment(html);

        Element body = doc.body();

        return parse(body);
    }

    public static String parse(Node element) {
        List<Node> children = element.childNodes();
        StringBuffer buf = new StringBuffer();

        for (Node e : children) {
            String text = "";

            // if just text, we extract the text
            if (e instanceof TextNode) {
                text = ((TextNode)e).text();
            }

            // ignore comment, DocumentType and XmlDoclaration
            if (e instanceof Comment || element instanceof DocumentType || element instanceof XmlDeclaration) {
                continue;
            }

            // if it's an element, we need to parse it again
            if (e instanceof Element) {
                text = parse(e);

                // add tex markup for those "known" tags
                String tagName = ((Element) e).tagName();
                String format = "%s";
                if ("strong".equals(tagName)) {
                    format = "*%s*";
                } else if ("sup".equals(tagName)) {
                    format = "[`^{%s}`]";
                } else if ("sub".equals(tagName)) {
                    format = "[`_{%s}`]";
                } else if ("em".equals(tagName)) {
                    format = "_%s_";
                } else if ("p".equals(tagName)) {
                    format = "%s\n\n";
                }

                text = String.format(format, text);
            }

            buf.append(text);
        }

        return buf.toString();
    }

}
