/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kalemsj.testapp.xml.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import static kalemsj.testapp.utils.Utils.getReportDate;
import kalemsj.testapp.xml.wrappers.FirstEntries;
import kalemsj.testapp.xml.wrappers.FirstEntry;
import kalemsj.testapp.xml.wrappers.SecondEntries;

/**
 *
 * @author Karagaev S.A.
 */
public class XmlService {

    private static Logger log = Logger.getLogger(XmlService.class.getName());

    /**
     *
     * @param entriesList
     * @param firstPath
     */
    public static void createFirstXml(List<FirstEntry> entriesList, Path firstPath) {
        try {
            Files.deleteIfExists(firstPath);
        } catch (IOException ex) {
            log.log(Level.SEVERE, null, ex);
        }
        try {
            Files.createFile(firstPath);
            JAXBContext jaxbContext = JAXBContext.newInstance(FirstEntries.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            FirstEntries en = new FirstEntries((ArrayList<FirstEntry>) entriesList);
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            try (OutputStream os = new FileOutputStream(firstPath.toFile())) {
                jaxbMarshaller.marshal(en, firstPath.toFile());
                log.log(Level.FINE, "First xml file 1.xml is ready. Current time: {0}", getReportDate());
            }
        } catch (JAXBException | FileNotFoundException ex) {
            log.log(Level.SEVERE, "Can not create first xml file, ", ex);
        } catch (IOException ex) {
            log.log(Level.SEVERE, null, ex);
        }

    }

    public static void transformToSecondXml(Path firstPath, Path secondPath) {
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            Files.deleteIfExists(secondPath);
        } catch (IOException ex) {
            log.log(Level.SEVERE, null, ex);
        }

        File file = new File("resources//xslt_template.xml");
        try {
            Source xslt = new StreamSource(file);
            transformer = factory.newTransformer(xslt);
            Source source = new StreamSource(firstPath.toFile());
            transformer.transform(source, new StreamResult(secondPath.toFile()));
            log.log(Level.FINE, "Second xml file 2.xml is ready. Current time: {0}", getReportDate());
        } catch (TransformerException e) {
            log.log(Level.SEVERE, "Can not transform to second xml file", e);
        }
    }

    public static SecondEntries unmarshallSecondXml(Path secondPath) {
        SecondEntries secondEntries = null;
        try {
            JAXBContext jc = JAXBContext.newInstance(SecondEntries.class);
            Unmarshaller unm = jc.createUnmarshaller();
            secondEntries = (SecondEntries) unm.unmarshal(secondPath.toFile());
            log.log(Level.FINE, "Second xml file 2.xml is read. Current time: {0}", getReportDate());
        } catch (JAXBException e) {
            log.log(Level.SEVERE, "Can not read second xml file", e);
        }
        return secondEntries;
    }

}
