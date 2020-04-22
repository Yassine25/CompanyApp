package model;

import java.sql.Date;

public class Job {
	private String name;
	private String description;
	private Date placeDate;
	private String qualifications;
	
	public String getName() { return name; }
	
	public void setName(final String name) { this.name = name; }

	public String getDescription() { return description; }
	
	public void setDescription(final String description) { this.description = description; }
	
	public Date getPlaceDate() { return placeDate; }
	
	public void setPlaceDate(final Date placeDate) { this.placeDate = placeDate; }
	
	public String getQualifications() { return qualifications; }
	
	public void setQualifications(final String qualifications) { this.qualifications = qualifications; }
}
