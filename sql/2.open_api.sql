-- maria & mysql : OPEN API 관리

create table API_MNGR (
          API_MNGR_ID INTEGER NOT NULL AUTO_INCREMENT comment '아이디',
          API_KEY VARCHAR(255) NOT NULL               comment 'API KEY',
          API_MNGR_NAME VARCHAR(255)                  comment 'key 관리명',
          API_MNGR_STATUS VARCHAR(10)                 comment 'Key 상태',
          API_MNGR_EXPR_DATE VARCHAR(255)             comment 'Key 만료일자',
          API_MNGR_CREATED_BY VARCHAR(255)            comment '생성자',
          CREATED_DATE VARCHAR(255)                   comment '생성일자',
          API_MNGR_UPDATED_BY VARCHAR(255)            comment '수정자',
          MODIFIED_DATE VARCHAR(255)                  comment '수정일자',

           constaint API_MNGR_PK
              primary key (API_MNGR_ID)
)
   comment 'OPEN API 관리';

INSERT INTO API_MNGR (API_MNGR_ID, API_KEY, API_MNGR_NAME, API_MNGR_STATUS, API_MNGR_EXPR_DATE , API_MNGR_CREATED_BY, CREATED_DATE, API_MNGR_UPDATED_BY, MODIFIED_DATE)
VALUES
    (1,
     'abcdefg',
     'test1',
     'Y',
     'lucas',
     '20251010',
     now(),
     'lucas',
     now()
    );