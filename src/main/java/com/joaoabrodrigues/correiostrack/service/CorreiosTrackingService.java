package com.joaoabrodrigues.correiostrack.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import javax.xml.soap.*;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;

@Service
public class CorreiosTrackingService {

    @Value("${correios.url.service}")
    private String url;

    private static final String XML_ENVELOPE = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:res=\"http://resource.webservice.correios.com.br/\">"
            + "   <soapenv:Header/>" + "   <soapenv:Body>" + "      <res:buscaEventos>" + "         <usuario>ECT</usuario>" + "         <senha>SRO</senha>"
            + "         <tipo>L</tipo>" + "         <resultado>T</resultado>" + "         <lingua>101</lingua>" + "         <objetos> %s </objetos>"
            + "      </res:buscaEventos>" + "   </soapenv:Body>" + "</soapenv:Envelope>";

    public String trackObject(String object) throws IOException, SOAPException {
        String envelope = String.format(XML_ENVELOPE, object);

        SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
        SOAPConnection soapConnection = soapConnectionFactory.createConnection();

        MimeHeaders headers = new MimeHeaders();
        headers.addHeader("Content-Type", MediaType.TEXT_XML_VALUE);

        MessageFactory messageFactory = MessageFactory.newInstance();

        SOAPMessage msg = messageFactory.createMessage(headers, (new ByteArrayInputStream(envelope.getBytes())));

        SOAPMessage soapResponse = soapConnection.call(msg, url);
        Document responseXML = soapResponse.getSOAPBody().getOwnerDocument();
        return parseXmlToString(responseXML);
    }

    private static String parseXmlToString(Document xml) {
        try {
            TransformerFactory factory = TransformerFactory.newInstance();
            factory.setAttribute("indent-number", 4);
            Transformer trans = factory.newTransformer();
            trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            trans.setOutputProperty(OutputKeys.INDENT, "yes");

            StringWriter sw = new StringWriter();
            StreamResult result = new StreamResult(sw);
            DOMSource source = new DOMSource(xml);
            trans.transform(source, result);
            String xmlString = sw.toString();
            return xmlString.substring(xmlString.indexOf("<objeto>"), xmlString.indexOf("</objeto>") + 10);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        return null;
    }
}