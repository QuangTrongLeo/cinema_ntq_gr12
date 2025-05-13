package ntq.cinema.movie_module.dto.response.movie;

import lombok.*;

import java.time.LocalDate;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MovieResponse {
    private long movieId;
    private String title;
    private int duration;
    private LocalDate releaseDate;
    private String director;
    private String description;
    private String posterUrl;
    private String genreName;        // Lấy tên thể loại từ Genre
    private String statusName;       // Lấy tên trạng thái từ MovieStatusEnum
}
