CREATE OR REPLACE TYPE objPopularTeacher IS OBJECT ( --Task2
  max_count         NUMBER,
  ders_kod         VARCHAR(200),
  practice_id       NUMBER,
  lecture_id           VARCHAR(200)
);


CREATE OR REPLACE TYPE objTeacherLoaded IS OBJECT ( --Task6
  emp_id            NUMBER,
  year              NUMBER,
  term              NUMBER,
  total_hours       NUMBER
);


CREATE OR REPLACE TYPE objTeacherDesign IS OBJECT (  --Task7
  emp_id            NUMBER,
  year              NUMBER,
  term              NUMBER,
  course_code       VARCHAR(20),
  day_schedule      VARCHAR(20),
  hours_schedule_teacher NUMBER
);


CREATE OR REPLACE TYPE objTeachersRateByYear IS OBJECT ( --task11
  rate            NUMBER,
  teacher_id      NUMBER,
  term            NUMBER,
  year            NUMBER
);

CREATE OR REPLACE TYPE objCertainTeacher IS OBJECT ( --Task15
  stud_count         NUMBER,
  ders_kod         VARCHAR(200),
  emp_id           VARCHAR(200)
);

DROP TYPE objPopularTeacher FORCE; --Task2
DROP TYPE objTeacherLoaded FORCE; --Task6
DROP TYPE objTeacherDesign FORCE; --Task7
DROP TYPE objTeachersRateByYear FORCE; --task11
DROP TYPE objCertainTeacher FORCE; --task15


-----------------------------------------------------------------------------------------------------------------------------------------------
CREATE OR REPLACE TYPE tblPopularTeacher IS TABLE OF objPopularTeacher; --task2
CREATE OR REPLACE TYPE tblTeacher IS TABLE OF objTeacherLoaded;  --task6
CREATE OR REPLACE TYPE tblTeacherDesign IS TABLE OF objTeacherDesign; --task7
CREATE OR REPLACE TYPE tblTeachersRateByYear IS TABLE OF objTeachersRateByYear;  --task11
CREATE OR REPLACE TYPE tblCertainTeacher IS TABLE OF objCertainTeacher; --Task15

DROP TYPE tblPopularTeacher FORCE; --Task2
DROP TYPE tblTeacherLoaded FORCE; --Task6
DROP TYPE tblTeacherDesign FORCE; --Task7
DROP TYPE tblTeachersRateByYear FORCE; --task11
DROP TYPE tblCertainTeacher FORCE; --task15

-----------------------------------------------------------------------------------------------------------------------------------------------
CREATE OR REPLACE PACKAGE pkgTeacher AS
FUNCTION getPopularTeacher(p_term INT, p_year INT, t_code VARCHAR) RETURN tblPopularTeacher; --Function For Task2
FUNCTION getTeacherTotalHours(teacher_id IN NUMBER) RETURN tblTeacher;     --Function For Task6  
FUNCTION getTeacherDesign(teacher_id IN NUMBER, g_year NUMBER, g_term NUMBER) RETURN tblTeacherDesign;  --Function For Task7
FUNCTION getTeachersRateByYear(semester IN NUMBER, g_year NUMBER) RETURN tblTeachersRateByYear;  --Function For Task11
FUNCTION getCertainTeacher RETURN tblCertainTeacher;  --Function for Task15
END pkgTeacher;
-----------------------------------------------------------------------------------------------------------------------------------------------


