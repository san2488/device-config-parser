package ncsu.ads541;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

class Project2_Del1 {

    public static void main(String[] args) throws Exception {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        OptimizedDeviceConfigParser odcf = new OptimizedDeviceConfigParser();
        DeviceConfigParser dcf = new DeviceConfigParser();
        
        
        long startTime, endTime;
        
        try {
            String command = br.readLine();
            
            while (!command.equals("end")) {
                String[] inputs = command.split(" ");
                if (inputs[0].equals("Configuration")) {
                    String filename = inputs[1];
                    odcf.loadXML(filename);
                    dcf.loadXML(filename);
                } else if (odcf.hasLoadedXML() && inputs.length == 1 && inputs[0].equals("DPDevice")) {
                    int deviceCount = 0;
                    
                    startTime = System.nanoTime();
                    deviceCount = odcf.getDPDeviceCount();
                    endTime = System.nanoTime();
                    System.out.println("Optimized Time: " + (endTime - startTime));
                    System.out.println(deviceCount);
                    
                    startTime = System.nanoTime();
                    deviceCount = dcf.getDPDeviceCount();
                    endTime = System.nanoTime();
                    System.out.println("Non-optimized Time: " + (endTime - startTime));
                    System.out.println(deviceCount);
                } else if(odcf.hasLoadedXML() && inputs.length == 3 && inputs[0].equals("DPDevice") 
                        && inputs[2].equals("DPDomain")) {
                    int domainCount = 0;
                    
                    startTime = System.nanoTime();
                    domainCount = odcf.getDPDeviceDomains(inputs[1]);
                    endTime = System.nanoTime();
                    System.out.println("Optimized Time: " + (endTime - startTime));
                    System.out.println(domainCount);
                    
                    startTime = System.nanoTime();
                    domainCount = dcf.getDPDeviceDomains(inputs[1]);
                    endTime = System.nanoTime();
                    System.out.println("Non-optimized Time: " + (endTime - startTime));
                    System.out.println(domainCount);
                    
                } else if(odcf.hasLoadedXML() && inputs.length == 5 && inputs[0].equals("DPDevice") 
                        && inputs[2].equals("DPDomain")
                        && inputs[4].equals("DeploymentPolicy")) {
                    int deploymentPolicyCount = odcf.getDeployPolicyCount(inputs[1], inputs[3]);
                    System.out.println(deploymentPolicyCount);
                } else if(odcf.hasLoadedXML() && inputs.length == 3 && inputs[0].equals("DeploymentPolicy") 
                        && inputs[2].equals("Serviceendpoint")) {
                    int servicePolicyCount = odcf.getServicePolicyCount(inputs[1]);
                    System.out.println(servicePolicyCount);
                } else if(odcf.hasLoadedXML() && inputs.length == 4 && inputs[0].equals("DeploymentPolicy") 
                        && inputs[2].equals("Serviceendpoint")) {
                    odcf.printServiceEndPointAttributes(inputs[1], inputs[3]);
                } else {
                    System.out.println("Invalid command. Did you load the configuration file?");
                }
                
                command = br.readLine();  
                odcf.reset(); 
                dcf.reset();
            }
        } catch (IOException ioe) {
            System.out.println("IO error trying to read command!");
            System.exit(1);
        }
    }

    
}