package com.jtorn.bot.misc;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.logging.Level;

import javax.imageio.ImageReader;

import com.DeathByCaptcha.Captcha;
import com.DeathByCaptcha.Client;
import com.DeathByCaptcha.SocketClient;
import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.*;
import com.jtorn.bot.core.CaptchaSolver;

public class TestDriverFra 
{
	
	static WebClient wc;

	public static void main(String[] args)
	{
		try
		{
			System.out.println("Starting application...");
			java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF);
			
			wc = new WebClient(BrowserVersion.FIREFOX_10);
			//wc = new WebClient(BrowserVersion.FIREFOX_10, "50.117.67.126", 3131);
		    final DefaultCredentialsProvider scp = new DefaultCredentialsProvider();
		    scp.addCredentials("754d539bc6ade1ad", "454585f105747a76");
		    //wc.setCredentialsProvider(scp);
			wc.setJavaScriptEnabled(false);
			wc.setThrowExceptionOnScriptError(false);
			wc.setThrowExceptionOnFailingStatusCode(false);
			
		    // Get the first page
		    HtmlPage page = wc.getPage("http://torn.com/index.php");
		    if (page.asText().contains("You are no longer logged in."))
		    	page = login(page, "tempaccount", "temp123");
		    else
		    	System.out.println("Already logged in.");
		    	
		    System.out.println("Index.php loaded.");
	        int loop = 0;
	        int errors = 0;
		    while (true)
		    {

		    	System.out.println("Beginning loop "+loop+"...");
		    	loop++;
		    	try
		    	{
			    	if(inHospital(page) || inJail(page))
			    	{
			    		System.out.println("Sleeping 5 minutes...");	
					    Thread.sleep(1000*5*60);
			    	}
			    	else
			    	{
			    		System.out.println("Loading page...");
			    		wc.setJavaScriptEnabled(true);
			    		page = wc.getPage("http://torn.com/gym.php");
			    		//page = wc.getPage("http://torn.com/crimes.php");
					    while (onCaptcha(page))
					    {
					    	wc.setJavaScriptEnabled(true);
						    System.out.println("Captcha encountered.");
						    page = solveCaptcha(page);
						    wc.setJavaScriptEnabled(false);
					    }
					    //System.out.println(page.getWebResponse().getContentAsString());
					    trainStrength(page, 20);
					    //doCrime(page, 5);
					    System.out.println("Sleeping 30 minutes...");
					    Thread.sleep(1000*15);
					    
				    }
			    	page = loadIndex();
		    	}
		    	catch (Exception e)
		    	{
		    		System.out.println(e.toString());
		    		errors++;
		    		page = loadIndex();
		    		if (errors > 3)
		    			System.exit(1);
		    	}
		     }
		    
		}
		catch (Exception e)
		{
			wc.closeAllWindows();
			e.printStackTrace();
		}
	}
	
	public static void doCrime(HtmlPage page, int amount) throws FailingHttpStatusCodeException, MalformedURLException, IOException
	{
		
		System.out.println("Committing crimes...");
		//System.out.println(page.getWebResponse().getContentAsString());
		for (int i = 1; i < amount+1; i++)
		{
			System.out.print("Crime "+i+"...");
			ScriptResult sr;
			sr = page.executeJavaScript("document.getElementById('1').checked=true; document.crimes.submit();void(0);");
			sr = page.executeJavaScript("document.getElementById('1').checked=true; document.crimes.submit();void(0);");
			page = wc.getPage("http://torn.com/crimes.php");
		}
	}
	
	public static HtmlPage loadIndex()
	{
		System.out.println("Loading Index...");
		try {
			return wc.getPage("http://torn.com/index.php");
		} catch (FailingHttpStatusCodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static boolean inHospital(HtmlPage page)
	{
		HtmlElement e = page.getElementById("icon15");
		if (e == null)
			return false;
	    boolean result = e.getAttribute("title").toLowerCase().contains("hospital");
	    if (result)
	    	System.out.println("In hospital.");
	    return result;
	}
	
	public static boolean inJail(HtmlPage page)
	{
		boolean result = (page.getElementById("icon16") != null);
		if (result)
			System.out.println("In jail.");
		return result;
		
	}
	
	public static boolean onCaptcha(HtmlPage page)
	{
		try
		{
			page.getAnchorByText("Use re-captcha");
			return true;
		}
		catch (ElementNotFoundException e)
		{
			return false;
		}
	}
	
	
	public static HtmlPage login(HtmlPage page, String username, String password)
	{
		int attempts = 0;
		boolean loggedIn = false;
		while (!loggedIn)
		{
			try
			{
				if (attempts > 3)
					System.exit(1);
				attempts++;
				System.out.println("Logging in... (Attempt "+attempts+")");
				
				page = wc.getPage("http://torn.com/login");
			    
			    final HtmlForm form = page.getFormByName("login");
		
			    HtmlSubmitInput button = form.getInputByName("btnLogin");
			    
			    HtmlTextInput textField = form.getInputByName("player");
		
			    // Change the value of the text field
			    textField.setValueAttribute(username);
			    
			    final HtmlPasswordInput passwordField = form.getInputByName("password");
				
			    // Change the value of the text field
			    passwordField.setValueAttribute(password);
		
			    // Now submit the form by clicking the button and get back the second page.
			    page = button.click();
			    
			    page = wc.getPage("http://torn.com/index.php");
			    loggedIn = true;
			    
			}
			catch (Exception e)
			{
				System.out.println(e.toString());
				loggedIn = false;
			}
		}
		return page;
	    
	}
	
	public static HtmlPage trainStrength(HtmlPage page, int amount) throws IOException
	{
		System.out.println("Training " + amount + " strength...");
		for (DomNode n: page.getElementById("divStrength").getDescendants())
		{
			//System.out.println(n.toString());
			HtmlTextInput textInput;
			HtmlSubmitInput submitInput;
			if (n.toString().contains("HtmlTextInput[<input type=\"text\" value=\"1\" id=\"t\" name=\"t\">]"))
			{
				textInput = (HtmlTextInput) n;
				textInput.setValueAttribute(Integer.toString(amount));
				System.out.println(textInput.toString());
			}
			else if (n.toString().contains("HtmlSubmitInput[<input type=\"submit\" value=\"Train\">]"))
			{
				submitInput = (HtmlSubmitInput) n;
				page = submitInput.click();
				System.out.println(submitInput.toString());
			}
			
		}
		/*
		System.out.println(this.user.getUsername()+"-"+"Training "+amount+" strength...");
		HtmlTextInput amountInput = (HtmlTextInput) page.getByXPath("/html/body/div[4]/table/tbody/tr/td[2]/center/div[2]/div/div[2]/div[3]/div[1]/div[1]/div[2]/form/table/tbody/tr/td[1]/input[1]").get(0);
		amountInput.setValueAttribute(Integer.toString(amount));
		HtmlElement e = (HtmlElement) page.getByXPath("/html/body/div[4]/table/tbody/tr/td[2]/center/div[2]/div/div[2]/div[3]/div[1]/div[1]/div[2]/form/table/tbody/tr/td[3]/input").get(0);
		e.click();
		*/
		System.out.println(page.asText());
		return page;
	}
	
	public static HtmlPage solveCaptcha(HtmlPage page) throws Exception
	{
		System.out.println("Solving captcha...");
		String url = page.getUrl().toString();
		page = wc.getPage(url+"?bypass=1");
		
	    HtmlImage captchaImage = (HtmlImage) page.getElementById("recaptcha_image").getFirstChild();
	    captchaImage.saveAs(new File("captcha-image.jpg"));
	    
	    CaptchaSolver captchaSolver = new CaptchaSolver(new SocketClient("trialaccount", "Cappie1!"), "captcha-image.jpg");
	    captchaSolver.run();
	    Captcha captcha = captchaSolver.getCaptcha();
	    
        HtmlTextInput textField = (HtmlTextInput)page.getElementById("recaptcha_response_field");
	    textField.setValueAttribute(captcha.text);
	    HtmlElement element = (HtmlElement)page.getElementByName("submit");
	    page = element.click();
	    System.out.println("Captcha entered.");
	    return wc.getPage(url);
	    
	}
}
