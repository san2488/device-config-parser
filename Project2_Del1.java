package ncsu.ads541;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

class Project2_Del1 {

    public static void main(String[] args) throws Exception {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        DeviceConfigParser dcf = new DeviceConfigParser();
        
        try {
            String command = br.readLine();
            
            while (!command.equals("end")) {
                String[] inputs = command.split(" ");
                if (inputs[0].equals("Configuration")) {
                    String filename = inputs[1];
                    dcf.loadXML(filename);
                } else if (dcf.hasLoadedXML() && inputs.length == 1 && inputs[0].equals("DPDevice")) {
                    int deviceCount = dcf.getDPDeviceCount();
                    System.out.println(deviceCount);
                } else if(dcf.hasLoadedXML() && inputs.length == 3 && inputs[0].equals("DPDevice") 
                        && inputs[2].equals("DPDomain")) {
                    int domainCount = dcf.getDPDeviceDomains(inputs[1]);
                    System.out.println(domainCount);
                } else if(dcf.hasLoadedXML() && inputs.length == 5 && inputs[0].equals("DPDevice") 
                        && inputs[2].equals("DPDomain")
                        && inputs[4].equals("DeploymentPolicy")) {
                    int deploymentPolicyCount = dcf.getDeployPolicyCount(inputs[1], inputs[3]);
                    System.out.println(deploymentPolicyCount);
                } else if(dcf.hasLoadedXML() && inputs.length == 3 && inputs[0].equals("DeploymentPolicy") 
                        && inputs[2].equals("Serviceendpoint")) {
                    int servicePolicyCount = dcf.getServicePolicyCount(inputs[1]);
                    System.out.println(servicePolicyCount);
                } else if(dcf.hasLoadedXML() && inputs.length == 4 && inputs[0].equals("DeploymentPolicy") 
                        && inputs[2].equals("Serviceendpoint")) {
                    dcf.printServiceEndPointAttributes(inputs[1], inputs[3]);
                } else {
                    System.out.println("Invalid command. Did you load the configuration file?");
                }
                if(dcf.hasLoadedXML()) {
                    dcf.reset();
                }
                
                command = br.readLine();
            }
        } catch (IOException ioe) {
            System.out.println("IO error trying to read command!");
            System.exit(1);
        }
    }

    
}