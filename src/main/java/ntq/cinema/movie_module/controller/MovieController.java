package ntq.cinema.movie_module.controller;

import lombok.RequiredArgsConstructor;
import ntq.cinema.movie_module.service.MovieService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.ntq-cinema-url}/movies")
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;

//    @GetMapping("/all")
//    public ResponseEntity<List<MovieResponse>>  getAllMovies() {
//        List<MovieResponse> movies = movieService.getAllMovies();
//        return ResponseEntity.ok(movies);
//    }
//
//    @GetMapping("/status/{status}")
//    public ResponseEntity<List<MovieResponse>>  getMoviesByStatus(@PathVariable MovieStatusEnum status) {
//        List<MovieResponse> movies = movieService.getMoviesByStatus(status);
//        return ResponseEntity.ok(movies);
//    }
}
