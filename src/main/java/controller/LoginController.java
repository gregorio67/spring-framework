
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cmn.util.base.BaseController;
import cmn.util.common.NullUtil;
import cmn.util.common.ResMessage;


@Controller
public class LoginController extends BaseController {

	@RequestMapping(value = "/cmn/loginPage.do")
	public ModelAndView loginPage() throws Exception {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("login");
		return mav;
	}
	
	@RequestMapping(value = "/cmn/login.do")
	public @ResponseBody Map<String, Object> userLogin(@RequestBody Map<String, Object> param) throws Exception {
		
		if (NullUtil.isNull(param.get("id")) ||  NullUtil.isNull(param.get("password"))) {
			return ResMessage.makeResponse("err.com.login", null ,true);
		}
		
		return ResMessage.makeResponse("info.com.login", null ,false);
	}

	
	@RequestMapping(value = "/cmn/viewMap.do")
	public ModelAndView viewGoogleMap() throws Exception {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("googleMap");
		return mav;
	}
	
}
