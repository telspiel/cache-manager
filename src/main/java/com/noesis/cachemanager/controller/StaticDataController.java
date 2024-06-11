package com.noesis.cachemanager.controller;

import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.validator.constraints.Length;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.noesis.domain.service.GlobalBlackListService;
import com.noesis.domain.service.GlobalPremiumListService;
import com.noesis.domain.service.NgOperatorErrorCodeMappingService;
import com.noesis.domain.service.StaticDataService;
import com.noesis.domain.service.UserSenderIdMapService;
 

@RestController
public class StaticDataController {

	private final Logger logger = LoggerFactory.getLogger(getClass());

/*	select * from ng_carrier_master;
	select * from ng_circle_master;
	select * from ng_number_carrier_circle_mapping;

	select * from ng_routing_group_master;
	select * from ng_routing_type_master;
	select * from ng_kannel_group_mapping;
	select * from ng_routing_info;
*/
	@Autowired
	StaticDataService staticDataService;
	
	@Autowired
	GlobalBlackListService globalBlackListService;
	
	@Autowired
	GlobalPremiumListService globalPremiumListService;
	
	@Autowired
	UserSenderIdMapService userSenderIdMapService;
	
	@Autowired
	NgOperatorErrorCodeMappingService ngOperatorErrorCodeMappingService;
	
	@RequestMapping(value = "/loadAllStaticData", method = RequestMethod.GET)
	public void loadAllStaticData() {
		logger.info("Request received for loading all routing data");
		loadAllGlobalBlackListData();
		//loadAllGlobalSenderIdBlackListData();
		loadCarrierData();
		loadCircleData();
		loadNumberSeriesData();
		loadRoutingGroups();
		loadRoutingTypes();
		loadRoutingInfoTrans();
		loadRoutingInfoTransPromoDnd();
		loadRoutingInfoTransPromoNonDnd();
		loadRoutingInfoPromo();
		loadKannelGroupMapping();
		
		//Changes for YSA
		loadAllSenderId();
		loadAllEntityIdData();
		//loadAllSenderIdAndEntityId();
		//loadAllContentTemplateData();
		loadAllGlobalPremiumListData();
		//loadAllPlatformErrorCodesAndDesc();
		//loadAllOperatorErrorCodesAndDesc();
		
	}
	
	@RequestMapping(value = "/loadAllGlobalBlackListData", method = RequestMethod.GET)
	public Boolean loadAllGlobalBlackListData() {
		logger.info("Load All Global Black List Data Request");
		return globalBlackListService.loadAllGlobalBlackListDataInCache();
	}

	@RequestMapping(value = "/clearAllGlobalBlackListData", method = RequestMethod.GET)
	public Boolean clearAllGlobalBlackListData() {
		logger.info("Load All Dnd Data List Request");
		return globalBlackListService.clearAllBlackListDataFromCache();
	}
	
	Set<String> set = new HashSet<>();
	@RequestMapping(value = "/isGlobalBlackListNumber/{phoneNumber}", method = RequestMethod.GET)
	public Boolean isGlobalBlackListNumber(@PathVariable String phoneNumber){
		logger.info("Checking is number in global black list {}.", phoneNumber);
		return globalBlackListService.isBlackListNumber(phoneNumber, set); 
	}
	
	@RequestMapping(value = "/loadAllGlobalSenderIdBlackListData", method = RequestMethod.GET)
	public Boolean loadAllGlobalSenderIdBlackListData() {
		logger.info("Load All Global Sender Id Black List Data Request");
		return globalBlackListService.loadAllGlobalSenderIdBlackListDataInCache();
	}

	@RequestMapping(value = "/clearAllGlobalSenderIdBlackListData", method = RequestMethod.GET)
	public Boolean clearAllGlobalSenderIdBlackListData() {
		logger.info("Clear All black list sender id Data Request");
		return globalBlackListService.clearAllSenderIdBlackListDataFromCache();
	}

	@RequestMapping(value = "/isGlobalBlackListSenderId/{senderId}", method = RequestMethod.GET)
	public Boolean isGlobalBlackListSenderId(@PathVariable String senderId){
		logger.info("Checking is sender id black list {}.", senderId);
		return globalBlackListService.isBlackListSenderId(senderId); 
	}
	
	@RequestMapping(value = "/loadCarrierData", method = RequestMethod.GET)
	public boolean loadCarrierData() {
		logger.info("Request received for loading carrier data");
		boolean status = staticDataService.loadCarrierData();
		logger.info("Status for loading carrier data is : {}", status);
		return status;
	}
	
	@RequestMapping(value = "/loadCircleData", method = RequestMethod.GET)
	public boolean loadCircleData() {
		logger.info("Request received for loading circle data");
		boolean status = staticDataService.loadCircleData();
		logger.info("Status for loading circle data is: {}", status);
		return status;
	}
	
	@RequestMapping(value = "/loadNumberSeriesData", method = RequestMethod.GET)
	public boolean loadNumberSeriesData() {
		logger.info("Request received for loading number series data");
		boolean status = staticDataService.loadNumberSeriesData();
		logger.info("Status for loading number series data data is : {}", status);
		return status;
	}
	
