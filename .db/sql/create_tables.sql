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


-- Creation of movie_reviews table
CREATE TABLE IF NOT EXISTS movie_reviews (
    id varchar(250) UNIQUE NOT NULL,
    author_name varchar(500) NOT NULL,
    content varchar(10000),
    rating INT CONSTRAINT rating CHECK (rating <= 10 AND rating >= 0),
    movie_id varchar(250) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_customer
    FOREIGN KEY(movie_id)
    REFERENCES movies(id)
);



