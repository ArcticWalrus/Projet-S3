--Creating table from insert
INSERT INTO tblstruct
  VALUES (
    DEFAULT ,     -- To avoid issues with Serial, the DEFAULT clause must be added
    't000000000', --The tblIdentifier will start with a 't' followed by a series of 9 numbers.
    'Temp',       -- The name of the column can be anything that fits within 30 chars.
    'INT'         --The last value MUST be a DATATYPE for the code to update the table.
  );

--Adding a column from insert
INSERT INTO tblstruct VALUES (DEFAULT , 't000000000', 'Date', 'DATE'); -- Using the same sentence structure, the trigger will not create the table if it already exists.

--Adding Values into the created tables
INSERT INTO t000000000 VALUES (DEFAULT , 30, '2018-11-25');

--Searching for values
SELECT temp       --The column you want to search in.
FROM t000000000   --The table you are looking in.
WHERE temp = 30   --The value you want.
;

--Updating values
UPDATE t000000000 --The table you want to update in.
SET temp = 40     --The column in wich the change will be done and the value you want changed.
WHERE temp = 30   --the original value you want changed.
;