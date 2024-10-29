package com.movieflix.dto;

import jakarta.persistence.CollectionTable;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class MovieDto {

    private Integer movieId;

    @NotBlank(message = "Please provide the movie's title")
    private String title;

    @NotBlank(message = "Please provide the movie's director")
    private String director;

    @NotBlank(message = "Please provide the movie's studio")
    private String studio;

    @CollectionTable(name = "movie_cast")
    private Set<String> movieCast;

    private Integer releaseYear;

    @NotBlank(message = "Please provide the movie's poster")
    private String poster;

    @NotBlank(message = "Please provide the poster's url")
    private String posterUrl ;

}
