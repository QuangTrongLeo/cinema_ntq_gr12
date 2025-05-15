package ntq.cinema.movie_module.controller;

import lombok.RequiredArgsConstructor;
import ntq.cinema.movie_module.dto.request.movie.MovieSearchRequest;
import ntq.cinema.movie_module.dto.response.movie.MovieResponse;
import ntq.cinema.movie_module.entity.Movie;
import ntq.cinema.movie_module.service.MovieService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${api.ntq-cinema-url}/movies")
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;

    @PostMapping("/user")
    public ResponseEntity<List<MovieResponse>> searchMovies(@RequestParam(required = false) String title) {
        List<MovieResponse> movieResponses = movieService.searchMovies(title);
        return ResponseEntity.ok(movieResponses);
    }
}
