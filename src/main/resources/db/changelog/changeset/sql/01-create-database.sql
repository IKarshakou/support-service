DROP TABLE IF EXISTS user_comments;
DROP TABLE IF EXISTS history;
DROP TABLE IF EXISTS attachments;
DROP TABLE IF EXISTS feedbacks;
DROP TABLE IF EXISTS tickets;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS users_principal;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS roles_permissions;
DROP TABLE IF EXISTS permissions;
DROP TABLE IF EXISTS roles;

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE OR REPLACE FUNCTION get_user_id(user_email varchar(100)) RETURNS uuid
AS
'
    DECLARE
        id uuid;
    BEGIN
        SELECT users.id
        INTO STRICT id
        FROM users
        WHERE users.email = get_user_id.user_email;
        RETURN id;
    END;
'LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_category_id(category_name varchar(63)) RETURNS uuid
AS
'
    DECLARE
        id uuid;
    BEGIN
        SELECT categories.id
        INTO STRICT id
        FROM categories
        WHERE categories.name = get_category_id.category_name;
        RETURN id;
    END;
'LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_permission_id(permission_name varchar(63)) RETURNS uuid
AS
'
    DECLARE
        id uuid;
    BEGIN
        SELECT permissions.id
        INTO STRICT id
        FROM permissions
        WHERE permissions.name = get_permission_id.permission_name;
        RETURN id;
    END;
'LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_role_id(role_name varchar(63)) RETURNS uuid
AS
'
    DECLARE
        id uuid;
    BEGIN
        SELECT roles.id
        INTO STRICT id
        FROM roles
        WHERE roles.name = get_role_id.role_name;
        RETURN id;
    END;
'LANGUAGE plpgsql;

CREATE TABLE categories
(
    id   uuid PRIMARY KEY,
    name varchar(63) UNIQUE NOT NULL
);

CREATE TABLE roles
(
    id   uuid PRIMARY KEY,
    name varchar(63) UNIQUE NOT NULL
);

CREATE TABLE permissions
(
    id   uuid PRIMARY KEY,
    name varchar(63) UNIQUE NOT NULL
);

CREATE TABLE roles_permissions
(
    role_id       uuid NOT NULL REFERENCES roles (id),
    permission_id uuid NOT NULL REFERENCES permissions (id),
    UNIQUE (role_id, permission_id)
);

CREATE TABLE users
(
    id         uuid PRIMARY KEY,
    first_name varchar(63),
    last_name  varchar(63),
    role_id    uuid REFERENCES roles (id) NOT NULL,
    email      varchar(100) UNIQUE        NOT NULL,
    password   varchar(60)                NOT NULL
);

CREATE TABLE users_principal
(
    id uuid PRIMARY KEY,
    username varchar(100) UNIQUE        NOT NULL,
    user_id uuid REFERENCES users (id) NOT NULL,
    refresh_token text,
    enabled boolean NOT NULL
);

CREATE TABLE tickets
(
    id                      uuid PRIMARY KEY,
    name                    varchar(255)                    NOT NULL,
    description             text,
    created_on              timestamptz                     NOT NULL,
    desired_resolution_date timestamptz,
    assignee_id             uuid REFERENCES users (id),
    owner_id                uuid REFERENCES users (id)      NOT NULL,
    state                   varchar(24)                     NOT NULL,
    category_id             uuid REFERENCES categories (id) NOT NULL,
    urgency                 varchar(24)                     NOT NULL,
    approver_id             uuid REFERENCES users (id)
);

CREATE TABLE feedbacks
(
    id            uuid PRIMARY KEY,
    user_id       uuid REFERENCES users (id)          NOT NULL,
    rate          smallint                            NOT NULL,
    creation_date timestamptz                         NOT NULL,
    text          text,
    ticket_id     uuid REFERENCES tickets (id) UNIQUE NOT NULL
);

CREATE TABLE attachments
(
    id        uuid PRIMARY KEY,
    path      text UNIQUE                  NOT NULL,
    ticket_id uuid REFERENCES tickets (id) NOT NULL,
    name      varchar(63)                  NOT NULL
);

CREATE TABLE history
(
    id            uuid PRIMARY KEY,
    ticket_id     uuid REFERENCES tickets (id) NOT NULL,
    creation_date timestamptz                  NOT NULL,
    action        text,
    user_id       uuid REFERENCES users (id)   NOT NULL,
    description   text
);

CREATE TABLE user_comments
(
    id            uuid PRIMARY KEY,
    user_id       uuid REFERENCES users (id)   NOT NULL,
    text          text                         NOT NULL,
    creation_date timestamptz                  NOT NULL,
    ticket_id     uuid REFERENCES tickets (id) NOT NULL
);

-- CREATE OR REPLACE VIEW manager_tickets AS
--     SELECT t.id, t.name, cat.name, t.description, t.created_on, t.desired_resolution_date, t.state, t.urgency,
--            ow.email AS owner, ap.email AS approver, assi.email AS assignee, at.name AS attachment,
--            uc.text AS comment
--     FROM tickets t
-- LEFT JOIN categories cat ON t.category_id = cat.id
-- LEFT JOIN users ow ON t.owner_id = ow.id
-- LEFT JOIN users ap ON t.approver_id = ap.id
-- LEFT JOIN users assi ON t.assignee_id = assi.id
-- LEFT JOIN attachments at on t.id = at.ticket_id
-- LEFT JOIN user_comments uc on t.id = uc.ticket_id;
