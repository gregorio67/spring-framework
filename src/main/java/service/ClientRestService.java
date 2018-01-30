package sehati.inf.rest.service;

import java.util.Map;

public interface ClientRestService {

	public abstract <T> T  callRestService(String systemId, String infId, String serviceName, Map<String, Object> params, Map<String, Object> transLog) throws Exception;
}
