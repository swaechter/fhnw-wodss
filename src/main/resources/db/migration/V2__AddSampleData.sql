INSERT INTO employee (first_name, last_name, email_address, password_hash, is_active, role) VALUES ('Simon', 'Wächter', 'simon.waechter@students.fhnw.ch', 'TODO', true, 'ADMINISTRATOR');
INSERT INTO employee (first_name, last_name, email_address, password_hash, is_active, role) VALUES ('Philipp', 'Lüthi', 'philipp.luethi@students.fhnw.ch', 'TODO', true, 'ADMINISTRATOR');
INSERT INTO employee (first_name, last_name, email_address, password_hash, is_active, role) VALUES ('Thibault', 'Gagnaux', 'thibault.gagnaux@students.fhnw.ch', 'TODO', true, 'ADMINISTRATOR');

INSERT INTO project (name, fte_percentage, start_date, end_date, project_manager_id) VALUES ('IP5 Simon Wächter', 2.0, now(), now(), 1);
INSERT INTO project (name, fte_percentage, start_date, end_date, project_manager_id) VALUES ('IP6 Philipp Lüthi & Thibault Gagnaux', 2.0, now(), now(), 2);
INSERT INTO project (name, fte_percentage, start_date, end_date, project_manager_id) VALUES ('Projekt wodss', 3.0, now(), now(), 1);

INSERT INTO contract (start_date, end_date, pensum_percentage, employee_id) VALUES (now(), now(), 0.4, 1);
INSERT INTO contract (start_date, end_date, pensum_percentage, employee_id) VALUES (now(), now(), 1.0, 2);
INSERT INTO contract (start_date, end_date, pensum_percentage, employee_id) VALUES (now(), now(), 1.0, 3);

INSERT INTO allocation (start_date, end_date, pensum_percentage, contract_id, project_id) VALUES (now(), now(), 0.2, 1, 1);
INSERT INTO allocation (start_date, end_date, pensum_percentage, contract_id, project_id) VALUES (now(), now(), 0.4, 2, 2);
INSERT INTO allocation (start_date, end_date, pensum_percentage, contract_id, project_id) VALUES (now(), now(), 0.4, 3, 2);
INSERT INTO allocation (start_date, end_date, pensum_percentage, contract_id, project_id) VALUES (now(), now(), 0.2, 1, 3);
INSERT INTO allocation (start_date, end_date, pensum_percentage, contract_id, project_id) VALUES (now(), now(), 0.2, 2, 3);
INSERT INTO allocation (start_date, end_date, pensum_percentage, contract_id, project_id) VALUES (now(), now(), 0.2, 3, 3);
