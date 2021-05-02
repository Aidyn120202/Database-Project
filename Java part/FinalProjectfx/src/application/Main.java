package application;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.event.ActionEvent; 
import javafx.event.EventHandler; 
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.geometry.*;
import javafx.scene.paint.*;
import javafx.scene.text.Font;
import javafx.scene.layout.HBox;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import java.sql.*;
import java.util.*;
import java.io.FileInputStream;
import java.lang.*;
import java.sql.*;
import java.util.*;
import java.lang.*;
import java.sql.*;

public class Main extends Application {
	public static Connection conn = null;
	public static Statement stmt = null;
	public static Scene sc;
	Stage s;
	int semester;
	int year;
	String course_code;
	String l_type;
	String student_id;
	int teacher_id;
	
	static void createConnection() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");

			Driver myDriver = new oracle.jdbc.driver.OracleDriver();
			DriverManager.registerDriver(myDriver);
			
			String URL = "jdbc:oracle:thin:@localhost:1521:ORC";
			String USER = "system";
			String PASS = "123";
			conn = DriverManager.getConnection(URL, USER, PASS);
			stmt = conn.createStatement();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	//Close connection
	static void closeConnection() {
		try {
			stmt.close();
			conn.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	//Task 1
	static ArrayList<Object> getPopularCourse(int term,int year) {
		ArrayList<Object> res = new ArrayList<Object>();
		try {
			createConnection();
			long time = java.lang.System.currentTimeMillis();
			
			ResultSet rs = stmt.executeQuery("SELECT * FROM pkgCourse.getPopularCourse(" + term + "," + year + ")");
			
			long time2 =  java.lang.System.currentTimeMillis() - time;
			
			System.out.println("Millisceonds: " + time2);
			while (rs.next()) {
				res.add(rs.getInt(1));
				res.add(rs.getString(2));
				res.add(rs.getInt(3));			
			}
			rs.close();
			closeConnection();
		}
		catch(Exception e){
			e.printStackTrace();}
		return res;

	}
	
	//Task2 Find most popular teacher in section for semester
	static ArrayList<Object> getPopularTeacher(int term,int year,String code) {
		ArrayList<Object> PopularTeacher = new ArrayList<Object>();
		try {
			createConnection();
			long time = java.lang.System.currentTimeMillis();

			ResultSet rs = stmt.executeQuery("SELECT * FROM pkgTeacher.getPopularTeacher("+ term + "," + year + "," + "'"+code+"'" +")");

			long time2 =  java.lang.System.currentTimeMillis() - time;
			System.out.println("Millisceonds: " + time2);

			while (rs.next()) {
				PopularTeacher.add(rs.getInt("Max_count"));
				PopularTeacher.add(rs.getString("Ders_kod"));
				PopularTeacher.add(rs.getInt("Practice_id"));
				PopularTeacher.add(rs.getInt("Lecture_id"));			}
			rs.close();
			closeConnection();
		}
		catch(Exception e){
			e.printStackTrace();}
		return PopularTeacher;
	}
	
	//Task3
	static ArrayList<ArrayList<Object>> getGpa(String student_id) {
		ArrayList<ArrayList<Object>> StudentsGpa = new ArrayList<>(5);

		try {
			createConnection();
			ResultSet rs = stmt.executeQuery("SELECT * FROM pkgGPA.getGpa("+ "'" + student_id+ "'" +")");
			
			int n = 0;
			while (rs.next()) {
			    StudentsGpa.add(new ArrayList());
				StudentsGpa.get(n).add(rs.getString("stud_id"));
				StudentsGpa.get(n).add(rs.getInt("year"));
				StudentsGpa.get(n).add(rs.getInt("term"));
				StudentsGpa.get(n).add(rs.getFloat("total_gpa"));
				StudentsGpa.get(n).add(rs.getFloat("gpa_per_semester"));
				n++;
			}
			
				
			rs.close();
			closeConnection();
		}
		catch(Exception e){
			e.printStackTrace();}
		
		return StudentsGpa;
	}
	
	//Task4
	static ArrayList<ArrayList<Object>> getNoneRegStudents(){
		ArrayList<ArrayList<Object>> noneregstudents = new ArrayList<>(5);
		
		try {
			createConnection();
			ResultSet rs = stmt.executeQuery("SELECT * FROM pkgStudent.getNoneRegStudents()");
			int n = 0;
			while (rs.next()) {
				noneregstudents.add(new ArrayList());
				noneregstudents.get(n).add(rs.getInt("max_count"));
				noneregstudents.get(n).add(rs.getString("Student_id"));
				noneregstudents.get(n).add(rs.getInt("Term"));
				noneregstudents.get(n).add(rs.getInt("s_year"));
				n++;
			}
			rs.close();
			closeConnection();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return noneregstudents;
	}
	
	//Task5
	static ArrayList<Object> getStudentRetakes(String std_id, int year, int semester) {
		ArrayList<Object> StudentRetakes = new ArrayList<Object>();
		try {
			createConnection();
			ResultSet rs = stmt.executeQuery("SELECT * FROM pkgStudent.getstudentretakes('"+ std_id + "'," + year + "," + "'"+semester+"'" +")");
			while (rs.next()) {
				StudentRetakes.add(rs.getString("Student_id"));
				StudentRetakes.add(rs.getInt("Year"));
				StudentRetakes.add(rs.getInt("Semester"));
				StudentRetakes.add(rs.getInt("Quantity"));
				StudentRetakes.add(rs.getInt("semester_sum"));			
				StudentRetakes.add(rs.getInt("total_quantity"));			
				StudentRetakes.add(rs.getInt("total_sum"));			
			}
			rs.close();
			closeConnection();
		}
		catch(Exception e){
			e.printStackTrace();}
		return StudentRetakes;
	}
	//Task6
	static ArrayList<ArrayList<Object>> getTeacherTotalHours(int teacher_id ){
		ArrayList<ArrayList<Object>> totalhoursset = new ArrayList<>();
		try {
			createConnection();
			ResultSet rs = stmt.executeQuery("SELECT * FROM pkgTeacher.getTeacherTotalHours("+ teacher_id+")");
			int n = 0;
			while (rs.next()) {
				totalhoursset.add(new ArrayList());
				totalhoursset.get(n).add(rs.getInt("emp_id"));
				totalhoursset.get(n).add(rs.getInt("year"));
				totalhoursset.get(n).add(rs.getInt("Term"));
				totalhoursset.get(n).add(rs.getString("Total_hours"));
				n++;}
			rs.close();
			closeConnection();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return totalhoursset;
	}
	
	//Task7
	static ArrayList<ArrayList<Object>> getTeacherDesign(int teacher_id,int year, int semester){
		ArrayList<ArrayList<Object>> teacherDesign = new ArrayList<>();
		try {
			createConnection();
			
			long time = java.lang.System.currentTimeMillis();
			
			ResultSet rs = stmt.executeQuery("SELECT * FROM pkgTeacher.getTeacherDesign(" + teacher_id + "," + year + "," + semester + ")");
			
			long time2 =  java.lang.System.currentTimeMillis() - time;

			System.out.println("Millisceonds: " + time2);

			int n = 0;
			while (rs.next()) {
				teacherDesign.add(new ArrayList());
				teacherDesign.get(n).add(rs.getInt("emp_id"));
				teacherDesign.get(n).add(rs.getInt("year"));
				teacherDesign.get(n).add(rs.getInt("term"));
				teacherDesign.get(n).add(rs.getString("course_code"));
				teacherDesign.get(n).add(rs.getString("day_schedule"));
				teacherDesign.get(n).add(rs.getInt("hours_schedule_teacher"));
				n++;
			}
			rs.close();
			closeConnection();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return teacherDesign;
	}

	//Task8
	static ArrayList<ArrayList<Object>> getStudentDesign(String student_id,int year, int semester){
		ArrayList<ArrayList<Object>> studentDesign = new ArrayList<>();
		try {
			createConnection();
			long time = java.lang.System.currentTimeMillis();

			ResultSet rs = stmt.executeQuery("SELECT * FROM pkgStudent.getStudentDesign('" + student_id + "'," + year + "," + semester + ")");
			long time2 =  java.lang.System.currentTimeMillis() - time;

			System.out.println("Millisceonds: " + time2);

			int n = 0;
			while (rs.next()) {
				studentDesign.add(new ArrayList());
				studentDesign.get(n).add(rs.getString("student_id"));
				studentDesign.get(n).add(rs.getInt("year"));
				studentDesign.get(n).add(rs.getInt("term"));
				studentDesign.get(n).add(rs.getString("course_code"));
				studentDesign.get(n).add(rs.getString("day_schedule"));
				studentDesign.get(n).add(rs.getInt("hours_schedule_student"));
				n++;
			}
			rs.close();
			closeConnection();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return studentDesign;
	}
	
	//task9
	static ArrayList<Object> getStudentCredit(String student_id,int year, int semester){
		ArrayList<Object> StudentCredits = new ArrayList<>();
		try {
			createConnection();
			ResultSet rs = stmt.executeQuery("SELECT * FROM pkgStudent.getStudentCredit("+ "'" + student_id + "'," + year + "," + semester + ")");
			while (rs.next()) {
				StudentCredits.add(rs.getString("student_id"));
				StudentCredits.add(rs.getInt("year"));
				StudentCredits.add(rs.getInt("semester"));
				StudentCredits.add(rs.getInt("total_subjects"));
				StudentCredits.add(rs.getInt("total_credits"));
			}
				
			rs.close();
			closeConnection();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return StudentCredits;
	}
	
	//task10
	static ArrayList<ArrayList<Object>> getCleverStudents(int teacher_id,String course_code){
		ArrayList<ArrayList<Object>> CleverStudents = new ArrayList<>();
		try {
			createConnection();
			
			long time = java.lang.System.currentTimeMillis();
			
			ResultSet rs = stmt.executeQuery("SELECT * FROM pkgStudent.getCleverStudents(" + teacher_id + ",'"+course_code+ "'"+")");
			
			long time2 =  java.lang.System.currentTimeMillis() - time;

			System.out.println("Millisceonds: " + time2);

			int n = 0;
			while (rs.next()) {
				CleverStudents.add(new ArrayList());
				CleverStudents.get(n).add(rs.getString("student_id"));
				CleverStudents.get(n).add(rs.getInt("teacher_id"));
				CleverStudents.get(n).add(rs.getString("course_code"));
				CleverStudents.get(n).add(rs.getInt("average_rate"));
				n++;
				}
			rs.close();
			closeConnection();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return CleverStudents;
	}
	
	//task11
	static ArrayList<ArrayList<Object>> getTeachersRateByYear(int term,int year){
		ArrayList<ArrayList<Object>> teachersrate = new ArrayList<>();
		try {
			createConnection();
			long time = java.lang.System.currentTimeMillis();

			
			ResultSet rs = stmt.executeQuery("SELECT * FROM pkgTeacher.getTeachersRateByYear(" + term + "," + year + ")");
			
			long time2 =  java.lang.System.currentTimeMillis() - time;

			System.out.println("Millisceonds: " + time2);

			int n = 0;
			while (rs.next()) {
				teachersrate.add(new ArrayList());
				teachersrate.get(n).add(rs.getInt("rate"));
				teachersrate.get(n).add(rs.getInt("teacher_id"));
				teachersrate.get(n).add(rs.getInt("term"));
				teachersrate.get(n).add(rs.getInt("year"));
				n++;
			}
			rs.close();
			closeConnection();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return teachersrate;
	}
	
	//task12
	static ArrayList<ArrayList<Object>> getCourseRateByYear(int term,int year) {
		ArrayList<ArrayList<Object>> rateset = new ArrayList<>();
		try {
			createConnection();
			
			long time = java.lang.System.currentTimeMillis();

			ResultSet rs = stmt.executeQuery("SELECT * FROM pkgCourse.getCourseRateByYear(" + term + "," + year +")");
			
			long time2 =  java.lang.System.currentTimeMillis() - time;

			System.out.println("Millisceonds: " + time2);

			int n = 0;
			while (rs.next()) {
				rateset.add(new ArrayList());
				rateset.get(n).add(rs.getInt("course_rate"));
				rateset.get(n).add(rs.getString("course_code"));
				rateset.get(n).add(rs.getInt("term"));
				rateset.get(n).add(rs.getInt("year"));
				n++;
			}
			rs.close();
			closeConnection();
		}
		catch(Exception e){
			e.printStackTrace();}
		return rateset;
	}
	
	//task13
	static ArrayList<Object> getRetakeProfit(){
		ArrayList<Object> retakeCount = new ArrayList<Object>();
		try {
			createConnection();
			ResultSet rs = stmt.executeQuery("SELECT * FROM pkgFinance.getRetakeProfit()");
			while (rs.next()) {
				retakeCount.add(rs.getInt("retake_count"));
				retakeCount.add(rs.getInt("profit"));
			}
			rs.close();
			closeConnection();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return retakeCount;
	}
	//task14
	static ArrayList<Object> getRetakeSubject(){
		ArrayList<Object> RetakeSubjectarr = new ArrayList<Object>();
		try {
			createConnection();
			ResultSet rs = stmt.executeQuery("SELECT * FROM pkgCourse.getRetakeSubject()");
			while (rs.next()) {
				RetakeSubjectarr.add(rs.getInt("retake_count"));
				RetakeSubjectarr.add(rs.getString("ders_kod"));
			}
			rs.close();
			closeConnection();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return RetakeSubjectarr;
	}
	
	//task15
	static ArrayList<ArrayList<Object>> getCertainTeacher(){
		ArrayList<ArrayList<Object>> certainTeacher = new ArrayList<>();
		try {
			createConnection();
			ResultSet rs = stmt.executeQuery("SELECT * FROM pkgTeacher.getCertainTeacher()");
			int n = 0;
			while (rs.next()) {
				certainTeacher.add(new ArrayList());
				certainTeacher.get(n).add(rs.getInt("stud_count"));
				certainTeacher.get(n).add(rs.getString("ders_kod"));
				certainTeacher.get(n).add(rs.getInt("emp_id"));
				n++;
			}
			rs.close();
			closeConnection();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return certainTeacher;
	}
	
	private GridPane createRegistrationFormPane() {
        // Instantiate a new Grid Pane
        GridPane gridPane = new GridPane();

        // Position the pane at the center of the screen, both vertically and horizontally
        gridPane.setAlignment(Pos.CENTER);

        // Set a padding of 20px on each side
        gridPane.setPadding(new Insets(40, 40, 40, 40));

        // Set the horizontal gap between columns
        gridPane.setHgap(10);

        // Set the vertical gap between rows
        gridPane.setVgap(10);

        // Add Column Constraints

        // columnOneConstraints will be applied to all the nodes placed in column one.
        ColumnConstraints columnOneConstraints = new ColumnConstraints(100, 100, Double.MAX_VALUE);
        columnOneConstraints.setHalignment(HPos.RIGHT);

        // columnTwoConstraints will be applied to all the nodes placed in column two.
        ColumnConstraints columnTwoConstrains = new ColumnConstraints(200,200, Double.MAX_VALUE);
        columnTwoConstrains.setHgrow(Priority.ALWAYS);

        gridPane.getColumnConstraints().addAll(columnOneConstraints, columnTwoConstrains);

        return gridPane;
    }

    private void addUIControls(GridPane gridPane,GridPane r,Stage s) {
        // Add Header
    	this.s = s;
        Label headerLabel = new Label("Registration Form");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gridPane.add(headerLabel, 0,0,2,1);
        GridPane.setHalignment(headerLabel, HPos.CENTER);
        GridPane.setMargin(headerLabel, new Insets(20, 0,20,0));

        // Add Name Label
        Label nameLabel = new Label("Full Name : ");
        gridPane.add(nameLabel, 0,1);

        // Add Name Text Field
        TextField nameField = new TextField();
        nameField.setPrefHeight(40);
        nameField.setPrefWidth(10);
        gridPane.add(nameField, 1,1);


        // Add Password Label
        Label passwordLabel = new Label("Password : ");
        gridPane.add(passwordLabel, 0, 2);

        // Add Password Field
        PasswordField passwordField = new PasswordField();
        passwordField.setPrefHeight(40);
        gridPane.add(passwordField, 1, 2);

        // Add Submit Button
        Button submitButton = new Button("Log in");
        submitButton.setPrefHeight(40);
        submitButton.setDefaultButton(true);
        submitButton.setPrefWidth(100);
        gridPane.add(submitButton, 0, 4, 2, 1);
        GridPane.setHalignment(submitButton, HPos.CENTER);
        GridPane.setMargin(submitButton, new Insets(20, 0,20,0));

        submitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(nameField.getText().isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Form Error!", "Please enter your name");
                    return;
                }
                if(passwordField.getText().isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Form Error!", "Please enter a password");
                    return;
                }
            	String id = "";
            	String password = "";
            	String email = "";

                try {
                
                	createConnection();
        			ResultSet rs = stmt.executeQuery("SELECT * FROM Student_account where stud_id = '" + nameField.getText() + "'");
        			while (rs.next()) {
        				id = rs.getString(1);
        				password = rs.getString(2);
        				
        			}
        			rs.close();
        			closeConnection();
                }catch(Exception e){
        			e.printStackTrace();}
                
                if(!nameField.getText().equals(id) && !passwordField.getText().equals(password)) {
                	 try {
                         
                     	createConnection();
             			stmt.executeUpdate("INSERT INTO student_account " + "VALUES ('"+nameField.getText()+"', '"+passwordField.getText()+"')");
             			closeConnection();
                     	
                     }catch(Exception e){
             			e.printStackTrace();}
          			showAlert(Alert.AlertType.CONFIRMATION, gridPane.getScene().getWindow(), "", "Welcome " + nameField.getText());
                }
                else if(nameField.getText().equals(id) && !passwordField.getText().equals(password)) {
                	showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Form Error!", "Please enter a correct password");
                    return;
                }
                sc = new Scene(r,900,700);
                sc.getStylesheets().add
    	        (Main.class.getResource("application.css").toExternalForm());
                s.setScene(sc);
                s.show();
            }
        });
    }

    private void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }
	
	
	
	@Override
	public void start(Stage s) {
		try {
	    	Button tsk1 = new Button("Find most popular courses for semester "); 
	        Button tsk2 = new Button("Find most popular teacher in section for semester"); 
	        Button tsk3 = new Button("Calculate GPA of student for the semester and total"); 
	        Button tsk4 = new Button("Find students who didn’t register any subjects for one semester"); 
	        Button tsk5 = new Button("Calculate how much money the student spent on retakeks"); 
	        Button tsk6 = new Button("How many hours Teacher have for given semester"); 
	        Button tsk7 = new Button("Design schedule of teacher on semester."); 
	        Button tsk8 = new Button("Design schedule of student on semester."); 
	        Button tsk9 = new Button("Display how many subjects and credits was selected by student"); 
	        Button tsk10 = new Button("Find most clever flow of students"); 
	        Button tsk11 = new Button("Teachers rating for the semester"); 
	        Button tsk12 = new Button("Subject ratings for the semester"); 
	        Button tsk13 = new Button("Calculate total number of retakes for all time"); 
	        Button tsk14 = new Button("Find in which subject the students received the most retakes."); 
	        Button tsk15 = new Button("Display how many students have chosen a certain teacher."); 


	        tsk1.setMinWidth(350);
	        tsk2.setMinWidth(350);
	        tsk3.setMinWidth(350);
	        tsk4.setMinWidth(350);
	        tsk5.setMinWidth(350);
	        tsk6.setMinWidth(350);
	        tsk7.setMinWidth(350);
	        tsk8.setMinWidth(350);
	        tsk9.setMinWidth(350);
	        tsk10.setMinWidth(350);
	        tsk11.setMinWidth(350);
	        tsk12.setMinWidth(350);
	        tsk13.setMinWidth(350);
	        tsk14.setMinWidth(350);
	        tsk15.setMinWidth(350);



	        VBox vbButtons = new VBox();
	        vbButtons.setSpacing(10);
	        vbButtons.setPadding(new Insets(0, 20, 10, 20)); 
	        vbButtons.getChildren().addAll(tsk1,tsk2,tsk3,tsk4,tsk5,tsk6,tsk7,tsk8,tsk9,tsk10,tsk11,tsk12,tsk13,tsk14,tsk15);
	        
	        BackgroundFill background_fill = new BackgroundFill(Color.rgb(204, 229, 255),  
	                CornerRadii.EMPTY, Insets.EMPTY); 
			// create Background 
			Background background = new Background(background_fill); 
			 // create a stack pane 
	        GridPane r = new GridPane(); 
	        r.setHgap(10);
	        r.setVgap(10);
	        r.setBackground(background);

	        // add buttons 
	        
	        FileInputStream inputstream = new FileInputStream("C:\\Program Files (x86)\\Study\\photo_2020-12-17_20-05-30.jpg"); 
	        Image image = new Image(inputstream); 
	        ImageView imageView = new ImageView(image);
	        imageView.setFitHeight(100); 
	        imageView.setFitWidth(200); 
	        imageView.setX(600);
	        
	        r.add(imageView,1,1); 
	        r.setMargin(vbButtons, new Insets(50, 10, 5, 10));
	        r.add(vbButtons, 2, 2);
	        
	    
	        // action tsk1event -----------------------------------------------------------
	        EventHandler<ActionEvent> tsk1event = new EventHandler<ActionEvent>() { 
	            public void handle(ActionEvent e) 
	            { 
	            	GridPane newpane = new GridPane(); 
	            	  BackgroundFill background_fill = new BackgroundFill(Color.rgb(204, 229, 255),  
	      	                CornerRadii.EMPTY, Insets.EMPTY); 
	      			// create Background 
	      			Background background = new Background(background_fill); 
	      			newpane.setBackground(background);
	            	VBox vb = new VBox();
	            	Label qs = new Label("To find most popular course: ");
	            	Label label1 = new Label("Enter the semester:");
	            	Label label2 = new Label("Enter the year:");
	            	Button back = new Button("Back");
	            	Button enter = new Button("Enter");
	            	TextField textField = new TextField ();
	            	TextField textField2 = new TextField ();
	            	HBox hb = new HBox();
	            	hb.getChildren().addAll(label1, textField);
	            	hb.setSpacing(10);
	            	HBox hb2 = new HBox();
	            	hb2.getChildren().addAll(label2, textField2,enter);
	            	hb2.setSpacing(10);
	            	vb.getChildren().addAll(back,qs,hb,hb2);
	            	vb.setSpacing(10);
	            	newpane.getChildren().addAll(vb);
	            	newpane.setMargin(vb,new Insets(70,0,0,30));
	            	Scene newScene = new Scene(newpane,900,700);
	            	newScene.getStylesheets().add
	    	        (Main.class.getResource("application.css").toExternalForm());
	            	s.setScene(newScene);
	            	EventHandler<ActionEvent> backevent = new EventHandler<ActionEvent>() { 
	    	            public void handle(ActionEvent e) 
	    	            { 
	    	    	        s.setScene(sc); 
	    	            }
	    	            
	            	};
	            	EventHandler<ActionEvent> enterevent = new EventHandler<ActionEvent>() { 
	    	            public void handle(ActionEvent e) 
	    	            { 
	    	            	semester = Integer.valueOf(textField.getText());
	    	                year = Integer.valueOf(textField2.getText());
	    	                ArrayList<Object> t = new ArrayList<Object>();
	    	            	t = getPopularCourse(semester,year);
	    	            	Label text = new Label("The result:");
	    	            	text.setStyle("-fx-text-fill: #FF0000;");
	    	            	Label max_count = new Label("Max count: " + t.get(0));
	    	            	Label course_code = new Label("Course code: " + t.get(1));
	    	            	Label teacher = new Label("Teacher: " + t.get(2));
	    	            	vb.getChildren().addAll(text,max_count,course_code,teacher);
	    	            }
	    	            
	            	};
	            	back.setOnAction(backevent);
	            	enter.setOnAction(enterevent);
	            	
	            	
	            	
	            } 
	        }; 
	        tsk1.setOnAction(tsk1event); 
	        //--------------------------------------------------------------------------------------
	        
	        
	        //action tsk2event----------------------------------------------------------------------
	        EventHandler<ActionEvent> tsk2event = new EventHandler<ActionEvent>() { 
	            public void handle(ActionEvent e) 
	            { 
	            	GridPane newpane = new GridPane();
	            	BackgroundFill background_fill = new BackgroundFill(Color.rgb(204, 229, 255),  
		      	                CornerRadii.EMPTY, Insets.EMPTY); 
		      			// create Background 
		      			Background background = new Background(background_fill); 
		      			newpane.setBackground(background);
	            	VBox vb = new VBox();
	            	Label qs = new Label("To find most popular popular: ");
	            	Label label1 = new Label("Enter the semester:");
	            	Label label2 = new Label("Enter the year:");
	            	Label label3 = new Label("Enter the course code:");
	            	Button back = new Button("Back");
	            	Button enter = new Button("Enter");
	            	TextField textField = new TextField ();
	            	TextField textField2 = new TextField ();
	            	TextField textField3 = new TextField ();

	            	HBox hb = new HBox();
	            	hb.getChildren().addAll(label1, textField);
	            	hb.setSpacing(10);
	            	HBox hb2 = new HBox();
	            	hb2.getChildren().addAll(label2, textField2,enter);
	            	hb2.setSpacing(10);
	            	HBox hb3 = new HBox();
	            	hb3.getChildren().addAll(label3, textField3,enter);
	            	hb3.setSpacing(10);
	            	vb.getChildren().addAll(back,qs,hb,hb2,hb3);
	            	vb.setSpacing(10);
	            	newpane.getChildren().addAll(vb);
	            	newpane.setMargin(vb,new Insets(70,0,0,30));
	            	Scene newScene = new Scene(newpane,900,700);
	            	newScene.getStylesheets().add
	    	        (Main.class.getResource("application.css").toExternalForm());
	            	s.setScene(newScene);
	            	EventHandler<ActionEvent> backevent = new EventHandler<ActionEvent>() { 
	    	            public void handle(ActionEvent e) 
	    	            { 
	    	    	        s.setScene(sc); 
	    	            }
	    	            
	            	};
	            	EventHandler<ActionEvent> enterevent = new EventHandler<ActionEvent>() { 
	    	            public void handle(ActionEvent e) 
	    	            { 
	    	            	semester = Integer.valueOf(textField.getText());
	    	                year = Integer.valueOf(textField2.getText());
	    	                course_code = String.valueOf(textField3.getText());
	    	                ArrayList<Object> t = new ArrayList<Object>();
	    	                t = getPopularTeacher(semester,year,course_code);
	    	                if(t.size()>0) {
	    	                	Label text = new Label("The result:");
		    	            	text.setStyle("-fx-text-fill: #FF0000;");
	 	    	            	Label max_count = new Label("Max count: " + t.get(0));
	 	    	            	Label crs_code = new Label("Course code: " + t.get(1));
	 	    	            	Label practice_id = new Label("Practice_id: " + t.get(2));
	 	    	            	Label lecture = new Label("Lecture: " + t.get(3));
	 	    	            	
	 	    	            	vb.getChildren().addAll(text,max_count,crs_code,practice_id,lecture);
	    	                }
	    	                else {
	    	                	Label text = new Label("No such result:");
	    	                	vb.getChildren().addAll(text);
	    	                }
	    	               
	    	            }
	    	            
	            	};
	            	back.setOnAction(backevent);
	            	enter.setOnAction(enterevent);

	            }
	            
	        };
	        tsk2.setOnAction(tsk2event);
	        //---------------------------------------------------------------------------------------
	        
	        //action task3---------------------------------------------------------------------------
	        EventHandler<ActionEvent> tsk3event = new EventHandler<ActionEvent>() { 
	            public void handle(ActionEvent e) 
	            { 
	            	GridPane newpane = new GridPane(); 
	            	  BackgroundFill background_fill = new BackgroundFill(Color.rgb(204, 229, 255),  
		      	                CornerRadii.EMPTY, Insets.EMPTY); 
		      			// create Background 
		      			Background background = new Background(background_fill); 
		      			newpane.setBackground(background);
	            	VBox vb = new VBox();
	            	Label qs = new Label("Get GPA of student: ");
	            	Label label1 = new Label("Enter the student id:");
	            	Button back = new Button("Back");
	            	Button enter = new Button("Enter");
	            	TextField textField = new TextField ();
	            	
	            	HBox hb = new HBox();
	            	hb.getChildren().addAll(label1, textField);
	            	hb.setSpacing(10);
	            	vb.getChildren().addAll(back,qs,hb,enter);
	            	vb.setSpacing(10);
	            	newpane.getChildren().addAll(vb);
	            	newpane.setMargin(vb,new Insets(70,0,0,30));
	            	Scene newScene = new Scene(newpane,900,700);
	            	newScene.getStylesheets().add
	    	        (Main.class.getResource("application.css").toExternalForm());
	            	s.setScene(newScene);
	            	EventHandler<ActionEvent> backevent = new EventHandler<ActionEvent>() { 
	    	            public void handle(ActionEvent e) 
	    	            { 
	    	    	        s.setScene(sc); 
	    	            }
	    	            
	            	};
	            	EventHandler<ActionEvent> enterevent = new EventHandler<ActionEvent>() { 
	    	            public void handle(ActionEvent e) 
	    	            {
	    	                student_id = String.valueOf(textField.getText());
	    	                
	    	                ArrayList<ArrayList<Object>> t = new ArrayList<>();
	    	                t = getGpa(student_id);
	    	                Label text = new Label("The result:");
	    	            	text.setStyle("-fx-text-fill: #FF0000;");
	    	            	vb.getChildren().add(text);
	    	                for(int i = 0;i < t.size();i++) {
	    	                	Label student_id = new Label("Student id: " + t.get(i).get(0));
		 	    	           	Label v_year = new Label("Year: " + t.get(i).get(1));
		 	    	           	Label v_term = new Label("Term: " + t.get(i).get(2));
		 	    	           	Label totalgpa = new Label("Total Gpa: " + t.get(i).get(3));
		 	    	           	Label gpaperterm = new Label("Gpa per semester: " + t.get(i).get(4));
		 	    	           	HBox newhb = new HBox();
		 		            	newhb.setSpacing(20);
		 	    	           	newhb.getChildren().addAll(student_id,v_year,v_term,totalgpa,gpaperterm);	
		 	    	           	vb.getChildren().addAll(newhb);	

	    	                }
	    	                
	    	            }
	    	            
	            	};
	            	back.setOnAction(backevent);
	            	enter.setOnAction(enterevent);

	            }
	            
	        };
	        tsk3.setOnAction(tsk3event);
	        //----------------------------------------------------------------------------------------
	        
	        //action for tsk4-------------------------------------------------------------------------
	        EventHandler<ActionEvent> tsk4event = new EventHandler<ActionEvent>() { 
	            public void handle(ActionEvent e) 
	            { 
	            	TableView<NonRegStd> table = new TableView<>();
	            	ObservableList<NonRegStd> data = FXCollections.observableArrayList();
	            	table.setPrefWidth(900);
	            	table.setPrefHeight(500);

	                ArrayList<ArrayList<Object>> t = new ArrayList<>();
	                t = getNoneRegStudents();
	                for(int i = 0;i < t.size();i++) {
 	    	           	data.add(new NonRegStd((int)t.get(i).get(0),(String)t.get(i).get(1),(int)t.get(i).get(2),(int)t.get(i).get(3)));
	                }
	                
	                TableColumn c_count = new TableColumn("Max count");
	                c_count.setCellValueFactory(new PropertyValueFactory<NonRegStd,Integer>("hours"));
	                
	                TableColumn c_stdid = new TableColumn("Student id");
	                c_stdid.setCellValueFactory(new PropertyValueFactory<NonRegStd,String>("courseCode"));
	                
	                TableColumn<NonRegStd,Integer> c_year = new TableColumn<NonRegStd,Integer>("Year");
	                c_year.setCellValueFactory(new PropertyValueFactory<NonRegStd,Integer>("year"));
	                
	                TableColumn<NonRegStd,Integer> c_term = new TableColumn<NonRegStd,Integer>("Term");
	                c_term.setCellValueFactory(new PropertyValueFactory<NonRegStd,Integer>("term"));
	                
	                table.setItems(data);
	                table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	            	
	                table.getColumns().addAll(c_count,c_stdid,c_year,c_term);
	          
	                Group root = new Group();
	                
	                VBox vv = new VBox();
	                Button back = new Button("Back");
	            	EventHandler<ActionEvent> backevent = new EventHandler<ActionEvent>() { 
	    	            public void handle(ActionEvent e) 
	    	            { 
	    	    	        s.setScene(sc); 
	    	            }
	    	            
	            	};
	            	vv.getChildren().addAll(table,back);
	            	vv.setAlignment(Pos.CENTER);
	            	back.setOnAction(backevent);

	                root.getChildren().addAll(vv);
	            	Scene newScene = new Scene(root,900,700);
	            	newScene.getStylesheets().add
	    	        (Main.class.getResource("application.css").toExternalForm());
	            	s.setScene(newScene);
	            

	                }
	            
	        };
	        tsk4.setOnAction(tsk4event);
	        //----------------------------------------------------------------------------------------
	        
	        
	        //action tsk5event----------------------------------------------------------------------
	        EventHandler<ActionEvent> tsk5event = new EventHandler<ActionEvent>() { 
	            public void handle(ActionEvent e) 
	            { 
	            	GridPane newpane = new GridPane();
	            	BackgroundFill background_fill = new BackgroundFill(Color.rgb(204, 229, 255),  
		      	                CornerRadii.EMPTY, Insets.EMPTY); 
		      			// create Background 
		      			Background background = new Background(background_fill); 
		      			newpane.setBackground(background);
	            	VBox vb = new VBox();
	            	Label qs = new Label("Calculate how much money the student spent on retakes: ");
	            	Label std_id = new Label("Enter the student id:");
	            	Label l_year = new Label("Enter the year:");
	            	Label l_semester= new Label("Enter the semester:");
	            	Button back = new Button("Back");
	            	Button enter = new Button("Enter");
	            	TextField textField = new TextField ();
	            	TextField textField2 = new TextField ();
	            	TextField textField3 = new TextField ();

	            	HBox hb = new HBox();
	            	hb.getChildren().addAll(std_id, textField);
	            	hb.setSpacing(10);
	            	HBox hb2 = new HBox();
	            	hb2.getChildren().addAll(l_year, textField2,enter);
	            	hb2.setSpacing(10);
	            	HBox hb3 = new HBox();
	            	hb3.getChildren().addAll(l_semester, textField3,enter);
	            	hb3.setSpacing(10);
	            	vb.getChildren().addAll(back,qs,hb,hb2,hb3);
	            	vb.setSpacing(10);
	            	newpane.getChildren().addAll(vb);
	            	newpane.setMargin(vb,new Insets(70,0,0,30));
	            	Scene newScene = new Scene(newpane,900,700);
	            	newScene.getStylesheets().add
	    	        (Main.class.getResource("application.css").toExternalForm());
	            	s.setScene(newScene);
	            	EventHandler<ActionEvent> backevent = new EventHandler<ActionEvent>() { 
	    	            public void handle(ActionEvent e) 
	    	            { 
	    	    	        s.setScene(sc); 
	    	            }
	    	            
	            	};
	            	EventHandler<ActionEvent> enterevent = new EventHandler<ActionEvent>() { 
	    	            public void handle(ActionEvent e) 
	    	            { 
	    	            	student_id = String.valueOf(textField.getText());
	    	                year = Integer.valueOf(textField2.getText());
	    	                semester = Integer.valueOf(textField3.getText());
	    	                ArrayList<Object> t = new ArrayList<Object>();
	    	                t = getStudentRetakes(student_id,year,semester);
	    	                if(t.size()>0) {
	    	                	Label text = new Label("The result:");
		    	            	text.setStyle("-fx-text-fill: #FF0000;");
	 	    	            	Label std_id = new Label("Student id: " + t.get(0));
	 	    	            	Label year = new Label("Year: " + t.get(1));
	 	    	            	Label sem = new Label("Semester: " + t.get(2));
	 	    	            	Label quantity = new Label("Quantity: " + t.get(3));
	 	    	            	Label sem_sum = new Label("Semester sum: " + t.get(4));
	 	    	            	Label total_quan = new Label("Total quantity: " + t.get(5));
	 	    	            	Label total_sum = new Label("Total sum: " + t.get(6));
	 	    	            	
	 	    	            	vb.getChildren().addAll(text,std_id,year,sem,quantity,sem_sum,total_quan,total_sum);
	    	                }
	    	                else {
	    	                	Label text = new Label("No such result:");
	    	                	vb.getChildren().addAll(text);
	    	                }
	    	               
	    	            }
	    	            
	            	};
	            	back.setOnAction(backevent);
	            	enter.setOnAction(enterevent);

	            }
	            
	        };
	        tsk5.setOnAction(tsk5event);
	        //---------------------------------------------------------------------------------------
	        
	        //action for task6------------------------------------------------------------------------
	        EventHandler<ActionEvent> tsk6event = new EventHandler<ActionEvent>() { 
	            public void handle(ActionEvent e) 
	            { 
	            	GridPane newpane = new GridPane(); 
	            	  BackgroundFill background_fill = new BackgroundFill(Color.rgb(204, 229, 255),  
		      	                CornerRadii.EMPTY, Insets.EMPTY); 
		      			// create Background 
		      			Background background = new Background(background_fill); 
		      			newpane.setBackground(background);
	            	VBox vb = new VBox();
	            	Label qs = new Label("Get total hours of teacher for semester: ");
	            	Label label1 = new Label("Enter the teacher id:");
	            	Button back = new Button("Back");
	            	Button enter = new Button("Enter");
	            	TextField textField = new TextField ();
	            	
	            	HBox hb = new HBox();
	            	hb.getChildren().addAll(label1, textField);
	            	hb.setSpacing(10);
	            	vb.getChildren().addAll(back,qs,hb,enter);
	            	vb.setSpacing(10);
	            	newpane.getChildren().addAll(vb);
	            	newpane.setMargin(vb,new Insets(70,0,0,30));
	            	Scene newScene = new Scene(newpane,900,700);
	            	newScene.getStylesheets().add
	    	        (Main.class.getResource("application.css").toExternalForm());
	            	s.setScene(newScene);
	            	EventHandler<ActionEvent> backevent = new EventHandler<ActionEvent>() { 
	    	            public void handle(ActionEvent e) 
	    	            { 
	    	    	        s.setScene(sc); 
	    	            }
	    	            
	            	};
	            	EventHandler<ActionEvent> enterevent = new EventHandler<ActionEvent>() { 
	    	            public void handle(ActionEvent e) 
	    	            {
	    	                teacher_id = Integer.valueOf(textField.getText());
	    	                
	    	                ArrayList<ArrayList<Object>> t = new ArrayList<>();
	    	                t = getTeacherTotalHours(teacher_id);
	    	                Label text = new Label("The result:");
	    	            	text.setStyle("-fx-text-fill: #FF0000;");
	    	            	vb.getChildren().add(text);
	    	                for(int i = 0;i < t.size();i++) {
	    	                	Label tch_id = new Label("Teacher id: " + t.get(i).get(0));
		 	    	           	Label v_year = new Label("Year: " + t.get(i).get(1));
		 	    	           	Label v_term = new Label("Term: " + t.get(i).get(2));
		 	    	           	Label totalhours = new Label("Total hours: " + t.get(i).get(3));
		 	    	           	HBox newhb = new HBox();
		 		            	newhb.setSpacing(20);
		 	    	           	newhb.getChildren().addAll(tch_id,v_year,v_term,totalhours);	
		 	    	           	vb.getChildren().addAll(newhb);	

	    	                }
	    	                
	    	            }
	    	            
	            	};
	            	back.setOnAction(backevent);
	            	enter.setOnAction(enterevent);

	            }
	            
	        };
	        tsk6.setOnAction(tsk6event);
	        //----------------------------------------------------------------------------------------
	        
	        //action tsk7---------------------------------------------------------------------------
	        EventHandler<ActionEvent> tsk7event = new EventHandler<ActionEvent>() { 
	            public void handle(ActionEvent e) 
	            { 
	            	GridPane newpane = new GridPane(); 
	            	  BackgroundFill background_fill = new BackgroundFill(Color.rgb(204, 229, 255),  
		      	                CornerRadii.EMPTY, Insets.EMPTY); 
		      			// create Background 
		      			Background background = new Background(background_fill); 
		      			newpane.setBackground(background);
	            	VBox vb = new VBox();
	            	Label qs = new Label("Teachers rating for the semester: ");
	            	Label t_id = new Label("Enter the teacher id:");
	            	Label yearr = new Label("Enter the year:");
	            	Label term = new Label("Enter the term:");
	            	Button back = new Button("Back");
	            	Button enter = new Button("Enter");
	            	TextField textField = new TextField ();
	            	TextField textField2 = new TextField ();
	            	TextField textField3 = new TextField ();
	            	HBox hb = new HBox();
	            	hb.getChildren().addAll(t_id, textField);
	            	hb.setSpacing(10);
	            	
	            	HBox hb2 = new HBox();
	            	hb2.getChildren().addAll(yearr, textField2);
	            	hb2.setSpacing(10);
	            	
	            	HBox hb3 = new HBox();
	            	hb3.getChildren().addAll(term, textField3);
	            	hb3.setSpacing(10);
	            	vb.getChildren().addAll(back,qs,hb,hb2,hb3,enter);
	            	vb.setSpacing(10);
	            	newpane.getChildren().addAll(vb);
	            	newpane.setMargin(vb,new Insets(70,0,0,30));
	            	Scene newScene = new Scene(newpane,900,700);
	            	newScene.getStylesheets().add
	    	        (Main.class.getResource("application.css").toExternalForm());
	            	s.setScene(newScene);
	            	EventHandler<ActionEvent> backevent = new EventHandler<ActionEvent>() { 
	    	            public void handle(ActionEvent e) 
	    	            { 
	    	    	        s.setScene(sc); 
	    	            }
	    	            
	            	};
	            	back.setOnAction(backevent);
	            	
	            	EventHandler<ActionEvent> enterevent = new EventHandler<ActionEvent>() { 
	    	            public void handle(ActionEvent e) 
	    	            {
	    	            	TableView<TeacherDesign> table = new TableView<>();
	    	            	ObservableList<TeacherDesign> data = FXCollections.observableArrayList();
	    	            	table.setPrefWidth(900);
	    	            	table.setPrefHeight(500);
	 
	    	                teacher_id = Integer.valueOf(textField.getText());
	    	                year = Integer.valueOf(textField2.getText());
	    	                semester=Integer.valueOf(textField3.getText());
	    	                
	    	                ArrayList<ArrayList<Object>> t = new ArrayList<>();
	    	                t = getTeacherDesign(teacher_id,year,semester);
	    	                for(int i = 0;i < t.size();i++) {
	     	    	           	data.add(new TeacherDesign((int)t.get(i).get(0),(int)t.get(i).get(1),(int)t.get(i).get(2),(String)t.get(i).get(3),(String)t.get(i).get(4),(int)t.get(i).get(5)));
	    	                }
	    	                
	    	                TableColumn c_tchId = new TableColumn("Teacher id");
	    	                c_tchId.setCellValueFactory(new PropertyValueFactory<>("tch_id"));
	    	                
	    	                TableColumn c_year = new TableColumn("Year");
	    	                c_year.setCellValueFactory(new PropertyValueFactory<>("year"));
	    	                
	    	                TableColumn c_term = new TableColumn("Term");
	    	                c_term.setCellValueFactory(new PropertyValueFactory<>("term"));
	    	                
	    	                TableColumn c_crseCode = new TableColumn("Course code");
	    	                c_crseCode.setCellValueFactory(new PropertyValueFactory<>("courseCode"));
	    	                
	    	                TableColumn c_day = new TableColumn("Day");
	    	                c_day.setCellValueFactory(new PropertyValueFactory<>("day"));
	    	                
	    	                TableColumn c_hour = new TableColumn("Schedule hours");
	    	                c_hour.setCellValueFactory(new PropertyValueFactory<TeacherDesign,Integer>("hours"));

	    	                table.setItems(data);
	    	                table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	    	            	
	    	                table.getColumns().addAll(c_tchId,c_year,c_term,c_crseCode,c_day,c_hour);
	    	                
	    	                Group root = new Group();
	    	                
	    	                VBox vv = new VBox();
	    	            	EventHandler<ActionEvent> backevent = new EventHandler<ActionEvent>() { 
	    	    	            public void handle(ActionEvent e) 
	    	    	            { 
	    	    	    	        s.setScene(sc); 
	    	    	            }
	    	    	            
	    	            	};
	    	            	vv.getChildren().addAll(table,back);
	    	            	vv.setAlignment(Pos.CENTER);
	    	            	back.setOnAction(backevent);

	    	                root.getChildren().addAll(vv);
	    	            	Scene newScene = new Scene(root,900,700);
	    	            	newScene.getStylesheets().add
	    	    	        (Main.class.getResource("application.css").toExternalForm());
	    	            	s.setScene(newScene);
	    	            	
	    	            }
	    	            
	            	};
	            	enter.setOnAction(enterevent);
	                }
	            
	        };
	        tsk7.setOnAction(tsk7event);
	        //---------------------------------------------------------------------------------------
	   
	        //action tsk8---------------------------------------------------------------------------
	        EventHandler<ActionEvent> tsk8event = new EventHandler<ActionEvent>() { 
	            public void handle(ActionEvent e) 
	            { 
	            	GridPane newpane = new GridPane(); 
	            	  BackgroundFill background_fill = new BackgroundFill(Color.rgb(204, 229, 255),  
		      	                CornerRadii.EMPTY, Insets.EMPTY); 
		      			// create Background 
		      			Background background = new Background(background_fill); 
		      			newpane.setBackground(background);
	            	VBox vb = new VBox();
	            	Label qs = new Label("Schedule of student: ");
	            	Label std_id = new Label("Enter the student id:");
	            	Label yearr = new Label("Enter the year:");
	            	Label term = new Label("Enter the term:");
	            	Button back = new Button("Back");
	            	Button enter = new Button("Enter");
	            	TextField textField = new TextField ();
	            	TextField textField2 = new TextField ();
	            	TextField textField3 = new TextField ();
	            	HBox hb = new HBox();
	            	hb.getChildren().addAll(std_id, textField);
	            	hb.setSpacing(10);
	            	
	            	HBox hb2 = new HBox();
	            	hb2.getChildren().addAll(yearr, textField2);
	            	hb2.setSpacing(10);
	            	
	            	HBox hb3 = new HBox();
	            	hb3.getChildren().addAll(term, textField3);
	            	hb3.setSpacing(10);
	            	vb.getChildren().addAll(back,qs,hb,hb2,hb3,enter);
	            	vb.setSpacing(10);
	            	newpane.getChildren().addAll(vb);
	            	newpane.setMargin(vb,new Insets(70,0,0,30));
	            	Scene newScene = new Scene(newpane,900,700);
	            	newScene.getStylesheets().add
	    	        (Main.class.getResource("application.css").toExternalForm());
	            	s.setScene(newScene);
	            	EventHandler<ActionEvent> backevent = new EventHandler<ActionEvent>() { 
	    	            public void handle(ActionEvent e) 
	    	            { 
	    	    	        s.setScene(sc); 
	    	            }
	    	            
	            	};
	            	back.setOnAction(backevent);
	            	EventHandler<ActionEvent> enterevent = new EventHandler<ActionEvent>() { 
	    	            public void handle(ActionEvent e) 
	    	            {
	    	            	TableView<StudentDesign> table = new TableView<>();
	    	            	ObservableList<StudentDesign> data = FXCollections.observableArrayList();
	    	            	table.setPrefWidth(900);
	    	            	table.setPrefHeight(500);
	 
	    	                
	    	                student_id = String.valueOf(textField.getText());
	    	                year = Integer.valueOf(textField2.getText());
	    	                semester=Integer.valueOf(textField3.getText());
	    	                
	    	                ArrayList<ArrayList<Object>> t = new ArrayList<>();
	    	                t = getStudentDesign(student_id,year,semester);
	    	                for(int i = 0;i < t.size();i++) {
	     	    	           	
	     	    	           	data.add(new StudentDesign((String)t.get(i).get(0),(int)t.get(i).get(1),(int)t.get(i).get(2),(String)t.get(i).get(3),(String)t.get(i).get(4),(int)t.get(i).get(5)));

	    	                }
	    	                
	    	                TableColumn c_stdId = new TableColumn("Student id");
	    	                c_stdId.setCellValueFactory(new PropertyValueFactory<>("std_id"));
	    	                
	    	                TableColumn c_year = new TableColumn("Year");
	    	                c_year.setCellValueFactory(new PropertyValueFactory<StudentDesign,Integer>("year"));
	    	                
	    	                TableColumn c_term = new TableColumn("Term");
	    	                c_term.setCellValueFactory(new PropertyValueFactory<StudentDesign,Integer>("term"));
	    	                
	    	                TableColumn c_crseCode = new TableColumn("Course code");
	    	                c_crseCode.setCellValueFactory(new PropertyValueFactory<StudentDesign,String>("courseCode"));
	    	                
	    	                TableColumn c_day = new TableColumn("Day");
	    	                c_day.setCellValueFactory(new PropertyValueFactory<StudentDesign,String>("day"));
	    	                
	    	                TableColumn c_hour = new TableColumn("Schedule hours");
	    	                c_hour.setCellValueFactory(new PropertyValueFactory<StudentDesign,Integer>("hours"));

	    	                table.setItems(data);
	    	                table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	    	            	
	    	                table.getColumns().addAll(c_stdId,c_year,c_term,c_crseCode,c_day,c_hour);
	    	                
	    	                
	    	                
	    	                Group root = new Group();
	    	                
	    	                VBox vv = new VBox();
	    	            	EventHandler<ActionEvent> backevent = new EventHandler<ActionEvent>() { 
	    	    	            public void handle(ActionEvent e) 
	    	    	            { 
	    	    	    	        s.setScene(sc); 
	    	    	            }
	    	    	            
	    	            	};
	    	            	vv.getChildren().addAll(table,back);
	    	            	vv.setAlignment(Pos.CENTER);
	    	            	back.setOnAction(backevent);

	    	                root.getChildren().addAll(vv);
	    	            	Scene newScene = new Scene(root,900,700);
	    	            	newScene.getStylesheets().add
	    	    	        (Main.class.getResource("application.css").toExternalForm());
	    	            	s.setScene(newScene);
	    	            	
	    	            }
	    	            
	            	};
	            	enter.setOnAction(enterevent);
	                }
	            
	        };
	        tsk8.setOnAction(tsk8event);
	        //---------------------------------------------------------------------------------------
	        
	        //action for task 9------------------------------------------------------------------------
	        EventHandler<ActionEvent> tsk9event = new EventHandler<ActionEvent>() { 
	            public void handle(ActionEvent e) 
	            { 
	            	GridPane newpane = new GridPane();
	            	  BackgroundFill background_fill = new BackgroundFill(Color.rgb(204, 229, 255),  
		      	                CornerRadii.EMPTY, Insets.EMPTY); 
		      			// create Background 
		      			Background background = new Background(background_fill); 
		      			newpane.setBackground(background);
	            	VBox vb = new VBox();
	            	Label qs = new Label("Display how many subjects and credits was selected by student: ");
	            	Label std_id = new Label("Enter the student id:");
	            	Label year_of = new Label("Enter the year:");
	            	Label term = new Label("Enter the semester:");

	            	Button back = new Button("Back");
	            	Button enter = new Button("Enter");
	            	TextField textField = new TextField ();
	            	TextField textField2 = new TextField ();
	            	TextField textField3 = new TextField ();

	            	HBox hb = new HBox();
	            	hb.getChildren().addAll(std_id, textField);
	            	hb.setSpacing(10);
	            	
	            	HBox hb2 = new HBox();
	            	hb2.getChildren().addAll(year_of,textField2);
	            	hb2.setSpacing(10);
	            	
	            	HBox hb3 = new HBox();
	            	hb3.getChildren().addAll(term,textField3,enter);
	            	hb3.setSpacing(10);
	            	
	            	vb.getChildren().addAll(back,qs,hb,hb2,hb3);
	            	vb.setSpacing(10);
	            	newpane.getChildren().addAll(vb);
	            	newpane.setMargin(vb,new Insets(70,0,0,30));
	            	Scene newScene = new Scene(newpane,900,700);
	            	newScene.getStylesheets().add
	    	        (Main.class.getResource("application.css").toExternalForm());
	            	s.setScene(newScene);
	            	EventHandler<ActionEvent> backevent = new EventHandler<ActionEvent>() { 
	    	            public void handle(ActionEvent e) 
	    	            { 
	    	    	        s.setScene(sc); 
	    	            }
	    	            
	            	};
	            	EventHandler<ActionEvent> enterevent = new EventHandler<ActionEvent>() { 
	    	            public void handle(ActionEvent e) 
	    	            { 
	    	            	student_id = String.valueOf(textField.getText());
	    	            	year = Integer.valueOf(textField2.getText());
	    	            	semester = Integer.valueOf(textField3.getText());
	    	            	
	    	                ArrayList<Object> t = new ArrayList<Object>();
	    	            	t = getStudentCredit(student_id,year,semester);
	    	            	System.out.println(t);
	    	            	
	    	            	Label text = new Label("The result:");
	    	            	Label std_id = new Label("Student_id: " + t.get(0));
	    	            	Label yyear = new Label("Year: " + t.get(1));
	    	            	Label term = new Label("Semester: " + t.get(2));
	    	            	Label subjects = new Label("Count of subjects: " + t.get(3));
	    	            	Label cred = new Label("Sum of credits: " + t.get(4));
	    	            	vb.getChildren().addAll(text,std_id,yyear,term,subjects,cred);
	    	            }
	    	            
	            	};
	            	back.setOnAction(backevent);
	            	enter.setOnAction(enterevent);       	
	            } 
	        }; 
	        tsk9.setOnAction(tsk9event); 
	        //---------------------------------------------------------------------------------------
	        
	        
	        //action for tsk10-----------------------------------------------------------------------
	        EventHandler<ActionEvent> tsk10event = new EventHandler<ActionEvent>() { 
	            public void handle(ActionEvent e) 
	            { 
	            	GridPane newpane = new GridPane(); 
	            	  BackgroundFill background_fill = new BackgroundFill(Color.rgb(204, 229, 255),  
		      	                CornerRadii.EMPTY, Insets.EMPTY); 
		      			// create Background 
		      			Background background = new Background(background_fill); 
		      			newpane.setBackground(background);
	            	VBox vb = new VBox();
	            	Label qs = new Label("Display the flow of clever student: ");
	            	Label label1 = new Label("Enter the teacher id:");
	            	Label label2 = new Label("Enter the course_code:");
	            	Button back = new Button("Back");
	            	Button enter = new Button("Enter");
	            	TextField textField = new TextField ();
	            	TextField textField2 = new TextField ();

	            	HBox hb = new HBox();
	            	hb.getChildren().addAll(label1, textField);
	            	hb.setSpacing(10);
	            	HBox hb2 = new HBox();
	            	hb2.getChildren().addAll(label2,textField2,enter);
	            	hb2.setSpacing(10);
	            	vb.getChildren().addAll(back,qs,hb,hb2);
	            	vb.setSpacing(10);
	            	newpane.getChildren().addAll(vb);
	            	newpane.setMargin(vb,new Insets(70,0,0,30));
	            	Scene newScene = new Scene(newpane,900,700);
	            	newScene.getStylesheets().add
	    	        (Main.class.getResource("application.css").toExternalForm());
	            	s.setScene(newScene);
	            	EventHandler<ActionEvent> backevent = new EventHandler<ActionEvent>() { 
	    	            public void handle(ActionEvent e) 
	    	            { 
	    	    	        s.setScene(sc); 
	    	            }
	    	            
	            	};
	            	back.setOnAction(backevent);
	            	EventHandler<ActionEvent> enterevent = new EventHandler<ActionEvent>() { 
	    	            public void handle(ActionEvent e) 
	    	            {
	    	            	TableView<CleverStudents> table = new TableView<>();
	    	            	ObservableList<CleverStudents> data = FXCollections.observableArrayList();
	    	            	table.setPrefWidth(900);
	    	            	table.setPrefHeight(500);
	    	                
	    	                teacher_id=Integer.valueOf(textField.getText());
	    	                course_code = String.valueOf(textField2.getText());
	    	                
	    	                ArrayList<ArrayList<Object>> t = new ArrayList<>();
	    	                t = getCleverStudents(teacher_id,course_code);
	    	                for(int i = 0;i < t.size();i++) {
	     	    	           	data.add(new CleverStudents((String)t.get(i).get(0),(int)t.get(i).get(1),(String)t.get(i).get(2),(int)t.get(i).get(3)));
	     	    	         }
	    	                
	    	                TableColumn c_stdId = new TableColumn("Student id");
	    	                c_stdId.setCellValueFactory(new PropertyValueFactory<>("std_id"));

	    	                TableColumn c_t = new TableColumn("Teacher id");
	    	                c_t.setCellValueFactory(new PropertyValueFactory<CleverStudents,Integer>("year"));
	    	                
	    	                TableColumn c_crseCode = new TableColumn("Course code");
	    	                c_crseCode.setCellValueFactory(new PropertyValueFactory<CleverStudents,String>("courseCode"));
	    	                
	    	                TableColumn c_avg = new TableColumn("Average rate");
	    	                c_avg.setCellValueFactory(new PropertyValueFactory<CleverStudents,Integer>("hours"));
	    	                
	    	                table.setItems(data);
	    	                table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	    	            	
	    	                table.getColumns().addAll(c_stdId,c_t,c_crseCode,c_avg);
	    	                
	    	                Group root = new Group();
	    	                
	    	                VBox vv = new VBox();
	    	            	EventHandler<ActionEvent> backevent = new EventHandler<ActionEvent>() { 
	    	    	            public void handle(ActionEvent e) 
	    	    	            { 
	    	    	    	        s.setScene(sc); 
	    	    	            }
	    	    	            
	    	            	};
	    	            	vv.getChildren().addAll(table,back);
	    	            	vv.setAlignment(Pos.CENTER);
	    	            	back.setOnAction(backevent);

	    	                root.getChildren().addAll(vv);
	    	            	Scene newScene = new Scene(root,900,700);
	    	            	newScene.getStylesheets().add
	    	    	        (Main.class.getResource("application.css").toExternalForm());
	    	            	s.setScene(newScene);
	    	            	
	    	            }
	    	            
	            	};
	            	enter.setOnAction(enterevent);
	                }
	            
	        };
	        tsk10.setOnAction(tsk10event);
	        //---------------------------------------------------------------------------------------
	        
	        //action tsk11---------------------------------------------------------------------------
	        EventHandler<ActionEvent> tsk11event = new EventHandler<ActionEvent>() { 
	            public void handle(ActionEvent e) 
	            { 
	            	GridPane newpane = new GridPane(); 
	            	  BackgroundFill background_fill = new BackgroundFill(Color.rgb(204, 229, 255),  
		      	                CornerRadii.EMPTY, Insets.EMPTY); 
		      			// create Background 
		      			Background background = new Background(background_fill); 
		      			newpane.setBackground(background);
	            	VBox vb = new VBox();
	            	Label qs = new Label("Teachers rating for the semester: ");
	            	Label term = new Label("Enter the term:");
	            	Label yearr = new Label("Enter the year");
	            	Button back = new Button("Back");
	            	Button enter = new Button("Enter");
	            	TextField textField = new TextField ();
	            	TextField textField2 = new TextField ();

	            	HBox hb = new HBox();
	            	hb.getChildren().addAll(term, textField);
	            	hb.setSpacing(10);
	            	
	            	HBox hb2 = new HBox();
	            	hb2.getChildren().addAll(yearr, textField2);
	            	hb2.setSpacing(10);
	            	
	            	vb.getChildren().addAll(back,qs,hb,hb2,enter);
	            	vb.setSpacing(10);
	            	newpane.getChildren().addAll(vb);
	            	newpane.setMargin(vb,new Insets(70,0,0,30));
	            	Scene newScene = new Scene(newpane,900,700);
	            	newScene.getStylesheets().add
	    	        (Main.class.getResource("application.css").toExternalForm());
	            	s.setScene(newScene);
	            	EventHandler<ActionEvent> backevent = new EventHandler<ActionEvent>() { 
	    	            public void handle(ActionEvent e) 
	    	            { 
	    	    	        s.setScene(sc); 
	    	            }
	    	            
	            	};
	            	back.setOnAction(backevent);
	            	EventHandler<ActionEvent> enterevent = new EventHandler<ActionEvent>() { 
	    	            public void handle(ActionEvent e) 
	    	            {
	    	            	TableView<TeacherRate> table = new TableView<>();
	    	            	ObservableList<TeacherRate> data = FXCollections.observableArrayList();
	    	            	table.setPrefWidth(900);
	    	            	table.setPrefHeight(500);
	    	                
	    	                semester=Integer.valueOf(textField.getText());
	    	                year = Integer.valueOf(textField2.getText());
	    	                
	    	                ArrayList<ArrayList<Object>> t = new ArrayList<>();
	    	                t = getTeachersRateByYear(semester,year);
	    	                for(int i = 0;i < t.size();i++) {
	     	    	           	data.add(new TeacherRate((int)t.get(i).get(0),(int)t.get(i).get(1),(int)t.get(i).get(2),(int)t.get(i).get(3)));   	
	    	                }
	    	                
	    	                TableColumn c_rate = new TableColumn("Rate");
	    	                c_rate.setCellValueFactory(new PropertyValueFactory<TeacherRate,Integer>("rate"));
	    	                
	    	                TableColumn c_hour = new TableColumn("Teacher id");
	    	                c_hour.setCellValueFactory(new PropertyValueFactory<TeacherRate,Integer>("hours"));
	    	                
	    	                TableColumn c_term = new TableColumn("Term");
	    	                c_term.setCellValueFactory(new PropertyValueFactory<TeacherRate,Integer>("term"));
	    	 	    	                
	    	                TableColumn c_year = new TableColumn("Year");
	    	                c_year.setCellValueFactory(new PropertyValueFactory<TeacherRate,Integer>("year"));
	    	                
	    	              
	    	               
	    	                table.setItems(data);
	    	                table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	    	            	
	    	                table.getColumns().addAll(c_rate,c_hour,c_term,c_year);
	    	                
	    	                Group root = new Group();
	    	                
	    	                VBox vv = new VBox();
	    	            	EventHandler<ActionEvent> backevent = new EventHandler<ActionEvent>() { 
	    	    	            public void handle(ActionEvent e) 
	    	    	            { 
	    	    	    	        s.setScene(sc); 
	    	    	            }
	    	    	            
	    	            	};
	    	            	vv.getChildren().addAll(table,back);
	    	            	vv.setAlignment(Pos.CENTER);
	    	            	back.setOnAction(backevent);

	    	                root.getChildren().addAll(vv);
	    	            	Scene newScene = new Scene(root,900,700);
	    	            	newScene.getStylesheets().add
	    	    	        (Main.class.getResource("application.css").toExternalForm());
	    	            	s.setScene(newScene);
	    	            	
	    	            }
	    	            
	            	};
	            	enter.setOnAction(enterevent);
	                }
	            
	        };
	        tsk11.setOnAction(tsk11event);
	        //---------------------------------------------------------------------------------------
	       
	        //action task 12-------------------------------------------------------------------------
	        EventHandler<ActionEvent> tsk12event = new EventHandler<ActionEvent>() { 
	            public void handle(ActionEvent e) 
	            { 
	            	GridPane newpane = new GridPane(); 
	            	  BackgroundFill background_fill = new BackgroundFill(Color.rgb(204, 229, 255),  
		      	                CornerRadii.EMPTY, Insets.EMPTY); 
		      			// create Background 
		      			Background background = new Background(background_fill); 
		      			newpane.setBackground(background);
	            	VBox vb = new VBox();
	            	Label qs = new Label("Course rating for the semester: ");
	            	Label termm = new Label("Enter the term:");
	            	Label yearr = new Label("Enter the year: ");
	            	Button back = new Button("Back");
	            	Button enter = new Button("Enter");
	            	TextField textField = new TextField ();
	            	TextField textField2 = new TextField ();

	            	HBox hb = new HBox();
	            	hb.getChildren().addAll(termm, textField);
	            	hb.setSpacing(10);
	            	
	            	HBox hb2 = new HBox();
	            	hb2.getChildren().addAll(yearr, textField2);
	            	hb2.setSpacing(10);
	            	
	            	vb.getChildren().addAll(back,qs,hb,hb2,enter);
	            	vb.setSpacing(10);
	            	newpane.getChildren().addAll(vb);
	            	newpane.setMargin(vb,new Insets(70,0,0,30));
	            	Scene newScene = new Scene(newpane,900,700);
	            	newScene.getStylesheets().add
	    	        (Main.class.getResource("application.css").toExternalForm());
	            	s.setScene(newScene);
	            	EventHandler<ActionEvent> backevent = new EventHandler<ActionEvent>() { 
	    	            public void handle(ActionEvent e) 
	    	            { 
	    	    	        s.setScene(sc); 
	    	            }
	    	            
	            	};
	            	back.setOnAction(backevent);
	            	EventHandler<ActionEvent> enterevent = new EventHandler<ActionEvent>() { 
	    	            public void handle(ActionEvent e) 
	    	            {
	    	            	TableView<SubjectRate> table = new TableView<>();
	    	            	ObservableList<SubjectRate> data = FXCollections.observableArrayList();
	    	            	table.setPrefWidth(900);
	    	            	table.setPrefHeight(500);
	    	                
	    	                semester=Integer.valueOf(textField.getText());
	    	                year = Integer.valueOf(textField2.getText());
	    	                ArrayList<ArrayList<Object>> t = new ArrayList<>();
	    	                t = getCourseRateByYear(semester,year);
	    	                for(int i = 0;i < t.size();i++) {
	     	    	           	data.add(new SubjectRate((int)t.get(i).get(0),(String)t.get(i).get(1),(int)t.get(i).get(2),(int)t.get(i).get(3)));   	
	     	    	           	}
	    	                
	    	                TableColumn c_rate = new TableColumn("Rate");
	    	                c_rate.setCellValueFactory(new PropertyValueFactory<SubjectRate,Integer>("rate"));
	    	                
	    	                TableColumn c_crsCode = new TableColumn("Teacher id");
	    	                c_crsCode.setCellValueFactory(new PropertyValueFactory<SubjectRate,Integer>("courseCode"));
	    	                
	    	                TableColumn c_term = new TableColumn("Term");
	    	                c_term.setCellValueFactory(new PropertyValueFactory<SubjectRate,Integer>("term"));
	    	 	    	                
	    	                TableColumn c_year = new TableColumn("Year");
	    	                c_year.setCellValueFactory(new PropertyValueFactory<SubjectRate,Integer>("year"));
	    	                
	    	                table.setItems(data);
	    	                table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	    	            	
	    	                table.getColumns().addAll(c_rate,c_crsCode,c_term,c_year);
	    	                
	    	                
	    	                Group root = new Group();
	    	                
	    	                VBox vv = new VBox();
	    	            	EventHandler<ActionEvent> backevent = new EventHandler<ActionEvent>() { 
	    	    	            public void handle(ActionEvent e) 
	    	    	            { 
	    	    	    	        s.setScene(sc); 
	    	    	            }
	    	    	            
	    	            	};
	    	            	vv.getChildren().addAll(table,back);
	    	            	vv.setAlignment(Pos.CENTER);
	    	            	back.setOnAction(backevent);

	    	                root.getChildren().addAll(vv);
	    	            	Scene newScene = new Scene(root,900,700);
	    	            	newScene.getStylesheets().add
	    	    	        (Main.class.getResource("application.css").toExternalForm());
	    	            	s.setScene(newScene);
	    	            	
	    	            }
	    	            
	            	};
	            	enter.setOnAction(enterevent);
	                }
	            
	        };
	        tsk12.setOnAction(tsk12event);
	        //---------------------------------------------------------------------------------------
	        
	        //action tsk13---------------------------------------------------------------------------
	        EventHandler<ActionEvent> tsk13event = new EventHandler<ActionEvent>() { 
	            public void handle(ActionEvent e) 
	            { 
	                ArrayList<Object> t = new ArrayList<>();
	                t = getRetakeProfit();
 	    	           	Label rtcount = new Label("Retake count: " + t.get(0)+"  Profit: " + t.get(1));
 	    	           	
 	    	          
	                
	                BorderPane root = new BorderPane();
	            	  BackgroundFill background_fill = new BackgroundFill(Color.rgb(204, 229, 255),  
		      	                CornerRadii.EMPTY, Insets.EMPTY); 
		      			// create Background 
		      			Background background = new Background(background_fill); 
		      			root.setBackground(background);
	                
	                Button back = new Button("Back");
	            	EventHandler<ActionEvent> backevent = new EventHandler<ActionEvent>() { 
	    	            public void handle(ActionEvent e) 
	    	            { 
	    	    	        s.setScene(sc); 
	    	            }
	    	            
	            	};
	            	back.setOnAction(backevent);
	            	
	                root.setCenter(rtcount);
	                root.setTop(back);
	            	Scene newScene = new Scene(root,900,700);
	            	newScene.getStylesheets().add
	    	        (Main.class.getResource("application.css").toExternalForm());
	            	s.setScene(newScene);
	            

	                
	            }
	        };
	        tsk13.setOnAction(tsk13event);
	        //---------------------------------------------------------------------------------------
	        
	        
	        //action tsk14---------------------------------------------------------------------------
	        EventHandler<ActionEvent> tsk14event = new EventHandler<ActionEvent>() { 
	            public void handle(ActionEvent e) 
	            { 
	                ArrayList<Object> t = new ArrayList<>();
	                t = getRetakeSubject();
 	    	           	Label rtcount = new Label("Retake count: " + t.get(0)+"  Course code: " + t.get(1));
	                
	                BorderPane root = new BorderPane();
	            	  BackgroundFill background_fill = new BackgroundFill(Color.rgb(204, 229, 255),  
		      	                CornerRadii.EMPTY, Insets.EMPTY); 
		      			// create Background 
		      			Background background = new Background(background_fill); 
		      			root.setBackground(background);
	                
	                Button back = new Button("Back");
	            	EventHandler<ActionEvent> backevent = new EventHandler<ActionEvent>() { 
	    	            public void handle(ActionEvent e) 
	    	            { 
	    	    	        s.setScene(sc); 
	    	            }
	    	            
	            	};
	            	back.setOnAction(backevent);
	            	
	                root.setCenter(rtcount);
	                root.setTop(back);
	            	Scene newScene = new Scene(root,900,700);
	            	newScene.getStylesheets().add
	    	        (Main.class.getResource("application.css").toExternalForm());
	            	s.setScene(newScene);
	           
	                
	            }
	        };
	        tsk14.setOnAction(tsk14event);
	        //---------------------------------------------------------------------------------------
	        
	        //action for tsk4-------------------------------------------------------------------------
	        EventHandler<ActionEvent> tsk15event = new EventHandler<ActionEvent>() { 
	            public void handle(ActionEvent e) 
	            { 
	            	TableView<CertainTeacher> table = new TableView<>();
	            	ObservableList<CertainTeacher> data = FXCollections.observableArrayList();
	            	table.setPrefWidth(900);
	            	table.setPrefHeight(500);

	                ArrayList<ArrayList<Object>> t = new ArrayList<>();
	                t = getCertainTeacher();
	                for(int i = 0;i < t.size();i++) {
 	    	           	data.add(new CertainTeacher((int)t.get(i).get(0),(String)t.get(i).get(1),(int)t.get(i).get(2)));
	                }
	                
	                TableColumn c_count = new TableColumn("Student count");
	                c_count.setCellValueFactory(new PropertyValueFactory<StudentDesign,Integer>("count"));
	                
	                TableColumn crsCode = new TableColumn("Course code");
	                crsCode.setCellValueFactory(new PropertyValueFactory<CertainTeacher,String>("courseCode"));

	                TableColumn<CertainTeacher,Integer> c_tchId = new TableColumn<CertainTeacher,Integer>("Teacher id");
	                c_tchId.setCellValueFactory(new PropertyValueFactory<CertainTeacher,Integer>("tch"));
	                
	                table.setItems(data);
	                table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	            	
	                table.getColumns().addAll(c_count,crsCode, c_tchId);
	          
	                Group root = new Group();
	                
	                VBox vv = new VBox();
	                Button back = new Button("Back");
	            	EventHandler<ActionEvent> backevent = new EventHandler<ActionEvent>() { 
	    	            public void handle(ActionEvent e) 
	    	            { 
	    	    	        s.setScene(sc); 
	    	            }
	    	            
	            	};
	            	vv.getChildren().addAll(table,back);
	            	vv.setAlignment(Pos.CENTER);
	            	back.setOnAction(backevent);

	                root.getChildren().addAll(vv);
	            	Scene newScene = new Scene(root,900,700);
	            	newScene.getStylesheets().add
	    	        (Main.class.getResource("application.css").toExternalForm());
	            	s.setScene(newScene);
	            
	                }
	            
	        };
	        tsk15.setOnAction(tsk15event);
	        //----------------------------------------------------------------------------------------
	        GridPane gridPane = createRegistrationFormPane();
	        addUIControls(gridPane,r,s);
	       
	        // create a scene 
	        sc = new Scene(gridPane, 900, 700); 
	        
	        // set the scene 
	        s.setScene(sc); 
	        
	        s.show(); 
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
