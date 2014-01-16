package com.jtorn.bot.core;

import java.util.Date;
import java.util.logging.Level;

import org.apache.log4j.Logger;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.jtorn.util.TornUtil;

public class TornRoutine
{
	private TornAction action;
	private WebClient wc;
	private TornUser user;
	private boolean shouldRun = true;
	private int errors = 0;
	private static Logger logger = Logger.getLogger(TornRoutine.class);
	
	public TornRoutine(WebClient wc, TornUser user)
	{
		this.wc = wc;
		this.user = user;
		this.action = new TornAction(wc, user);
	}
	
	public TornAction getAction()
	{
		return this.action;
	}

	public enum RoutineType
	{
		FLOWERS, SIMPLE, LEVELUP
	}
	
	public HtmlPage levelupRoutine(HtmlPage page, String[] args) throws Exception
	{
		page = action.loadIndex();
		logger.info("Checking for levelup...");
		if (action.checkLevelup(page))
		{
			page = action.levelup(page);
			logger.info("Leveled up!");
		}
		else
			action.setShouldRun(false);
			
		return page;
	}
	
	public HtmlPage simpleRoutine(HtmlPage page, String[] args) throws Exception
	{
		if (args != null)
		{
			if (args[0].toLowerCase().contains("def"))
				page = action.trainDefence(page, 20);
			else
				page = action.trainStrength(page, 20);
		}
		else
			page = action.trainStrength(page, 20);
		action.setShouldRun(false);
		return page;
	}
	
	public HtmlPage flowerRoutine(HtmlPage page, String[] args) throws Exception
	{
		
		String country_id = args[0];
		String flower_id = args[1];
		int travel_time = Integer.parseInt(args[2]);
		
		if (action.checkXml(page, TornConstants.youAreTraveling))
			page = action.loadIndex();
		if (action.onCaptcha(page))
			page = action.solveCaptcha(page);
		if (action.checkText(page,TornConstants.onThePlane))
		{	
			logger.info(TornConstants.onThePlane);
			int remainingTime = action.extractTravelMinutes(page);
			if (remainingTime < 0)
				remainingTime = 4;
			logger.info(TornUtil.sleepMessage(remainingTime+1));
			Thread.sleep(1000*60*(remainingTime++));
		}
		else if (page.asText().toLowerCase().contains(TornConstants.atStoreAbroad))
		{
			logger.info("At store abroad.");
			page = action.buyAnyFlowers(page, 21,flower_id);
			page = action.travelHome(page);
			logger.info("Sleeping "+travel_time+" minutes...");
			Thread.sleep(1000*travel_time*60);
		}
		else if (page.asText().toLowerCase().contains(TornConstants.atHome))
		{
			action.writeItems(action.loadItems().asXml());
			page = action.trainDefence(page, 20);
			logger.info("Starting new flower run...");
			page = action.runAnyFlowers(page, 21,country_id,flower_id,travel_time);
		}
		else
			throw new Exception("Unknown State");
		logger.info("Completed flower run.");
		return page;
	}
	
	public void mainRoutine(RoutineType routineType, String[] args)
	{	
		try
		{
			logger.info(this.user.getUsername()+"-"+"Starting application...");
			java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF);
			logger.info(this.user.getUsername()+"-"+"Loading Torn...");
			HtmlPage page = wc.getPage("http://torn.com");
	        int loop = 0;
	        
		    while (action.isShouldRun() && this.errors < 4)
		    {
		    	loop++;
		    	logger.info(this.user.getUsername()+"-"+"Beginning loop "+loop+" at "+TornConstants.getTimeDateFormat().format(new Date()));
		    	try
		    	{
		    		//logger.info(this.user.getUsername()+"-"+page.getWebResponse().getContentAsString());
		    		page = action.loadIndex();
		    		if (!action.isLoggedIn())
				    	page = action.login(page, user.getUsername(), user.getPassword());
		    		//logger.info(this.user.getUsername()+"-"+page.getWebResponse().getContentAsString());
		    		action.writeProfile(action.loadIndex().asXml());
		    		
		    		switch (routineType)
		    		{
			    		case FLOWERS:
			    		{
			    			page = flowerRoutine(page, args);
			    			break;
			    		}
			    		case SIMPLE:
			    		{
			    			page = simpleRoutine(page, args);
			    			break;
			    		}
			    		case LEVELUP:
			    		{
			    			page = levelupRoutine(page, args);
			    			break;
			    		}
			    		default:
			    			throw new Exception("Invalid Routine");
		    		}
		    	}
		    	catch (Exception e)
		    	{
		    		logger.info(this.user.getUsername()+"-"+e.toString());
		    		//logger.info(page.getWebResponse().getContentAsString());
		    		e.printStackTrace();
		    		action.writeError(page.asXml());
		    		if (!action.handlePageError(page))
		    			this.errors++;
		    		if (this.errors > 3)
		    			action.abort();
		    	}
		    }
		    logger.info(this.user.getUsername()+"-"+"Thread terminated!");
		    return;
		    
		}
		catch (Exception e)
		{
			logger.info(this.user.getUsername()+"-"+e.toString());
			e.printStackTrace();
			action.abort();
		}
	}
}
