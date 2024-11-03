package de.functionfactory.movie_api.moviereview.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.functionfactory.movie_api.movie.entity.Movie;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewUpdateRequestDto {

    private String id;

    private String authorName;

    @Size(min = 20, max = 1000, message = "Review content must be between 20 and 1000 characters")
    private String content;

    @Min(value = 0, message = "Rating must be at least 0")
    @Max(value = 10, message = "Rating must not be greater than 10")
    private Integer rating;

}