CREATE OR REPLACE PACKAGE BODY pkgTeacher AS

    FUNCTION getPopularTeacher(p_term INT, p_year INT, t_code VARCHAR) RETURN tblPopularTeacher --Function Task2
    IS
        tbl tblPopularTeacher := tblPopularTeacher();
        max_count NUMBER(20);
        subject VARCHAR(10);
      practice_id NUMBER(20);
      lecture_id VARCHAR(10);

    BEGIN
  FOR rec IN (SELECT maximum,ders_kod,practice,emp_id 
    FROM(select count(*) maximum,sc.ders_kod,sel.practice,sc.emp_id,sc.year,sc.term
    from course_selections sel join course_sections sc
    on sc.ders_kod=sel.ders_kod and  sc.emp_id IS NOT NULL AND sel.practice IS NOT NULL and
          sc.year = p_year AND 
          sc.term = p_term AND 
          sc.ders_kod = t_code
        GROUP BY sc.ders_kod,sel.practice,sc.emp_id,sc.year,sc.term
        ORDER BY maximum DESC) where rownum=1)
    
        LOOP
          max_count := rec.maximum;
           practice_id := rec.EMP_ID;
           lecture_id := rec.PRACTICE;
            subject := rec.ders_kod;
            
            tbl.EXTEND;
            tbl(tbl.LAST) := objPopularTeacher(max_count, subject,practice_id, lecture_id);

      END LOOP;
   RETURN(tbl);
   END getPopularTeacher;
   
   
   
   
       FUNCTION getTeacherTotalHours(teacher_id IN NUMBER) RETURN tblTeacher                                    --Function Task6
    AS
    tbl tblTeacher := tblTeacher();
    
    BEGIN
    FOR rec IN (SELECT emp_id AS TEACHER_ID, 
                       year, 
                       term AS SEMESTER, 
                       sum(hour_num) AS TOTAL_HOURS 
                FROM course_sections 
                    WHERE hour_num is not null and emp_id = teacher_id  
                    GROUP BY year, emp_id, term 
                    ORDER BY year)
        
        LOOP
            tbl.EXTEND;
            tbl(tbl.LAST) := objTeacherLoaded(rec.TEACHER_ID, 
                                    rec.year, 
                                    rec.SEMESTER, 
                                    rec.TOTAL_HOURS);
        END LOOP;
        
        RETURN tbl;
    END getTeacherTotalHours;
    
    
    
    FUNCTION getTeacherDesign(teacher_id IN NUMBER, g_year NUMBER, g_term NUMBER) RETURN tblTeacherDesign            --Function Task7
    IS
        tbl tblTeacherDesign := tblTeacherDesign();
    BEGIN
    
    FOR rec IN (SELECT 
        sections.emp_id AS TEACHER_ID, 
        schedule.year, 
        schedule.term AS SEMESTER, 
        schedule.ders_kod AS COURSE_CODE,
(to_char(cast(schedule.start_time as DATE),'Day','NLS_DATE_LANGUAGE = ENGLISH')) as day_schedule,
extract(HOUR from schedule.start_time) as hours_schedule_teacher
FROM course_schedule schedule
    INNER JOIN course_sections sections ON
        schedule.ders_kod = sections.ders_kod  AND 
        schedule.term = sections.term AND
        sections.emp_id = teacher_id AND
        schedule.year = g_year AND
        schedule.term = g_term
        GROUP BY schedule.year, schedule.term, schedule.ders_kod, sections.emp_id,schedule.start_time
        ORDER BY schedule.year,schedule.start_time ASC

)
        
        LOOP
            tbl.EXTEND;
            tbl(tbl.LAST) := objTeacherDesign(rec.TEACHER_ID, 
                                    rec.year, 
                                    rec.SEMESTER, 
                                    rec.COURSE_CODE,
                                    rec.day_schedule,
                                    rec.hours_schedule_teacher);
        END LOOP;
        
        RETURN tbl;
    END getTeacherDesign;
    
    
    
    
    FUNCTION getTeachersRateByYear(semester IN NUMBER, g_year NUMBER) RETURN tblTeachersRateByYear --Function Task11
    IS
        tbl tblTeachersRateByYear := tblTeachersRateByYear();
        
        max_rate    NUMBER(10);
        teacher_id  NUMBER(10);
        term        NUMBER(10);
        l_year      NUMBER(10);
        
        CURSOR cur IS SELECT 
            count(practice) AS TEACHER_RATE, 
            practice AS TEACHER_ID,
            term AS SEMESTER,
            year
        FROM course_selections
            WHERE term = semester AND year = g_year AND practice IS NOT NULL
        GROUP BY practice, term, year
        ORDER BY TEACHER_RATE DESC;
    
    BEGIN
    
        OPEN cur;
            LOOP FETCH cur INTO
                max_rate,
                teacher_id,
                term,
                l_year;
            EXIT WHEN cur%NOTFOUND;
            
            tbl.EXTEND;
            tbl(tbl.LAST) := objTeachersRateByYear(max_rate, teacher_id, term, l_year);
            
            END LOOP;
        CLOSE cur;
        
    RETURN tbl;
    END getTeachersRateByYear;
    
    FUNCTION getCertainTeacher RETURN tblCertainTeacher AS  --Task 15

        tbl tblCertainTeacher := tblCertainTeacher();
        
        stud_count NUMBER(20);
        ders_kod    VARCHAR(200);
        emp_id      VARCHAR(200);
        

    BEGIN
    
        FOR rec IN (SELECT count(sel.stud_id) AS stud_count,ss.ders_kod,ss.emp_id 
        FROM course_selections sel JOIN course_sections ss ON  ss.ders_kod=sel.ders_kod GROUP BY ss.ders_kod,ss.emp_id)
    
        LOOP
          stud_count := rec.stud_count;
           ders_kod := rec.ders_kod;
           emp_id := rec.emp_id;
            
            tbl.EXTEND;
            tbl(tbl.LAST) := objCertainTeacher(stud_count,ders_kod,emp_id);

      END LOOP;
   RETURN(tbl);

    END getCertainTeacher;

END pkgTeacher;

SELECT * FROM TABLE(pkgTeacher.getPopularTeacher(1, 2015, 'CSS 307')); --Task2
SELECT * FROM TABLE(pkgTeacher.getTeacherTotalHours(10166));   --Task6
SELECT * FROM TABLE(pkgTeacher.getTeacherDesign(10166, 2017, 1));   --Task7
SELECT * FROM TABLE(pkgTeacher.getTeachersRateByYear(2, 2016)); --Task11
SELECT * FROM TABLE(pkgTeacher.getCertainTeacher()); --Task15