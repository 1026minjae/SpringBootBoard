-- Just reference.

CREATE TABLE QUESTIONS (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    created_time TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE ANSWERS (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    question_id BIGINT NOT NULL,
    content TEXT NOT NULL,
    created_time TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    FOREIGN KEY (question_id) REFERENCES QUESTIONS(id) ON DELETE CASCADE
);

CREATE TABLE USERS (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    pw VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE
);