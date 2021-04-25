package com.solutio.twitter.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.solutio.twitter.dto.Tweet;
import com.solutio.twitter.service.TweetService;
import com.solutio.twitter.service.TwitterConsultService;

import twitter4j.Trend;
import twitter4j.TwitterException;

@RestController
public class TwitterApi {
	
	@Autowired
	private TweetService tweetService;
	
	@Autowired
	private TwitterConsultService twitterConsultService;

	@RequestMapping(value="/tweets", method=RequestMethod.GET)
	public List<Tweet> getTweets() {
		return tweetService.getTweets();
	}
	
	@RequestMapping(value="/validateTweet", method=RequestMethod.PUT)
	public Tweet validateTweet(long tweetId) {
		return tweetService.validateTweet(tweetId).get();
	}
	
	@RequestMapping(value="/getValidatedTweets", method=RequestMethod.GET)
	public List<Tweet> getValidatedTweetsByUser(long id) {
		return tweetService.getValidatedTweetsByUser(id);
	}

	@RequestMapping(value="/trends", method=RequestMethod.GET)
	public List<Trend> getCurrentTrends() throws TwitterException {
		return twitterConsultService.getTrends();
	}
}
