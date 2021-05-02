CREATE OR REPLACE TYPE objPopularCourse AS OBJECT (    --Task1
    max_count NUMBER,
    course_code VARCHAR(20),
    prac_teacher NUMBER
);


CREATE OR REPLACE TYPE objCourseRateByYear IS OBJECT ( --Task12
  course_rate            NUMBER,
  course_code     VARCHAR(20),
  term            NUMBER,
  year            NUMBER
);

CREATE OR REPLACE TYPE objRetakeSubject IS OBJECT (
  retake_count         NUMBER,
  ders_kod         VARCHAR(200)
);

DROP TYPE objPopularCourse FORCE; --Task1
DROP TYPE objCourseRateByYear FORCE; --Task12
DROP TYPE objRetakeSubject FORCE; --Task14

--------------------------------------------------------------------------------------------
CREATE OR REPLACE TYPE tblPopularCourse IS TABLE OF objPopularCourse; --Task1
CREATE OR REPLACE TYPE tblCourseRateByYear IS TABLE OF objCourseRateByYear; --Task12
CREATE OR REPLACE TYPE tblRetakeSubject IS TABLE OF objRetakeSubject; --Task14
--------------------------------------------------------------------------------------------
DROP TYPE tblPopularCourse FORCE; --Task1
DROP TYPE tblCourseRateByYear FORCE; --Task12
DROP TYPE tblRetakeSubject FORCE; --Task14

CREATE OR REPLACE PACKAGE pkgCourse AS
    FUNCTION getPopularCourse(p_term INT, p_year INT) RETURN tblPopularCourse; --Task1
    FUNCTION getCourseRateByYear(semester IN NUMBER, g_year NUMBER) RETURN tblCourseRateByYear; --Task12
    FUNCTION getRetakeSubject RETURN tblRetakeSubject; --Task14
END pkgCourse;

--------------------------------------------------------------------------------------------
CREATE OR REPLACE PACKAGE BODY pkgCourse AS

    FUNCTION getPopularCourse(p_term INT, p_year INT) RETURN tblPopularCourse                           --Function for Task1
    IS 
        tbl tblPopularCourse := tblPopularCourse();

        max_count NUMBER(20);
        d_kod VARCHAR(50);
        prac NUMBER(20);

        CURSOR cur IS SELECT maximum, 
            DERS_KOD, 
            PRACTICE 
        FROM (
            SELECT count(*) AS maximum, 
                DERS_KOD, 
                PRACTICE 
            FROM 
            COURSE_SELECTIONS 
                WHERE PRACTICE IS NOT NULL AND DERS_KOD IS NOT NULL AND YEAR = p_year AND TERM = p_term
            GROUP BY DERS_KOD, PRACTICE
            ORDER BY maximum DESC 
 ) s
    where ROWNUM = 1;

    BEGIN
    OPEN cur;
        LOOP FETCH cur INTO  
            max_count,
            d_kod,
            prac;
        EXIT WHEN cur%NOTFOUND;

            tbl.EXTEND; ---Добавляет элементы в конец коллекции \ добавляет один элемент со значением null в конец коллекции
            tbl(tbl.LAST) := objPopularCourse(max_count, d_kod, prac); -- Возвращает последний индекс коллекции
        
        END LOOP;
   CLOSE cur;
   
   RETURN tbl;
   END getPopularCourse;
   
   
   
   FUNCTION getCourseRateByYear(semester IN NUMBER, g_year NUMBER) RETURN tblCourseRateByYear             --Function For Task12
    IS
        tbl tblCourseRateByYear := tblCourseRateByYear();
        
        max_rate     NUMBER(10);
        course_code  VARCHAR(10);
        term         NUMBER(10);
        l_year       NUMBER(10);
        
        CURSOR cur IS SELECT 
            count(DERS_KOD) AS COURSE_RATE, 
            ders_kod AS COURSE_CODE,
            term AS SEMESTER,
            year
        FROM course_selections
                WHERE term = semester AND year = g_year AND ders_kod IS NOT NULL
            GROUP BY ders_kod, term, year
            ORDER BY COURSE_RATE DESC;
    
    BEGIN
    
        OPEN cur;
            LOOP FETCH cur INTO
                max_rate,
                course_code,
                term,
                l_year;
            EXIT WHEN cur%NOTFOUND;
            
            tbl.EXTEND;
            tbl(tbl.LAST) := objCourseRateByYear(max_rate, course_code, term, l_year);
            
            END LOOP;
        CLOSE cur;
        
    RETURN tbl;
    END getCourseRateByYear;
  
      FUNCTION getRetakeSubject RETURN tblRetakeSubject AS     --Task14

        tbl tblRetakeSubject := tblRetakeSubject();
        
        retake_count NUMBER(20);
        ders_kod    VARCHAR(200);
        

    BEGIN
    
        FOR rec IN (SELECT retake_count,ders_kod FROM (SELECT count(STUD_ID) AS retake_count,ders_kod
                      FROM course_selections
                      WHERE QIYMET_HERF = 'F' OR QIYMET_HERF = 'FX' GROUP BY ders_kod
                      ORDER BY retake_count DESC)where rownum=1)
    
        LOOP
          retake_count := rec.retake_count;
           ders_kod := rec.ders_kod;
            
            tbl.EXTEND;
            tbl(tbl.LAST) := objRetakeSubject(retake_count,ders_kod );

      END LOOP;
   RETURN(tbl);

    END getRetakeSubject;
END pkgCourse; 
---------------------------------------------------------------------------------------------------------
DROP PACKAGE  pkgCourse;
----------------------------------------------
SELECT * FROM TABLE(pkgCourse.getPopularCourse(1, 2016)); --Task1
SELECT * FROM TABLE(pkgCourse.getCourseRateByYear(1, 2016)); --Task12
SELECT * FROM TABLE(pkgCourse.getRetakeSubject()); --Task14
