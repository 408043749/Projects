package com.linjiangpan.springneo4j.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linjiangpan.springneo4j.domain.Movie;
import com.linjiangpan.springneo4j.repository.MovieRepository;

@Service
public class Neo4jService {
	@Autowired
	private MovieRepository movieRepository;
	
	public void save(Movie movie){
		movieRepository.findAll();
		System.out.println(System.currentTimeMillis());
		movie.setId("13123f");
		movie.setTitle("123");
		//movieRepository.save(movie);
		System.out.println(System.currentTimeMillis());
	}
}
