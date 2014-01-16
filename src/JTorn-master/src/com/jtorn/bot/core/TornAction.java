package com.jtorn.bot.core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

import com.DeathByCaptcha.Captcha;
import com.DeathByCaptcha.SocketClient;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

public class TornAction{
	
	private WebClient wc;
	private HtmlPage htmlPage;
	private TornUser user;
	private int errors = 0;
	private boolean shouldRun = true;
	private static Logger logger = Logger.getLogger(TornRoutine.class);
	
	
	public TornAction(WebClient webClient, TornUser user)
	{
		this.wc = webClient;
		this.user = user;
	}
	
	/**
	 * @return the shouldRun
	 */
	public boolean isShouldRun()
	{
		return shouldRun;
	}

	/**
	 * @param shouldRun the shouldRun to set
	 */
	public void setShouldRun(boolean shouldRun)
	{
		this.shouldRun = shouldRun;
	}


	public HtmlPage solveCaptcha(HtmlPage page) throws Exception {
		wc.setJavaScriptEnabled(true);
		logger.info(user.getUsername() + "-" + "Solving captcha...");
		String url = page.getUrl().toString();
		page = wc.getPage(url + "?bypass=1");
		//logger.info(page.getWebResponse().getContentAsString());
	
		HtmlImage captchaImage = (HtmlImage) page.getElementById("recaptcha_image").getFirstChild();
		captchaImage.saveAs(new File("./files/captcha-image.jpg"));
	
		
		CaptchaSolver captchaSolver = new CaptchaSolver(new SocketClient(
				"trialaccount", "Cappie1!"), "./files/captcha-image.jpg");
		captchaSolver.run();
		Captcha captcha = captchaSolver.getCaptcha();
	
		HtmlTextInput textField = (HtmlTextInput) page
				.getElementById("recaptcha_response_field");
		textField.setValueAttribute(captcha.text);
		HtmlElement element = (HtmlElement) page.getElementByName("submit");
		page = element.click();
		logger.info(user.getUsername() + "-" + "Captcha entered.");
		wc.setJavaScriptEnabled(false);
		return wc.getPage(url);
	}
	
	public boolean checkMailbox(HtmlPage page)
	{
		return false;
	}
	
	public boolean checkEvents(HtmlPage page)
	{
		return false;
	}
	
	
	
	public int extractTravelMinutes(HtmlPage page)
	{
		logger.info("Extracting remaining travel time...");
		String[] words = ((HtmlElement) page.getByXPath("/html/body/div[3]/center/table/tbody/tr/td/center").get(0)).asText().split(" ");
	    int minutes = -1;
		for (int i = 0; i < words.length; i++)
	    {
			//logger.info(words[i]);
	    	if (words[i].toLowerCase().contains("minute"))
	    		minutes = Integer.parseInt(words[i-1]);
	    }
		if (minutes > -1)
			logger.info("Eta is "+ minutes+" minutes.");
		else 
			logger.info("Unable to extract Eta.");
	    return minutes;
	}
	
	public HtmlPage login(HtmlPage page, String username, String password) {
		// wc.setJavaScriptEnabled(true);
		int attempts = 0;
		boolean loggedIn = false;
		while (!loggedIn && isShouldRun()) 
		{
			try 
			{
				if (attempts > 3 || this.errors > 3)
					abort();
				attempts++;
				logger.info(this.user.getUsername() + "-"
						+ "Logging in... (Attempt " + attempts + ")");
				page = wc.getPage("http://torn.com/login");
				// logger.info(page.getWebResponse().getContentAsString());
				final HtmlForm form = page.getFormByName("login");
				HtmlSubmitInput button = form.getInputByName("btnLogin");
				HtmlTextInput textField = form.getInputByName("player");
				textField.setValueAttribute(username);
				final HtmlPasswordInput passwordField = form
						.getInputByName("password");
				passwordField.setValueAttribute(password);
				page = button.click();
				// logger.info(page.getWebResponse().getContentAsString());
				if (isLoggedIn())
					loggedIn = true;
			} 
			catch (Exception e) 
			{
				System.out
						.println(this.user.getUsername() + "-" + e.toString());
				loggedIn = false;
			}
		}
		// wc.setJavaScriptEnabled(false);
		return page;
	}
	
	public boolean isLoggedIn() 
	{
		return !loadIndex().asText().toLowerCase()
				.contains("you are no longer logged in.");
	}
	
