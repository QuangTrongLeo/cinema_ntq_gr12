package ntq.cinema.movie_module.repository;

import ntq.cinema.movie_module.entity.MovieStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieStatusRepository extends JpaRepository<MovieStatus, Long> {
}
