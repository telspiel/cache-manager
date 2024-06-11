package com.noesis.cachemanager.controller;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.noesis.domain.persistence.NgUser;
import com.noesis.domain.service.AbusingWordsListService;
import com.noesis.domain.service.GlobalBlackListService;
import com.noesis.domain.service.GlobalDndListService;
import com.noesis.domain.service.UserBlackListService;
import com.noesis.domain.service.UserCreditMapService;
import com.noesis.domain.service.UserPremiumListService;
import com.noesis.domain.service.UserService;
 

@RestController
public class UserController {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	UserService userService;
	
	@Autowired
	UserCreditMapService userCreditMapService;
	
	@Autowired
	GlobalDndListService globalDndListService;
	
	@Autowired
	UserBlackListService userBlackListService;
	
	@Autowired
	UserPremiumListService userPremiumListService;
	
	@Autowired
	AbusingWordsListService abusingWordsListService;

	@RequestMapping(value = "/loadAllUserStaticData", method = RequestMethod.GET)
	public void loadAllUserStaticData() {
		logger.info("Request received for loading all user data");
		loadAllUserAbusingWordsData();
		loadAllUserBlackListData();
		loadAllUserPremiumListData();
		loadAllUserSenderIdBlackListData();
	}
	
	@RequestMapping(value = "/findUser/{id}", method = RequestMethod.GET)
	public NgUser getUser(@PathVariable String id) {
		logger.info("Getting user by ID {}.", id);
		NgUser user = userService.getUserById(Integer.parseInt(id));
		logger.info("User password is : " + user.getPassword());
		return user;
	}
	
	@RequestMapping(value = "/findUserByName/{userName}", method = RequestMethod.GET)
	public NgUser getUserByName(@PathVariable String userName) {
		logger.info("Getting user by Name {}.", userName);
		NgUser user = userService.getUserByName(userName);
		logger.info("User password is : " + user.getPassword());
		return user;
	}
	
	@RequestMapping(value = "/updateUser/{id}", method = RequestMethod.GET)
	public NgUser updateUser(@PathVariable String id) {
		logger.info("Getting user by ID {}.", id);
		NgUser user = userService.updateUserById(Integer.parseInt(id));
		logger.info("User password is : " + user.getPassword());
		return user;
	}

	@RequestMapping(value = "/loadAll", method = RequestMethod.GET)
	public Iterable<NgUser> loadAllUser() {
		ArrayList<NgUser> users = userService.loadAllUsers();
		logger.info("Total User Found: "+users.size());
		if(users.size()>0) {
			logger.info("User1 id: "+users.get(0).getUserName());
		}
		
		for (NgUser user : users) {
			logger.info("Getting user password: "+user.getPassword());
			userService.addUserInCacheById(user.getId());
		}
		return users;
	}

	@CacheEvict(value = {"users-all","user"} , allEntries = true)
	@GetMapping("/clearAll")
	public void evictAllUsers() {
		logger.info("Evict users-all group from cache");
	}
	
	@RequestMapping(value = "/getUserCredit/{userName}", method = RequestMethod.GET)
	public Integer getUserCredit(@PathVariable String userName) {
		logger.info("Getting user by name {}.", userName);
		Integer availableCredit = userCreditMapService.getUserCreditByUserNameFromRedis(userName);
		logger.info("Available user credit is : " + availableCredit);
		return availableCredit;
	}
	
	@RequestMapping(value = "/addUserCredit/{userName}/{creditToBeAdded}", method = RequestMethod.GET)
	public Integer addUserCredit(@PathVariable String userName, @PathVariable String creditToBeAdded) {
		logger.info("Getting user by name {}.", userName);
		Integer availableCredit = userCreditMapService.allocateAndAddUserCreditByUserNameInRedisAndDB(userName, Integer.parseInt(creditToBeAdded),"Credit "+creditToBeAdded+" allocated by support team.");
		logger.info("Available user credit is : " + availableCredit);
		return availableCredit;
	}
	
	
	/*@RequestMapping(value = "/updateUserCredit/{userName}", method = RequestMethod.GET)
	public Integer updateUserCredit(@PathVariable String userName) {
		logger.info("Getting user by name {}.", userName);
		Integer availableCredit = userCreditMapService.updateUserCreditByUserName(userName,1);
		logger.info("Available user credit is : " + availableCredit);
		return availableCredit;
	}*/
	
	@RequestMapping(value = "/loadAllDndData", method = RequestMethod.GET)
	public Boolean loadAllDndData() {
		logger.info("Load All Dnd Data List Request");
		return globalDndListService.loadAllDndDataInCache();
	}

	@RequestMapping(value = "/clearAllDndData", method = RequestMethod.GET)
	public Boolean clearAllDndData() {
		logger.info("Load All Dnd Data List Request");
		return globalDndListService.clearAllDndDataFromCache();
	}

	@RequestMapping(value = "/isDndNumber/{phoneNumber}", method = RequestMethod.GET)
	public Boolean isDndNumber(@PathVariable String phoneNumber){
		logger.info("Checking Dnd by number {}.", phoneNumber);
		return globalDndListService.isDndNumber(phoneNumber); 
	}
	
