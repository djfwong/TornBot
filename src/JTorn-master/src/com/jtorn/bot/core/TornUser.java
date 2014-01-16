package com.jtorn.bot.core;

public class TornUser 
{
	private String username;
	private String password;
	private String email;
	private TornProxy proxy;
	
	/**
	 * @param username
	 * @param password
	 * @param email
	 * @param proxy
	 */
	public TornUser(String username, String password, String email,
			TornProxy proxy) 
	{
		this.username = username;
		this.password = password;
		this.email = email;
		this.proxy = proxy;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the proxy
	 */
	public TornProxy getProxy() {
		return proxy;
	}

	/**
	 * @param proxy the proxy to set
	 */
	public void setProxy(TornProxy proxy) {
		this.proxy = proxy;
	}
	
	
}
