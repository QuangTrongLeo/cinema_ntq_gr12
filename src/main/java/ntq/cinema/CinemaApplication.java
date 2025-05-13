package ntq.cinema;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CinemaApplication {

	public static void main(String[] args) {
		// Load .env
		Dotenv dotenv = Dotenv.load();

		// Gán tất cả biến cần thiết vào System properties
		// ========== SERVER ==========
		System.setProperty("SERVER_PORT", dotenv.get("SERVER_PORT"));

		// ========== DATABASE ==========
		System.setProperty("SPRING_DATASOURCE_HOST", dotenv.get("SPRING_DATASOURCE_HOST"));
		System.setProperty("SPRING_DATASOURCE_PORT", dotenv.get("SPRING_DATASOURCE_PORT"));
		System.setProperty("SPRING_DATASOURCE_DB", dotenv.get("SPRING_DATASOURCE_DB"));
		System.setProperty("SPRING_DATASOURCE_USE_SSL", dotenv.get("SPRING_DATASOURCE_USE_SSL"));
		System.setProperty("SPRING_DATASOURCE_ALLOW_PUBLIC_KEY_RETRIEVAL", dotenv.get("SPRING_DATASOURCE_ALLOW_PUBLIC_KEY_RETRIEVAL"));
		System.setProperty("SPRING_DATASOURCE_SERVER_TIMEZONE", dotenv.get("SPRING_DATASOURCE_SERVER_TIMEZONE"));
		System.setProperty("SPRING_DATASOURCE_USERNAME", dotenv.get("SPRING_DATASOURCE_USERNAME"));
		System.setProperty("SPRING_DATASOURCE_PASSWORD", dotenv.get("SPRING_DATASOURCE_PASSWORD"));

		// ========== MAIL ==========
		System.setProperty("SPRING_MAIL_USERNAME", dotenv.get("SPRING_MAIL_USERNAME"));
		System.setProperty("SPRING_MAIL_PASSWORD", dotenv.get("SPRING_MAIL_PASSWORD"));

		// ========== JWT ==========
		System.setProperty("JWT_SECRET_KEY", dotenv.get("JWT_SECRET_KEY"));
		System.setProperty("JWT_ACCESS_TOKEN_EXPIRATION", dotenv.get("JWT_ACCESS_TOKEN_EXPIRATION"));
		System.setProperty("JWT_REFRESH_EXPIRATION", dotenv.get("JWT_REFRESH_EXPIRATION"));

		// ========== OTP ==========
		System.setProperty("OTP_EXPIRY_MINUTES", dotenv.get("OTP_EXPIRY_MINUTES"));

		// ========== API ==========
		System.setProperty("API_NTQ-CINEMA-URL", dotenv.get("API_NTQ-CINEMA-URL"));

		// Khởi động Spring Boot
		SpringApplication.run(CinemaApplication.class, args);
	}
}
