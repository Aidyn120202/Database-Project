CREATE OR REPLACE TYPE objNoneRegStudents AS OBJECT ( --Task4
    max_count NUMBER,
    student_id VARCHAR(200),
    term NUMBER,
    s_year NUMBER
);

CREATE OR REPLACE TYPE objStudentRetakes IS OBJECT ( --Task5
  student_id        VARCHAR(200),
  year              NUMBER,
  semester          NUMBER,
  quantity          NUMBER,
  semester_sum      NUMBER,
  total_quantity    NUMBER,
  total_sum         NUMBER
);


CREATE OR REPLACE TYPE objStudentDesign IS OBJECT ( --Task8
  student_id        VARCHAR(200),
  year              NUMBER,
  term              NUMBER,
  course_code       VARCHAR(20),
   day_schedule      VARCHAR(20),
  hours_schedule_student NUMBER
);

CREATE OR REPLACE TYPE objStudentCredit IS OBJECT (  --Task9
  student_id        VARCHAR(200),
  year              NUMBER,
  semester          NUMBER,
  total_subjects       VARCHAR(20),
  total_credits           NUMBER
);


CREATE OR REPLACE TYPE objCleverStudents IS OBJECT ( --Task10
  student_id        VARCHAR(200),
  teacher_id        NUMBER,
  course_code       VARCHAR(20),
  Average_rate      NUMBER);

DROP TYPE objNoneRegStudents FORCE; --Task4
DROP TYPE objStudentRetakes FORCE; --Task5
DROP TYPE objStudentDesign FORCE; --Task8
DROP TYPE objStudentCredit FORCE; --Task9
DROP TYPE objCleverStudents FORCE; --Task10


----------------------------------------------
CREATE OR REPLACE TYPE tblNoneRegStudents IS TABLE OF objNoneRegStudents;--Task4
CREATE OR REPLACE TYPE tblStudentRetakes IS TABLE OF objStudentRetakes; --Task5
CREATE OR REPLACE TYPE tblStudentDesign IS TABLE OF objStudentDesign; --Task8
CREATE OR REPLACE TYPE tblStudentCredit IS TABLE OF objStudentCredit; --Task9
CREATE OR REPLACE TYPE tblCleverStudents IS TABLE OF objCleverStudents; --Task10


DROP TYPE tblNoneRegStudents FORCE; --Task4
DROP TYPE tblStudentRetakes FORCE; --Task5
DROP TYPE tblStudentDesign FORCE; --Task8
DROP TYPE tblStudentCredit FORCE; --Task9
DROP TYPE tblCleverStudents FORCE; --Task10

----------------------------------------------
CREATE OR REPLACE PACKAGE pkgStudent AS
FUNCTION getNoneRegStudents RETURN tblNoneRegStudents; --Function Task4
FUNCTION getStudentRetakes(student_id VARCHAR, g_year NUMBER, g_term NUMBER) RETURN tblStudentRetakes; --Function Task5
FUNCTION getStudentDesign(student_id IN VARCHAR, g_year NUMBER, g_term NUMBER) RETURN tblStudentDesign; --Function Task8
FUNCTION getStudentCredit(student_id IN VARCHAR, p_year NUMBER, p_term NUMBER) RETURN tblStudentCredit; --Function Task9
FUNCTION getCleverStudents(teacher_id NUMBER, course_code VARCHAR) RETURN tblCleverStudents;         --Function Task10
END pkgStudent;
----------------------------------------------



