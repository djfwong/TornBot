package com.jtorn.bot.misc;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

public class Test {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws MalformedURLException 
	 * @throws FailingHttpStatusCodeException 
	 * @throws URISyntaxException 
	 */
	public static void main(String[] args) throws FailingHttpStatusCodeException, MalformedURLException, IOException, URISyntaxException
	{
		WebClient wc = new WebClient(BrowserVersion.FIREFOX_3_6);
		wc.setJavaScriptEnabled(false);
		
	    // Get the first page
	    //HtmlPage page = wc.getPage("file://D:/torncity/offline-gym.html");
	    HtmlPage page = wc.getPage("file://D:/torncity/TORN-Homepage.html");
	    //HtmlPage page = wc.getPage("file://D:/workspace/JTorn/files/offline-docrime2.html");
//	    for (HtmlElement e: page.getElementsByTagName("center"))
//		{
////			System.out.println(e.toString());
////			HtmlElement e2 = e.getElementById("t");
//			System.out.println(e.asText());
//		}
	    /*
	    String[] words = ((HtmlElement) page.getByXPath("/html/body/div[3]/center/table/tbody/tr/td/center").get(0)).asText().split(" ");
	    int minutes = -1;
		for (int i = 0; i < words.length; i++)
	    {
			System.out.println(words[i]);
	    	if (words[i].toLowerCase().contains("minute"))
	    		minutes = Integer.parseInt(words[i-1]);
	    }
	    System.out.println(minutes);
	    */
	    
	    //System.out.println(page.asXml().contains(("Please Validate").toString()));
	   // System.out.println(page.asXml());
	    System.out.println(page.getBody().asText());
	    
	    /*
	    for (DomNode n: page.getElementById("divStrength").getDescendants())
		{
			//System.out.println(n.toString());
			if (n.toString().contains("HtmlTextInput[<input type=\"text\" value=\"1\" id=\"t\" name=\"t\">]"))
			{
				 = (HtmlTextInput) n;
				input.setValueAttribute(amount);
			}
			
		}
		*/
		
	    
	    /*
	    for (HtmlForm f: page.getForms())
	    {
	    	
	    	HtmlSubmitInput submit = null;
			if (f.asText().toLowerCase().contains("try again"))
			{
				System.out.println(f.asXml());
				//f.getButtonByName("docrime");
	    		submit = f.getInputByName("docrime");
	    		if (submit != null)
	    		{
	    			System.out.println(submit.asXml());
	    			System.out.println(submit.getCanonicalXPath());
	    		}
			}
			
	    	
	    }
	    */

//	    HtmlInput input = (HtmlInput) page.getElementById("docrime");
	//    System.out.println(input.toString());
	    
	    
	}

}
