package com.jtorn.bot.test;


import java.io.IOException;
import java.net.MalformedURLException;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;

public class LoginTest {


	
	//Login To Torn
	@Test
	public void testHomePageLogin() throws FailingHttpStatusCodeException, MalformedURLException, IOException{
		WebClient webclient = new WebClient(BrowserVersion.FIREFOX_10);
		HtmlPage htmlPage1 = webclient.getPage("http://www.torn.com/login");

		HtmlForm form = htmlPage1.getFormByName("login");
		form.getInputByName("player").setValueAttribute("djfwong");
		form.getInputByName("password").setValueAttribute("127910");

		HtmlSubmitInput LoginButton = form.getInputByName("btnLogin");
		htmlPage1 = LoginButton.click();
		
		htmlPage1 = webclient.getPage("http://www.torn.com/item.php");
		
		System.out.println(htmlPage1.asText());
		
		webclient.closeAllWindows();
	}

}
