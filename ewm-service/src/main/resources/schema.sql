DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS events CASCADE;
DROP TABLE IF EXISTS categories CASCADE;
DROP TABLE IF EXISTS requests CASCADE;
DROP TABLE IF EXISTS compilations CASCADE;
DROP TABLE IF EXISTS compilation_events CASCADE;
DROP TABLE IF EXISTS comments CASCADE;

CREATE TABLE IF NOT EXISTS users (
  user_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  email VARCHAR(256) NOT NULL,
  name VARCHAR(256) NOT NULL,
  CONSTRAINT pk_user PRIMARY KEY (user_id),
  CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS categories (
  cat_id INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name VARCHAR(50) NOT NULL,
  CONSTRAINT pk_categories PRIMARY KEY (cat_id),
  CONSTRAINT UQ_CATEGORIES_NAME UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS events (
  event_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  annotation VARCHAR(2000) NOT NULL,
  cat_id BIGINT NOT NULL,
  created_on TIMESTAMP WITHOUT TIME ZONE,
  description VARCHAR(7000),
  event_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  initiator_id BIGINT NOT NULL REFERENCES users (user_id),
  lat FLOAT NOT NULL,
  lon FLOAT NOT NULL,
  paid BOOLEAN,
  participant_limit INTEGER,
  published_on TIMESTAMP WITHOUT TIME ZONE,
  request_moderation BOOLEAN,
  state VARCHAR(16),
  title VARCHAR(120),
  CONSTRAINT pk_events PRIMARY KEY (event_id),
  CONSTRAINT fk_events_cat FOREIGN KEY (cat_id) REFERENCES categories (cat_id)
);

CREATE TABLE IF NOT EXISTS requests (
  req_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  req_created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  event_id BIGINT NOT NULL,
  user_id BIGINT NOT NULL,
  status VARCHAR(16) NOT NULL,
  CONSTRAINT pk_requests PRIMARY KEY (req_id),
  CONSTRAINT fk_requests_event FOREIGN KEY (event_id) REFERENCES events (event_id),
  CONSTRAINT fk_requests_user FOREIGN KEY (user_id) REFERENCES users (user_id)
);

CREATE TABLE IF NOT EXISTS compilations (
  comp_id INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  pinned BOOLEAN NOT NULL,
  title VARCHAR(64) NOT NULL,
  CONSTRAINT pk_compilations PRIMARY KEY (comp_id)
);

CREATE TABLE IF NOT EXISTS compilation_events (
  compilation_id BIGINT NOT NULL,
  event_id BIGINT NOT NULL,
  CONSTRAINT events_compilation FOREIGN KEY (compilation_id) REFERENCES compilations (comp_id),
  CONSTRAINT fk_compilation_events FOREIGN KEY (event_id) REFERENCES events (event_id)
);

CREATE TABLE IF NOT EXISTS comments (
 id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
 text VARCHAR NOT NULL,
 event_id BIGINT REFERENCES events (event_id),
 author_id BIGINT REFERENCES users (user_id),
 created TIMESTAMP WITHOUT TIME ZONE NOT NULL
 );