CREATE OR REPLACE PACKAGE BODY pkgStudent AS
FUNCTION getNoneRegStudents RETURN tblNoneRegStudents AS --Task4 Function

        tbl tblNoneRegStudents := tblNoneRegStudents();

        max_count NUMBER(20);
    	stud_id VARCHAR(200);
    	term NUMBER(20);
        year NUMBER(20);

        CURSOR cur IS SELECT 
                count(*) AS s_count, 
                cs.stud_id, 
                cs.term,
                cs.year
              FROM COURSE_SELECTIONS cs 
                WHERE cs.reg_date IS NULL AND cs.attended IS NULL 
                GROUP BY cs.year, cs.term, cs.stud_id 
                ORDER BY s_count;

    BEGIN
        OPEN cur;
            LOOP FETCH cur INTO  
                max_count,
                stud_id,
                term,
                year;
            EXIT WHEN cur%NOTFOUND;

                tbl.EXTEND;
                tbl(tbl.LAST) := objNoneRegStudents(max_count, stud_id, term, year);
                
        END LOOP;
    CLOSE cur;

    RETURN tbl;
    END getNoneRegStudents;
    
FUNCTION getStudentRetakes(student_id VARCHAR, g_year NUMBER, g_term NUMBER) RETURN tblStudentRetakes                --Task5 Function
    IS
        tbl tblStudentRetakes := tblStudentRetakes();
 
    BEGIN
  FOR rec IN (SELECT 
        cs.STUD_ID, 
        cs.YEAR, 
        cs.TERM AS SEMESTER, 
        count(*) AS SEMESTER_SUM, --quantity
        (SELECT count(*) AS SEMESTER_SUM FROM course_selections WHERE STUD_ID = cs.STUD_ID) AS TOTAL_SUM,  --total quantity
        (SELECT DISTINCT CREDITS FROM course_sections WHERE YEAR = cs.YEAR AND TERM = cs.TERM) AS CREDIT
    FROM course_selections cs
         WHERE 
            cs.QIYMET_HERF IN ('F', 'FX') AND 
            cs.STUD_ID = student_id AND
            cs.YEAR = g_year AND
            cs.TERM = g_term
            GROUP BY cs.YEAR, cs.TERM, cs.STUD_ID)
        LOOP
            
            tbl.EXTEND;
            tbl(tbl.LAST) := objStudentRetakes(rec.stud_id, 
                                               rec.year, 
                                               rec.SEMESTER, 
                                               rec.SEMESTER_SUM,
                                               rec.SEMESTER_SUM * 25000 * rec.CREDIT, 
                                               rec.TOTAL_SUM,
                                               rec.TOTAL_SUM * 25000 * rec.CREDIT);

      END LOOP;
   RETURN(tbl);
   END getStudentRetakes;


FUNCTION getStudentDesign(student_id IN VARCHAR, g_year NUMBER, g_term NUMBER) RETURN tblStudentDesign                     --Task8 Function
    IS
        tbl tblStudentDesign := tblStudentDesign();
    
    BEGIN
    FOR rec IN (SELECT 
        selections.stud_id AS STUDENT_ID, 
        schedule.year, 
        schedule.term AS SEMESTER, 
        schedule.ders_kod AS COURSE_CODE,
       (to_char(cast(schedule.start_time as DATE),'Day','NLS_DATE_LANGUAGE = ENGLISH')) as day_schedule,
        extract(HOUR from schedule.start_time) as hours_schedule_student ,
        selections.section 
FROM course_schedule schedule
        INNER JOIN course_selections selections ON
            schedule.ders_kod = selections.ders_kod AND 
            schedule.year = selections.year AND
            schedule.term = selections.term AND selections.section=schedule.section AND
            selections.stud_id = student_id AND
            schedule.year = g_year AND
            schedule.term = g_term
        GROUP BY schedule.year, schedule.term, schedule.ders_kod, selections.stud_id,schedule.start_time,selections.section 
        ORDER BY schedule.year,schedule.start_time asc,schedule.ders_kod asc)
        
        LOOP
            tbl.EXTEND;
            tbl(tbl.LAST) := objStudentDesign(rec.STUDENT_ID, 
                                    rec.year, 
                                    rec.SEMESTER, 
                                    rec.COURSE_CODE,
                                    rec.day_schedule,
                                    rec.hours_schedule_student);
        END LOOP;
        
        RETURN tbl;
    END getStudentDesign;
    
    
    
