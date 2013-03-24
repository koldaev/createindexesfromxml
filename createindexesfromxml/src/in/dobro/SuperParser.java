package in.dobro;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.lucene.analysis.ThaiAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class SuperParser {
	
	private String langxml = null;
	private File dirindexes = null;

	String bible;
	String chapter;
	String poem;
	String poemtext;
	
	public SuperParser(String lang) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
		langxml = lang;
		parserxml();
	}

	private void parserxml() throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {

        dirindexes = new File(langxml+"_indexes");
        dirindexes.mkdir();
        
        Directory index = FSDirectory.open(dirindexes); 
        ThaiAnalyzer  analyzer = new ThaiAnalyzer(Version.LUCENE_35);
        //StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_35);
		
        //создаем индекс на диске
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_35, analyzer);
        IndexWriter writer = new IndexWriter(index, config);

       	//String biblenamefield = "biblename";
       	String biblefield = "bible";
       	String chapterfield = "chapter";
       	String poemfield = "poem";
       	String poemtextfield = "poemtext";

        
		InputStream is = getClass().getResourceAsStream("resources/"+langxml+"bible.xml");
		
		DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
	    domFactory.setNamespaceAware(true); // never forget this!
	    DocumentBuilder builder = domFactory.newDocumentBuilder();
	    
	    org.w3c.dom.Document doc = builder.parse(is);
		
	    XPathFactory factory = XPathFactory.newInstance();
	    XPath xpath = factory.newXPath();
	    
	    for (int i = 1; i < 67; i++) {
	        
	    	String forxpath = "//booktext["+i+"]/chapter/verse";
	    	
	    	XPathExpression expr = xpath.compile(forxpath);
	    	
	    	Object result = expr.evaluate(doc, XPathConstants.NODESET);
	        NodeList nodes = (NodeList) result;
	        
	        for (int iglav = 0; iglav < nodes.getLength(); iglav++) {
	        	String[] bookglav = null;
	        	Document document = new Document();
	        	
	        	String str = nodes.item(iglav).getAttributes().item(0).getTextContent();
	        	bookglav = str.split("\\.");
	        	
	        	bible = i+"";
    			chapter = bookglav[1]+"";
    			poem = bookglav[2]+"";
    			poemtext = nodes.item(iglav).getTextContent();

	        	System.out.println(bible + "/" + chapter + "/" + poem + "/"  + poemtext);
	        	
	        	document.add(new Field(biblefield, bible, Field.Store.YES, Field.Index.ANALYZED));
    			document.add(new Field(chapterfield, chapter, Field.Store.YES, Field.Index.ANALYZED));
    			document.add(new Field(poemfield, poem, Field.Store.YES, Field.Index.ANALYZED));
    			document.add(new Field(poemtextfield, poemtext, Field.Store.YES, Field.Index.ANALYZED));
    			
    			writer.addDocument(document);
	        
	        }

	    	
	    }
	    
	    
	    writer.close();
        is.close();
	    
	}
	
}
