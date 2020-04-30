package eg.edu.alexu.csd.filestructure.btree;

import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class Parser {
     static class Document {
        private final List<String> words;
        private final String id;

        public Document(List<String> words, String id) {
            this.words = words;
            this.id = id;
        }

        public List<String> getWords() {
            return words;
        }

        public String getId() {
            return id;
        }
    }

    public static List<Document> parseFile(String path) throws ParserConfigurationException, IOException, SAXException {
        File inputFile = new File(path);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        org.w3c.dom.Document doc = dBuilder.parse(inputFile);
        doc.getDocumentElement().normalize();
        NodeList docs = doc.getElementsByTagName("doc");
        List<Document> resultDocuments = new ArrayList<>();
        for (int i = 0; i < docs.getLength(); i++) {
            String str = docs.item(i).getTextContent();
            String id = docs.item(i).getAttributes().getNamedItem("id").getNodeValue();
            List<String> words = new ArrayList<>(Arrays.asList(str.replaceAll("\\s+", " ").toLowerCase().split(" ")));
            str = docs.item(i).getAttributes().getNamedItem("title").getNodeValue();
            words.addAll(Arrays.asList(str.replaceAll("\\s+", " ").toLowerCase().split(" ")));
            resultDocuments.add(new Document(words, id));
        }
        return resultDocuments;
    }
}
