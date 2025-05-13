package ntq.cinema.movie_module.mapper;

import ntq.cinema.movie_module.dto.response.movie.MovieResponse;
import ntq.cinema.movie_module.entity.Movie;
import org.springframework.stereotype.Component;

@Component
public class MovieMapper {

    public MovieResponse toResponse(Movie movie) {
        if (movie == null) {
            return null;
        }

        return MovieResponse.builder()
                .movieId(movie.getMovieId())
                .title(movie.getTitle())
                .duration(movie.getDuration())
                .releaseDate(movie.getReleaseDate())
                .director(movie.getDirector())
                .description(movie.getDescription())
                .posterUrl(movie.getPosterUrl())
                .genreName(movie.getGenre() != null ? movie.getGenre().getName() : null)
                .statusName(movie.getStatus() != null ? movie.getStatus().getName().name() : null)
                .build();
    }
}
