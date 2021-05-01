package com.jupiter.DSXMLParse;

public class Collection {

	/**
	 * @param args
	 */
	private int JobID;
	private int RecordID    ;
	private int CollectionID;
	private String Name        ;
	private String Type        ;
	
	public Collection(int JobID,int RecordID,int CollectionID,String Name,String Type){
		this.JobID = JobID;
		this.RecordID = RecordID;
		this.CollectionID = CollectionID;
		this.Name = Name;
		this.Type = Type;
		//System.out.println("==Collection==>RecordID=" + RecordID + ", CollectionID=" + CollectionID + ", Name=" + Name + ", Type=" + Type );
		//com.WriteLog.writeFile("Collection:JobID="+JobID+", RecordID=" + RecordID + ", CollectionID=" + CollectionID + ", Name=" + Name + ", Type=" + Type );
		com.jupiter.WriteLog.writeCSV("Collection.CSV","Collection:JobID="+JobID+", RecordID=" + RecordID + ", CollectionID=" + CollectionID + ", Name=" + Name + ", Type=" + Type );
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	public int getRecordID() {
		return RecordID;
	}
	public void setRecordID(int recordID) {
		RecordID = recordID;
	}
	public int getCollectionID() {
		return CollectionID;
	}
	public void setCollectionID(int collectionID) {
		CollectionID = collectionID;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getType() {
		return Type;
	}
	public void setType(String type) {
		Type = type;
	}

	public int getJobID() {
		return JobID;
	}

	public void setJobID(int jobID) {
		JobID = jobID;
	}

}
