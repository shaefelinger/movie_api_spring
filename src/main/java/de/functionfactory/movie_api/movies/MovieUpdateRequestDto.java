package de.functionfactory.movie_api.movies;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Size;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieUpdateRequestDto {
    @Size(min=3, message="Title must be at least 3 characters long")
    @Size(max=500, message="Title must be max 500 characters long")
    private String title;

    @Size(max = 2048)
    private String overview;

    private String tagline;

    @Pattern(regexp = "^([0-1]\\d|2[0-3]):([0-5]\\d):([0-5]\\d)$", message = "Time must be in the format HH:mm:ss")
    private String runtime;

    private String release_date;

    private Integer revenue;

    private String poster_path;
}

