
import java.util.Map;


public interface ClientWsService {

	public abstract <T> T  callService(String infId, String serviceName, Map<String, Object> params, Map<String, Object> transLog) throws Exception;
}
