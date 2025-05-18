package ntq.cinema.movie_module.service;

import ntq.cinema.movie_module.dto.request.movie.MovieSearchRequest;
import ntq.cinema.movie_module.dto.response.movie.MovieResponse;
import ntq.cinema.movie_module.entity.Genre;
import ntq.cinema.movie_module.entity.Movie;
import ntq.cinema.movie_module.enums.MovieStatusEnum;
import ntq.cinema.movie_module.mapper.MovieMapper;
import ntq.cinema.movie_module.repository.GenreRepository;
import ntq.cinema.movie_module.repository.MovieRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;
    private final MovieMapper movieMapper;

    public MovieService(MovieRepository movieRepository,
                        GenreRepository genreRepository,
                        MovieMapper movieMapper) {
        this.movieRepository = movieRepository;
        this.genreRepository = genreRepository;
        this.movieMapper = movieMapper;
    }

    public List<Movie> getAllMovies() {
        return movieRepository.findAllByOrderByMovieIdDesc();
    }

    public List<Movie> getMoviesByStatus(MovieStatusEnum status) {
        return movieRepository.findByStatus_NameOrderByMovieIdDesc(status);
    }

    public List<MovieResponse> searchMovies(String title){
        List<Movie> movies;

        if (title == null || title.trim().isEmpty()) {
            movies = movieRepository.findAllByOrderByMovieIdDesc();
        } else {
            movies = movieRepository.findByTitleContainingIgnoreCaseOrderByMovieIdDesc(title);
        }

        return movieMapper.mapperToResponseList(movies);
    }

    // 4.2 Lọc phim theo thể loại
    public List<MovieResponse> searchMoviesByGenreName(String nameGenre) {
        // 4.2.3 Tìm thể loại theo tên từ genreRepository
        Genre genre = genreRepository.findByName(nameGenre).orElse(null);
        // 4.2.4  Tìm danh sách phim thuộc thể loại, sắp xếp theo MovieId giảm dần
        List<Movie> movies = movieRepository.findByGenreOrderByMovieIdDesc(genre);
        // 4.2.5 Chuyển danh sách entity movie thành movieReponse để trả ra ngoài
        return movieMapper.mapperToResponseList(movies);
    }
}
