
import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Service;

import cmn.util.base.BaseConstants;
import cmn.util.base.BaseConstants.TransactionStatus;
import cmn.util.common.NullUtil;
import cmn.util.common.ResMessage;
import cmn.util.converter.JsonUtil;
import cmn.util.converter.MapUtil;
import cmn.util.spring.MessageUtil;
import sehati.inf.ws.service.BaseWsService;
import sehati.inf.ws.service.ClientWsService;
import sehati.inf.ws.vo.request.GetCitiesByCountry;
import sehati.inf.ws.vo.response.GetCitiesByCountryResponse;



@Service("weatherCheckService")
public class WeatherCheckServiceImpl extends BaseWsService implements ClientWsService {

	/**
	 * 
	 *<pre>
	 *
	 *</pre>
	 * @param params
	 * @param infId
	 * @param serviceName
	 * @return
	 * @throws Exception
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <T> T executeService(String infId, String serviceName, Map<String, Object> params, Map<String, Object> transLog) throws Exception {

		String[] msgParam = {serviceName};
		
		/** Convert Map to ApiUser **/
		GetCitiesByCountry weatherSearchReqVo = new GetCitiesByCountry();

		/** Convert Map to Value Object **/
		Map<String, Object> dataMap = (Map<String, Object>) params.get("reqData");
		weatherSearchReqVo = (GetCitiesByCountry) MapUtil.convertMapToObject(dataMap, weatherSearchReqVo);

		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Result :: {} :: {}", weatherSearchReqVo.getCountryName());
		}

		transLog.put("callStartTime", new Date());

		/** Web Service Client **/ 
		GetCitiesByCountryResponse weatherResVo = webServiceClient.requestWsIf(infId, serviceName, weatherSearchReqVo, GetCitiesByCountryResponse.class, null);

		if (LOGGER.isDebugEnabled()) {
			LOGGER.info("weatherResVo :: {}", weatherResVo.getGetCitiesByCountryResult());
		}
		
		
		transLog.put("callEndTime", new Date());

		
		if (NullUtil.isNull(weatherResVo)) {
			LOGGER.error("Web Service Result is null");

			Map<String, Object> errorMap = ResMessage.makeResponse("info.com.noresult", msgParam, true);

			transLog.put("endTime", new Date());
			transLog.put("status", TransactionStatus.FAILED);
			transLog.put("exitCode", "info.com.noresult");
			transLog.put("exitMessage", MessageUtil.getMessage("info.com.noresult"));
			transLog.put("responseData", JsonUtil.map2Json(errorMap));
			
			tranLogService.updateTransLog(transLog);
			
			return (T) errorMap;
		}
	
		transLog.put("endTime", new Date());
		transLog.put("status", TransactionStatus.COMPLETED);
		transLog.put("exitCode", "info.com.success");
		transLog.put("exitMessage", MessageUtil.getMessage("info.com.success", msgParam));
		transLog.put("responseData", JsonUtil.object2Json(weatherResVo));
		
		tranLogService.updateTransLog(transLog);
		return (T)weatherResVo;
	}

}
