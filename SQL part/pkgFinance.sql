CREATE OR REPLACE TYPE objRetakeProfit AS OBJECT (
    retake_count NUMBER,
    profit NUMBER
);

DROP TYPE objRetakeProfit FORCE; 
----------------------------------------------
CREATE OR REPLACE TYPE tblRetakeProfit IS TABLE OF objRetakeProfit;
----------------------------------------------

DROP TYPE tblRetakeProfit FORCE; 


CREATE OR REPLACE PACKAGE pkgFinance AS
FUNCTION getRetakeProfit RETURN tblRetakeProfit;
END pkgFinance;
----------------------------------------------

CREATE OR REPLACE PACKAGE BODY pkgFinance AS

    FUNCTION getRetakeProfit RETURN tblRetakeProfit AS 

        tbl tblRetakeProfit := tblRetakeProfit();
        
        retake_count NUMBER(20);
        c NUMBER(5);
        
        CURSOR cur IS SELECT count(*) as RETAKE_COUNT
                      FROM course_selections
                      WHERE QIYMET_HERF = 'F' OR QIYMET_HERF = 'FX'
                      ORDER BY RETAKE_COUNT;

    BEGIN
    
        SELECT DISTINCT CREDITS INTO c FROM course_sections;
        OPEN cur;
            LOOP FETCH cur INTO retake_count;
            EXIT WHEN cur%NOTFOUND;

                tbl.EXTEND;
                tbl(tbl.LAST) := objRetakeProfit(retake_count, retake_count * 25000 * c);
                
            END LOOP;
        CLOSE cur;

    RETURN tbl;
    END getRetakeProfit;

END pkgFinance;
----------------------------------------------
SELECT * FROM TABLE(pkgFinance.getRetakeProfit());