DROP PROCEDURE IF EXISTS ChangePaintJobColor;
DROP PROCEDURE IF EXISTS DeleteCutJobsInRange;
DROP PROCEDURE IF EXISTS GetCustomersByCategoryRange;
DROP PROCEDURE IF EXISTS GetProcessesForAssembly;
DROP PROCEDURE IF EXISTS GetTotalLaborTimeInDepartment;
DROP PROCEDURE IF EXISTS GetTotalCostForAssembly;
DROP PROCEDURE IF EXISTS UpdateAccountCosts;
DROP PROCEDURE IF EXISTS CompleteJobWithDetails;
DROP PROCEDURE IF EXISTS EnterNewJob;
DROP PROCEDURE IF EXISTS CreateNewAccountAndAssociate;
DROP PROCEDURE IF EXISTS InsertNewAssembly;
DROP PROCEDURE IF EXISTS InsertProcess;
DROP PROCEDURE IF EXISTS InsertDepartment;
DROP PROCEDURE IF EXISTS InsertCustomer;


--1
GO
CREATE PROCEDURE InsertCustomer
    @customer_name VARCHAR(255),
    @address VARCHAR(255),
    @category INT
AS
BEGIN
    INSERT INTO customer (name, address, category)
    VALUES (@customer_name, @address, @category);
END;

--2
go
CREATE PROCEDURE InsertDepartment
    @dept_no INT,
    @dept_data VARCHAR(255)
AS
BEGIN
    INSERT INTO Department (dept_no, dept_data)
    VALUES (@dept_no, @dept_data);
END;


-- 3

GO
CREATE PROCEDURE InsertProcess
    @p_process_id INT,
    @p_process_data VARCHAR(255),
    @p_dept_no INT,
    @p_dept_data VARCHAR(255),
    @p_type VARCHAR(50),
    @p_type_info1 VARCHAR(255),
    @p_type_info2 VARCHAR(255)
AS
BEGIN
    -- Insert into process table
    INSERT INTO process(process_id, process_data, dept_no) VALUES (@p_process_id, @p_process_data, @p_dept_no);

    -- Insert into appropriate type table based on the given type
    IF @p_type = 'fit'
    BEGIN
        INSERT INTO fit(process_id, fit_type) VALUES (@p_process_id, @p_type_info1);
    END
    ELSE IF @p_type = 'cut'
    BEGIN
        INSERT INTO cut(process_id, cutting_type, machine_type) VALUES (@p_process_id, @p_type_info1, @p_type_info2);
    END
    ELSE IF @p_type = 'paint'
    BEGIN
        INSERT INTO paint(process_id, paint_type, paint_method) VALUES (@p_process_id, @p_type_info1, @p_type_info2);
    END

END;



--4

GO
CREATE PROCEDURE InsertNewAssembly
    @customerName NVARCHAR(255),
    @assemblyDetails NVARCHAR(255),
    @assemblyID INT,
    @dateOrdered DATE,
    @processIDs NVARCHAR(MAX)
AS
BEGIN
    DECLARE @i INT = 0;

    -- Insert into the assembly table
    INSERT INTO assembly (assembly_id, name, assembly_details, date_ordered)
    VALUES (@assemblyID, @customerName, @assemblyDetails, @dateOrdered);

    -- Associate the assembly with specified processes
    DECLARE @processID INT;
    DECLARE @pos INT;

    WHILE LEN(@processIDs) > 0
    BEGIN
        SET @pos = CHARINDEX(',', @processIDs);

        IF @pos > 0
            SET @processID = CAST(LEFT(@processIDs, @pos - 1) AS INT);
        ELSE
            SET @processID = CAST(@processIDs AS INT);

        INSERT INTO manufacture_details (assembly_id, process_id)
        VALUES (@assemblyID, @processID);

        IF @pos > 0
            SET @processIDs = SUBSTRING(@processIDs, @pos + 1, LEN(@processIDs) - @pos);
        ELSE
            SET @processIDs = '';
    END;
END;

