INSERT INTO roles_permissions (role_id, permission_id) VALUES (get_role_id('EMPLOYEE'), get_permission_id('CREATE_TICKET'));
INSERT INTO roles_permissions (role_id, permission_id) VALUES (get_role_id('EMPLOYEE'), get_permission_id('READ_TICKET'));
INSERT INTO roles_permissions (role_id, permission_id) VALUES (get_role_id('EMPLOYEE'), get_permission_id('UPDATE_TICKET'));
INSERT INTO roles_permissions (role_id, permission_id) VALUES (get_role_id('EMPLOYEE'), get_permission_id('CREATE_COMMENT'));
INSERT INTO roles_permissions (role_id, permission_id) VALUES (get_role_id('EMPLOYEE'), get_permission_id('READ_COMMENT'));
INSERT INTO roles_permissions (role_id, permission_id) VALUES (get_role_id('EMPLOYEE'), get_permission_id('UPDATE_COMMENT'));
INSERT INTO roles_permissions (role_id, permission_id) VALUES (get_role_id('EMPLOYEE'), get_permission_id('CREATE_FEEDBACK'));
INSERT INTO roles_permissions (role_id, permission_id) VALUES (get_role_id('EMPLOYEE'), get_permission_id('READ_FEEDBACK'));

INSERT INTO roles_permissions (role_id, permission_id) VALUES (get_role_id('MANAGER'), get_permission_id('CREATE_TICKET'));
INSERT INTO roles_permissions (role_id, permission_id) VALUES (get_role_id('MANAGER'), get_permission_id('READ_TICKET'));
INSERT INTO roles_permissions (role_id, permission_id) VALUES (get_role_id('MANAGER'), get_permission_id('UPDATE_TICKET'));
INSERT INTO roles_permissions (role_id, permission_id) VALUES (get_role_id('MANAGER'), get_permission_id('CREATE_COMMENT'));
INSERT INTO roles_permissions (role_id, permission_id) VALUES (get_role_id('MANAGER'), get_permission_id('READ_COMMENT'));
INSERT INTO roles_permissions (role_id, permission_id) VALUES (get_role_id('MANAGER'), get_permission_id('UPDATE_COMMENT'));
INSERT INTO roles_permissions (role_id, permission_id) VALUES (get_role_id('MANAGER'), get_permission_id('CREATE_FEEDBACK'));
INSERT INTO roles_permissions (role_id, permission_id) VALUES (get_role_id('MANAGER'), get_permission_id('READ_FEEDBACK'));

INSERT INTO roles_permissions (role_id, permission_id) VALUES (get_role_id('ENGINEER'), get_permission_id('READ_TICKET'));
INSERT INTO roles_permissions (role_id, permission_id) VALUES (get_role_id('ENGINEER'), get_permission_id('CREATE_COMMENT'));
INSERT INTO roles_permissions (role_id, permission_id) VALUES (get_role_id('ENGINEER'), get_permission_id('READ_COMMENT'));
INSERT INTO roles_permissions (role_id, permission_id) VALUES (get_role_id('ENGINEER'), get_permission_id('UPDATE_COMMENT'));
INSERT INTO roles_permissions (role_id, permission_id) VALUES (get_role_id('ENGINEER'), get_permission_id('READ_FEEDBACK'));
INSERT INTO roles_permissions (role_id, permission_id) VALUES (get_role_id('ENGINEER'), get_permission_id('DELETE'));
