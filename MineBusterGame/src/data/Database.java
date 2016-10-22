package data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;
import org.xml.sax.SAXException;

public class Database {

    private static File fXmlFile;
    private static Document doc;
    private static DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

    public static void init() {
        try {
            fXmlFile = new File("res/data/credentials.xml");
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void getXMLFile(String fileName) {
        try {
            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            NodeList nList = doc.getElementsByTagName("account");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    System.out.println("User Name : " + eElement.getElementsByTagName("username").item(0).getTextContent());
                    System.out.println("Password : " + eElement.getElementsByTagName("password").item(0).getTextContent());
                }
            }
        } catch (Exception e) {
        }
    }

    public static String getAccountInfo(String name) {
        Element node = (Element) getElementById(name);

        if (node == null) {
            return "";
        }

        String it = node.getElementsByTagName("iterations").item(0).getTextContent();
        String salt = node.getElementsByTagName("salt").item(0).getTextContent();
        String pass = node.getElementsByTagName("password").item(0).getTextContent();

        return it + ":" + salt + ":" + pass;
    }

    private static Node getElementById(String id) {
        XPath xpath = XPathFactory.newInstance().newXPath();
        try {
            return (Node) xpath.evaluate("//*[@id='" + id + "']", doc, XPathConstants.NODE);
        } catch (XPathExpressionException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        return doc.getDocumentElement();
    }

    private static void createAccountSave(String name) throws IOException {
        File save = new File("res/data/saves/" + name + ".json");

        if (!save.exists()) {
            save.createNewFile();
        }
    }
    
    public static void save(String name, String json) {
        File save = new File("res/data/saves/" + name + ".json");
        
        if(!save.exists()) {
            return;
        }
        
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(save));
            writer.write(json);
            writer.close();
        } catch (IOException ex) {
        } 
    }

    public static String getSave(String name) {
        File save = new File("res/data/saves/" + name + ".json");
        ArrayList<String> str = new ArrayList();
        String json = "";
        
        if(!save.exists()) {
            return json;
        }
        
        try {
            BufferedReader reader = new BufferedReader(new FileReader(save));
            String line;
            
            while((line = reader.readLine()) != null) {
                str.add(line);
            }
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        }
        
        for(String s : str) {
            json += s + "\n";
        }
        
        return json;
    }
    
    public static void addNewAccount(String name, String iteration, String salthash, String hash) {

        Node node = null;

        node = getElementById(name);

        if (node != null) {
            System.out.println("User '" + name + "' already exists.");
            return;
        }

        try {
            createAccountSave(name);
        } catch (IOException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }

        Element account = doc.createElement("account");
        Element username = doc.createElement("username");
        Element iterations = doc.createElement("iterations");
        Element salt = doc.createElement("salt");
        Element password = doc.createElement("password");

        username.setTextContent(name);
        iterations.setTextContent(iteration);
        salt.setTextContent(salthash);
        password.setTextContent(hash);

        account.setAttribute("id", name);
        account.appendChild(username);
        account.appendChild(iterations);
        account.appendChild(salt);
        account.appendChild(password);

        doc.getFirstChild().appendChild(account);
        removeEmptyText(doc);
        writeToFile(doc, fXmlFile);
    }

    private static void writeToFile(Document d, File f) {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(d);
            StreamResult result = new StreamResult(f);

            //format flie
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.transform(source, result);
        } catch (TransformerException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void removeEmptyText(Node node) {
        Node child = node.getFirstChild();
        while (child != null) {
            Node sibling = child.getNextSibling();
            if (child.getNodeType() == Node.TEXT_NODE) {
                if (child.getTextContent().trim().isEmpty()) {
                    node.removeChild(child);
                }
            } else {
                removeEmptyText(child);
            }
            child = sibling;
        }
    }
}
