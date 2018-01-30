
import java.util.Map;

import javax.annotation.Resource;

import cmn.util.spring.BaseService;
import cmn.util.spring.TranLogService;
import cmn.util.spring.rest.RestClient;
import cmn.util.spring.ws.WebServiceClient;

public abstract class BaseWsService extends BaseService implements ClientWsService{
	@Resource(name="webServiceClient")
	protected WebServiceClient webServiceClient;
	
	@Resource(name = "restClient")
	protected RestClient restClient;	
	
	@Resource(name = "tranLogService")
	protected TranLogService tranLogService;

	@Override
	public <T> T callService(String infId, String serviceName, Map<String, Object> params, Map<String, Object> transLog) throws Exception {

		/** Transaction log insert **/
		try {
			tranLogService.insertTransLog(transLog);			
		}
		catch(Exception logex) {
			LOGGER.error("Transaction Log Insert Error...{}", logex.getMessage());
		}

		return executeService(infId, serviceName, params, transLog);
	}
	
	public abstract <T> T executeService(String infId, String serviceName, Map<String, Object> params, Map<String, Object> transLog) throws Exception;

}
