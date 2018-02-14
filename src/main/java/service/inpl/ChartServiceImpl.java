

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import cmn.util.dao.CamelMap;
import cmn.util.spring.BaseService;
import sehati.inf.chart.service.ChartService;


@Service("chartService")
public class ChartServiceImpl extends BaseService implements ChartService {

	@Override
	public List<CamelMap> selectChartData(Map<String, Object> param) throws Exception {

		String chartType = param.get("chartType") != null ? String.valueOf(param.get("chartType")) : "";

		List<CamelMap> resultList = null;
		if ("daily".equals(chartType)) {
			resultList = baseDao.selectList("sehati.inf.translog.selDailyChart", param);
		}
		else if ("monthly".equals(chartType)) {
			resultList = baseDao.selectList("sehati.inf.translog.selMonthlyChart", param);			
		}
		else {
			resultList = baseDao.selectList("sehati.inf.translog.selMonthlyChart", param);						
		}
		return resultList;
	}

	@Override
	public List<String> selectPivotQueryTarget(Map<String, Object> param) throws Exception {

		String chartType = param.get("chartType") != null ? String.valueOf(param.get("chartType")) : "";

		List<String> resultList = null;

		if ("daily".equals(chartType)) {
			resultList = baseDao.selectList("sehati.inf.translog.selStartDays", param);
		}
		else if ("monthly".equals(chartType)) {
			resultList = baseDao.selectList("sehati.inf.translog.selStartMonths", param);			
		}
		else {
			resultList = baseDao.selectList("sehati.inf.translog.selStartDays", param);						
		}
		
		return resultList;
	}
}
