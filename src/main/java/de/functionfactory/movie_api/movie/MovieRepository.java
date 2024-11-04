package de.functionfactory.movie_api.movie;

import de.functionfactory.movie_api.movie.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, String> {

    Page<Movie> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}

