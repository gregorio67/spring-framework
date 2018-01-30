
import java.util.Date;
import java.util.Map;


import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import cmn.util.base.BaseConstants;
import cmn.util.base.BaseConstants.TransactionStatus;
import cmn.util.common.NullUtil;
import cmn.util.common.ResMessage;
import cmn.util.converter.JsonUtil;
import cmn.util.spring.MessageUtil;
import sehati.inf.rest.service.BaseRestService;
import sehati.inf.rest.service.ClientRestService;


@Service("dscRestService")
public class DscRestServiceImpl extends BaseRestService implements ClientRestService {
	
	
	@SuppressWarnings("unchecked")
	public <T> T executeService(String systemId, String infId, String serviceName, Map<String, Object> params, Map<String, Object> transLog) throws Exception {
		/** Check authorization with client Id and authorization code **/

		String[] msgParam = {serviceName};
		
		Map<String, Object> restResultMap = null;
	
		/** Call Target Restful Service **/
		String intfGroupId = systemId + "." + infId;
		transLog.put("callStartTime", new Date());
//		try {
			restResultMap = restClient.exchange(intfGroupId, serviceName, null, MediaType.APPLICATION_JSON, null, params);			
//		}
//		catch(Exception ex) {
//			
//			Map<String, Object> errorMap =  ResMessage.makeResponse("err.com.service", msgParam, true);
//
//			transLog.put("callEndTime", new Date());
//			transLog.put("endTime", new Date());
//			transLog.put("status", BaseConstants.TRAN_FAILED_CODE);
//			transLog.put("exitCode", "err.com.service");
//			transLog.put("exitMessage", MessageUtil.getMessage("err.com.uri", msgParam));
//			transLog.put("responseData", JsonUtil.map2Json(errorMap));
//
//			tranLogService.updateTransLog(transLog);			
//			
//			return (T)errorMap;
//		}

		transLog.put("callEndTime", new Date());			

		Map<String, Object> resMessage = (Map<String, Object>) restResultMap.get("resMessage");
		
		if ("ERROR".equals(resMessage.get("stautsCode"))) {
			Map<String, Object> errorMap = ResMessage.makeResponseWithMessage("err.com.service", (String)resMessage.get("message"), true);

			transLog.put("endTime", new Date());
			transLog.put("status",TransactionStatus.FAILED);
			transLog.put("exitCode", "err.com.service");
			transLog.put("exitMessage", (String)resMessage.get("message"));
			transLog.put("responseData", JsonUtil.map2Json(errorMap));

			tranLogService.updateTransLog(transLog);			
			
			return (T)errorMap;			
			
		}
		LOGGER.info("Rest Service Result :: {} :: {}", systemId + infId + serviceName, restResultMap != null ? restResultMap.toString():"no result");

		if (NullUtil.isNull(restResultMap)) {
			Map<String, Object> errorMap = ResMessage.makeResponse("info.com.noresult", true);
			
			transLog.put("endTime", new Date());
			transLog.put("status", TransactionStatus.FAILED);
			transLog.put("exitCode", "info.com.noresult");
			transLog.put("exitMessage", MessageUtil.getMessage("info.com.noresult"));
			transLog.put("responseData", JsonUtil.map2Json(errorMap));

			tranLogService.updateTransLog(transLog);			
			
			return (T)errorMap;
		}
		
		/** Successful Result **/
		Map<String, Object> resultMap = ResMessage.makeResponse(restResultMap, "info.com.success", false);
		
		transLog.put("endTime", new Date());
		transLog.put("status", TransactionStatus.COMPLETED);
		transLog.put("exitCode", "info.com.success");
		transLog.put("exitMessage", MessageUtil.getMessage("info.com.success", msgParam));
		transLog.put("responseData", JsonUtil.map2Json(resultMap));

		tranLogService.updateTransLog(transLog);	
		
		return (T)resultMap;		
	}

}
