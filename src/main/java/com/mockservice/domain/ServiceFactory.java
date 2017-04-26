/*
 * -------------------------------------------------------------------------
 *
 * (C) Copyright / American Express, Inc. All rights reserved.
 * The contents of this file represent American Express trade secrets and
 * are confidential. Use outside of American Express is prohibited and in
 * violation of copyright law.
 *
 * -------------------------------------------------------------------------
 */

package com.mockservice.domain;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ServiceFactory
 *
 * @author shegde6
 * @version $Id$
 */
public class ServiceFactory {
	
	private Map<String,Service> serviceMap;
	private String serializedFilePath;
	private static ServiceFactory serviceFactory = new ServiceFactory();
	public static final String FILENAME = "/serializedData.dat";
	
	private ServiceFactory()
	{
		initFilePath();
	}
	
	public static ServiceFactory getInstance()
	{
		return serviceFactory;
	}
			
	private void initFilePath()
	{
		String filepath = System.getProperty("user.home") + FILENAME;
		System.out.println("The File Path is***--> "+ filepath);
		try
		{
			File file = new File(filepath);
			FileInputStream fileInputStream = new FileInputStream(file);
			ObjectInputStream objInputStream = new ObjectInputStream(fileInputStream);
			serviceMap = (Map<String,Service>)objInputStream.readObject();
			fileInputStream.close();
			objInputStream.close();
		}
		catch(FileNotFoundException fe)
		{
			System.out.println("The file "+filepath+" is not found");
		}
		catch(Throwable tx){
			System.out.println("Exception while retrieving file "+filepath+ tx.getMessage());
		}
		finally{
			if(serviceMap==null)
				serviceMap = new ConcurrentHashMap<String,Service>();			
		}
	}
	public Map<String, Service> getServiceMap() {
		return serviceMap;
	}

	public void saveMockResponse()
    {
			String filepath = System.getProperty("user.home") + FILENAME;
        try
        {
            FileOutputStream fos = new FileOutputStream(new File((filepath)));
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(serviceMap);
            fos.close();
            oos.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

	public void setSerializedFilePath(String serializedFilePath) {
		this.serializedFilePath = serializedFilePath;
	}

	public String getSerializedFilePath() {
		return serializedFilePath;
	}
	
	public void updateServiceMap(String serviceName,Service service)
	{
		serviceMap.put(serviceName, service);
		saveMockResponse();
	}
	
}
