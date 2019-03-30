CREATE TABLE employee (
    id BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email_address VARCHAR(120) NOT NULL,
    is_active BOOLEAN NOT NULL,
    role ENUM ('ADMINISTRATOR', 'PROJECTMANAGER', 'DEVELOPER')
);

CREATE TABLE project (
    id BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    name VARCHAR(50) NOT NULL,
    fte_percentage BIGINT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    project_manager_id BIGINT REFERENCES employee(id)
);

CREATE TABLE contract (
    id BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    pensum_percentage SMALLINT NOT NULL,
    employee_id BIGINT REFERENCES employee(id)
);

CREATE TABLE allocation (
    id BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    pensum_percentage SMALLINT NOT NULL,
    contract_id BIGINT REFERENCES contract(id),
    project_id BIGINT REFERENCES project(id)
);
