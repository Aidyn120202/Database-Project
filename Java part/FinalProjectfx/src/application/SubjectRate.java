package application;

public class SubjectRate {
	int rate;
	String courseCode;
	int term;
	int year;
	
	SubjectRate(int rate,String courseCode,int term,int year){
		this.rate = rate;
		this.courseCode = courseCode;
		this.term = term;
		this.year = year;

	}
	public int getRate(){
	      return this.rate;
	   }
	   public void setRate(int new_rate){
	      this.rate = new_rate;
	   }
	   public String getCourseCode(){
		      return this.courseCode;
		   }
		   public void setCourseCode(String newCode){
		      this.courseCode= newCode;
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
