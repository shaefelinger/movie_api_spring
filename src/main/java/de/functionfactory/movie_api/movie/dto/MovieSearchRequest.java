package de.functionfactory.movie_api.movie.dto;

import lombok.Data;
import jakarta.validation.constraints.Size;
@Data
public class MovieSearchRequest {
    @Size(min=3, message="Author must be at least 3 characters long")
    private String title;
    private String overview;
}
