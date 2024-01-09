--DROP STATEMENTS IF TABLE EXISTS

DROP TABLE if exists updates;
DROP TABLE if exists transaction_data;
DROP TABLE if exists assembly_account;
DROP TABLE if exists department_account;
DROP TABLE if exists process_account;
DROP TABLE if exists manufacture_details;
DROP TABLE if exists fit_job;
DROP TABLE if exists paint_job;
DROP TABLE if exists cut_job;
DROP TABLE if exists job;
DROP TABLE if exists assembly;
DROP TABLE if exists fit;
DROP TABLE if exists paint;
DROP TABLE if exists cut;
DROP TABLE if exists process;
DROP TABLE if exists department;
DROP TABLE if exists account;
DROP TABLE if exists customer;



-- CREATE TABLES


-- CREATE customer TABLE
CREATE TABLE customer (
    name VARCHAR(255) PRIMARY KEY,
    address VARCHAR(255),
    category INT
);
-- creating non clustered B+ index on category in customer table.
create nonclustered index idx_category on customer(category);

-- CREATE account TABLE
CREATE TABLE account (
    acc_no INT PRIMARY KEY,
    acc_est_date DATE
);
-- Since clustered index is created by default i am commenting it 
--create clustered index idx_acc_no on account(account_no);

-- CREATE department TABLE
CREATE TABLE department (
    dept_no INT PRIMARY KEY,
    dept_data VARCHAR(255)
);

-- Since clustered index is created by default i am commenting it 
-- create clustered index idx_dept_no on department(dept_no);

-- CREATE process TABLE
CREATE TABLE process (
    process_id INT PRIMARY KEY,
    process_data VARCHAR(255),
    dept_no INT,
    FOREIGN KEY (dept_no) REFERENCES department(dept_no)
);
create nonclustered index idx_process_dept_no on process(dept_no);

-- CREATE cut TABLE
CREATE TABLE cut (
    process_id INT PRIMARY KEY nonclustered,
    cutting_type VARCHAR(255),
    machine_type VARCHAR(255),
    FOREIGN KEY (process_id) REFERENCES process(process_id)
);

-- CREATE paint TABLE
CREATE TABLE paint (
    process_id INT PRIMARY KEY nonclustered,
    paint_type VARCHAR(255),
    paint_method VARCHAR(255),
    FOREIGN KEY (process_id) REFERENCES process(process_id)
);

-- CREATE fit TABLE
CREATE TABLE fit (
    process_id INT PRIMARY KEY nonclustered,
    fit_type VARCHAR(255),
    FOREIGN KEY (process_id) REFERENCES process(process_id)
);

-- CREATE assembly TABLE
CREATE TABLE assembly (
    assembly_id INT PRIMARY KEY,
    name VARCHAR(255),
    date_ordered DATE,
    assembly_details VARCHAR(255),
    FOREIGN KEY (name) REFERENCES customer(name)
);

-- Since clustered index is created i am commenting it out
--create clustered index idx_assembly_id on assembly(assembly_id);

-- CREATE job TABLE
CREATE TABLE job (
    job_no INT PRIMARY KEY,
    job_start_date DATE,
    job_end_date DATE
);
create nonclustered index idx_date_ended on job(job_end_date);

-- CREATE cut_job TABLE
CREATE TABLE cut_job (
    job_no INT PRIMARY KEY,
    machine_type VARCHAR(255),
    time_used INT,
    material_used VARCHAR(255),
    labor_time INT,
    FOREIGN KEY (job_no) REFERENCES job(job_no)
);

-- Since clustered index is created default i am commenting it 
--create clustered index idx_cut_job_no on cut_job(job_no);

-- CREATE paint_job TABLE
CREATE TABLE paint_job (
    job_no INT PRIMARY KEY,
    colour VARCHAR(255),
    volume VARCHAR(255),
    labor_time INT,
    FOREIGN KEY (job_no) REFERENCES job(job_no)
);

-- Since clustered index is created by default i am commenting it 
--create clustered index idx_paint_job_no on paint_job(job_no);

-- CREATE fit_job TABLE
CREATE TABLE fit_job (
    job_no INT PRIMARY KEY,
    labor_time INT,
    FOREIGN KEY (job_no) REFERENCES job(job_no)
);

-- Since clustered index is created by default i am commenting it 
--create clustered index idx_fit_job_no on fit_job(job_no);

-- CREATE manufacture_details TABLE
CREATE TABLE manufacture_details (
    assembly_id INT,
    process_id INT,
    job_no INT,
    PRIMARY KEY (assembly_id, process_id),
    FOREIGN KEY (assembly_id) REFERENCES assembly(assembly_id),
    FOREIGN KEY (process_id) REFERENCES process(process_id),
    FOREIGN KEY (job_no) REFERENCES job(job_no)
);
create nonclustered index idx_manufacture_details_assembly_id on manufacture_details(assembly_id);

-- CREATE process_account TABLE
CREATE TABLE process_account (
    acc_no INT PRIMARY KEY,
    cost_details_3 VARCHAR(255),
    process_id INT,
    FOREIGN KEY (acc_no) REFERENCES account(acc_no),
    FOREIGN KEY (process_id) REFERENCES process(process_id)
);
-- Since clustered index is created by default i am commenting it 
-- create clustered index idx_process_account_number on process_account(account_no);

-- CREATE department_account TABLE
CREATE TABLE department_account (
    acc_no INT PRIMARY KEY,
    cost_details_2 VARCHAR(255),
    dept_no INT,
    FOREIGN KEY (acc_no) REFERENCES account(acc_no),
    FOREIGN KEY (dept_no) REFERENCES department(dept_no)
);
-- Since clustered index is created by default i am commenting it 
--create clustered index idx_department_account_number on department_account(account_no);

-- CREATE assembly_account TABLE
CREATE TABLE assembly_account (
    acc_no INT PRIMARY KEY,
    cost_details_1 VARCHAR(255),
    assembly_id INT,
    FOREIGN KEY (assembly_id) REFERENCES assembly(assembly_id)
);
create nonclustered index idx_assembly_acc_no on assembly_account(assembly_id);

-- CREATE transaction_data TABLE
CREATE TABLE transaction_data (
    transc_num INT PRIMARY KEY nonclustered,
    sup_cost INT,
    job_id INT,
    FOREIGN KEY (job_id) REFERENCES job(job_no)
);

-- CREATE updates TABLE
CREATE TABLE updates (
    acc_no INT ,
    transc_num INT,
    PRIMARY KEY nonclustered(acc_no, transc_num),
    FOREIGN KEY (transc_num) REFERENCES transaction_data(transc_num),
    FOREIGN KEY (acc_no) REFERENCES account(acc_no)
);




