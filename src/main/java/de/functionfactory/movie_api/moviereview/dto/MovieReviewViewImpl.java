package de.functionfactory.movie_api.moviereview.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MovieReviewViewImpl implements MovieReviewView {
    private final String id;
    private final String authorName;
    private final String content;
    private final Integer rating;
    private final String movieId;
}
