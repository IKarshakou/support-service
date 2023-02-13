--password is 12345 Authentication is Basic TWFyaWFAeW9wbWFpbC5jb206MTIzNDU=
INSERT INTO users (id, first_name, last_name, role_id, email, password)
VALUES (uuid_generate_v4(), 'Maria', 'Anders', get_role_id('ENGINEER'), 'Maria@yopmail.com', '$2a$04$jbYPuDwgZCWrIdolvbKCq.5OeBiF7yBVyYQJtQxxJbIO6ovG44Qyu');
--password is 012345 Authentication is Basic QW5hQHlvcG1haWwuY29tOjAxMjM0NQ==
INSERT INTO users (id, first_name, last_name, role_id, email, password)
VALUES (uuid_generate_v4(), 'Ana', 'Trujillo', get_role_id('MANAGER'), 'Ana@yopmail.com', '$2a$04$6Ybaqcd8pooCQ97ZA4kBdeP5.OMrWP/o4yVkiVHXnrt2Lw7qgYXgy');
--password is 11111 Authentication is Basic QW50b25pb0B5b3BtYWlsLmNvbToxMTExMQ==
INSERT INTO users (id, first_name, last_name, role_id, email, password)
VALUES (uuid_generate_v4(), 'Antonio', 'Moreno', get_role_id('EMPLOYEE'), 'Antonio@yopmail.com', '$2a$04$oDvGGKL0rLgskeDcbAwvIe7ITZKtd1P5Tp0NdZFhlZ.5mlrZaIK46');
--password is 11111 Authentication is Basic TWF4QHlvcG1haWwuY29tOjExMTEx
INSERT INTO users (id, first_name, last_name, role_id, email, password)
VALUES (uuid_generate_v4(), 'Max', 'Dalleck', get_role_id('MANAGER'), 'Max@yopmail.com', '$2a$04$oDvGGKL0rLgskeDcbAwvIe7ITZKtd1P5Tp0NdZFhlZ.5mlrZaIK46');
--password is 11111 Authentication is Basic Sm9obkB5b3BtYWlsLmNvbToxMTExMQ==
INSERT INTO users (id, first_name, last_name, role_id, email, password)
VALUES (uuid_generate_v4(), 'John', 'Karpec', get_role_id('ENGINEER'), 'John@yopmail.com', '$2a$04$oDvGGKL0rLgskeDcbAwvIe7ITZKtd1P5Tp0NdZFhlZ.5mlrZaIK46');
--password is 11111 Authentication is Basic SGVybWFuQHlvcG1haWwuY29tOjExMTEx
INSERT INTO users (id, first_name, last_name, role_id, email, password)
VALUES (uuid_generate_v4(), 'Herman', 'Gachinsky', get_role_id('EMPLOYEE'), 'Herman@yopmail.com', '$2a$04$oDvGGKL0rLgskeDcbAwvIe7ITZKtd1P5Tp0NdZFhlZ.5mlrZaIK46');
--password is 11111 Authentication is Basic THVjQHlvcG1haWwuY29tOjExMTEx
INSERT INTO users (id, first_name, last_name, role_id, email, password)
VALUES (uuid_generate_v4(), 'Luc', 'Tanens', get_role_id('EMPLOYEE'), 'Luc@yopmail.com', '$2a$04$oDvGGKL0rLgskeDcbAwvIe7ITZKtd1P5Tp0NdZFhlZ.5mlrZaIK46');
--password is 11111 Authentication is Basic QmVuZWRldHRvQHlvcG1haWwuY29tOjExMTEx
INSERT INTO users (id, first_name, last_name, role_id, email, password)
VALUES (uuid_generate_v4(), 'Benedetto', 'Croce', get_role_id('EMPLOYEE'), 'Benedetto@yopmail.com', '$2a$04$oDvGGKL0rLgskeDcbAwvIe7ITZKtd1P5Tp0NdZFhlZ.5mlrZaIK46');
--password is 11111 Authentication is Basic SmFtZXNAeW9wbWFpbC5jb206MTExMTE=
INSERT INTO users (id, first_name, last_name, role_id, email, password)
VALUES (uuid_generate_v4(), 'James', 'Milner', get_role_id('EMPLOYEE'), 'James@yopmail.com', '$2a$04$oDvGGKL0rLgskeDcbAwvIe7ITZKtd1P5Tp0NdZFhlZ.5mlrZaIK46');
