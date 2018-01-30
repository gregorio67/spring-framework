
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cmn.util.base.BaseConstants.TransactionStatus;
import cmn.util.base.BaseController;
import cmn.util.common.NullUtil;
import cmn.util.common.RandomUtil;
import cmn.util.common.ResMessage;
import cmn.util.converter.JsonUtil;
import cmn.util.net.NetUtil;
import cmn.util.spring.BeanUtil;
import cmn.util.spring.HttpUtil;
import cmn.util.spring.MessageUtil;
import cmn.util.spring.PropertiesUtil;
import cmn.util.spring.TranLogService;
import sehati.inf.rest.service.ClientRestService;
import sehati.inf.service.ApiService;

@RestController

public class InfRestController extends BaseController {

	
	private static final String SERVICE_BEAN_NAME = "serviceRest";

	
	/** API Auth Check Service **/
	@Resource(name = "apiService")
	private ApiService apiService;

	/** Transaction Log **/
	@Resource(name = "tranLogService")
	private TranLogService tranLogService;


	/**
	 * 
	 *<pre>
	 * Interface Rest controller. All of the service is accepted only post method
	 *</pre>
	 * @param params Map<String, Object> JSON Parameter
	 * @param systemId String Target System ID
	 * @param infId String Target Interface ID
	 * @param serviceName String Target Service Name
	 * @return Map<String, Object>
	 * @throws Exception
	 */
	@RequestMapping(value="/api/{systemId}/{infId}/{serviceName}", method={RequestMethod.POST}, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Map<String, Object> serviceCall(@RequestBody Map<String, Object> params, @PathVariable String systemId, @PathVariable String infId, @PathVariable String serviceName) throws Exception {

		boolean dbLogging = PropertiesUtil.getString("if.loggin.db") != null ? "true".equalsIgnoreCase(PropertiesUtil.getString("if.loggin.db")) : false;
		
		Map<String, Object> infLog = new HashMap<String, Object>();

		Map<String, Object> headerMap = getRequestHeader(params);
		
		int tranSeq = 0;

		if (dbLogging) {
			/** Get Transaction Log Sequence **/
			tranSeq = tranLogService.selectSequence();


			/** TransLog **/
			infLog.put("tranSeq", tranSeq);
			infLog.put("serverId", "INF");
			infLog.put("startTime", new Date());
			infLog.put("interfaceId", infId + "." + serviceName);
			infLog.put("interfaceName", "");
			infLog.put("status", TransactionStatus.STARTED);
			infLog.put("clientId", "");
			infLog.put("clientIp", NetUtil.getClinetIP());
			infLog.put("authKey",  headerMap.get("authCode"));
			infLog.put("requestData", JsonUtil.map2Json(params));
			infLog.put("sourceServer", "");
			infLog.put("targetServer", systemId);			
		}
				
		/** Path Null Check **/
		if (NullUtil.isNull(systemId) || NullUtil.isNull(infId) || NullUtil.isNull(serviceName)) {
	
			String[] uriParam = {systemId + "/" + infId + "/" + serviceName};
			
			Map<String, Object> errorMap =  ResMessage.makeResponse("err.com.uri", uriParam, true);

			LOGGER.error("URL is not matched");

			if (dbLogging) {
				infLog.put("status", TransactionStatus.FAILED);
				infLog.put("exitCode", "err.com.uri");
				infLog.put("exitMessage", MessageUtil.getMessage("err.com.uri", uriParam));
				infLog.put("responseData", JsonUtil.map2Json(errorMap));	
				tranLogService.insertTransLog(infLog);				
			}
			
			return errorMap;
		}

		
		String[] msgParam = {serviceName};

		LOGGER.info("Input Header Param :: {}", headerMap != null ? headerMap.toString() : "headerMap is null");
		
		if (NullUtil.isNull(headerMap) || NullUtil.isNull(headerMap.get("authCode"))) {
			
			Map<String, Object> errorMap =  ResMessage.makeResponse("err.com.param", msgParam, true);
			LOGGER.error("Request Header is null");
			
			if (dbLogging) {
				infLog.put("status", TransactionStatus.FAILED);
				infLog.put("exitCode", "err.com.uri");
				infLog.put("exitMessage", MessageUtil.getMessage("err.com.uri", msgParam));
				infLog.put("responseData", JsonUtil.map2Json(errorMap));
				
				tranLogService.insertTransLog(infLog);				
			}
			
			return errorMap;
		}

		/** Get Data Map and Null Check **/
		Map<String, Object> dataMap = getRequestData(params);
		LOGGER.info("Input Data Param :: {}", dataMap != null ? dataMap.toString() : "dataMap is null");

		if (NullUtil.isNull(dataMap)) {
			
			Map<String, Object> errorMap = ResMessage.makeResponse("err.com.param", msgParam, true);
			LOGGER.error("Request Data is null");

			if (dbLogging) {
				infLog.put("status", TransactionStatus.FAILED);
				infLog.put("exitCode", "err.com.uri");
				infLog.put("exitMessage", MessageUtil.getMessage("err.com.param", msgParam));
				infLog.put("responseData", JsonUtil.map2Json(errorMap));
				
				tranLogService.insertTransLog(infLog);				
			}
			
			return errorMap;
		}

		/** Set Request **/
		HttpUtil.setRequestData("infId", infId);
		HttpUtil.setRequestData("serviceName", serviceName);
		if (dbLogging) {
			HttpUtil.setRequestData("tranSeq", tranSeq);			
		}
		
		/** Call Restful Service **/
		
		LOGGER.info("Rest Service call start :: {}::{}", systemId + infId + serviceName, dataMap.toString());

		
		/** Service Bean lookup with Interface Id**/
		Map<String, String> serviceMap = BeanUtil.getBean(SERVICE_BEAN_NAME);
		String callServiceName = serviceMap.get(infId) != null ? serviceMap.get(infId) : null;
		if (NullUtil.isNull(callServiceName) ) {
			Map<String, Object> errorMap =  ResMessage.makeResponse("err.com.param", msgParam, true);
			LOGGER.error("Request Data is null");

			if (dbLogging) {
				infLog.put("status", TransactionStatus.FAILED);
				infLog.put("exitCode", "err.com.uri");
				infLog.put("exitMessage", MessageUtil.getMessage("err.com.param", msgParam));
				infLog.put("responseData", JsonUtil.map2Json(errorMap));
				
				tranLogService.insertTransLog(infLog);				
			}
			
			return errorMap;	
		}
		
		/** Call Service **/		
		ClientRestService restService = BeanUtil.getBean(callServiceName);
		
		/** Call Rest Service **/
		Map<String, Object> resultMap = restService.callRestService(systemId, infId, serviceName, params, infLog);
		
		return resultMap;
	}


	/**
	 * 
	 *<pre>
	 * 
	 *</pre>
	 * @param params Map<String, Object>
	 * @param userId 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/api/authcode/{clientId}", method={RequestMethod.GET, RequestMethod.POST}, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Map<String, Object> getClientId(@RequestBody Map<String, Object> params, @PathVariable String clientId) throws Exception {

		if (NullUtil.isNull(clientId)) {
			return ResMessage.makeResponse("err.com.uri", true);
		}

		String[] msgParam = {"authcodeService"};
		
		Map<String, Object> dataMap = new HashMap<String, Object>();

		/** Create client ID **/
		dataMap.put("clientId", clientId);
		dataMap.put("authCode", RandomUtil.getUUID());
		dataMap.put("authTime", System.currentTimeMillis());


		try {
			if (apiService.updateApiUser(dataMap) < 1 ) {
				return ResMessage.makeResponse("info.com.noresult", msgParam ,true);
			}
			else {
				return ResMessage.makeResponse(dataMap, "info.com.success", false);
			}
		}
		catch(Exception ex) {
			return ResMessage.makeResponse("err.com.service", msgParam, true);
		}
	}
}
