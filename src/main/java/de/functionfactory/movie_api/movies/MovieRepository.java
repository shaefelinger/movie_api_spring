package de.functionfactory.movie_api.movies;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MovieRepository extends JpaRepository<Movie, String> {
    List<Movie> findByTitleContainingIgnoreCase(String title);
}