	@RequestMapping(value = "/loadAllUserBlackListData", method = RequestMethod.GET)
	public Boolean loadAllUserBlackListData() {
		logger.info("Load All User Black List Data Request");
		return userBlackListService.loadAllUserBlackListDataInCache();
	}
	
	@RequestMapping(value = "/isUserBlackListNumber/{userName}/{phoneNumber}", method = RequestMethod.GET)
	public Boolean isUserBlackListNumber(@PathVariable String userName, @PathVariable String phoneNumber){
		logger.info("Checking User Black List Number for user{},  {}.", userName, phoneNumber);
		return userBlackListService.isUserBlackListNumber(userName, phoneNumber); 
	}
	
	@RequestMapping(value = "/loadAllUserSenderIdBlackListData", method = RequestMethod.GET)
	public Boolean loadAllUserSenderIdBlackListData() {
		logger.info("Load All User Sender Id Black List Data Request");
		return userBlackListService.loadAllUserSenderIdBlackListDataInCache();
	}
	
	@RequestMapping(value = "/isUserSenderIdBlackList/{userName}/{senderId}", method = RequestMethod.GET)
	public Boolean isUserSenderIdBlackList(@PathVariable String userName, @PathVariable String senderId){
		logger.info("Checking User Sender Id Black List for user{},  {}.", userName, senderId);
		return userBlackListService.isUserSenderIdBlackList(userName, senderId); 
	}
	
	@RequestMapping(value = "/loadAllUserPremiumListData", method = RequestMethod.GET)
	public Boolean loadAllUserPremiumListData() {
		logger.info("Load All User Premium List Data Request");
		return userPremiumListService.loadAllUserPremiumListDataInCache();
	}
	
	@RequestMapping(value = "/isUserPremiumListNumber/{userName}/{phoneNumber}", method = RequestMethod.GET)
	public Boolean isUserPremiumListNumber(@PathVariable String userName, @PathVariable String phoneNumber){
		logger.info("Checking User Premium List Number for user{},  {}.", userName, phoneNumber);
		return userPremiumListService.isUserPremiumListNumber(userName, phoneNumber); 
	}
	
	@RequestMapping(value = "/loadAllUserAbusingWordsData", method = RequestMethod.GET)
	public Boolean loadAllUserAbusingWordsData() {
		logger.info("Load All User Abusing Words Data Request");
		return  abusingWordsListService.loadAllUserAbusingWordsDataInCache();
	}
	
	@RequestMapping(value = "/isUserAbusingWord/{userName}/{abusingWord}", method = RequestMethod.GET)
	public Boolean isUserAbusingWord(@PathVariable String userName, @PathVariable String abusingWord){
		logger.info("Checking abusing word for user{},  {}.", userName, abusingWord);
		return abusingWordsListService.isUserAbusingWord(userName, abusingWord); 
	}
	
	@RequestMapping(value = "/generateAndSaveTokenForUser/{userName}", method = RequestMethod.GET)
	public String generateAndSaveTokenForUser(@PathVariable String userName){
		logger.info("Going to Generate Token For User : {} ", userName);
		return userService.generateAndSaveTokenForUser(userName).getEncryptedPassword(); 
	}

	@RequestMapping(value = "/deductUserCreditByOne/{userName}", method = RequestMethod.GET)
	public Integer deductUserCreditByOne(@PathVariable String userName){
		logger.info("Going to deduct user credit by one for user: {} ", userName);
		return userCreditMapService.updateUserCacheCreditByUserNameInCache(userName, -1); 
	}
 
	@RequestMapping(value = "/updateUserCreditInDbFromCache", method = RequestMethod.GET)
	public boolean updateUserCreditInDbFromCache(){
		logger.info("Going to update user credit in DB ");
		System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println("userCreditMap is :"+userCreditMapService.updateUserCreditFromCacheToDB());
		return userCreditMapService.updateUserCreditFromCacheToDB();
		
	}
 
	@RequestMapping(value = "/loadTemplateForUserIdAndSenderId/{userId}/{senderId}", method = RequestMethod.GET)
	public boolean loadTemplateForUserIdAndSenderId(@PathVariable String userId, @PathVariable String senderId){
		logger.info("Going to load template for userid and sender id {}, {}", userId, senderId);
		return userService.loadTemplateForSenderIdAndUserId(userId, senderId); 
		
	}
	
	@RequestMapping(value = "/loadTemplateForUser/{userId}/{senderId}/{template}/{dltId}/{serviceType}", method = RequestMethod.GET)
	public boolean loadTemplateForUser(@PathVariable String userId, @PathVariable String senderId,
															@PathVariable String template, @PathVariable String dltId, @PathVariable String serviceType){
		logger.info("Going to load template for userid and sender id {}, {}", userId, senderId);
		return userService.loadTemplateForUser(userId, senderId, template, dltId, serviceType); 
		
	}

}
