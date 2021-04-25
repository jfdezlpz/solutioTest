package com.solutio.twitter.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.solutio.twitter.dto.Tweet;

public interface TweetDao extends JpaRepository<Tweet, Long> {
	
	@Query("SELECT t FROM Tweet t WHERE t.validation is true AND t.user = :id")
	public List<Tweet> findValidatedByUser(@Param("id") long id);

}
