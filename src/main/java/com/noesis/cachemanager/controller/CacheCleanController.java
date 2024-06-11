package com.noesis.cachemanager.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.noesis.domain.service.NgShortUrlChildMappingService;
 

@RestController
public class CacheCleanController {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	NgShortUrlChildMappingService ngShortUrlChildMappingService;
	
	@RequestMapping(value = "/deleteShortUrlKeysFromFile/{fileName}", method = RequestMethod.GET)
	public String getUser(@PathVariable String fileName) {
		logger.info("FileName to be used:", fileName);
		try  
		{  
			File file=new File("/tmp/"+fileName);   
			FileReader fr=new FileReader(file);     
			BufferedReader br=new BufferedReader(fr);  
			String line;  
			while((line=br.readLine()) != null)  
			{  
				logger.info("Key from file is {}: ", line.trim());
//				ngShortUrlChildMappingService.deleteKeyFromRedisOlderThanSevenDays(line.trim());
				
			}  
			fr.close(); 
		}  
		catch(IOException e)  
		{  
			e.printStackTrace();  
		} 
		return "Keys from file deleted successfully";
	}
}