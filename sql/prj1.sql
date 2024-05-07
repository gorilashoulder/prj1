CREATE DATABASE prj1;

USE prj1;

DROP TABLE board;

CREATE TABLE board
(
    id       INT PRIMARY KEY AUTO_INCREMENT,
    title    VARCHAR(30)   NOT NULL,
    content  VARCHAR(3000) NOT NULL,
    writer   VARCHAR(100)  NOT NULL,
    inserted DATETIME      NOT NULL DEFAULT NOW()
);

SELECT *
FROM board;

# 회원테이블
CREATE TABLE member
(
    id        INT PRIMARY KEY AUTO_INCREMENT,
    email     VARCHAR(200) NOT NULL UNIQUE,
    password  VARCHAR(200) NOT NULL,
    nick_name VARCHAR(100) NOT NULL UNIQUE
);

ALTER TABLE member
    ADD COLUMN inserted DATETIME NOT NULL DEFAULT NOW();

SELECT *
FROM member;

INSERT INTO board
    (title, content, writer)
SELECT title, content, writer
FROM board;

# board 테이블 수정
# writer 컬럼 지우기
# member_id INT REFERENCES member(id) 컬럼추가

ALTER TABLE board
    DROP COLUMN writer;

ALTER TABLE prj1.board
    ADD COLUMN member_id INT REFERENCES member (id);


SELECT *
FROM board
LIMIT 4;
SELECT *
FROM member;

UPDATE board
SET member_id=5
WHERE id > 0;

DESCRIBE board;

create table authority
(
    id        INT AUTO_INCREMENT PRIMARY KEY,
    member_id INT         NOT NULL REFERENCES member (id),
    name      VARCHAR(20) NOT NULL
);

SELECT *
FROM member;
INSERT INTO authority
    (member_id, name)
VALUES (12, 'admin');