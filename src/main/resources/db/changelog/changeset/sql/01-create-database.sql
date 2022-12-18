DROP TABLE IF EXISTS user_comments;
DROP TABLE IF EXISTS history;
DROP TABLE IF EXISTS attachments;
DROP TABLE IF EXISTS feedback;
DROP TABLE IF EXISTS tickets;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS users;

CREATE TABLE categories
(
    category_id   bigserial PRIMARY KEY,
    category_name varchar(64) UNIQUE NOT NULL
);

CREATE TABLE users
(
    user_id       bigserial PRIMARY KEY,
    first_name    varchar(64),
    last_name     varchar(64),
    role_id       varchar(24)         NOT NULL,
    user_email    varchar(100) UNIQUE NOT NULL,
    user_password varchar(60)         NOT NULL
);

CREATE TABLE tickets
(
    ticket_id               bigserial PRIMARY KEY,
    ticket_name             varchar(255)                               NOT NULL,
    ticket_description      text                                       NOT NULL,
    created_on              date                                       NOT NULL,
    desired_resolution_date date,
    assignee_id             bigint REFERENCES users (user_id),
    owner_id                bigint REFERENCES users (user_id)          NOT NULL,
    state_id                varchar(24)                                NOT NULL,
    category_id             bigint REFERENCES categories (category_id) NOT NULL,
    urgency_id              varchar(24)                                NOT NULL,
    approver_id             bigint REFERENCES users (user_id)
);

CREATE TABLE feedbacks
(
    feedback_id   bigserial PRIMARY KEY,
    user_id       bigint REFERENCES users (user_id)     NOT NULL,
    feedback_rate smallint                              NOT NULL,
    feedback_date date                                  NOT NULL,
    feedback_text text,
    ticket_id     bigint REFERENCES tickets (ticket_id) NOT NULL
);

CREATE TABLE attachments
(
    attachment_id   bigserial PRIMARY KEY,
    attachment_blob text UNIQUE                           NOT NULL,
    ticket_id       bigint REFERENCES tickets (ticket_id) NOT NULL,
    attachment_name varchar(64)                           NOT NULL
);

CREATE TABLE history
(
    history_id     bigserial PRIMARY KEY,
    ticket_id      bigint REFERENCES tickets (ticket_id) NOT NULL,
    history_date   date                                  NOT NULL,
    history_action text,
    user_id        bigint REFERENCES users (user_id)     NOT NULL,
    description    text
);

CREATE TABLE user_comments
(
    user_comment_id   bigserial PRIMARY KEY,
    user_id           bigint REFERENCES users (user_id)     NOT NULL,
    user_comment_text text                                  NOT NULL,
    user_comment_date date                                  NOT NULL,
    ticket_id         bigint REFERENCES tickets (ticket_id) NOT NULL
);
