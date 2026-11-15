CREATE TABLE expenses(reference text PRIMARY KEY,category text NOT NULL,amount_minor bigint NOT NULL CHECK(amount_minor BETWEEN 1 AND 1000000));
