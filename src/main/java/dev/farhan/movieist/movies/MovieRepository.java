package dev.farhan.movieist.movies;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends MongoRepository<Movie, String> {
    Optional<Movie> findMovieByImdbId(String imdbId);
    List<Movie> findByTitleContainingIgnoreCase(String title);
}