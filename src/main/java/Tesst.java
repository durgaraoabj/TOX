import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Tesst {
	public static void main(String[] args) {
		String filePath = "D:\\caliculation.xml";
        File xmlFile = new File(filePath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try {
        	dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            NodeList nodeList = doc.getElementsByTagName("EleCaliculation");
            List<EleCaliculation> empList = new ArrayList<EleCaliculation>();
            for (int i = 0; i < nodeList.getLength(); i++) {
                empList.add(getEleCaliculation(nodeList.item(i)));
            }
            
          //lets print Employee list information
            for (EleCaliculation emp : empList) {
                System.out.println(emp.toString());
            }
        }catch (Exception e) {
			// TODO: handle exception
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
