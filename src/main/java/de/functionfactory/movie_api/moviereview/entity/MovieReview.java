package de.functionfactory.movie_api.moviereview.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.functionfactory.movie_api.movie.entity.Movie;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(exclude = "movie")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "movie_review")
@Entity
public class MovieReview {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    @NotBlank(message = "Author name is required")
    private String authorName;

    @Column(nullable = false)
    @Size(min = 20, max = 1000, message = "Review content must be between 20 and 1000 characters")
    private String content;

    @Column(nullable = false)
    @NotNull(message = "Rating is required")
    @Min(value = 0, message = "Rating must be at least 0")
    @Max(value = 10, message = "Rating must not be greater than 10")
    private Integer rating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", nullable = false)
    @JsonIgnore
    private Movie movie;

    public String getMovieId() {
        return movie != null ? movie.getId() : null;
    }
}
