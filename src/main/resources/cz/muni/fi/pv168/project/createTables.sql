CREATE TABLE Agent (
    Id BIGINT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    Alias VARCHAR(100),
    Experience VARCHAR(30)
);

CREATE TABLE Mission (
    Id BIGINT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    Codename VARCHAR(100),
    Description VARCHAR(3000),
    Difficulty VARCHAR(30)
);

CREATE TABLE Assignment (
    Id BIGINT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    Status VARCHAR(30),
    Start DATE,
    AgentId BIGINT REFERENCES Agent (Id),
    MissionId BIGINT REFERENCES Mission (Id)
);