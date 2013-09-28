package ncsu.ads541;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

class Project2_Del1 {

    static XMLStreamReader xmlr;
    static String filename;

    public static void main(String[] args) throws Exception {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        XMLInputFactory xmlif = XMLInputFactory.newInstance();

        xmlif.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES,
                Boolean.TRUE);
        xmlif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES,
                Boolean.FALSE);
        xmlif.setProperty(XMLInputFactory.IS_COALESCING, Boolean.FALSE);
        try {
            String input = br.readLine();
            while (!input.equals("end")) {
                String[] inputs = input.split(" ");
                if(filename != null) {
                    xmlr = xmlif.createXMLStreamReader(filename, new FileInputStream(
                            filename));
                }
                if (inputs[0].equals("Configuration")) {
                    filename = inputs[1];
                    xmlr = xmlif.createXMLStreamReader(filename, new FileInputStream(
                            filename));
                } else if (hasLoadedXML() && inputs.length == 1 && inputs[0].equals("DPDevice")) {
                    int deviceCount = getDPDeviceCount();
                    System.out.println(deviceCount);
                } else if(hasLoadedXML() && inputs.length == 3 && inputs[0].equals("DPDevice") 
                        && inputs[2].equals("DPDomain")) {
                    int domainCount = getDPDeviceDomains(inputs[1]);
                    System.out.println(domainCount);
                } else if(hasLoadedXML() && inputs.length == 5 && inputs[0].equals("DPDevice") 
                        && inputs[2].equals("DPDomain")
                        && inputs[4].equals("DeploymentPolicy")) {
                    int deploymentPolicyCount = getDeployPolicyCount(inputs[1], inputs[3]);
                    System.out.println(deploymentPolicyCount);
                } else if(hasLoadedXML() && inputs.length == 3 && inputs[0].equals("DeploymentPolicy") 
                        && inputs[2].equals("Serviceendpoint")) {
                    int servicePolicyCount = getServicePolicyCount(inputs[1]);
                    System.out.println(servicePolicyCount);
                } else if(hasLoadedXML() && inputs.length == 4 && inputs[0].equals("DeploymentPolicy") 
                        && inputs[2].equals("Serviceendpoint")) {
                    printServiceEndPointAttributes(inputs[1], inputs[3]);
                } else {
                    System.out.println("Invalid command. Did you load the configuration file?");
                }
                input = br.readLine();
            }
            xmlr.close();

        } catch (XMLStreamException ex) {
            System.out.println("Error opening XML file!");
            xmlr.close();
        } catch (IOException ioe) {
            System.out.println("IO error trying to read command!");
            System.exit(1);
            xmlr.close();
        }
    }

    private static boolean hasLoadedXML() {
        return !(xmlr == null);
    }
    
    private static int getDPDeviceCount() throws XMLStreamException {
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

    private static int getDPDeviceDomains(String DPDevice)
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

    public static int getDeployPolicyCount(String device_id, String domain_id)
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

    public static int getServicePolicyCount(String policy_id)
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

    public static void printServiceEndPointAttributes(String DPPolicy,
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