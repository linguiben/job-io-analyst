package com.jupiter.DSXMLParse;

public class Job {

	/**
	 * @param args
	 */
	
	private int JobID;
	private String Identifier;
	private String DateModified;
	private String TimeModified;

	public Job(int jobid, String identifier, String dateModified, String timeModified) {
		this.JobID = jobid;
		this.Identifier = identifier;
		this.DateModified = dateModified;
		this.TimeModified = timeModified;
		//System.out.println("==Job==>Jobid=" + JobID + ", Identifier=" + Identifier + ", DateModified=" + DateModified + ", TimeModified=" + TimeModified + "\n");
		//com.WriteLog.writeFile("Job:       JobID=" + JobID + ", Identifier=" + Identifier + ", DateModified=" + DateModified + ", TimeModified=" + TimeModified);
		com.jupiter.WriteLog.writeCSV("Job.CSV","Job:       JobID=" + JobID + ", Identifier=" + Identifier + ", DateModified=" + DateModified + ", TimeModified=" + TimeModified);
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

	public String getIdentifier() {
		return Identifier;
	}

	public void setIdentifier(String identifier) {
		Identifier = identifier;
	}

	public String getDateModified() {
		return DateModified;
	}

	public void setDateModified(String dateModified) {
		DateModified = dateModified;
	}

	public String getTimeModified() {
		return TimeModified;
	}

	public void setTimeModified(String timeModified) {
		TimeModified = timeModified;
	}

}