FUNCTION getStudentCredit(student_id IN VARCHAR, p_year NUMBER, p_term NUMBER) RETURN tblStudentCredit                  --Task9 Function
    IS
        tbl tblStudentCredit := tblStudentCredit();
        c NUMBER(5);
            
    BEGIN
    SELECT DISTINCT CREDITS INTO c FROM course_sections;
    for cur IN (SELECT count(*) as total_subjects FROM(
            SELECT  selections.stud_id , 
                    schedule.year, 
                    schedule.term , 
                    schedule.ders_kod,
                    schedule.section
                FROM course_schedule schedule
                    INNER JOIN course_selections selections ON
                        schedule.ders_kod = selections.ders_kod AND schedule.section=selections.section and
                        schedule.year = selections.year AND
                        schedule.term = selections.term AND
                        selections.stud_id = student_id AND
                        schedule.year = p_year AND
                        schedule.term = p_term
                    GROUP BY schedule.year, schedule.term, schedule.ders_kod, selections.stud_id,schedule.section
                    ORDER BY schedule.year))
        LOOP
        FOR cur1 IN (SELECT  selections.stud_id AS student, 
                    schedule.year, 
                    schedule.term AS semester
                FROM course_schedule schedule
                    INNER JOIN course_selections selections ON
                        schedule.year = selections.year AND
                        schedule.term = selections.term AND
                        selections.stud_id = student_id AND
                        selections.year = p_year AND
                        selections.term = p_term
                    GROUP BY schedule.year, schedule.term,  selections.stud_id
                    ORDER BY schedule.year)
                    LOOP
                tbl.EXTEND;
                tbl(tbl.LAST) := objStudentCredit(cur1.student,
                                                cur1.year,
                                                cur1.semester,
                                                cur.total_subjects,
                                                cur.total_subjects*c);
        END LOOP;
       END LOOP;
   
        RETURN tbl;
    END getStudentCredit;


FUNCTION getCleverStudents(teacher_id NUMBER, course_code VARCHAR) RETURN tblCleverStudents                   --Task10 Function
    AS
    tbl tblCleverStudents := tblCleverStudents();

    BEGIN
    FOR rec IN ( SELECT stud_id AS STUDENT_ID,practice AS TEACHER_ID, 
        DERS_KOD AS COURSE_CODE,qiymet_yuz as AVERAGE_RATE from course_selections where 
          qiymet_yuz >(select round(avg(qiymet_yuz),2) 
          from course_selections  where practice = teacher_id  and DERS_KOD = course_code) 
        AND  QIYMET_YUZ IS NOT NULL 
        AND practice IS NOT NULL 
        AND practice = teacher_id 
        AND DERS_KOD = course_code
    GROUP BY practice, DERS_KOD, stud_id,qiymet_yuz
    ORDER BY Average_Rate DESC)
    
   
        
        LOOP
            tbl.EXTEND;
            tbl(tbl.LAST) := objCleverStudents(rec.STUDENT_ID, 
                                    rec.TEACHER_ID, 
                                    rec.COURSE_CODE, 
                                    rec.AVERAGE_RATE);
        END LOOP;
    RETURN tbl;    
    END getCleverStudents;
    
  
END pkgStudent;

----------------------------------------------
SELECT * FROM TABLE(pkgStudent.getNoneRegStudents()); --Task4
SELECT * FROM TABLE(pkgStudent.getstudentretakes('A41A708C44F7128B95A0A52E697FF319102077BF', 2016, 1)); --Task5
SELECT * FROM TABLE(pkgStudent.getStudentDesign('F5969065974B1FFCAC09DB2E087F33D1A1465B69', 2017, 1)); --Task8
SELECT * FROM TABLE(pkgStudent.getStudentCredit('F5969065974B1FFCAC09DB2E087F33D1A1465B69',2016,1)); --Task9
SELECT * FROM TABLE(pkgStudent.getCleverStudents(6361, 'FIN 307')); --Task10