	public HtmlPage loadIndex() 
	{
		logger.info(this.user.getUsername() + "-" + "Loading Index...");
		try {
			return wc.getPage("http://torn.com/index.php");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean handlePageError(HtmlPage page) throws Exception 
	{
		logger.info("Attemping to determine and rectify state...");
		if (!isLoggedIn()) {
			login(page, user.getUsername(), user.getPassword());
			return true;
		} else if (inHospital(page)) {
			while (inHospital(page)) {
				logger.info("You are in the hospital.");
				Thread.sleep(1000 * 60 * 5);
			}
			return true;
		} else if (inJail(page)) {
			while (inJail(page)) {
				logger.info("You are in jail.");
			}
			return true;
		} else if (onCaptcha(page)) {
			while (onCaptcha(page)) {
				wc.setJavaScriptEnabled(true);
				logger.info(this.user.getUsername() + "-"
						+ "Captcha encountered.");
				page = solveCaptcha(page);
				wc.setJavaScriptEnabled(false);
			}
			return true;
		} else
			return false;
	}
	
	public boolean inHospital(HtmlPage page) {
		HtmlElement e = page.getElementById("icon15");
		if (e == null)
			return false;
		boolean result = e.getAttribute("title").toLowerCase()
				.contains("hospital");
		if (result)
			logger.info(this.user.getUsername() + "-" + "In hospital.");
		return result;
	}

	public boolean checkLevelup(HtmlPage page)
	{
		HtmlElement e = page.getElementById("btnUpgrade");
		return (e != null);
	}
	
	public HtmlPage levelup(HtmlPage page) throws IOException
	{
		HtmlElement e = page.getElementById("btnUpgrade");
		String url = e.getAttribute("href");
		page = wc.getPage("http://torn.com/"+url);
		String[] urls = url.split("\\?");
		String confirmUrl = urls[0]+"?confirm=1&"+urls[1];
		page = wc.getPage("http://torn.com/"+confirmUrl);
		return page;
	}
	
	public boolean inJail(HtmlPage page) {
		boolean result = (page.getElementById("icon16") != null);
		if (result)
			logger.info(this.user.getUsername() + "-" + "In jail.");
		return result;

	}
	
	public boolean onCaptcha(HtmlPage page) 
	{
		return checkXml(page, TornConstants.captcha); 
	}
	
	public boolean checkXml(HtmlPage page, String text)
	{
		return page.asXml().toLowerCase().contains(text);
	}
	
	public boolean checkText(HtmlPage page, String text)
	{
		return page.asText().toLowerCase().contains(text);
	}
	
	public HtmlPage trainDefence(HtmlPage page, int amount) throws Exception {
		logger.info("Training " + amount + " defence...");
		page = wc.getPage("http://torn.com/gym.php");
		if (onCaptcha(page))
			page = solveCaptcha(page);
		// logger.info(page.getWebResponse().getContentAsString());
		HtmlTextInput textInput = null;
		HtmlSubmitInput submitInput = null;
		for (DomNode n : page.getElementById("divDefence").getDescendants()) {
			// logger.info(n.toString());
			if (n.toString().contains("id=\"t\" name=\"t\">]")) {
				textInput = (HtmlTextInput) n;
				textInput.setValueAttribute(Integer.toString(amount));
				// logger.info(textInput.toString());
			} else if (n.toString().contains(
					"HtmlSubmitInput[<input type=\"submit\" value=\"Train\">]")) {
				submitInput = (HtmlSubmitInput) n;
				page = submitInput.click();
				// logger.info(submitInput.toString());
			}
		}
		if (textInput == null)
			throw new ElementNotFoundException("trainDefence", "textInput",
					"HtmlTextInput[<input type=\"text\" value=\"1\" id=\"t\" name=\"t\">]");
		if (submitInput == null)
			throw new ElementNotFoundException("trainDefence", "submitInput",
					"HtmlSubmitInput[<input type=\"submit\" value=\"Train\">]");

		return page;
	}
	
	public HtmlPage trainStrength(HtmlPage page, int amount) throws Exception {
		logger.info("Training " + amount + " Strength...");
		page = wc.getPage("http://torn.com/gym.php");
		if (onCaptcha(page))
			page = solveCaptcha(page);
		// logger.info(page.getWebResponse().getContentAsString());
		HtmlTextInput textInput = null;
		HtmlSubmitInput submitInput = null;
		for (DomNode n : page.getElementById("divStrength").getDescendants()) {
			// logger.info(n.toString());
			if (n.toString().contains("id=\"t\" name=\"t\">]")) {
				textInput = (HtmlTextInput) n;
				textInput.setValueAttribute(Integer.toString(amount));
				// logger.info(textInput.toString());
			} else if (n.toString().contains(
					"HtmlSubmitInput[<input type=\"submit\" value=\"Train\">]")) {
				submitInput = (HtmlSubmitInput) n;
				page = submitInput.click();
				// logger.info(submitInput.toString());
			}
		}
		if (textInput == null)
			throw new ElementNotFoundException("trainStrength", "textInput",
					"HtmlTextInput[<input type=\"text\" value=\"1\" id=\"t\" name=\"t\">]");
		if (submitInput == null)
			throw new ElementNotFoundException("trainStrength", "submitInput",
					"HtmlSubmitInput[<input type=\"submit\" value=\"Train\">]");
		logger.info("Strength successfully trained.");
		return page;
	}
	
	public void abort() 
	{
		logger.info(this.user.getUsername() + "-"
				+ "Too many errors encountered. Aborting all operations");
		wc.closeAllWindows();
		setShouldRun(false);
	}
	
	public HtmlPage loadItems() throws Exception
	{
		return wc.getPage("http://www.torn.com/item.php");
	}
	
	public void writeError(String contents)
	{
		writePage("files/page-logs/errors", contents);
	}
	
	public void writeItems(String contents)
	{
		writePage("files/page-logs/items", contents);
	}
	
	public void writeAbroad(String contents)
	{
		writePage("files/page-logs/abroad", contents);
	}
	public void writeProfile(String contents)
	{
		writePage("files/page-logs/profile/", contents);
	}
	
	public void writePage(String dir, String contents)
	{
		String filename = dir+"/"+this.user.getUsername()+"/"+TornConstants.getDayDateFormat().format(new Date())+"/"+TornConstants.getTimeDateFormat().format(new Date())+".html";
		writeFile(filename, contents);
	}
	
	public void writeFile(String filename, String contents)
	{
		try
		{
			// Create file
			File file = new File(filename);
			File dir = file.getParentFile();
			// Create the directory if it doesnt already exist
			dir.mkdirs();
			file.createNewFile();
			FileWriter fstream = new FileWriter(file);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(contents);
			//Close the output stream
			out.close();
		}
		catch (Exception e)
		{
			//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}
	
	public HtmlPage runAnyFlowers(HtmlPage page, int amount, String countryId, String flowerId, int time) throws Exception
	{
		page = travelAbroad(page, countryId);
		logger.info("Sleeping "+time+" minutes...");
		Thread.sleep(1000*time*60);
		page = buyAnyFlowers(page, 21, flowerId);
		page = travelHome(page);
		logger.info("Sleeping "+time+" minutes...");
		Thread.sleep(1000*time*60);

		return page;
	}
	
	
	public HtmlPage travelHome(HtmlPage page) throws Exception {
		page = loadIndex();
		logger.info("Traveling Home...");
		page = wc.getPage("http://www.torn.com/holder.php?case=travelAgency&ID=63&action=travelback&jsoff=none&ID2=");
		//page = wc.getPage("http://www.torn.com/holder.php?case=travelAgency&ID=2&action=travelback&jsoff=none&ID2=0&value=");
		page = wc.getPage("http://www.torn.com/holder.php?case=travelAgency&ID=2&action=travelback2&jsoff=none&ID2=0&value=");
		
		return page;
	}
	
	public HtmlPage buyAnyFlowers(HtmlPage page, int amount, String flowerId) throws Exception 
	{
		logger.info("Buying flowers...");
		if (!isLoggedIn())
			login(page, user.getUsername(), user.getPassword());
		page = loadIndex();
		if (onCaptcha(page))
			page = solveCaptcha(page);
		writeAbroad(loadIndex().asXml());
		page = wc
				.getPage("http://www.torn.com/holder.php?case=buyItem&ID="+flowerId+"&action=confirm&jsoff=none&ID2=");
		page = wc
				.getPage("http://www.torn.com/holder.php?case=buyItem&ID="+flowerId+"&action=confirm2&jsoff=none&ID2=&value=");
		// page =
		// wc.getPage("http://www.torn.com/holder.php?case=buyItem&ID=260&action=confirm3&jsoff=none&ID2=938&amount260=19");
		HtmlTextInput input = page.getElementByName("amount"+flowerId);
		input.setValueAttribute(Integer.toString(amount));
		HtmlSubmitInput submit = (HtmlSubmitInput) page.getByXPath(
				"/html/body/div[3]/table/tbody/tr/td/div/center/div/input")
				.get(0);
		page = submit.click();
		return page;
	}

	public HtmlPage travelAbroad(HtmlPage page, String countryId) throws Exception 
	{
		logger.info("Traveling Abroad...");
		page = wc.getPage("http://www.torn.com/travelagency.php");
		//logger.info(page.asXml());
		if (onCaptcha(page))
		{
			solveCaptcha(page);
			page = wc.getPage("http://www.torn.com/travelagency.php");
		}
		page = wc.getPage("http://www.torn.com/holder.php?case=travelAgency&ID="+countryId+"&action=confirm&jsoff=none&ID2=2");
		
		page = wc.getPage("http://www.torn.com/holder.php?case=travelAgency&ID="+countryId+"&action=travel&jsoff=none&ID2=2&value=");
		return page;
	}
	
	public void loadProperties()
	{
		Properties flowerProperties = new Properties();
		Properties countryProperties = new Properties();
		Properties countryFlowerProperties = new Properties();
		
		try
		{
			flowerProperties.load(new FileInputStream("files/java-properties/item-id.properties"));
			countryProperties.load(new FileInputStream("files/java-properties/country-id.properties"));
			countryFlowerProperties.load(new FileInputStream("files/java-properties/country-flower.properties"));
			
			Map<String, String> map = new HashMap<String, String>((Map) countryProperties);
			
			//this.countryMap = new HashMap<String, String>((Map) countryProperties));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private HashMap<String, String> propertiesToMap(Properties properties)
	{
		HashMap<String, String> hashMap = new HashMap<String, String>();
		Map<String, String> map = new HashMap<String, String>((Map) properties);
		
		Set propertySet = map.entrySet();
        for (Object o : propertySet) 
        {
            Map.Entry entry = (Map.Entry) o;
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            hashMap.put(key,value);
        }
        return hashMap;
	}
}