/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ncsu.ads541;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 *
 * @author SAN
 */
public class DeviceConfigParser {
    
    
    private XMLStreamReader xmlr;
    private String filename;
    
    public XMLStreamReader loadXML(String filename) {
        this.filename = filename;
        
        XMLInputFactory xmlif = XMLInputFactory.newInstance();

        xmlif.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES,
                Boolean.TRUE);
        xmlif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES,
                Boolean.FALSE);
        xmlif.setProperty(XMLInputFactory.IS_COALESCING, Boolean.FALSE);
        
        try {
            
            if(filename == null) {
                System.out.println("XML file not specified. Run Configuration <file_name>");
                return null;
            }
            
            if(xmlr != null) {
                xmlr.close();
            }
            
            xmlr = xmlif.createXMLStreamReader(filename, new FileInputStream(filename));
            
        } catch (XMLStreamException ex) {
            System.out.println("XML could not be parsed");
        } catch (FileNotFoundException ex) {
            System.out.println("File could not be loaded");
        }
        return xmlr;
    }
    
    public void reset() {
        XMLInputFactory xmlif = XMLInputFactory.newInstance();

        xmlif.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES,
                Boolean.TRUE);
        xmlif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES,
                Boolean.FALSE);
        xmlif.setProperty(XMLInputFactory.IS_COALESCING, Boolean.FALSE);
        
        try {
            
            if(filename == null) {
                System.out.println("XML file not specified. Run Configuration <file_name>");
                return;
            }
            
            if(xmlr != null) {
                xmlr.close();
            }
            
            xmlr = xmlif.createXMLStreamReader(filename, new FileInputStream(filename));
        } catch (XMLStreamException ex) {
            System.out.println("XML could not be parsed");
        } catch (FileNotFoundException ex) {
            System.out.println("File could not be loaded");
        }
    }
  
    public boolean hasLoadedXML() {
        return !(xmlr == null);
    }
    
    public int getDPDeviceCount() throws XMLStreamException {
        int count = 0;
        while (xmlr.hasNext()) {
            xmlr.next();
            if (xmlr.isStartElement()
                    && xmlr.getName().toString().equals("devices")) {
                count++;
            }
        }
        return count;
    }

    public int getDPDeviceDomains(String DPDevice)
            throws XMLStreamException {
        boolean isReqDPDevice = false;
        int count = 0;
        while (xmlr.hasNext()) {
            xmlr.next();
            if (xmlr.isStartElement()
                    && xmlr.getName().toString().equals("devices")
                    && xmlr.getAttributeValue(xmlr.getNamespaceURI(), "id")
                    .equals(DPDevice)) {
                count = 0;
                isReqDPDevice = true;
            } else if (isReqDPDevice && xmlr.isEndElement()
                    && xmlr.getName().toString().equals("devices")) {
                break;
            } else if (isReqDPDevice && xmlr.isStartElement()
                    && xmlr.getName().toString().equals("domains")) {
                count++;
            }
        }

        return count;
    }

    public int getDeployPolicyCount(String device_id, String domain_id)
            throws XMLStreamException {
        boolean isReqDPDevice = false, isReqDPDomain = false;
        int count = 0;
        while (xmlr.hasNext()) {
            xmlr.next();
            if (xmlr.isStartElement()
                    && xmlr.getName().toString().equals("devices")
                    && xmlr.getAttributeValue(xmlr.getNamespaceURI(), "id")
                    .equals(device_id)) {
                isReqDPDevice = true;

            } else if (isReqDPDevice
                    && xmlr.isStartElement()
                    && xmlr.getName().toString().equals("domains")
                    && xmlr.getAttributeValue(xmlr.getNamespaceURI(), "id")
                    .equals(domain_id)) {
                isReqDPDomain = true;
                count = 0;
            } else if (isReqDPDomain && isReqDPDevice && xmlr.isEndElement()
                    && xmlr.getName().toString().equals("domains")) {
                break;
            } else if (isReqDPDevice && isReqDPDomain && xmlr.isStartElement()
                    && xmlr.getName().toString().equals("deploymentPolicy")) {
                count++;
            }
        }

        return count;
    }

    public int getServicePolicyCount(String policy_id)
            throws XMLStreamException {
        boolean isReqDPDevice = false;
        int count = 0;
        while (xmlr.hasNext()) {
            xmlr.next();
            if (xmlr.isStartElement()
                    && xmlr.getName().toString().equals("deploymentPolicy")
                    && xmlr.getAttributeValue(xmlr.getNamespaceURI(), "id")
                    .equals(policy_id)) {
                count = 0;
                isReqDPDevice = true;
            } else if (isReqDPDevice && xmlr.isEndElement()
                    && xmlr.getName().toString().equals("deploymentPolicy")) {
                break;
            } else if (isReqDPDevice && xmlr.isStartElement()
                    && xmlr.getName().toString().equals("serviceend-point")) {
                count++;
            }
        }

        return count;

    }

    public void printServiceEndPointAttributes(String DPPolicy,
            String ServiceEndPoint) throws XMLStreamException {
        boolean isReqDPDevice = false, isReqDPDomain = false;

        while (xmlr.hasNext()) {
            xmlr.next();
            if (xmlr.isStartElement()
                    && xmlr.getName().toString().equals("deploymentPolicy")
                    && xmlr.getAttributeValue(xmlr.getNamespaceURI(), "id")
                    .equals(DPPolicy)) {
                isReqDPDevice = true;

            } else if (isReqDPDevice
                    && xmlr.isStartElement()
                    && xmlr.getName().toString().equals("serviceend-point")
                    && xmlr.getAttributeValue(xmlr.getNamespaceURI(), "id")
                    .equals(ServiceEndPoint)) {
                isReqDPDomain = true;
                int count = xmlr.getAttributeCount();

                for (int i = 0; i < count; i++) {
                    System.out.println(xmlr.getAttributeName(i).getLocalPart()
                            + ": " + xmlr.getAttributeValue(i).toString());
                }
            } else if (isReqDPDomain && isReqDPDevice && xmlr.isEndElement()
                    && xmlr.getName().toString().equals("serviceend-point")) {
                break;
            }

        }

    }
    
    
}
