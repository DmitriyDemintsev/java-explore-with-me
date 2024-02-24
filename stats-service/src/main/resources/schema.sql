DROP TABLE IF EXISTS applications CASCADE;
DROP TABLE IF EXISTS stats CASCADE;

CREATE TABLE IF NOT EXISTS applications (
  id INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  app VARCHAR NOT NULL,
  CONSTRAINT pk_application PRIMARY KEY (id),
  CONSTRAINT uq_app UNIQUE (app)
);

CREATE TABLE IF NOT EXISTS stats (
  id INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  app_id INTEGER REFERENCES applications(id),
  uri VARCHAR NOT NULL,
  ip VARCHAR(16) NOT NULL,
  time_stamp VARCHAR,
  CONSTRAINT pk_stat PRIMARY KEY (id),
  CONSTRAINT fk_app_id FOREIGN KEY (app_id) REFERENCES applications (id)
);
