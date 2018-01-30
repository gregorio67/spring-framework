
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
import cmn.util.common.ResMessage;
import cmn.util.converter.JsonUtil;
import cmn.util.net.NetUtil;
import cmn.util.spring.BeanUtil;
import cmn.util.spring.HttpUtil;
import cmn.util.spring.MessageUtil;
import cmn.util.spring.PropertiesUtil;
import cmn.util.spring.TranLogService;
import sehati.inf.ws.service.ClientWsService;

@RestController

public class InfWsController extends BaseController {

	private static final String SERVICE_BEAN_NAME = "serviceWebService";
	
	@Resource(name="tranLogService")
	private TranLogService tranLogService;
	
	
	
	/**
	 * 
	 *<pre>
	 * Web service call with SOAP Message
	 *</pre>
	 * @param params Map<String, Object> JSON Message
	 * @param infId String Interface ID
	 * @param serviceName String Service Name
	 * @return Map<String, Object>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/api/webservice/{infId}/{serviceName}", method={RequestMethod.POST}, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody <T> T callWebService(@RequestBody Map<String, Object> params, @PathVariable String infId, @PathVariable String serviceName) throws Exception {

		boolean dbLogging = PropertiesUtil.getString("if.loggin.db") != null ? "true".equalsIgnoreCase(PropertiesUtil.getString("if.loggin.db")) : false;
		
		/** Interface Log Sequence **/
		int tranSeq = 0;		
		
		/** TransLog **/
		Map<String, Object> infLog = new HashMap<String, Object>();
		if (dbLogging) {

			tranSeq = tranLogService.selectSequence();

			infLog.put("tranSeq", tranSeq);
			infLog.put("serverId", "INF");
			infLog.put("startTime", new Date());
			infLog.put("interfaceId", infId + "." + serviceName);
			infLog.put("interfaceName", "");
			infLog.put("status", TransactionStatus.STARTED);
			infLog.put("clientId", "");
			infLog.put("clientIp", NetUtil.getClinetIP());
			infLog.put("authKey",  "");
			infLog.put("requestData", JsonUtil.map2Json(params));
			infLog.put("sourceServer", "");
			infLog.put("targetServer", infId);
			
		}

		if (NullUtil.isNull(infId) || NullUtil.isNull(serviceName)) {
			String[] uriParam = {infId + "/" + serviceName};
			Map<String, Object> errorMap =  ResMessage.makeResponse("err.com.uri", uriParam, true);
			LOGGER.error("URI is not correctlry :: {}, {}", infId, serviceName);
			if (dbLogging) {
				infLog.put("status", TransactionStatus.FAILED);
				infLog.put("exitCode", "err.com.uri");
				infLog.put("exitMessage", MessageUtil.getMessage("err.com.uri", uriParam));
				infLog.put("responseData", JsonUtil.map2Json(errorMap));

				tranLogService.insertTransLog(infLog);				
			}
			
			return (T) errorMap;
		}

		/** Set Request **/		
		/** Set Request **/
		HttpUtil.setRequestData("infId", infId);
		HttpUtil.setRequestData("serviceName", serviceName);
		HttpUtil.setRequestData("tranSeq", tranSeq);
		
		
		String[] msgParam = {serviceName};

		LOGGER.info("Input Parameter :: {}", params != null ? params.toString() : "Input parameter is null");
		/** Check input parameter **/
		if (NullUtil.isNull(params)) {
			Map<String, Object> errorMap =  ResMessage.makeResponse("err.com.param", msgParam, true);
			LOGGER.error("Request Data is null");
			
			if (dbLogging) {
				infLog.put("status", TransactionStatus.FAILED);
				infLog.put("exitCode", "err.com.uri");
				infLog.put("exitMessage", MessageUtil.getMessage("err.com.param", msgParam));
				infLog.put("responseData", JsonUtil.map2Json(errorMap));
				
				tranLogService.insertTransLog(infLog);				
			}
			
			return (T)errorMap;
		}
		
		
		/** Service Bean lookup **/
		Map<String, String> serviceMap = BeanUtil.getBean(SERVICE_BEAN_NAME);
		String callServiceName = serviceMap.get(serviceName) != null ? serviceMap.get(serviceName) : null;
		if (NullUtil.isNull(callServiceName) ) {
			Map<String, Object> errorMap =  ResMessage.makeResponse("err.com.param", msgParam, true);
			LOGGER.error("Can't find service. Check your configuration and servicename :: {}", serviceName);
			
			if (dbLogging) {
				infLog.put("status", TransactionStatus.FAILED);
				infLog.put("exitCode", "err.com.uri");
				infLog.put("exitMessage", MessageUtil.getMessage("err.com.param", msgParam));
				infLog.put("responseData", JsonUtil.map2Json(errorMap));
				
				tranLogService.insertTransLog(infLog);				
			}
			
			return (T)errorMap;	
		}
		
		/** Call Service **/		
		ClientWsService wsService = BeanUtil.getBean(callServiceName);
		T result = wsService.callService(infId, serviceName, params, infLog);
		
		return result;
	}
}
