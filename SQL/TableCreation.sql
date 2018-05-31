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