	@RequestMapping(value = "/loadRoutingTypes", method = RequestMethod.GET)
	public boolean loadRoutingTypes() {
		logger.info("Request received for loading routing types");
		boolean status = staticDataService.loadRoutingTypes();
		logger.info("Status for loading routing types data is : {}", status);
		return status;
	}
	
	@RequestMapping(value = "/loadRoutingGroups", method = RequestMethod.GET)
	public boolean loadRoutingGroups() {
		logger.info("Request received for loading routing groups");
		boolean status = staticDataService.loadRoutingGroups();
		logger.info("Status for loading routing group data is : {}", status);
		return status;
	}

	@RequestMapping(value = "/loadKannelGroupMapping", method = RequestMethod.GET)
	public boolean loadKannelGroupMapping() {
		logger.info("Request received for loading kannel group mapping");
		boolean status = staticDataService.loadKannelGroupMapping();
		logger.info("Status for loading kannel group mapping is : {}", status);
		return status;
	}

	@RequestMapping(value = "/loadRoutingInfoTrans", method = RequestMethod.GET)
	public boolean loadRoutingInfoTrans() {
		logger.info("Request received for loading routing info trans");
		boolean status = staticDataService.loadRoutingInfoTrans();
		logger.info("Status for loading routing info trans is : {}", status);
		return status;
	}
	
	@RequestMapping(value = "/loadRoutingInfoPromo", method = RequestMethod.GET)
	public boolean loadRoutingInfoPromo() {
		logger.info("Request received for loading routing info promo");
		boolean status = staticDataService.loadRoutingInfoPromo();
		logger.info("Status for loading routing info promo is : {}", status);
		return status;
	}
	
	@RequestMapping(value = "/loadRoutingInfoTransPromoDnd", method = RequestMethod.GET)
	public boolean loadRoutingInfoTransPromoDnd() {
		logger.info("Request received for loading routing info transpromo dnd");
		boolean status = staticDataService.loadRoutingInfoTransPromoDnd();
		logger.info("Status for loading routing info transpromo is : {}", status);
		return status;
	}
	
	@RequestMapping(value = "/loadRoutingInfoTransPromoNonDnd", method = RequestMethod.GET)
	public boolean loadRoutingInfoTransPromoNonDnd() {
		logger.info("Request received for loading routing info transpromo non dnd");
		boolean status = staticDataService.loadRoutingInfoTransPromoNonDnd();
		logger.info("Status for loading routing info transpromo is : {}", status);
		return status;
	}
	
	@RequestMapping(value = "/getRoutingGroupForKeyForTrans/{routingInfo}", method = RequestMethod.GET)
	public Integer getRoutingGroupForKeyForTrans(@PathVariable String routingInfo) {
		logger.info("Request received for getting routing group from routing info.");
		Integer routingGroup = staticDataService.getRoutingGroupForKeyForTrans("1#1#1");
		logger.info("Routing group for given key is : {}", routingGroup);
		return routingGroup;
	}
	
	@RequestMapping(value = "/getRoutingGroupForKeyForPromo/{routingInfo}", method = RequestMethod.GET)
	public Integer getRoutingGroupForKeyForPromo(@PathVariable String routingInfo) {
		logger.info("Request received for getting routing group from routing info for promo.");
		Integer routingGroup = staticDataService.getRoutingGroupForKeyForPromo("1#1#1");
		logger.info("Routing group for given key is : {}", routingGroup);
		return routingGroup;
	}
	
	@RequestMapping(value = "/getRoutingGroupForKeyForTransPromoDnd/{routingInfo}", method = RequestMethod.GET)
	public Integer getRoutingGroupForKeyForTransPromoDnd(@PathVariable String routingInfo) {
		logger.info("Request received for getting routing group from routing info for trans promo dnd.");
		Integer routingGroup = staticDataService.getRoutingGroupForKeyForTransPromoDnd("1#1#1");
		logger.info("Routing group for given key is : {}", routingGroup);
		return routingGroup;
	}
	
	@RequestMapping(value = "/getRoutingGroupForKeyForTransPromoNonDnd/{routingInfo}", method = RequestMethod.GET)
	public Integer getRoutingGroupForKeyForTransPromoNonDnd(@PathVariable String routingInfo) {
		logger.info("Request received for getting routing group from routing info for trans promo non dnd.");
		Integer routingGroup = staticDataService.getRoutingGroupForKeyForTransPromoNonDnd("1#1#1");
		logger.info("Routing group for given key is : {}", routingGroup);
		return routingGroup;
	}
	
	@RequestMapping(value = "/loadAllSenderId", method = RequestMethod.GET)
	public boolean loadAllSenderId() {
		logger.info("Request received for loading all sender ids");
		boolean status = staticDataService.loadAllSenderId();
		logger.info("Status for loading all sender id is : {}", status);
		return status;
	}
	
