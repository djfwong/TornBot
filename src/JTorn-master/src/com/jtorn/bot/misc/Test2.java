package com.jtorn.bot.misc;

import java.util.logging.Level;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.DefaultCredentialsProvider;
import com.gargoylesoftware.htmlunit.ProxyConfig;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class Test2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception
	{
		System.out.println("Starting application...");
		java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF);

		WebClient wc = new WebClient(BrowserVersion.FIREFOX_10, "199.116.87.66", 3131);
	    final DefaultCredentialsProvider scp = new DefaultCredentialsProvider();
	    scp.addCredentials("754d539bc6ade1ad", "454585f105747a76");
	    wc.setCredentialsProvider(scp);
		wc.setJavaScriptEnabled(false);
		
	    // Get the first page
	    HtmlPage page = wc.getPage("http://automation.whatismyip.com/n09230945.asp");
	    System.out.println(page.asText());
	    wc.closeAllWindows();
	}

}
