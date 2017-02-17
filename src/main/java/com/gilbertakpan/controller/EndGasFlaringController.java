package com.gilbertakpan.controller;

import com.gilbertakpan.entities.ContactUs;
import com.gilbertakpan.services.ContactUsRepository;
import org.h2.tools.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.data.jpa.repository.Query;
import org.springframework.social.twitter.api.SearchResults;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by gilbertakpan on 2/8/17.
 */
@Controller
public class EndGasFlaringController {

    @Value("${spring.social.twitter.appId}")
    private String twitterConsumerKey;

    @Value("${spring.social.twitter.appSecret}")
    private String twitterConsumerSecret;

    @Value("${spring.social.twitter.accessToken}")
    private String twitterAccessToken;

    @Value("${spring.social.twitter.accessTokenSecret}")
    private String twitterAccessTokenSecret;

    private Twitter twitter;


    @Autowired
    ContactUsRepository contactUs;

    Server dbui = null;

    @PostConstruct
    public void init() throws SQLException {
        dbui = Server.createWebServer().start();
    }

    @PreDestroy
    public void destroy() {
        dbui.stop();
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String helloTwitter(Model model) throws IOException {
        if (twitter == null) {
            twitter = new TwitterTemplate(twitterConsumerKey, twitterConsumerSecret, twitterAccessToken, twitterAccessTokenSecret);

        }
        SearchResults searchResults = twitter.searchOperations().search("#climatechange");
        List<Tweet> tweets = searchResults.getTweets();
        model.addAttribute("list", tweets);
        return "index";
    }



    @RequestMapping(path = "/contact-us", method = RequestMethod.POST)
    public String addContact(String name, String email, String phoneNumber, String comment) {
        ContactUs contact = new ContactUs(name, email, phoneNumber, comment);
        contactUs.save(contact);
        return "redirect:/";
    }

    @RequestMapping(path = "/resources")
    public String resources() {
        return "index";
    }

    @RequestMapping(path = "/who-we-are")
    public String about(){
        return "index";
    }
    @RequestMapping(path = "/about")
    public String yyyyee(){
        return "about";
    }

}

