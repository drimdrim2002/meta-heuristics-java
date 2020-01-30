package common;

import domain.CloudBalance;
import domain.CloudComputer;
import domain.CloudProcess;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class XmlReader {

    public static CloudBalance createSolution() {
        try {

            String fileName = EngineConfig.FILE_PATH + EngineConfig.FILE_NAME;
            File fXmlFile = new File(fileName);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            //optional, but recommended
            //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();

            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

            NodeList cloudComputerNodeList = doc.getElementsByTagName("CloudComputer");
            System.out.println("----------------------------");

            List<CloudComputer> cloudComputerList = new ArrayList<CloudComputer>();
            for (int temp = 0; temp < cloudComputerNodeList.getLength(); temp++) {
                Node cloudComputerNode = cloudComputerNodeList.item(temp);
                System.out.println("Current Element :" + cloudComputerNode.getNodeName());
                if (cloudComputerNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) cloudComputerNode;
                    System.out.println("id : " + eElement.getElementsByTagName("id").item(0).getTextContent());
                    System.out.println("cpu Power : " + eElement.getElementsByTagName("cpuPower").item(0).getTextContent());
                    System.out.println("memory : " + eElement.getElementsByTagName("memory").item(0).getTextContent());
                    System.out.println("networkBandwidth  : " + eElement.getElementsByTagName("networkBandwidth").item(0).getTextContent());
                    System.out.println("cost : " + eElement.getElementsByTagName("cost").item(0).getTextContent());

                    int id =  Integer.parseInt(eElement.getElementsByTagName("id").item(0).getTextContent());
                    int cpuPower =  Integer.parseInt(eElement.getElementsByTagName("cpuPower").item(0).getTextContent());
                    int memory =  Integer.parseInt(eElement.getElementsByTagName("memory").item(0).getTextContent());
                    int networkBandwidth =  Integer.parseInt(eElement.getElementsByTagName("networkBandwidth").item(0).getTextContent());
                    int cost =  Integer.parseInt(eElement.getElementsByTagName("cost").item(0).getTextContent());

                    CloudComputer cloudComputer = new CloudComputer(id, cpuPower, memory, networkBandwidth, cost);
                    cloudComputerList.add(cloudComputer);
                }
            }

            NodeList cloudProcessNodeList = doc.getElementsByTagName("CloudProcess");
            List<CloudProcess> cloudProcessList = new ArrayList<CloudProcess>();
            System.out.println("----------------------------");
            for (int temp = 0; temp < cloudProcessNodeList.getLength(); temp++) {
                Node cloudProcessNode = cloudProcessNodeList.item(temp);
                System.out.println("Current Element :" + cloudProcessNode.getNodeName());
                if (cloudProcessNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) cloudProcessNode;
                    System.out.println("id : " + eElement.getElementsByTagName("id").item(0).getTextContent());
                    System.out.println("requiredCpuPower : " + eElement.getElementsByTagName("requiredCpuPower").item(0).getTextContent());
                    System.out.println("requiredMemory : " + eElement.getElementsByTagName("requiredMemory").item(0).getTextContent());
                    System.out.println("requiredNetworkBandwidth  : " + eElement.getElementsByTagName("requiredNetworkBandwidth").item(0).getTextContent());



                    int id =  Integer.parseInt(eElement.getElementsByTagName("id").item(0).getTextContent());
                    int cpuPower =  Integer.parseInt(eElement.getElementsByTagName("requiredCpuPower").item(0).getTextContent());
                    int memory =  Integer.parseInt(eElement.getElementsByTagName("requiredMemory").item(0).getTextContent());
                    int networkBandwidth =  Integer.parseInt(eElement.getElementsByTagName("requiredNetworkBandwidth").item(0).getTextContent());

                    CloudProcess cloudProcess = new CloudProcess(id,cpuPower, memory, networkBandwidth);
                    cloudProcessList.add(cloudProcess);
                }
            }

            CloudBalance cloudBalance = new CloudBalance(1, cloudComputerList, cloudProcessList);
            return cloudBalance;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  null;
    }
}
