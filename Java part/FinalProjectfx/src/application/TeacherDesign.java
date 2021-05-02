package application;
import javafx.beans.property.*;

public class TeacherDesign {
	SimpleIntegerProperty teacher_id;
	SimpleIntegerProperty year;
	SimpleIntegerProperty term;
	SimpleStringProperty courseCode;
	SimpleStringProperty day;
	int hours;

	TeacherDesign(int tch_id,int year,int term,String courseCode,String day,int tch_hours){
		this.teacher_id = new SimpleIntegerProperty(tch_id);
		this.year = new SimpleIntegerProperty(year);
		this.term = new SimpleIntegerProperty(term);
		this.courseCode = new SimpleStringProperty(courseCode);
		this.day = new SimpleStringProperty(day);
		this.hours = tch_hours;
	}
	public int getTch_id(){
	      return teacher_id.get();
	   }
	   public void setTch_id(int new_id){
	      teacher_id.set(new_id);
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
	   public String getCourseCode(){
	      return courseCode.get();
	   }
	   public void setCourseCode(String newCode){
	      courseCode.set(newCode);
	   }
	   public String getDay(){
		      return day.get();
	   }
	   public void setDay(String newDay){
		      day.set(newDay);
	   }
	   public int getHours(){
		      return this.hours;
	   }
	   public void setHours(int nHours){
		      hours = nHours;
	   }
}
