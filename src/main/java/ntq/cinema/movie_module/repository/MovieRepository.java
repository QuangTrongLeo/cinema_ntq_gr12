package ntq.cinema.movie_module.repository;

import ntq.cinema.movie_module.entity.Genre;
import ntq.cinema.movie_module.entity.Movie;
import ntq.cinema.movie_module.enums.MovieStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findByStatus_NameOrderByMovieIdDesc(MovieStatusEnum status);

    List<Movie> findAllByOrderByMovieIdDesc();

    List<Movie> findByTitleContainingIgnoreCaseOrderByMovieIdDesc(String title);

    // 4.2.4 Tìm danh sách phim theo thể loại, sắp xếp giảm dần theo id movie
    List<Movie> findByGenreOrderByMovieIdDesc(Genre genre);
}
