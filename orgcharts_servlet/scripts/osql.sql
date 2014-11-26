CREATE TABLE CHART (
    UUID        CHAR(36) NOT NULL,
    OWNER_ID    VARCHAR2(22),
    CHART_NAME  VARCHAR2(22),
    XML         BLOB,
    CREATE_TIME TIMESTAMP,
    VERSION     NUMBER(10),
    EDIT_USER   VARCHAR2(22),

    CONSTRAINT ORGCHARTXML_PK PRIMARY KEY (UUID),
    CONSTRAINT FK_EMP FOREIGN KEY (OWNER_ID) REFERENCES EMP(EMP_ID),
    CONSTRAINT FK_EMP FOREIGN KEY (EDIT_USER) REFERENCES EMP(EMP_ID)
);



SELECT table_name, column_name, data_type,data_length
  FROM all_tab_columns
  WHERE owner = 'HBXTRACK_OWNER';



SELECT cols.table_name, cols.column_name, cols.position, cons.status, cons.owner
FROM all_constraints cons, all_cons_columns cols
WHERE cols.table_name = 'EMP'
AND cons.constraint_type = 'P'
AND cons.constraint_name = cols.constraint_name
AND cons.owner = cols.owner
ORDER BY cols.table_name, cols.position;



GRANT select, insert, update, delete, References on TABLE_NAME to HBXTRACK;

CREATE OR REPLACE SYNONYM HBXTRACK.CHART FOR HBXTRACK_OWNER.CHART;