package com.solutio.twitter.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.solutio.twitter.dao.TweetDao;
import com.solutio.twitter.dto.Tweet;

import twitter4j.GeoLocation;
import twitter4j.Status;

@Service
public class TweetService {
	
	@Autowired
	private TweetDao tweetDao;
	
	public Tweet saveTweet(Tweet tweet) {
		return tweetDao.saveAndFlush(tweet);
	}

	public void saveTweet(Status status) {
		Tweet tweet = new Tweet();
		tweet.setText(status.getText());
		tweet.setUser(status.getUser().getId());
		tweet.setUserName(status.getUser().getName());
		GeoLocation location = status.getGeoLocation();
		if(Objects.nonNull(location)) {
			tweet.setLongitude(location.getLongitude());
			tweet.setLatitude(location.getLatitude());
		}
		tweet.setValidation(Boolean.FALSE);
		
		tweetDao.saveAndFlush(tweet);
	}
	
	public List<Tweet> getTweets() {
		return tweetDao.findAll();
	}
	
	public Optional<Tweet> validateTweet(long id) {
		Optional<Tweet> tweetOptional = tweetDao.findById(id);
		if(tweetOptional.isPresent()) {
			Tweet tweet = tweetOptional.get();
			tweet.setValidation(Boolean.TRUE);
			return Optional.of(tweetDao.saveAndFlush(tweet));
		}else {
			return Optional.empty(); 
		}
	}
	
	public List<Tweet> getValidatedTweetsByUser (long userId) {
		return tweetDao.findValidatedByUser(userId);
	}

}
