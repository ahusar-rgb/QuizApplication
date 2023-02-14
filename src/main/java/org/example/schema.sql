CREATE TABLE topic (
    id SERIAL PRIMARY KEY,
    topic_name VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE question (
    id SERIAL PRIMARY KEY,
    topic_id INT NOT NULL,
    question_content TEXT NOT NULL,
    difficulty_rank INT NOT NULL,
    FOREIGN KEY (topic_id)
        REFERENCES topic (id)
);

CREATE TABLE response (
    id SERIAL PRIMARY KEY,
    response_text TEXT NOT NULL,
    is_correct BOOLEAN NOT NULL
);

CREATE TABLE question_response (
    question_id INT NOT NULL,
    response_id INT NOT NULL,
    PRIMARY KEY (question_id, response_id),
    FOREIGN KEY (question_id)
        REFERENCES question (id),
    FOREIGN KEY (response_id)
       REFERENCES response (id),
   UNIQUE (question_id, response_id)
);