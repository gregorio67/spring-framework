
import java.util.List;
import java.util.Map;

import cmn.util.dao.CamelMap;


public interface ChartService {
	
	public List<CamelMap> selectChartData(Map<String, Object> param) throws Exception;
	
	public List<String> selectPivotQueryTarget(Map<String, Object> param) throws Exception;

}
