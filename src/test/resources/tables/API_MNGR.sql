CREATE TABLE API_MNGR (
                          API_MNGR_ID INTEGER NOT NULL AUTO_INCREMENT,
                          API_KEY VARCHAR(255) NOT NULL,
                          API_MNGR_NAME VARCHAR(255) ,
                          API_MNGR_STATUS VARCHAR(10),
                          API_MNGR_EXPR_DATE VARCHAR(255),
                          API_MNGR_CREATED_BY VARCHAR(255),
                          CREATED_DATE VARCHAR(255) ,
                          API_MNGR_UPDATED_BY VARCHAR(255),
                          MODIFIED_DATE VARCHAR(255) ,
                          PRIMARY KEY (API_MNGR_ID)
);

INSERT INTO API_MNGR (API_MNGR_ID, API_KEY, API_MNGR_NAME, API_MNGR_STATUS, API_MNGR_EXPR_DATE , API_MNGR_CREATED_BY, CREATED_DATE, API_MNGR_UPDATED_BY, MODIFIED_DATE)
VALUES
    (1,
     'abcdefg',
     'test1',
     'Y',
     'lucas',
     '20241010',
     now(),
     'lucas',
     now());