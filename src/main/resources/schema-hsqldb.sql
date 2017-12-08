CREATE TABLE USER (
  first_name  VARCHAR(150),
  last_name   VARCHAR(150),
  email       VARCHAR(150) NOT NULL,
  points      INTEGER DEFAULT 0
);
CREATE TABLE USER_TRANSFER (
  email       VARCHAR(150) NOT NULL,
  action_list VARCHAR(1000)
);