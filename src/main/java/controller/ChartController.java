
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cmn.util.base.BaseController;
import cmn.util.dao.CamelMap;
import sehati.inf.chart.service.ChartService;


@Controller	
public class CharController extends BaseController {

	@Resource(name = "chartService")
	private ChartService chartService;
	
	
	@RequestMapping("/chart/viewChart")
	public ModelAndView viewChart() throws Exception {
			ModelAndView mav = new ModelAndView();
			mav.setViewName("InterfaceStatistics");		
			return mav;
	}
	
	@RequestMapping(value="/chart/showStatistics.do")
	public @ResponseBody Map<String, Object> showStatistics(@RequestBody Map<String, Object> param) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		/** Search Condition **/
		List<String> categories = chartService.selectPivotQueryTarget(param);
		param.put("condDate", categories);
		
		List<CamelMap> resultList = chartService.selectChartData(param);
		
//		List<String> resultColumn = new ArrayList<String>();

//		for (CamelMap map : resultList) {
//			StringBuilder sb = new StringBuilder();
//			
//			@SuppressWarnings("unchecked")
//			Iterator<String> itr = map.keySet().iterator();
//			
//			sb.append("'").append(map.get("interfaceId")).append("'").append(",");
//			while(itr.hasNext()) {
//				String key = (String)itr.next();
//				if (!"interfaceId".equals(key)) {
//					sb.append(map.get(key)).append(",");
//				}
//			}
//			
//			int endIndex = sb.toString().lastIndexOf(",");
//			String column = sb.toString().substring(0, endIndex);
//			resultColumn.add(column);
//		}

//		StringBuilder sb = new StringBuilder().append("[");
//		for (String str : categories) {
//			sb.append(str).append(",");
//		}
//		int endIndex = sb.toString().lastIndexOf(",");
//		
//		String charCategories = sb.toString().substring(0, endIndex)  + ("]");
		resultMap.put("categories", categories);

		resultMap.put("columns", resultList);
		
		return resultMap;
	}
}
