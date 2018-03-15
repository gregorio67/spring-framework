import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import kics.main.sample.vo.CustomerMngtVo;


@WebService
public interface CustomerWs {
	
	@WebMethod
	public List<CustomerMngtVo> getCustomerList(CustomerVo customerVo) throws Exception ;
}
