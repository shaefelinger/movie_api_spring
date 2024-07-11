package de.functionfactory.movie_api.movies;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="movies")
@Entity
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Column(length = 2048)
    private String overview;
    private String tagline;
    private String runtime;
    private String release_date;
    private double revenue;
    private String poster_path;


    //    id: string
//    title: string
//    overview?: string
//    tagline?: string
//    runtime: string
//    release_date: Date
//    revenue?: number
//    poster_path?: string

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Movie movie = (Movie) o;
        return id != null && Objects.equals(id, movie.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
