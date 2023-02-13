INSERT INTO tickets (id, name, description, created_on, desired_resolution_date, owner_id, state, category_id, urgency)
VALUES (uuid_generate_v4(), 'Ticket1', '', '2021-07-14', '2021-08-30', get_user_id('Max@yopmail.com'), 'IN_PROGRESS', get_category_id('Benefits & Paper Work'), 'HIGH');
INSERT INTO tickets (id, name, description, created_on, desired_resolution_date, owner_id, state, category_id, urgency)
VALUES (uuid_generate_v4(), 'Ticket2', 'some description.', '2021-07-12', '2021-08-30', get_user_id('John@yopmail.com'), 'APPROVED', get_category_id('Workplaces & Facilities'), 'CRITICAL');
INSERT INTO tickets (id, name, description, created_on, desired_resolution_date, owner_id, state, category_id, urgency)
VALUES (uuid_generate_v4(), 'Ticket3', 'some another description.', '2021-07-21', '2021-08-30', get_user_id('Herman@yopmail.com'), 'NEW', get_category_id('Security & Access'), 'LOW');
INSERT INTO tickets (id, name, description, created_on, desired_resolution_date, owner_id, state, category_id, urgency)
VALUES (uuid_generate_v4(), 'Ticket4', 'description.', '2021-10-12', '2021-08-30', get_user_id('Luc@yopmail.com'), 'DRAFT', get_category_id('Hardware & Software'), 'LOW');
INSERT INTO tickets (id, name, description, created_on, desired_resolution_date, owner_id, state, category_id, urgency)
VALUES (uuid_generate_v4(), 'Ticket5', 'hm, description here.', '2021-09-12', '2021-08-30', get_user_id('Luc@yopmail.com'), 'DONE', get_category_id('Workplaces & Facilities'), 'AVERAGE');
INSERT INTO tickets (id, name, description, created_on, desired_resolution_date, owner_id, state, category_id, urgency)
VALUES (uuid_generate_v4(), 'Ticket6', 'hello, i am description.', '2021-07-23', '2021-08-30', get_user_id('Benedetto@yopmail.com'), 'APPROVED', get_category_id('Application & Service'), 'HIGH');
INSERT INTO tickets (id, name, description, created_on, desired_resolution_date, owner_id, state, category_id, urgency)
VALUES (uuid_generate_v4(), 'Ticket7', 'description.', '2021-07-30', '2021-08-30', get_user_id('Benedetto@yopmail.com'), 'DONE', get_category_id('Security & Access'), 'AVERAGE');
INSERT INTO tickets (id, name, description, created_on, desired_resolution_date, owner_id, state, category_id, urgency)
VALUES (uuid_generate_v4(), 'Ticket8', 'some description', '2021-08-21', '2021-08-30', get_user_id('Benedetto@yopmail.com'), 'DONE', get_category_id('Hardware & Software'), 'LOW');
INSERT INTO tickets (id, name, description, created_on, desired_resolution_date, owner_id, state, category_id, urgency)
VALUES (uuid_generate_v4(), 'Ticket9', '', '2021-06-21', '2021-08-30', get_user_id('James@yopmail.com'), 'DRAFT', get_category_id('Application & Service'), 'LOW');
