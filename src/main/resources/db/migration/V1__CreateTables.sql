CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TYPE role AS ENUM ('ADMINISTRATOR', 'PROJECTMANAGER', 'DEVELOPER');

CREATE TABLE employee (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email_address VARCHAR(120) UNIQUE NOT NULL,
    password_hash VARCHAR(60) NOT NULL,
    is_active BOOLEAN NOT NULL,
    role role NOT NULL
);

CREATE TABLE project (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(50) NOT NULL,
    fte_percentage BIGINT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    project_manager_id UUID REFERENCES employee(id)
);

CREATE TABLE contract (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    pensum_percentage SMALLINT NOT NULL,
    employee_id UUID REFERENCES employee(id)
);

CREATE TABLE allocation (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    pensum_percentage SMALLINT NOT NULL,
    contract_id UUID REFERENCES contract(id),
    project_id UUID REFERENCES project(id)
);
