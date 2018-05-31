CREATE TABLE tblStruct
(
  serStruct SERIAL not null PRIMARY KEY ,
  tblIdentifiers VARCHAR(10) not null ,
  valColumns VARCHAR(30) not null,
  valValues VARCHAR(16) not null
);

CREATE TABLE Users
(
  serUsers SERIAL not null ,
  valCIP VARCHAR(8) not null PRIMARY KEY
);

CREATE TABLE Devices
(
  serDevices SERIAL not null ,
  namDevice VARCHAR(30) not null ,
  valCIP VARCHAR(8) not null REFERENCES Users(valCIP),
  valIP VARCHAR(15) ,
  valMAC VARCHAR(30) ,
  valIO VARCHAR(30) UNIQUE
);

CREATE TABLE IO
(
  serIO SERIAL not null ,
  valIO VARCHAR(30) not null REFERENCES Devices(valIO)
);

CREATE TABLE Log
(
  serSerial SERIAL not null ,
  valErrorCode TEXT not null ,
  ValCIP VARCHAR(8) ,
  datDate TIMESTAMP not null ,
  Details TEXT not null
);

CREATE TABLE InputGroup
(
  serInputGroup SERIAL not null ,
  /*nam*/ConditionGroup INTEGER not null ,
  /*val*/InputId INTEGER not null ,
  /*val*/Ordre INTEGER not null
);

CREATE TABLE Conditions
(
  serConditions SERIAL not null ,
  /*val*/Operation INTEGER not null ,
  /*val*/InputGroup INTEGER not null ,
  /*val*/OutputGroup INTEGER not null
);

CREATE TABLE IntInput
(
  serIntInput SERIAL not null ,
  namName TEXT not null ,
  /*val*/Value numeric not null ,
  /*val*/Type INTEGER not null
);