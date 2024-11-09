package de.functionfactory.movie_api.movie;

import de.functionfactory.movie_api.movie.entity.Movie;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MoviePageResponse {
    private List<Movie> data;
    private int page;
    private int limit;
    private int lastPage;

    public MoviePageResponse(Page<Movie> page) {
        this.data = page.getContent();
        this.page = page.getNumber();
        this.limit = page.getSize();
        this.lastPage = page.getTotalPages();
    }
}
