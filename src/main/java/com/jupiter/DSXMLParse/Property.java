package com.jupiter.DSXMLParse;

public class Property {

	/**
	 * @param args
	 */
	private int JobID;
	private int RecordID;
	private int PropertyID;
	private int CollectionID;
	private int SubRecordID;
	private int SubPropertyID;
	private String Name;
	private String Value;
	private String PreFormatted;

	public Property(int JobID, int RecordID, int PropertyID, int CollectionID, int SubRecordID, int SubPropertyID, String Name, String Value,String PreFormatted) {
		this.JobID = JobID;
		this.RecordID = RecordID;
		this.PropertyID = PropertyID;
		this.CollectionID = CollectionID;
		this.SubRecordID = SubRecordID;
		this.SubPropertyID = SubPropertyID;
		this.Name = Name;
		this.Value = Value.substring(0,Math.min(Value.length(),4000)).replace("'","''");
		this.PreFormatted = PreFormatted;

		// System.out.println("==Property==>RecordID=" + RecordID + ",
		// PropertyID=" + PropertyID + ", SubRecordID=" + SubRecordID + ",
		// Name=" + Name + ", Value=" + Value);
		//com.WriteLog.writeFile("Property:  JobID=" + JobID + ", RecordID=" + RecordID + ", PropertyID=" + PropertyID + ", CollectionID=" + CollectionID
		//		+ ", SubRecordID=" + SubRecordID + ", SubPropertyID=" + SubPropertyID + ", Name=" + Name + ", Value=" + Value+ ", PreFormatted=" + PreFormatted);
		com.jupiter.WriteLog.writeCSV("Property.CSV","Property:  JobID=" + JobID + ", RecordID=" + RecordID + ", PropertyID=" + PropertyID + ", CollectionID=" + CollectionID
				+ ", SubRecordID=" + SubRecordID + ", SubPropertyID=" + SubPropertyID + ", Name=" + Name + ", Value=" + Value+ ", PreFormatted=" + PreFormatted);
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

	public int getPropertyID() {
		return PropertyID;
	}

	public void setPropertyID(int propertyID) {
		PropertyID = propertyID;
	}

	public int getSubRecordID() {
		return SubRecordID;
	}

	public void setSubRecordID(int subRecordID) {
		SubRecordID = subRecordID;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getValue() {
		return Value;
	}

	public void setValue(String value) {
		Value = value;
	}

	public int getJobID() {
		return JobID;
	}

	public void setJobID(int jobID) {
		JobID = jobID;
	}

	public int getCollectionID() {
		return CollectionID;
	}

	public void setCollectionID(int collectionID) {
		CollectionID = collectionID;
	}

	public int getSubPropertyID() {
		return SubPropertyID;
	}

	public void setSubPropertyID(int subPropertyID) {
		SubPropertyID = subPropertyID;
	}

	public String getPreFormatted() {
		return PreFormatted;
	}

	public void setPreFormatted(String preFormatted) {
		PreFormatted = preFormatted;
	}

}
