INSERT INTO acl_sid (id, principal, sid)
VALUES (1, 0, 'ROLE_ADMIN'),
       (2, 0, 'ROLE_ENGINEER');

INSERT INTO acl_class (id, class)
VALUES (1, 'com.training.dto.category.CategoryDto');

INSERT INTO acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting)
VALUES (1, 1, 1, NULL, 1, 1),
       (2, 1, 2, NULL, 1, 1),
       (3, 1, 3, NULL, 2, 1);

INSERT INTO acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure)
VALUES (1, 1, 0, 1, 16, 1, 0, 0), -- user1@mail.com has Admin permission for User1 Possession
       (2, 2, 0, 1, 16, 1, 0, 0), -- user1@mail.com has Admin permission for Common Possession
       (3, 2, 1, 2, 1, 1, 0, 0),  -- user2@mail.com has Read permission for Common Possession
       (4, 3, 0, 2, 16, 1, 0, 0); -- user2@mail.com has Admin permission for User2 Possession
