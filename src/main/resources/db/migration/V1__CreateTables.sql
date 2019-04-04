CREATE TYPE role AS ENUM ('ADMINISTRATOR', 'PROJECTMANAGER', 'DEVELOPER');

CREATE TABLE employee (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email_address VARCHAR(120) UNIQUE NOT NULL,
    password_hash VARCHAR(60) NOT NULL,
    is_active BOOLEAN NOT NULL,
    role role NOT NULL
);

CREATE TABLE project (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    fte_percentage BIGINT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    project_manager_id BIGINT REFERENCES employee(id)
);

CREATE TABLE contract (
    id BIGSERIAL PRIMARY KEY,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    pensum_percentage SMALLINT NOT NULL,
    employee_id BIGINT REFERENCES employee(id)
);

CREATE TABLE allocation (
    id BIGSERIAL PRIMARY KEY,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    pensum_percentage SMALLINT NOT NULL,
    contract_id BIGINT REFERENCES contract(id),
    project_id BIGINT REFERENCES project(id)
);
