package application;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class NonRegStd {
	int hours;
	String courseCode;
	SimpleIntegerProperty term;
	SimpleIntegerProperty year;
	

	NonRegStd(int tch_hours,String courseCode,int term,int year){
		this.hours = tch_hours;
		this.courseCode = courseCode;
		this.term = new SimpleIntegerProperty(term);
		this.year = new SimpleIntegerProperty(year);
	}
	   public int getHours(){
		      return this.hours;
	   }
	   public void setHours(int nHours){
		      hours = nHours;
	   }
	   public String getCourseCode(){
		      return this.courseCode;
		   }
		   public void setCourseCode(String newCode){
		      this.courseCode= newCode;
		   }
	   
	   
	   public int getYear(){
	      return year.get();
	   }
	   public void setYear(int newYear){
	      year.set(newYear);
	   }
	   public int getTerm(){
	      return term.get();
	   }
	   public void setTerm(int nTerm){
	      term.set(nTerm);
	   }
	  
	 
	  
}
