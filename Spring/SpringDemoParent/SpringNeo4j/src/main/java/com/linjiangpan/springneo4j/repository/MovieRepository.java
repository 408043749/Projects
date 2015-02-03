package com.linjiangpan.springneo4j.repository;

import org.springframework.data.neo4j.repository.GraphRepository;

import com.linjiangpan.springneo4j.domain.Movie;

public interface MovieRepository extends GraphRepository<Movie> {

}
