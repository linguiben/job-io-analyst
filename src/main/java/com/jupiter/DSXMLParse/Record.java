package com.jupiter.DSXMLParse;

public class Record {

	/**
	 * @param args
	 */
	
	private int JobID     ;
	private int RecordID  ;
	private String Identifier;
	private String Type      ;
	private String Readonly  ;
	
	public Record(int JobID, int RecordID, String Identifier, String Type, String Readonly) {
		this.JobID = JobID;
		this.RecordID = RecordID;
		this.Identifier = Identifier;
		this.Type = Type;
		this.Readonly = Readonly;

		//System.out.println("==Record==>Jobid=" + JobID + ", RecordID=" + RecordID + ", Identifier=" + Identifier + ", Type=" + Type + ", Readonly=" + Readonly);
		//com.WriteLog.writeFile("Record:    JobID=" + JobID + ", RecordID=" + RecordID + ", Identifier=" + Identifier + ", Type=" + Type + ", Readonly=" + Readonly);
		com.jupiter.WriteLog.writeCSV("Record.CSV","Record:    JobID=" + JobID + ", RecordID=" + RecordID + ", Identifier=" + Identifier + ", Type=" + Type + ", Readonly=" + Readonly);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

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

	public String getIdentifier() {
		return Identifier;
	}

	public void setIdentifier(String identifier) {
		Identifier = identifier;
	}

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	public String getReadonly() {
		return Readonly;
	}

	public void setReadonly(String readonly) {
		Readonly = readonly;
	}

}
