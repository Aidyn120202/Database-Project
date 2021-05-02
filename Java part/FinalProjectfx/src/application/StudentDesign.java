package application;
import javafx.beans.property.*;


public class StudentDesign {
	SimpleStringProperty student_id;
	int year;
	int term;
	String courseCode;
	String day;
	int hours;

	StudentDesign(String std_id,int year,int term,String courseCode,String day,int tch_hours){
		this.student_id = new SimpleStringProperty(std_id);
		this.year = year;
		this.term = term;
		this.courseCode = courseCode;
		this.day = day;
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
	   public int getTerm(){
	      return this.term;
	   }
	   public void setTerm(int nTerm){
	      this.term = nTerm;
	   }
	   public String getCourseCode(){
	      return this.courseCode;
	   }
	   public void setCourseCode(String newCode){
	      this.courseCode= newCode;
	   }
	   public String getDay(){
		      return this.day;
	   }
	   public void setDay(String newDay){
		      this.day = newDay;
	   }
	   public int getHours(){
		      return this.hours;
	   }
	   public void setHours(int nHours){
		      hours = nHours;
	   }
}
