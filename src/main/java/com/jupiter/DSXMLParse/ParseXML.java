package com.jupiter.DSXMLParse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.xpath.XPathAPI;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class ParseXML {
	//获取文件名称
	private static File getFile(String cFileName) {
		File tFile = new File(cFileName);
		if (!tFile.exists()) {
			return null;
		}
		return tFile;
	}
	//开始解析
	private static String parseXML(File cFile) throws Exception {
	      InputSource in = new InputSource(new FileInputStream(cFile));
	      DocumentBuilderFactory dfactory = DocumentBuilderFactory.newInstance();
	      dfactory.setNamespaceAware(true);
	      Document doc = dfactory.newDocumentBuilder().parse(in);
	      Element ele = doc.getDocumentElement();
	      //得到jobList
	      NodeList jobList = XPathAPI.selectNodeList(ele, "/DSExport/Job");
	      for (int i=0; i<jobList.getLength(); i++) {
	        Node tJobNode = jobList.item(i);
	        String jobName = tJobNode.getAttributes().getNamedItem("Identifier").getNodeValue();
	        System.out.println("===>JobName="+jobName);
	        NodeList tRecordList = tJobNode.getChildNodes();
	        for (int j=0; j<tRecordList.getLength(); j++) {
	        	Node tRecordNode = tRecordList.item(j);
	        	NamedNodeMap tAttr = tRecordNode.getAttributes();
	        	if(tAttr==null) continue;
	        	Node tRecordtNodeIdentifier = tAttr.getNamedItem("Identifier");
	        	if(tRecordtNodeIdentifier==null) continue;
		        String tRecordName = tRecordtNodeIdentifier.getNodeValue();
		        
	        	Node tRecordtNodeType = tAttr.getNamedItem("Type");
	        	if(tRecordtNodeType==null) continue;
		        String tRecordType = tRecordtNodeType.getNodeValue();
		        
		        if("V0S0P1".equals(tRecordName) && ("CustomOutput".equals(tRecordType) || "CustomInput".equals(tRecordType))) {
		        	NodeList tCollectionList = tRecordNode.getChildNodes();	  
		            for (int x=0; x<tCollectionList.getLength(); x++) {
		            	Node tCollectionNode = tCollectionList.item(x);
		            	String NodeName = tCollectionNode.getNodeName();
		            	if("Collection".equals(NodeName)) {
				        	NodeList tSubRecordList = tCollectionNode.getChildNodes();	  
				            for (int k=0; k<tSubRecordList.getLength(); k++) {
				      	        	Node tSubRecordNode = tSubRecordList.item(k);
				      				NodeList tPropertyList = tSubRecordNode.getChildNodes();	  
				      				for (int l=0; l<tPropertyList.getLength(); l++) {
					      	        	Node tPropertyNode = tPropertyList.item(l);
					    	        	NamedNodeMap tAttr2 = tPropertyNode.getAttributes();
					    	        	if(tAttr2==null) continue;
					    	        	Node tPropertyNameNode = tAttr2.getNamedItem("Name");
					    	        	if(tPropertyNameNode==null) continue;
					    	        	Node tFirstNode = tPropertyNode.getFirstChild();
					    	        	if(tFirstNode==null) continue;
					    		        String tPropertyNameNodeValue = tFirstNode.getNodeValue();
					    		        getValue(tPropertyNameNodeValue, tPropertyList);
				      	        }
				      	    }
		            	}
		            }
		        }
	        }
	      }
	      
		return null;
		
	}

	private static String getValue(String cPropertyNameNodeValue, NodeList cPropertyList) {
		if(!"BEFORESQL".equals(cPropertyNameNodeValue) && !"TARGETTABLE".equals(cPropertyNameNodeValue) && !"BEFORESQL".equals(cPropertyNameNodeValue) 
				&& !"USERSQL".equals(cPropertyNameNodeValue) && !"AFTERSQL".equals(cPropertyNameNodeValue)) {
			System.out.println("======>PropertyName="+cPropertyNameNodeValue);
		}
		
		String tPropertyNodeValue = null;
			for (int p=0; p<cPropertyList.getLength(); p++) {
				Node tPropertyNode = cPropertyList.item(p);
	        	NamedNodeMap tAttrMap = tPropertyNode.getAttributes();
	        	if(tAttrMap==null) continue;
	        	Node tPropertyNodeTmp = tAttrMap.getNamedItem("Name");
	        	if(tPropertyNodeTmp==null) continue;
	        	String tValueStr = tPropertyNodeTmp.getNodeValue();
	        	if("Value".equals(tValueStr)) {
	        		tPropertyNodeValue = tPropertyNode.getFirstChild().getNodeValue();
	        		System.out.println("======>PropertValue="+tPropertyNodeValue);
	        		break;
	        	}
			}
		return tPropertyNodeValue;
	}
	
	public static void main(String[] args) throws Exception {
		String tFileName= "D:\\jupiter\\workplace\\012.dsjob_analysis\\1.business\\4.ddbkpf\\jb_dwn_covrpf_to_covrpf_ins.xml";
		File tFile= getFile(tFileName);
		if(tFile==null) {
			System.out.println("找不到文件："+tFileName);
			return;
		}
		parseXML(tFile); 
		
	}

}
