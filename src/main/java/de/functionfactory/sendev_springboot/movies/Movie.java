package de.functionfactory.sendev_springboot.movies;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Movie {
//    @Id
//    @SequenceGenerator(
//            name="customer_id_sequence",
//            sequenceName = "movie_id_sequence"
//    )
//    @GeneratedValue(
//            strategy = GenerationType.SEQUENCE,
//            generator = "movie_id_sequence"
//    )
    private Integer id;
//    @Column
    private String title;
//    @Column
    private String overview;
//    @Column
//    private String tagline;
////    @Column
//    private String runtime;
////    @Column
//    private String release_date;
////    @Column
//    private double revenue;
////    @Column
//    private String poster_path;

    //    id: string
//    title: string
//    overview?: string
//    tagline?: string
//    runtime: string
//    release_date: Date
//    revenue?: number
//    poster_path?: string


    public Movie(Integer id, String title, String overview, String tagline, String runtime, String release_date, double revenue, String poster_path) {
        this.id = id;
        this.title = title;
        this.overview = overview;
//        this.tagline = tagline;
//        this.runtime = runtime;
//        this.release_date = release_date;
//        this.revenue = revenue;
//        this.poster_path = poster_path;
    }

    public Movie() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

//    public String getTagline() {
//        return tagline;
//    }
//
//    public void setTagline(String tagline) {
//        this.tagline = tagline;
//    }
//
//    public String getRuntime() {
//        return runtime;
//    }
//
//    public void setRuntime(String runtime) {
//        this.runtime = runtime;
//    }
//
//    public String getRelease_date() {
//        return release_date;
//    }
//
//    public void setRelease_date(String release_date) {
//        this.release_date = release_date;
//    }
//
//    public double getRevenue() {
//        return revenue;
//    }
//
//    public void setRevenue(double revenue) {
//        this.revenue = revenue;
//    }
//
//    public String getPoster_path() {
//        return poster_path;
//    }
//
//    public void setPoster_path(String poster_path) {
//        this.poster_path = poster_path;
//    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", overview='" + overview + '\'' +
//                ", tagline='" + tagline + '\'' +
//                ", runtime='" + runtime + '\'' +
//                ", release_date='" + release_date + '\'' +
//                ", revenue=" + revenue +
//                ", poster_path='" + poster_path + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return Objects.equals(id, movie.id) && Objects.equals(title, movie.title) && Objects.equals(overview, movie.overview);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, overview);
    }
}
