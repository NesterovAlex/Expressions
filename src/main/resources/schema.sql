DROP TABLE IF EXISTS expressions;
CREATE TABLE expressions
(
    id SERIAL NOT NULL,
    expression VARCHAR(100) NOT NULL,
    result DOUBLE PRECISION,
    PRIMARY KEY (id)
);