--5
GO
CREATE PROCEDURE CreateNewAccountAndAssociate
    @acc_no INT,
    @acc_est_date DATE,
    @process_id INT = NULL,
    @assembly_id INT = NULL,
    @dept_no INT = NULL
AS
BEGIN
    
    -- Insert into the account table
    INSERT INTO account (acc_no, acc_est_date)
    VALUES (@acc_no, @acc_est_date);

    -- Associate the account with process, assembly, or department accounts
    IF @process_id <> 0
    BEGIN
        INSERT INTO process_account (acc_no, process_id)
        VALUES (@acc_no, @process_id);
    END

    IF @assembly_id <>0
    BEGIN
        INSERT INTO assembly_account (acc_no, assembly_id)
        VALUES (@acc_no, @assembly_id);
    END

    IF @dept_no <>0
    BEGIN
        INSERT INTO department_account (acc_no, dept_no)
        VALUES (@acc_no, @dept_no);
    END
END



--6
GO
CREATE PROCEDURE EnterNewJob
    @job_no INT,
    @assembly_id INT,
    @process_id INT,
    @job_start_date DATE
AS
BEGIN
    -- Insert the new job
    INSERT INTO job (job_no, job_start_date)
    VALUES (@job_no, @job_start_date);

    -- Associate the job with assembly and process
    UPDATE manufacture_details
    SET job_no = @job_no
    WHERE assembly_id = @assembly_id
        AND process_id = @process_id;
END

--7
GO
CREATE PROCEDURE CompleteJobWithDetails
    @job_no INT,
    @completion_date DATE,
    @job_type VARCHAR(50),
    @machine_type VARCHAR(255) = NULL,
    @time_used INT = NULL,
    @material_used VARCHAR(255) = NULL,
    @labor_time INT = NULL,
    @color VARCHAR(255) = NULL,
    @volume VARCHAR(255) = NULL,
    @job_labor INT = NULL
AS
BEGIN
    -- Update the job with the completion date
    UPDATE job
    SET job_end_date = @completion_date
    WHERE job_no = @job_no;

    -- Determine the job type and insert information
    IF @job_type = 'cut-job'
    BEGIN
        -- Insert Cut-job specific information into cut_job table
        INSERT INTO cut_job (job_no, machine_type, time_used, material_used, labor_time)
        VALUES (@job_no, @machine_type, @time_used, @material_used, @labor_time);
    END
    ELSE IF @job_type = 'paint-job'
    BEGIN
        -- Insert Paint-job specific information into paint_job table
        INSERT INTO paint_job (job_no, colour, volume, labor_time)
        VALUES (@job_no, @color, @volume, @job_labor);
    END
    ELSE IF @job_type = 'fit-job'
    BEGIN
        -- Insert Fit-job specific information into fit_job table
        INSERT INTO fit_job (job_no, labor_time)
        VALUES (@job_no, @job_labor);
    END
END



--8
GO
CREATE PROCEDURE UpdateAccountCosts
    @transc_num INT,
    @sup_cost INT,
    @acc_no INT
AS
BEGIN


    -- Update the process account cost details
    UPDATE process_account
    SET cost_details_3 = ISNULL(cost_details_3, 0) + @sup_cost
    WHERE acc_no = @acc_no

    -- Update the department account cost details
    UPDATE department_account
    SET cost_details_2 = ISNULL(cost_details_2, 0) + @sup_cost
    WHERE acc_no = @acc_no

    -- Update the assembly account cost details
    UPDATE assembly_account
    SET cost_details_1 = ISNULL(cost_details_1, 0) + @sup_cost
    WHERE acc_no = @acc_no

    -- Insert the transaction into the updates table
    INSERT INTO transaction_data (transc_num, sup_cost)
    VALUES (@transc_num, @sup_cost)

    INSERT INTO updates (acc_no, transc_num)
    VALUES (@acc_no, @transc_num);
END;




--9

GO
CREATE PROCEDURE GetTotalCostForAssembly
    @assembly_id INT
