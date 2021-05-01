package com.jupiter.DSXMLParse;

public class SubRecord {

	/**
	 * @param args
	 */
	private int JobID;
	private int RecordID;
	private int CollectionID ;
	private int SubRecordID ;

	public SubRecord(int JobID,int RecordID,int CollectionID,int SubRecordID){
		this.JobID = JobID;
		this.RecordID = RecordID;
		this.CollectionID=CollectionID;
		this.SubRecordID=SubRecordID;
		//System.out.println("==SubRecord==>CollectionID=" + CollectionID + ", SubRecord_ID=" + SubRecord_ID);
		//com.WriteLog.writeFile("SubRecord: JobID=" + JobID +", CollectionID=" + CollectionID + ", RecordID=" + RecordID+ ", SubRecord_ID=" + SubRecord_ID);
		com.jupiter.WriteLog.writeCSV("SubRecord.CSV","SubRecord: JobID=" + JobID +", CollectionID=" + CollectionID + ", RecordID=" + RecordID+ ", SubRecord_ID=" + SubRecordID);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public int getCollectionID() {
		return CollectionID;
	}

	public void setCollectionID(int collectionID) {
		CollectionID = collectionID;
	}

	public int getSubRecord_ID() {
		return SubRecordID;
	}

	public void setSubRecord_ID(int subRecord_ID) {
		SubRecordID = subRecord_ID;
	}

	public int getJobID() {
		return JobID;
	}

	public void setJobID(int jobID) {
		JobID = jobID;
	}

	public int getRecordID() {
		return RecordID;
	}

	public void setRecordID(int recordID) {
		RecordID = recordID;
	}

	public int getSubRecordID() {
		return SubRecordID;
	}

	public void setSubRecordID(int subRecordID) {
		SubRecordID = subRecordID;
	}

}
