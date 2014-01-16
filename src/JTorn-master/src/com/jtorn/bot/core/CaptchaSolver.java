package com.jtorn.bot.core;


import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.DeathByCaptcha.Captcha;
import com.DeathByCaptcha.Client;
import com.DeathByCaptcha.SocketClient;


public class CaptchaSolver implements Runnable
{
    protected Client _client = null;
    protected String _captchaFilename = null;
    private Captcha _captcha = null;
    private static Logger logger = Logger.getLogger(CaptchaSolver.class);

    public CaptchaSolver(Client client, String captchaFilename)
    {
        this._client = client;
        this._captchaFilename = captchaFilename;
        this._captcha = new Captcha();
    }

    public Captcha getCaptcha()
    {
    	return this._captcha;
    }
    
    public void run()
    {
    	long timeout = 30*1000;
    	
        try {
            // Put your CAPTCHA image file, file object, input stream,
            // or vector of bytes here:
            this._captcha = this._client.upload(this._captchaFilename);
            if (null != this._captcha) 
            {
                logger.info("CAPTCHA " + this._captchaFilename + " uploaded: " + this._captcha.id);
                
                
                Calendar start = Calendar.getInstance();
                // Poll for the uploaded CAPTCHA status.
                
                while ((this._captcha.isUploaded() && !this._captcha.isSolved()) ||
                		Calendar.getInstance().getTimeInMillis() - start.getTimeInMillis() < timeout) 
                {
                    Thread.sleep(Client.POLLS_INTERVAL * 1000);
                    this._captcha = this._client.getCaptcha(this._captcha);
                }

                if (this._captcha.isSolved()) {
                    logger.info("CAPTCHA " + this._captchaFilename + " solved: " + this._captcha.text);
                    // Report incorrectly solved CAPTCHA if neccessary.
                    // Make sure you've checked if the CAPTCHA was in fact
                    // incorrectly solved, or else you might get banned as
                    // abuser.
                    /*if (this._client.report(this._captcha)) {
                        logger.info("CAPTCHA " + this._captchaFilename + " reported as incorrectly solved");
                    } else {
                        logger.info("Failed reporting incorrectly solved CAPTCHA");
                    }*/
                } else {
                    logger.info("Failed solving CAPTCHA");
                }
            }
        } catch (java.lang.Exception e) {
            System.err.println(e.toString());
        }
    }
}