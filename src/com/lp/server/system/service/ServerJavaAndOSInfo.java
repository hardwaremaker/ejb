package com.lp.server.system.service;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class ServerJavaAndOSInfo implements Externalizable {
	
	private String vmVendor;
	private String vmVersion;
	private String osName;
	private String osVersion;
	
	public ServerJavaAndOSInfo() {
		setVmVendor("<undefined>");
		setVmVersion("<undefined>");
		setOsName("<undefined>");
		setOsVersion("<undefined>");
	}
	
	/**
	 * Es werden dabei die Properties der JVM verwendet, in der diese Methode aufgerufen wird.
	 */
	public void initProperties() {
		setVmVendor(System.getProperty("java.vendor"));
		setVmVersion(System.getProperty("java.version"));
		setOsName(System.getProperty("os.name"));
		setOsVersion(System.getProperty("os.version"));
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		setVmVendor(in.readUTF());
		setVmVersion(in.readUTF());
		setOsName(in.readUTF());
		setOsVersion(in.readUTF());
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeUTF(getVmVendor());
		out.writeUTF(getVmVersion());
		out.writeUTF(getOsName());
		out.writeUTF(getOsVersion());
	}

	public String getVmVendor() {
		return vmVendor;
	}

	public void setVmVendor(String vendor) {
		this.vmVendor = vendor;
	}

	public String getVmVersion() {
		return vmVersion;
	}

	public void setVmVersion(String version) {
		this.vmVersion = version;
	}

	public String getOsName() {
		return osName;
	}

	public void setOsName(String osName) {
		this.osName = osName;
	}

	public String getOsVersion() {
		return osVersion;
	}

	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}
	
	public String formatString() {
		StringBuffer sb = new StringBuffer();
		sb.append("java.vendor = " + getVmVendor() + "\n");
		sb.append("java.version = " + getVmVersion() + "\n");
		sb.append("os.name = " + getOsName() + "\n");
		sb.append("os.version = " + getOsVersion() + "\n");
		return sb.toString();
	}
}
