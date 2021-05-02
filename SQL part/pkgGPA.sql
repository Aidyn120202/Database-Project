CREATE OR REPLACE TYPE objGPA IS OBJECT (
  stud_id           VARCHAR(200),
  year              NUMBER,
  term              NUMBER,
  total_gpa         NUMBER,
  gpa_per_semester  NUMBER
);

drop type objGPA force;
----------------------------------------------
CREATE OR REPLACE TYPE tblGPA IS TABLE OF objGPA;
----------------------------------------------

drop type tblGPA force;

CREATE OR REPLACE PACKAGE pkgGPA AS
FUNCTION getGPA (student_id IN VARCHAR2) RETURN tblGPA;
FUNCTION getGPAByGrade(GRADE NUMBER, QUANTITY NUMBER) RETURN NUMBER;
END pkgGPA;
----------------------------------------------
CREATE OR REPLACE PACKAGE BODY pkgGPA AS
    --by Stud_id GPA function  
    FUNCTION getGPA(student_id IN VARCHAR2)
    RETURN tblGPA
    AS
    
    tbl tblGPA := tblGPA();
    
    BEGIN
    FOR rec IN (SELECT 
        count(*) AS QUANTITY,
        (SELECT count(*) FROM course_selections WHERE stud_id = sc.stud_id) as TOTAL_QUANTITY,
        stud_id, 
        year, 
        term, 
        (SELECT SUM(QIYMET_YUZ) FROM course_selections WHERE stud_id = sc.stud_id) AS TOTAL_GPA, 
        SUM(QIYMET_YUZ) AS GPA_PER_SEMESTER 
    FROM course_selections sc WHERE stud_id = student_id
        GROUP BY year, term, stud_id 
        ORDER BY year, term)
        
        LOOP
            tbl.EXTEND;
            tbl(tbl.LAST) := objGPA(rec.stud_id, 
                                    rec.year, 
                                    rec.term, 
                                    getGPAByGrade(rec.total_GPA, rec.total_quantity), 
                                    getGPAByGrade(rec.GPA_PER_SEMESTER, rec.quantity));
        END LOOP;
        
        RETURN tbl;
    END getGPA;
    
    --get GPA
    FUNCTION getGPAByGrade(GRADE NUMBER, QUANTITY NUMBER) RETURN NUMBER
    IS
    GPA NUMBER;
    TMP NUMBER := GRADE/QUANTITY;
    BEGIN
        IF (TMP >= 95) THEN
            GPA := 4;
        END IF;
        IF (TMP >= 90) AND (TMP <=94) THEN
            GPA := 3.67;
        END IF;
        IF (TMP >= 85) AND (TMP <=89) THEN
            GPA := 3.33;
        END IF;
        IF (TMP >= 80) AND (TMP <=84) THEN
            GPA := 3;
        END IF;
        IF (TMP >= 75) AND (TMP <=79) THEN
            GPA := 2.67;
        END IF;
        IF (TMP >= 70) AND (TMP <=74) THEN
            GPA := 2.33;
        END IF;
        IF (TMP >= 65) AND (TMP <=69) THEN
            GPA := 2;
        END IF;
        IF (TMP >= 60) AND (TMP <=64) THEN
            GPA := 1.67;
        END IF;
        IF (TMP >= 55) AND (TMP <=59) THEN
            GPA := 1.33;
        END IF;
        IF (TMP >= 50) AND (TMP <=54) THEN
            GPA := 1;
        END IF;
        IF (TMP <=49) THEN
            GPA := 0;
        END IF;
    RETURN GPA;
    END getGPAByGrade;
    
 END pkgGPA;

----------------------------------------------
SELECT * FROM TABLE(pkgGPA.getGPA('004531EC911B9D3EF9A7C7A8524D054EA943BA3F'));