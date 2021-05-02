package application;

import javafx.beans.property.SimpleStringProperty;

public class CleverStudents {
	SimpleStringProperty student_id;
	int year;
	String courseCode;
	int hours;
	
	CleverStudents(String std_id,int year,String courseCode,int tch_hours){
		this.student_id = new SimpleStringProperty(std_id);
		this.year = year;
		this.courseCode = courseCode;
		this.hours = tch_hours;

	}
	public String getStd_id(){
	      return student_id.get();
	   }
	   public void setStd_id(String new_id){
	      student_id.set(new_id);
	   }
	   public int getYear(){
		      return this.year;
		   }
		   public void setYear(int newYear){
		      this.year = newYear;
		   }
	   public String getCourseCode(){
	      return this.courseCode;
	   }
	   public void setCourseCode(String newCode){
		  this.courseCode= newCode;
	   }
	   public int getHours(){
		      return this.hours;
	   }
	   public void setHours(int nHours){
		      hours = nHours;
	   }
	
}
