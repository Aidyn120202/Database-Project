package application;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class CertainTeacher {
	int count;
	String courseCode;
	int tch;
	

	CertainTeacher(int count,String courseCode,int tch){
		this.count = count;
		this.courseCode = courseCode;
		this.tch = tch;
	}
	public int getCount(){
	      return this.count;
	 }
	 public void setCount(int ncount){
		      count = ncount;
	 }
	 public String getCourseCode(){
	      return this.courseCode;
	   }
	   public void setCourseCode(String newCode){
	      this.courseCode= newCode;
	   }
	   public int getTch(){
		      return this.tch;
	   }
	   public void settch(int ntch){
		      tch = ntch;
	   }
	
	
}
