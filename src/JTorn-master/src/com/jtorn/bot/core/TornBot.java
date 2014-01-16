package com.jtorn.bot.core;

import java.io.BufferedWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.WriteAbortedException;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;

import javax.imageio.ImageReader;

import com.DeathByCaptcha.Client;
import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.*;
import com.jtorn.bot.core.TornRoutine.RoutineType;

public class TornBot extends Thread {

	private boolean shouldRun = true;
	public WebClient wc;
	public TornUser user;
	private TornAction tornAction;
	private TornRoutine routine;
	private RoutineType routineType;
	private String[] args;

	public TornBot(TornUser user, RoutineType routineType, String[] args) 
	{
		//loadProperties();
		this.user = user;
		TornProxy proxy = user.getProxy();
		if (proxy == null)
			wc = new WebClient(BrowserVersion.FIREFOX_10);
		else {
			wc = new WebClient(BrowserVersion.FIREFOX_10, proxy.getAddress(),
					Integer.parseInt(proxy.getPort()));
			final DefaultCredentialsProvider scp = new DefaultCredentialsProvider();
			scp.addCredentials(proxy.getUsername(), proxy.getPassword());
			wc.setCredentialsProvider(scp);
			
		}
		// JavaScript throws major errors on site.
		wc.setJavaScriptEnabled(false);
		wc.setThrowExceptionOnScriptError(false);
		wc.setThrowExceptionOnFailingStatusCode(false);
		// Cookies may be unnecessary
		CookieManager cookieManager = new CookieManager();
		cookieManager.setCookiesEnabled(true);
		wc.setCookieManager(cookieManager);
		this.routine = new TornRoutine(wc, user);
		this.routineType = routineType;
		this.args = args;
	}
	
	public void run()
	{
		while (routine.getAction().isShouldRun())
		{
			routine.mainRoutine(this.routineType, this.args);
		}
	}
	
	
}
