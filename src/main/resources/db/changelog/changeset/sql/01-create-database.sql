DROP TABLE IF EXISTS user_comments;
DROP TABLE IF EXISTS history;
DROP TABLE IF EXISTS attachments;
DROP TABLE IF EXISTS feedbacks;
DROP TABLE IF EXISTS tickets;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS users;

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE OR REPLACE FUNCTION get_user_id(user_email varchar(100)) RETURNS uuid
AS
'
    DECLARE
        id uuid;
    BEGIN
        SELECT users.user_id
        INTO STRICT id
        FROM users
        WHERE users.user_email = get_user_id.user_email;
        RETURN id;
    END;
'LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_category_id(category_name varchar(64)) RETURNS uuid
AS
'
    DECLARE
        id uuid;
    BEGIN
        SELECT categories.category_id
        INTO STRICT id
        FROM categories
        WHERE categories.category_name = get_category_id.category_name;
        RETURN id;
    END;
'LANGUAGE plpgsql;

CREATE TABLE categories
(
    category_id   uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    category_name varchar(64) UNIQUE NOT NULL
);

CREATE TABLE users
(
    user_id       uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    first_name    varchar(64),
    last_name     varchar(64),
    role_id       varchar(24)         NOT NULL,
    user_email    varchar(100) UNIQUE NOT NULL,
    user_password varchar(60)         NOT NULL
);

CREATE TABLE tickets
(
    ticket_id               uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    ticket_name             varchar(255)                             NOT NULL,
    ticket_description      text,
    created_on              timestamptz                              NOT NULL,
    desired_resolution_date timestamptz,
    assignee_id             uuid REFERENCES users (user_id),
    owner_id                uuid REFERENCES users (user_id)          NOT NULL,
    state_id                varchar(24)                              NOT NULL,
    category_id             uuid REFERENCES categories (category_id) NOT NULL,
    urgency_id              varchar(24)                              NOT NULL,
    approver_id             uuid REFERENCES users (user_id)
);

CREATE TABLE feedbacks
(
    feedback_id   uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    user_id       uuid REFERENCES users (user_id)            NOT NULL,
    feedback_rate smallint                                   NOT NULL,
    feedback_date timestamptz                                NOT NULL,
    feedback_text text,
    ticket_id     uuid REFERENCES tickets (ticket_id) UNIQUE NOT NULL
);

CREATE TABLE attachments
(
    attachment_id   uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    attachment_blob text UNIQUE                         NOT NULL,
    ticket_id       uuid REFERENCES tickets (ticket_id) NOT NULL,
    attachment_name varchar(64)                         NOT NULL
);

CREATE TABLE history
(
    history_id     uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    ticket_id      uuid REFERENCES tickets (ticket_id) NOT NULL,
    history_date   timestamptz                         NOT NULL,
    history_action text,
    user_id        uuid REFERENCES users (user_id)     NOT NULL,
    description    text
);

CREATE TABLE user_comments
(
    user_comment_id   uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    user_id           uuid REFERENCES users (user_id)     NOT NULL,
    user_comment_text text                                NOT NULL,
    user_comment_date timestamptz                         NOT NULL,
    ticket_id         uuid REFERENCES tickets (ticket_id) NOT NULL
);