	/*@RequestMapping(value = "/loadAllSenderIdAndEntityId", method = RequestMethod.GET)
	public boolean loadAllSenderIdAndEntityId() {
		logger.info("Request received for loading all sender ids and entity ids");
		boolean status = staticDataService.loadAllSenderIdAndEntityIdData();
		logger.info("Status for loading all sender id and entity id is : {}", status);
		return status;
	}*/
	
	@RequestMapping(value = "/loadSpecificSenderIdForUser/{userId}/{senderId}", method = RequestMethod.GET)
	public boolean loadSpecificSenderIdForUser(@PathVariable int userId, @PathVariable String senderId) {
		logger.info("Request received for loading specific sender id {} for user {}", senderId, userId);
		boolean status = staticDataService.loadSpecificSenderIdForUser(userId, senderId);
		logger.info("Status for loading specific sender id is : {}", status);
		return status;
	}
	
	@RequestMapping(value = "/loadAllEntityId", method = RequestMethod.GET)
	public boolean loadAllEntityIdData() {
		logger.info("Request received for loading all entity ids");
		boolean status = staticDataService.loadAllEntityIdData();
		logger.info("Status for loading all entity id is : {}", status);
		return status;
	}
	
	@RequestMapping(value = "/loadAllContentTemplateData", method = RequestMethod.GET)
	public boolean loadAllContentTemplateData() {
		logger.info("Request received for loading all content TemplateData.");
		boolean status = staticDataService.loadAllUserSenderIdTemplateData();
		logger.info("Status for loading all content template data is : {}", status);
		return status;
	}
	
	@RequestMapping(value = "/loadAllOperatorErrorCodes", method = RequestMethod.GET)
	public boolean loadAllOperatorErrorCodes() {
		logger.info("Request received for loading all operator error codes.");
		boolean status = ngOperatorErrorCodeMappingService.loadAllOperatorErrorCodes();
		logger.info("Status for loading all operator error codes : {}", status);
		return status;
	}
	
	@RequestMapping(value = "/loadAllOperatorErrorCodesAndDesc", method = RequestMethod.GET)
	public boolean loadAllOperatorErrorCodesAndDesc() {
		logger.info("Request received for loading all operator error codes and description");
		boolean status = ngOperatorErrorCodeMappingService.loadAllOperatorErrorCodesAndDescription();
		logger.info("Status for loading all operator error codes and desc: {}", status);
		return status;
	}
	
	@RequestMapping(value = "/loadAllPlatformErrorCodesAndDesc", method = RequestMethod.GET)
	public boolean loadAllPlatformErrorCodesAndDesc() {
		logger.info("Request received for loading all platform error codes and description");
		boolean status = ngOperatorErrorCodeMappingService.loadAllPlatformErrorCodesAndDescription();
		logger.info("Status for loading all platform error codes and desc: {}", status);
		return status;
	}
	
	@RequestMapping(value = "/approveSenderId/{userName}/{senderId}", method = RequestMethod.GET)
	public boolean approveSenderId(@PathVariable String userName, @PathVariable String senderId) {
		logger.info("Request received for approving sender id {} for user {}. ", senderId, userName);
		boolean approveSenderId = userSenderIdMapService.approveRejectSenderId(senderId, userName, 'Y');
		logger.info("Sender id approve status : {}", approveSenderId);
		return approveSenderId;
	}
	
	@RequestMapping(value = "/rejectSenderId/{userName}/{senderId}", method = RequestMethod.GET)
	public boolean rejectSenderId(@PathVariable String userName, @PathVariable String senderId) {
		logger.info("Request received for rejecting sender id {} for user {}. ", senderId, userName);
		boolean approveSenderId = userSenderIdMapService.approveRejectSenderId(senderId,userName,'N');
		logger.info("Sender id approve status : {}", approveSenderId);
		return approveSenderId;
	}
	
	@RequestMapping(value = "/addDefaultSenderId/{userName}/{senderId}", method = RequestMethod.GET)
	public boolean addDefaultSenderId(@PathVariable String userName, @PathVariable String senderId) {
		logger.info("Request received for adding default sender id {} for user {}. ", senderId, userName);
		boolean approveSenderId = userSenderIdMapService.addRemoveDefaultSenderId(senderId, userName, 'Y');
		logger.info("Sender id approve status : {}", approveSenderId);
		return approveSenderId;
	}

	@RequestMapping(value = "/removeDefaultSenderId/{userName}/{senderId}", method = RequestMethod.GET)
	public boolean removeDefaultSenderId(@PathVariable String userName, @PathVariable String senderId) {
		logger.info("Request received for removing default sender id {} for user {}. ", senderId, userName);
		boolean approveSenderId = userSenderIdMapService.addRemoveDefaultSenderId(senderId, userName, 'N');
		logger.info("Sender id approve status : {}", approveSenderId);
		return approveSenderId;
	}
	
	@RequestMapping(value = "/loadAllGlobalPremiumListData", method = RequestMethod.GET)
	public Boolean loadAllGlobalPremiumListData() {
		logger.info("Load All Global Premium List Data Request");
		return globalPremiumListService.loadAllGlobalPremiumNumberListDataInCache();
	}

}

