package com.covide.crf.xmlfiles;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.dom4j.io.DOMReader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Test22 {
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
		//create document builder factory
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				
		//create Document Builder
		DocumentBuilder builder = factory.newDocumentBuilder();
		
		//get inputstrem of xml file
		//file from folder location
		String filePath = "D:\\caliculation.xml";
        File xmlFile = new File(filePath);
        //file from class path location
        ClassLoader cl = DOMReader.class.getClassLoader();
		InputStream is = cl.getResourceAsStream("com/covide/crf/xmlfiles/caliculation2.xml");
		
		//parse xml file and  get Document object
		//Document document = builder.parse(is);
		
		
		Document document = builder.parse(xmlFile);
		
		
		//get Root element of xml doc
		Element rootElement = document.getDocumentElement();
		
		//get <Eform> tag value
		Node first = rootElement.getFirstChild();
		Node sibling = first.getNextSibling();
		Node finalNode = sibling.getFirstChild();
		String value = finalNode.getNodeValue();
		
		System.out.println(value);
		 NodeList nodeList = document.getElementsByTagName("EleCaliculation");
         List<EleCaliculation> empList = new ArrayList<EleCaliculation>();
         for (int i = 0; i < nodeList.getLength(); i++) {
             empList.add(getEleCaliculation(nodeList.item(i)));
         }
         
       //lets print Employee list information
         for (EleCaliculation emp : empList) {
             System.out.println(emp.toString());
         }
		
	}
	
	private static EleCaliculation getEleCaliculation(Node node) {
        //XMLReaderDOM domReader = new XMLReaderDOM();
		EleCaliculation emp = new EleCaliculation();
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            emp.setResultField(getTagValue("resultField", element));
            emp.setRule(getTagValue("rule", element));
//            emp.setAge(Integer.parseInt(getTagValue("age", element)));
        }

        return emp;
    }
	
	private static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = (Node) nodeList.item(0);
        return node.getNodeValue();
    }
}
