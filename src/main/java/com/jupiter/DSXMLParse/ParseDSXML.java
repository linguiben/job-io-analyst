package com.jupiter.DSXMLParse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

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

import com.jupiter.WriteDSParse;

public class ParseDSXML {

	//int JobID = 0;
	//private int RecordID = 0;
	//private int PropertyID = 0;
	//private int CollectionID = 0;
	//private int SubRecordID = 0;

	private ArrayList<Job> jobs = new ArrayList<Job>();
	private ArrayList<Record> records= new ArrayList<Record>();
	private ArrayList<Property> propertys = new ArrayList<Property>();
	private ArrayList<Collection> collections = new ArrayList<Collection>();
	private ArrayList<SubRecord> subRecords = new ArrayList<SubRecord>();

	// 获取文件名称
	private File getFile(String cFileName) {
		File tFile = new File(cFileName);
		if (!tFile.exists()) {
			System.out.println("找不到文件：" + cFileName);
			System.exit(0);
		}
		return tFile;
	}

	// 开始解析
	private String parseXML(File cFile) throws Exception {
		InputSource in = new InputSource(new FileInputStream(cFile));
		DocumentBuilderFactory dfactory = DocumentBuilderFactory.newInstance();
		dfactory.setNamespaceAware(true);
		Document doc = dfactory.newDocumentBuilder().parse(in);
		Element ele = doc.getDocumentElement();

		//DSexport
		NodeList nl = XPathAPI.selectNodeList(ele, "/DSExport");
		for (int i = 0; i < nl.getLength(); i++) {
			Node tN = nl.item(i);
			System.out.println("i:" + i + " " + tN.getNodeName());

			// Job
			NodeList ml = tN.getChildNodes();
			int JobID = 0;
			for (int j = 0; j < ml.getLength(); j++) {
				Node tJobNode = ml.item(j);
				NamedNodeMap tjbM = tJobNode.getAttributes();
				if (!tJobNode.getNodeName().equals("Job"))
					continue;
				JobID++;
				System.out.println("j:" + JobID + " " + tJobNode.getNodeName());
				//if (tjbM == null) continue;
				Job job = new Job(JobID, tjbM.getNamedItem("Identifier").getNodeValue(), tjbM.getNamedItem("DateModified").getNodeValue(), tjbM.getNamedItem(
						"TimeModified").getNodeValue());
				jobs.add(job);
				//NodeList jobList = XPathAPI.selectNodeList(ele, "/DSExport/Job");
				/*NodeList jobList = jobNode.getChildNodes();				
				for (int jobID = 0; jobID < jobList.getLength(); jobID++) {
					Node tJobNode = jobList.item(jobID);*/

				//System.out.println("jobID:"+jobID+" "+tJobNode.getNodeName());
				// Job.Record
				NodeList recordList = tJobNode.getChildNodes();
				int RecordID = 0;
				for (int m = 0; m < recordList.getLength(); m++) {
					Node tRecordNode = recordList.item(m);
					NamedNodeMap trM = tRecordNode.getAttributes();
					if (!tRecordNode.getNodeName().equals("Record"))
						continue;
					RecordID++;
					//System.out.println("recordID:"+recordID+" "+tRecordNode.getNodeName());
					//if (trM == null) continue;

					Record rd = new Record(JobID, RecordID, trM.getNamedItem("Identifier").getNodeValue(), trM.getNamedItem("Type").getNodeValue(), trM
							.getNamedItem("Readonly").getNodeValue());
					records.add(rd);
					// Job.Record.Property or Job.Record.Collection
					NodeList pOcList = tRecordNode.getChildNodes();
					int PropertyID = 0;
					for (int n = 0; n < pOcList.getLength(); n++) {
						//int PropertyID_CollectionID = 0;
						Node pOcNode = pOcList.item(n);
						NamedNodeMap tpOcM = pOcNode.getAttributes();
						//if (tpOcM == null) continue;
						if (pOcNode.getNodeName().equals("Property")) {
							PropertyID++;
							// Job.Record.Property
							Property pt = new Property(JobID, RecordID, PropertyID, 0, 0, 0, tpOcM.getNamedItem("Name").getNodeValue(), pOcNode
									.getTextContent(), "");
							propertys.add(pt);
						} else if (pOcNode.getNodeName().equals("Collection")) {
							int CollectionID = ++PropertyID;
							// Job.Record.Collection
							Collection clt = new Collection(JobID, RecordID, CollectionID, tpOcM.getNamedItem("Name").getNodeValue(), tpOcM
									.getNamedItem("Type").getNodeValue());
							collections.add(clt);
							// Job.Record.Collection.SubRecord
							NodeList subRecordRList = pOcNode.getChildNodes();
							int SubRecordID = 0;
							for (int s = 0; s < subRecordRList.getLength(); s++) {
								Node subRNode = subRecordRList.item(s);
								if (subRNode.getAttributes() == null)
									continue;
								SubRecordID++;
								SubRecord subRcorde = new SubRecord(JobID, RecordID, CollectionID, SubRecordID);
								subRecords.add(subRcorde);
								// Job.Record.Collection.SubRecord.Property
								NodeList spList = subRNode.getChildNodes();
								int SubPropertyID = 0;
								for (int t = 0; t < spList.getLength(); t++) {
									Node subPropertyNode = spList.item(t);
									NamedNodeMap tspM = subPropertyNode.getAttributes();
									if (tspM == null)
										continue;
									SubPropertyID++;
									Property spt = new Property(JobID, RecordID, 0, CollectionID, SubRecordID, SubPropertyID, tspM.getNamedItem("Name")
											.getNodeValue(), subPropertyNode.getTextContent(), "");
									propertys.add(spt);
								}
							}
						} else {
							//System.out.println("Unkown Node:Job.Record.Unkown");
						}
						
					}
				}
				//}
				
				// 超过1000时写入数据库，释放内存
				if (jobs.size() >= 1000) {
					com.jupiter.WriteDSParse.writeJobs(jobs);
					jobs.clear();
				}
				if (records.size() >= 1000) {
					com.jupiter.WriteDSParse.writeRecords(records);
					records.clear();
				}
				if (propertys.size() >= 1000) {
					com.jupiter.WriteDSParse.writePropertys(propertys);
					propertys.clear();
				}
				if (collections.size() >= 1000) {
					com.jupiter.WriteDSParse.writeCollections(collections);
					collections.clear();
				}
				if (subRecords.size() >= 1000) {
					com.jupiter.WriteDSParse.writeSubRecords(subRecords);
					subRecords.clear();
				}
			}

		}
		// 处理尾数
		if (jobs.size() > 0) {
			com.jupiter.WriteDSParse.writeJobs(jobs);
			jobs.clear();
		}
		if (records.size() > 0) {
			com.jupiter.WriteDSParse.writeRecords(records);
			records.clear();
		}
		if (propertys.size() > 0) {
			com.jupiter.WriteDSParse.writePropertys(propertys);
			propertys.clear();
		}
		if (collections.size() > 0) {
			com.jupiter.WriteDSParse.writeCollections(collections);
			collections.clear();
		}
		if (subRecords.size() > 0) {
			com.jupiter.WriteDSParse.writeSubRecords(subRecords);
			subRecords.clear();
		}

		return null;

	}

	public static void main(String[] args) throws Exception {

		ParseDSXML p = new ParseDSXML();
		String tFileName = "D:\\资料\\JAVAworkspace\\ETL_JOBINO\\xmlParse\\citi_bank.xml";
		//String tFileName = "\\\\gzbotsvr1\\Jupiter\\ETL_JOBINO\\xmlParse\\jb_dwn_covrpf_to_covrpf_ins.xml";
		File tFile = p.getFile(tFileName);
		
		com.jupiter.WriteDSParse.clearAll();
		p.parseXML(tFile);

	}

}
