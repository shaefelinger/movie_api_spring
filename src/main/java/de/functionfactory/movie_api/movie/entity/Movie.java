package de.functionfactory.movie_api.movie.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import de.functionfactory.movie_api.moviereview.entity.MovieReview;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "movie")
@Entity
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(length = 500)
    @Size(min = 3, message = "Title must be at least 3 characters long")
    @Size(max = 500, message = "Title must be max 500 characters long")
    private String title;

    @Column(length = 2048)
    private String overview;

    private String tagline;

    @Pattern(regexp = "^([0-1]\\d|2[0-3]):([0-5]\\d):([0-5]\\d)$", message = "Time must be in the format HH:mm:ss")
    private String runtime;

    private String release_date;

    private Integer revenue;

    private String poster_path;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<MovieReview> reviews;


    //    id: string
//    title: string
//    overview?: string
//    tagline?: string
//    runtime: string
//    release_date: Date
//    revenue?: number
//    poster_path?: string

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
//        Movie movie = (Movie) o;
//        return id != null && Objects.equals(id, movie.id);
//    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
