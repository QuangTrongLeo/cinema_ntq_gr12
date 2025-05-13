package ntq.cinema.movie_module.service;

import ntq.cinema.movie_module.entity.Movie;
import ntq.cinema.movie_module.enums.MovieStatusEnum;
import ntq.cinema.movie_module.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public List<Movie> getAllMovies() {
        return movieRepository.findAllByOrderByMovieIdDesc();
    }

    public List<Movie> getMoviesByStatus(MovieStatusEnum status) {
        return movieRepository.findByStatus_NameOrderByMovieIdDesc(status);
    }
}
