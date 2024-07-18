-- Creation of movie table
CREATE TABLE IF NOT EXISTS movies (
  id varchar(250) UNIQUE NOT NULL,
  title varchar(500) NOT NULL,
  overview varchar(10000),
  tagline varchar(1000),
  runtime varchar(250) NOT NULL,
  release_date varchar(250) NOT NULL,
  revenue INT,
  poster_path varchar(800),
  PRIMARY KEY (id)
);