AS
BEGIN
    DECLARE @total_cost DECIMAL(10, 2);

    -- Calculate the total cost for the given assembly ID
    SELECT @total_cost = ISNULL(SUM(CAST(ISNULL(aa.cost_details_1, 0) AS DECIMAL(10, 2))), 0)
    FROM assembly_account aa
    WHERE aa.assembly_id = @assembly_id;

    -- Return the total cost
    SELECT @total_cost AS TotalCost;
END


--10
GO
CREATE PROCEDURE GetTotalLaborTimeInDepartment 
    @targetDate DATE,
    @deptNo INT
AS
BEGIN
    SELECT
        d.dept_no,
        d.dept_data AS department_name,
        SUM(COALESCE(cut_job.labor_time, 0) + COALESCE(paint_job.labor_time, 0) + COALESCE(fit_job.labor_time, 0)) AS total_labor_time
    FROM
        department d
        LEFT JOIN process p ON d.dept_no = p.dept_no
        LEFT JOIN cut c ON p.process_id = c.process_id
        LEFT JOIN paint pa ON p.process_id = pa.process_id
        LEFT JOIN fit f ON p.process_id = f.process_id
        LEFT JOIN manufacture_details md ON p.process_id = md.process_id
        LEFT JOIN cut_job ON md.job_no = cut_job.job_no
        LEFT JOIN paint_job ON md.job_no = paint_job.job_no
        LEFT JOIN fit_job ON md.job_no = fit_job.job_no
        LEFT JOIN job j ON md.job_no = j.job_no
    WHERE
        j.job_end_date = @targetDate
        AND d.dept_no = @deptNo
    GROUP BY
        d.dept_no, d.dept_data;
END;



--11
GO
CREATE PROCEDURE GetProcessesForAssembly
    @assemblyId INT
AS
BEGIN
    -- Retrieve processes and department information for the given assembly ID
    SELECT p.process_id, p.process_data, d.dept_data, j.job_start_date
    FROM manufacture_details md
    JOIN process p ON md.process_id = p.process_id
    JOIN department d ON p.dept_no = d.dept_no
    JOIN job j ON md.job_no = j.job_no
    WHERE md.assembly_id = @assemblyId
    ORDER BY j.job_start_date;
END;

--12

GO
CREATE PROCEDURE GetCustomersByCategoryRange
    @minCategory INT,
    @maxCategory INT
AS
BEGIN
    -- Retrieve customers within the specified category range in name order
    SELECT name, address, category
    FROM customer
    WHERE category BETWEEN @minCategory AND @maxCategory
    ORDER BY name;
END

--13
GO
CREATE PROCEDURE DeleteCutJobsInRange
    @minJobNo INT,
    @maxJobNo INT
AS
BEGIN
    -- Delete cut-jobs whose job numbers are in the specified range
    DELETE FROM cut_job
    WHERE job_no BETWEEN @minJobNo AND @maxJobNo;
END


--14
GO
CREATE PROCEDURE ChangePaintJobColor
    @job_no INT,
    @newColor VARCHAR(255)
AS
BEGIN
    -- Update the color of the specified paint job
    UPDATE paint_job
    SET colour = @newColor
    WHERE job_no = @job_no;
END



--ERROR Checking 
--1
Insert into customer values ('Akhil', 'Texas', 10)
--2
INSERT INTO account(acc_no, acc_est_date)
values(123,'2023-11-14',100.00)
--3
INSERT into departmen_account VALUES (20,30,40)




























-- SELECT * FROM job;
-- SELECT * FROM fit_job;
-- SELECT * FROM paint_job;
-- SELECT * FROM cut_job;


-- SELECT * FROM assembly;
-- SELECT * FROM fit;
-- SELECT * FROM paint;
-- SELECT * FROM cut;
-- SELECT * FROM process;
-- SELECT * FROM department;
-- SELECT * FROM account;
-- SELECT * FROM transaction_data;
-- SELECT * FROM assembly_account;
-- SELECT * FROM department_account;
-- SELECT * FROM process_account;
-- SELECT * FROM updates;
-- SELECT * FROM customer;

-- SELECT * FROM manufacture_details;