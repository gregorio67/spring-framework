
import java.util.Map;

import javax.annotation.Resource;

import cmn.util.spring.BaseService;
import cmn.util.spring.TranLogService;
import cmn.util.spring.rest.RestClient;


public abstract class BaseRestService extends BaseService implements ClientRestService{
	
	@Resource(name = "restClient")
	protected RestClient restClient;	
	
	@Resource(name = "tranLogService")
	protected TranLogService tranLogService;

	@Override
	public <T> T callRestService(String systemId, String infId, String serviceName, Map<String, Object> params, Map<String, Object> transLog) throws Exception {

		/** Transaction log insert **/
		try {
			tranLogService.insertTransLog(transLog);			
		}
		catch(Exception logex) {
			LOGGER.error("Transaction Log Insert Error...{}", logex.getMessage());
		}

		return executeService(systemId, infId, serviceName, params, transLog);
	}
	
	public abstract <T> T executeService(String systemId, String infId, String serviceName, Map<String, Object> params, Map<String, Object> transLog) throws Exception;

}
