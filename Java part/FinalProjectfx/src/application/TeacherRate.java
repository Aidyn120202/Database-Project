package application;

import javafx.beans.property.SimpleStringProperty;

public class TeacherRate {
	int rate;
	int hours;
	int term;
	int year;
	
	TeacherRate(int rate,int tch_hours,int term,int year){
		this.rate = rate;
		this.hours = tch_hours;
		this.term = term;
		this.year = year;

	}
	public int getRate(){
	      return this.rate;
	   }
	   public void setRate(int new_rate){
	      this.rate = new_rate;
	   }
	   public int getHours(){
		      return this.hours;
	   }
	   public void setHours(int nHours){
		      hours = nHours;
	   }
	   public int getTerm(){
		      return this.term;
		   }
		   public void setTerm(int nTerm){
		      this.term = nTerm;
		   }

	   public int getYear(){
		      return this.year;
		   }
		   public void setYear(int newYear){
		      this.year = newYear;
		   }
	
}
