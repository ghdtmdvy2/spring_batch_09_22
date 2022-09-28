# DB 생성
DROP DATABASE IF EXISTS app_2022_09_23;
CREATE DATABASE app_2022_09_23;
USE app_2022_09_23;

# 게시물 테이블 생성
CREATE TABLE article (
                         id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
                         PRIMARY KEY(id),
                         createDate DATETIME NOT NULL,
                         modifyDate DATETIME NOT NULL,
                         `subject` CHAR(100) NOT NULL,
                         content LONGTEXT NOT NULL
);

# 게시물 데이터
INSERT INTO article
SET createDate = NOW(),
modifyDate = NOW(),
`subject` = '제목1',
content = '내용1';

INSERT INTO article
SET createDate = NOW(),
modifyDate = NOW(),
`subject` = '제목2',
content = '내용2';

# 회원 테이블 생성
CREATE TABLE `member` (
                          id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
                          PRIMARY KEY(id),
                          createDate DATETIME NOT NULL,
                          modifyDate DATETIME NOT NULL,
                          username CHAR(100) NOT NULL UNIQUE,
                          `password` CHAR(100) NOT NULL,
                          `name` CHAR(100) NOT NULL,
                          email CHAR(100) NOT NULL
);

# 회원 데이터
INSERT INTO `member`
SET createDate = NOW(),
modifyDate = NOW(),
username = 'user1',
`password` = '{noop}1234',
`name` = '유저1',
email = 'user1@test.com';

INSERT INTO `member`
SET createDate = NOW(),
modifyDate = NOW(),
username = 'user2',
`password` = '{noop}1234',
`name` = '유저2',
email = 'user2@test.com';

SELECT *
FROM `member`;