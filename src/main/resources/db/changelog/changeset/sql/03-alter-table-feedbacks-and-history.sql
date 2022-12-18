ALTER TABLE history ALTER COLUMN history_date SET DATA TYPE timestamp;
ALTER TABLE feedbacks ADD CONSTRAINT feedbacks_ticket_id_unique UNIQUE (ticket_id);
