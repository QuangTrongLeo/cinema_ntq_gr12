package ntq.cinema.movie_module.controller;

import lombok.RequiredArgsConstructor;
import ntq.cinema.movie_module.dto.request.movie.MovieSearchRequest;
import ntq.cinema.movie_module.dto.response.movie.MovieResponse;
import ntq.cinema.movie_module.entity.Movie;
import ntq.cinema.movie_module.service.MovieService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


//    4.2 Lọc phim theo thể loại
    @PostMapping("/user/by-genre")
    public ResponseEntity<List<MovieResponse>> searchMoviesByGenreName(@RequestParam String genre) {
        //    4.2.1 Gọi service để xử lý logic lọc phim
        List<MovieResponse> movieResponses = movieService.searchMoviesByGenreName(genre);
        // 4.2.6 Trả kết quả danh sách phim phù hợp theo thể loại
        return ResponseEntity.ok(movieResponses);
    }
}
