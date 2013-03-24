package in.dobro;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

public class MainCreateIndexes extends SuperParser {

	public MainCreateIndexes(String langfrommain) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
		super(langfrommain);
	}


	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
		new MainCreateIndexes("th");
	}

}
