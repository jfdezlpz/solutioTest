package com.solutio.twitter.service;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.Trend;
import twitter4j.Trends;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

@Service
public class TwitterConsultService {
	
	private static final int WOEID_GLOBAL = 1;
	
	@Autowired
	private TweetService tweetService;
	
	@Value("${twitter.languages}")
	private List<String> languages;
	
	@Value("${twitter.apikey}")
	private String consumerKey;
	
	@Value("${twitter.apisecretkey}")
	private String consumerSecret;
	
	@Value("${twitter.token}")
	private String accessToken;
	
	@Value("${twitter.tokensecret}")
	private String accessTokenSecret;
	
	@Value("${twitter.min.followers}")
	private int minFollowers;
	
	@Value("${twitter.num.hashtag}")
	private int numHashtags;
	
	@PostConstruct
	private void startListener() {
		
		ConfigurationBuilder conf = new ConfigurationBuilder();
		conf.setDebugEnabled(true)
	      .setOAuthConsumerKey(consumerKey)
	      .setOAuthConsumerSecret(consumerSecret)
	      .setOAuthAccessToken(accessToken)
	      .setOAuthAccessTokenSecret(accessTokenSecret);
		
		new TwitterStreamFactory(conf.build()).getInstance().addListener(new StatusListener() {
            @Override
            public void onStatus(Status status) {
            	if(status.getUser().getFollowersCount() > minFollowers && 
            			languages.contains(status.getLang())) {
            		tweetService.saveTweet(status);
            	}
                
            }

            @Override
            public void onException(Exception ex) {
                ex.printStackTrace();
            }

			@Override
			public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {	
			}

			@Override
			public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onScrubGeo(long userId, long upToStatusId) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStallWarning(StallWarning warning) {
				// TODO Auto-generated method stub
				
			}
        }).sample();

		
	}
	
	public List<Trend> getTrends() throws TwitterException {
		ConfigurationBuilder conf = new ConfigurationBuilder();
		conf.setDebugEnabled(true)
	      .setOAuthConsumerKey(consumerKey)
	      .setOAuthConsumerSecret(consumerSecret)
	      .setOAuthAccessToken(accessToken)
	      .setOAuthAccessTokenSecret(accessTokenSecret);
		
		Twitter twitter = new TwitterFactory(conf.build()).getInstance();
		Trends trends = twitter.trends().getPlaceTrends(WOEID_GLOBAL);
		List<Trend> trendList = Arrays.asList(trends.getTrends());
		trendList.sort((Trend t1, Trend t2) -> {return t2.getTweetVolume() - t1.getTweetVolume();});
		return trendList.subList(0, numHashtags<trendList.size()?numHashtags-1:trendList.size()-1);
	}
}
