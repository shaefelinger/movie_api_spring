package de.functionfactory.movie_api.movies;

import org.springframework.data.jpa.repository.JpaRepository;

import java.beans.JavaBean;

public interface MovieRepository extends JpaRepository<Movie, Long> {
}
