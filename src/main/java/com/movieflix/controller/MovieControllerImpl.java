package com.movieflix.controller;

import com.movieflix.controller.request.MovieRequest;
import com.movieflix.controller.response.MovieResponse;
import com.movieflix.entity.Movie;
import com.movieflix.mapper.MovieMapper;
import com.movieflix.service.MovieService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/movieflix/movie")
@RequiredArgsConstructor
@Tag(name = "Movie", description = "Recurso responsável pelo gerenciamento dos filmes.")
public class MovieControllerImpl implements MovieController {

    private final MovieService movieService;

    @PostMapping
    public ResponseEntity<MovieResponse> save(@Valid @RequestBody MovieRequest request) {
        Movie savedMovie = movieService.save(MovieMapper.toMovie(request));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(MovieMapper.toMovieResponse(savedMovie));
    }

    @GetMapping
    public ResponseEntity<List<MovieResponse>> findAll() {
        return ResponseEntity.ok(movieService.findAll()
                .stream()
                .map(movie -> MovieMapper.toMovieResponse(movie))
                .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieResponse> findById(@PathVariable Long id) {
        return movieService.findById(id)
                .map(movie -> ResponseEntity.ok(MovieMapper.toMovieResponse(movie)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovieResponse> update(@PathVariable Long id, @Valid @RequestBody MovieRequest request) {
        return movieService.update(id, MovieMapper.toMovie(request))
                .map(movie -> ResponseEntity.ok(MovieMapper.toMovieResponse(movie)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<MovieResponse>> findByCategory(@RequestParam Long category) {
        return ResponseEntity.ok(movieService.findByCategory(category)
                .stream()
                .map(MovieMapper::toMovieResponse)
                .toList());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<Movie> optMovie = movieService.findById(id);
        if (optMovie.isPresent()) {
            movieService.delete(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
