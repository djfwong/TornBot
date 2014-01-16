package com.jtorn.scraper;
import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.jsoup.nodes.*;
import org.jsoup.select.Elements;
import org.jsoup.*;

public class Main 
{
	File htmlFile;

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException
	{
		Elements eles;
		Document doc;
		Element e;
		
		for (long i = 1; i < 1000; i++) 
		{
		String filename = "D:/downloads/curl/torndata/userprofiles/userid-"+ i +".html";
		File htmlFile = new File(filename);
		
		if (!htmlFile.exists())
		{
			System.out.println(filename + " COULD NOT BE FOUND");
			continue;
		}
		
		TornProfile profile = new TornProfile();
		
		if  (htmlFile.length() < 10000)
		{
			profile.setUserid(i);
			profile.setExists(false);
			System.out.println(i + " DOES NOT EXIST");
			continue;
		}
		else
		{
			// Change this to store the userid
			profile.setExists(true);
		}
		
		doc = Jsoup.parse(htmlFile, "UTF-8");
		
		// Parse the statbox info		
		String sws = doc.getElementById("statsWrapper").text().toUpperCase().trim();
		//System.out.println(sws);
		
		/*
		 * Retrieve game stat values:
		 * level
		 * rank
		 * age
		 */
		String level = sws.substring(sws.indexOf("LEVEL") + 5, sws.indexOf("RANK")).trim();
		String rank = sws.substring(sws.indexOf("RANK") + 4, sws.indexOf(" AGE ")).trim();
		String age = sws.substring(sws.indexOf(rank) + rank.length() + 4).trim();
		/* Finish retrieval */
		
		
		// Parse basic information
		Element infoBoxLeftEle = doc.getElementById("infoBoxLeft");
		Elements infoBoxLeftEles = infoBoxLeftEle.getAllElements();
		String basicInfoString = infoBoxLeftEles.first().text();
		//System.out.println(basicInfoString);
		
		/*
		 * Retrieve basic info values:
		 * username
		 * userid
		 * status
		 * faction
		 * job
		 * life
		 * property
		 * maritalStatus
		 * friends
		 * enemies
		 * forumPosts
		 * forumScore
		 * lastAction (minutes)
		 */
		String bis = basicInfoString.toUpperCase().trim();
		
		String username = bis.substring(bis.indexOf("NAME:") + 5, bis.indexOf("ONLINE:")).trim().split(" ")[0];
		String status = bis.substring(bis.indexOf("STATUS:") + 7, bis.indexOf("FACTION:")).trim();
		String faction = bis.substring(bis.indexOf("FACTION:") + 8, bis.indexOf("JOB:")).trim();
		String job = bis.substring(bis.indexOf("JOB:") + 4, bis.indexOf("LIFE:")).trim();
		String life = bis.substring(bis.indexOf("LIFE:") + 5, bis.indexOf("PROPERTY:")).trim();
		String property = bis.substring(bis.indexOf("PROPERTY:") + 9, bis.indexOf("MARITAL STATUS:")).trim();
		String maritalStatus = bis.substring(bis.indexOf("MARITAL STATUS:") + 15, bis.indexOf("FRIENDS:")).trim();
		String friends = bis.substring(bis.indexOf("FRIENDS:") + 8, bis.indexOf("ENEMIES:")).trim();
		String enemies = bis.substring(bis.indexOf("ENEMIES:") + 8, bis.indexOf("FORUM POSTS:")).trim();
		String forumPosts = bis.substring(bis.indexOf("FORUM POSTS:") + 12, bis.indexOf("LAST ACTION:")).trim();
		String lastAction = bis.substring(bis.indexOf("LAST ACTION:") + 12).trim();
		/* Finish retrieval */
		
		/*
		 *  Clean up the values
		 */

		life = life.split(" ")[2];
		String forumScore = forumPosts.split(" ")[1];
		forumScore = forumScore.replace("(", "").trim();
		forumPosts = forumPosts.split(" ")[0].trim();
		forumPosts = forumPosts.replace(",", "");
		age = age.replace(",", "");
		maritalStatus = maritalStatus.replace("MARRIED TO", "").trim();
		
		long minutes = Long.parseLong(lastAction.split(" ")[0]);
		if (lastAction.contains("DAYS"))
		{
			minutes *= 24 * 60;
		}
		else if (lastAction.contains("HOURS"))
		{
			minutes *= 60;
		}
		else 
		{
			// Last Action Already in minutes
		}
		/* Finish Cleanup */
		
		/*
		 * Construct the profile
		 */
		profile.setLevel(Integer.parseInt(level));
		profile.setRank(rank);
		profile.setAge(Integer.parseInt(age));
		profile.setUsername(username);
		profile.setUserid(i);
		profile.setStatus(status);
		profile.setFaction(faction);
		profile.setJob(job);
		profile.setLife(Long.parseLong(life));
		profile.setProperty(property);
		profile.setMaritalStatus(maritalStatus);
		profile.setFriends(Integer.parseInt(friends));
		profile.setEnemies(Integer.parseInt(enemies));
		profile.setForumPosts(Integer.parseInt(forumPosts));
		profile.setForumScore(Integer.parseInt(forumScore));
		profile.setLastAction(minutes);
		/* Finish Construction */
		
		System.out.println(profile.toString());
		System.out.println();
		
		/*
		 * Serialize the profile to XML
		 */
		XMLEncoder xmle = new XMLEncoder(
                new BufferedOutputStream(
                    new FileOutputStream("D:/downloads/curl/torndata/xmlprofiles/user-profile-"+ i +".xml")));
		xmle.writeObject(profile);
		xmle.close();
		/* Finish serialization */
		
		}
	}

}
