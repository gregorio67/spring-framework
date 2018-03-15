import javax.annotation.Resource;
import javax.jws.WebService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kics.main.sample.service.CustomerMngtSvi;
import kics.main.sample.vo.CustomerMngtVo;


@WebService
public class CustomerWsI implements CustomerWs {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerWsI.class);
	
	@Resource(name = "customerMngtService")
	private CustomerMngtSvi customerMngtService;

	@Override
	public List<CustomerMngtVo> getCustomerList(CustomerVo customerVo) throws Exception {
		
		List<CustomerMngtVo> resultList = new ArrayList<CustomerMngtVo>();

		Map<String, Object> condParam = new HashMap<String, Object>();
		condParam.put("startNum", 1);
		condParam.put("endNum", 20);
		
		resultList = customerMngtService.retrieveCustomerList(condParam);
		
		return resultList;
			
	}

}
