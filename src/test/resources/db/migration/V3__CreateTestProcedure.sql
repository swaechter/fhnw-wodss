CREATE OR REPLACE PROCEDURE initialize_database_with_sample_data()
LANGUAGE plpgsql
AS $$
BEGIN
    /* Delete all existing data */
    DELETE FROM allocation;
    DELETE FROM contract;
    DELETE FROM project;
    DELETE FROM employee;

    /* Create an employee account for each team member */
    INSERT INTO employee (id, first_name, last_name, email_address, password_hash, is_active, role) VALUES ('6bae2bc7-9d1e-457d-ac92-dd78e467b7d1', 'Simon', 'Wächter', 'simon.waechter@students.fhnw.ch', '$2a$10$S7G/79ca.oiLxEFOWf3JauDqHJ2c9UautlF65kkY9PpY04Zyo0rIi', true, 'ADMINISTRATOR');
    INSERT INTO employee (id, first_name, last_name, email_address, password_hash, is_active, role) VALUES ('8d7a6f19-676d-42de-8f3a-039588a49c5f', 'Philipp', 'Lüthi', 'philipp.luethi@students.fhnw.ch', '$2a$10$xjyUmWTl4Fnli1EVIc6zmupK.TfQxpzxIn5L2r.ZTmW8khhxKLLXy', true, 'ADMINISTRATOR');
    INSERT INTO employee (id, first_name, last_name, email_address, password_hash, is_active, role) VALUES ('0ac9e9f4-66c6-44ed-b8d6-a038364afaa6', 'Thibault', 'Gagnaux', 'thibault.gagnaux@students.fhnw.ch', '$2a$10$6ruyoKCOW2.NGxMvPJRaO.oZGhqEDdGpq5ZLXhNQJVsIpAaz823e.', true, 'DEVELOPER');

    /* Create a contract for each team member */
    INSERT INTO contract (id, start_date, end_date, pensum_percentage, employee_id) VALUES ('20dc78ec-6a83-4959-81e4-fd3007508218', now(), now(), 40, '6bae2bc7-9d1e-457d-ac92-dd78e467b7d1');
    INSERT INTO contract (id, start_date, end_date, pensum_percentage, employee_id) VALUES ('84e36d3c-b628-4cad-ad67-44c8ec0551ff', now(), now(), 100, '8d7a6f19-676d-42de-8f3a-039588a49c5f');
    INSERT INTO contract (id, start_date, end_date, pensum_percentage, employee_id) VALUES ('5b5befe0-6aea-4555-81b6-0918ec3aea0c', now(), now(), 100, '0ac9e9f4-66c6-44ed-b8d6-a038364afaa6');

    /* Create several projects */
    INSERT INTO project (id, name, fte_percentage, start_date, end_date, project_manager_id) VALUES ('7f8ba31a-5e71-4dda-b456-74e05a1b1622', 'IP5 Simon Wächter', 200, now(), now(), '6bae2bc7-9d1e-457d-ac92-dd78e467b7d1');
    INSERT INTO project (id, name, fte_percentage, start_date, end_date, project_manager_id) VALUES ('e3883e80-e1e6-4ebd-96fb-8ebb51fd9d17', 'IP6 Philipp Lüthi & Thibault Gagnaux', 200, '2019-02-01', '2019-08-16', '8d7a6f19-676d-42de-8f3a-039588a49c5f');
    INSERT INTO project (id, name, fte_percentage, start_date, end_date, project_manager_id) VALUES ('9b6fa757-531e-4d09-9744-e47a40384189', 'Projekt wodss', 300, now(), now(), '6bae2bc7-9d1e-457d-ac92-dd78e467b7d1');

    /* Create several allocations */
    /* Simon: His IP 5 */
    INSERT INTO allocation (id, start_date, end_date, pensum_percentage, contract_id, project_id) VALUES ('6f07d80c-265e-418e-94ce-2a9431402003', now(), now(), 20, '20dc78ec-6a83-4959-81e4-fd3007508218', '7f8ba31a-5e71-4dda-b456-74e05a1b1622');

    /* Philipp and Thibo: Their IP6 */
    INSERT INTO allocation (id, start_date, end_date, pensum_percentage, contract_id, project_id) VALUES ('d4b5ef1d-d3f6-4c1d-beb8-709c78ada131', now(), now(), 40, '84e36d3c-b628-4cad-ad67-44c8ec0551ff', 'e3883e80-e1e6-4ebd-96fb-8ebb51fd9d17');
    INSERT INTO allocation (id, start_date, end_date, pensum_percentage, contract_id, project_id) VALUES ('3c99f7fc-6747-4b2a-b918-adda16f2629b', now(), now(), 40, '5b5befe0-6aea-4555-81b6-0918ec3aea0c', 'e3883e80-e1e6-4ebd-96fb-8ebb51fd9d17');

    /* Simon, Philipp and Thibo: Their wodss workshop */
    INSERT INTO allocation (id, start_date, end_date, pensum_percentage, contract_id, project_id) VALUES ('d8d517b8-dc8d-4132-bcd7-4a59f913e151', now(), now(), 20, '20dc78ec-6a83-4959-81e4-fd3007508218', '9b6fa757-531e-4d09-9744-e47a40384189');
    INSERT INTO allocation (id, start_date, end_date, pensum_percentage, contract_id, project_id) VALUES ('f280eb22-664b-4515-a007-eaa9afedb371', now(), now(), 20, '84e36d3c-b628-4cad-ad67-44c8ec0551ff', '9b6fa757-531e-4d09-9744-e47a40384189');
    INSERT INTO allocation (id, start_date, end_date, pensum_percentage, contract_id, project_id) VALUES ('555f63a0-ca5a-4809-a7bd-1053969358fb', now(), now(), 20, '5b5befe0-6aea-4555-81b6-0918ec3aea0c', '9b6fa757-531e-4d09-9744-e47a40384189');
END;
$$;
