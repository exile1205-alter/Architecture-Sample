package com.example.demo;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication
@RestController
public class DemoApplication {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private Random random = new Random();

	private static final String baseUrl = "http://localhost:8080/";

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@GetMapping("/")
	public String index() {
		return "Greetings from Spring Boot!";
	}

	@RequestMapping(value = "/{shortUrl}",  method = RequestMethod.POST)
	public String redirectToLongUrl(@PathVariable("shortUrl") String shortUrl){
		String sql = "SELECT long_url from url_mappings where short_url=?";
		String longUrl = jdbcTemplate.queryForObject(sql, String.class, shortUrl);
		if (longUrl != null){
			return "redirect:" + longUrl;
		}else{
			return "Short URL not found";
		}
	}

	@RequestMapping(value = "/createShortUrl/{longUrl}", method = RequestMethod.POST)
	public String createShortUrl(@PathVariable("longUrl") String longUrl){
		String shortUrl = generateShortUrl(longUrl);
		String sql =  "INSERT into url_mappings (short_url, long_url) VALUES (?, ?)";
		jdbcTemplate.update(sql, shortUrl, longUrl);
		return baseUrl + shortUrl;

	}
	private String generateShortUrl(String longUrl){
		String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKL<MOPQRSTUVWXYZ0123456789";
		StringBuilder shortUrl = new StringBuilder();
		for (int i = 0; i< 6; i++){
			shortUrl.append(characters.charAt(random.nextInt(characters.length())));
		}
		return shortUrl.toString();
	}
}
