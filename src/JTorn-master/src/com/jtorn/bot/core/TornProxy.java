package com.jtorn.bot.core;

public class TornProxy 
{
	private String type;
	private String address;
	private String port;
	private String username;
	private String password;
	private String lastScanned;
	private String status;
	
	/**
	 * @param type
	 * @param address
	 * @param port
	 * @param username
	 * @param password
	 * @param lastScanned
	 * @param status
	 */
	public TornProxy(String type, String address, String port, String username,
			String password, String lastScanned, String status) 
	{
		this.type = type;
		this.address = address;
		this.port = port;
		this.username = username;
		this.password = password;
		this.lastScanned = lastScanned;
		this.status = status;
	}
	
	public TornProxy(String address, String port)
	{
		this.address = address;
		this.port = port;
	}
	
	public TornProxy(String address, String port, String username, String password)
	{
		this(address, port);
		this.username = username;
		this.password = password;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the port
	 */
	public String getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(String port) {
		this.port = port;
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
	 * @return the lastScanned
	 */
	public String getLastScanned() {
		return lastScanned;
	}

	/**
	 * @param lastScanned the lastScanned to set
	 */
	public void setLastScanned(String lastScanned) {
		this.lastScanned = lastScanned;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TornProxy [type=" + type + ", address=" + address + ", port="
				+ port + ", username=" + username + ", password=" + password
				+ ", lastScanned=" + lastScanned + ", status=" + status + "]";
	}
	
	
}
