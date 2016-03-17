package com.login.verification.client;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;


public class SaaJSoapClient {

	private static SOAPMessage createSOAPRequest(String username, String password) throws Exception {
		MessageFactory messageFactory = MessageFactory.newInstance();
		SOAPMessage soapMessage = messageFactory.createMessage();
		SOAPPart soapPart = soapMessage.getSOAPPart();
		SOAPEnvelope envelope = soapPart.getEnvelope();
		envelope.addNamespaceDeclaration("soap", "http://service.authenticate.com/AuthenticationService/");

		// SOAP Body
		SOAPBody soapBody = envelope.getBody();
		SOAPElement soapBodyElem = soapBody.addChildElement("validate", "soap");
		SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("password", "soap");
		soapBodyElem1.addTextNode(password);
		soapBodyElem1 = soapBodyElem.addChildElement("username", "soap");
		soapBodyElem1.addTextNode(username);

		soapMessage.saveChanges();

		// Check the input
		System.out.println("Request SOAP Message = ");
		soapMessage.writeTo(System.out);
		return soapMessage;

	}

	private static String printSOAPResponse(SOAPMessage soapResponse) throws Exception {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		Source sourceContent = soapResponse.getSOAPPart().getContent();
		System.out.println("\nResponse SOAP Message = ");
		StreamResult result = new StreamResult(System.out);
		// transformer.transform(sourceContent, result);
		StringWriter writer = new StringWriter();
		transformer.transform(sourceContent, new StreamResult(writer));
		String output = writer.toString();
	   System.out.println(output);

		// PARSING XML

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document document = db.parse( new InputSource(new StringReader(output) )  );
		prettyPrint(document);
		Node node = ((Document) document).getFirstChild().getFirstChild().getFirstChild().getFirstChild();
		Node nodex = ((Document) document).getFirstChild();
		while(nodex.hasChildNodes()){
			System.out.println("<" + nodex.getNodeName() + ">" );
			nodex = nodex.getFirstChild();
			System.out.println();
			
		}
		System.out.println("NODE NAME = " + node.getNodeName());
		//String parseVal = node.getAttributes().getNamedItem("validateResponse").getNodeValue();
		String parseVal = node.getTextContent();
		System.out.println("PARSE VAL = " + parseVal);
		return parseVal;
		
	}

	private static void prettyPrint(Document document) {

		/*//Node node = ((Document) document).getFirstChild();
		Node nodex = ((Document) document).getFirstChild();
		while(nodex.hasChildNodes()){
			System.out.println("<" + nodex.getNodeName() + ">" );
			//System.out.println(nodex.getTextContent() + "</" + nodex.getNodeName() + ">"  );
			NodeList nodex1 = nodex.getChildNodes();
			while(nodex1.item(0).)
			System.out.println();*/
		System.out.println("PRETTY PRINT ------- >");
		OutputFormat format = new OutputFormat(document);
        format.setLineWidth(65);
        format.setIndenting(true);
        format.setIndent(2);
        Writer out = new StringWriter();
        XMLSerializer serializer = new XMLSerializer(out, format);
        try {
			serializer.serialize(document);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        System.out.println(out.toString());
		
		
		
		
		}
		
		
	
	public String authentication(String username, String password) {
		String responseString = "";

		try {
			// Create SOAP Connection
			SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
			SOAPConnection soapConnection = soapConnectionFactory.createConnection();

			// Send SOAP Message to SOAP Server
			String url = "http://localhost:8085/PasswordService/services/AuthenticationServiceSOAP?wsdl";
			SOAPMessage soapResponse = soapConnection.call(createSOAPRequest(username, password), url);
			// Process the SOAP Response
			
			responseString = printSOAPResponse(soapResponse);
			//responseString = (String) soapResponse.getProperty("out");
			soapConnection.close();
		} catch (Exception e) {
			System.err.println("Error occurred while sending SOAP Request to Server");
			e.printStackTrace();
		}

		return responseString;

	}

	/*public static void main(String args[]) {

	}*/

}
