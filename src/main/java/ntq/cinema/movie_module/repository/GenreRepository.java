package ntq.cinema.movie_module.repository;

import ntq.cinema.movie_module.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// 4.2.3 Tìm thể loại theo tên
public interface GenreRepository extends JpaRepository<Genre, Long> {
    Optional<Genre> findByName(String name);


}
