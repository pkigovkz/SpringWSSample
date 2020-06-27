package kz.gov.pki.sample.ws;

import kz.gov.pki.sample.soap.GetShinobiRequest;
import kz.gov.pki.sample.soap.GetShinobiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class ShinobiEndpoint {

    private static final String NAMESPACE_URI = "http://pki.gov.kz/sample/soap";

    private ShinobiRepository repository;

    @Autowired
    public ShinobiEndpoint(ShinobiRepository repository) {
        this.repository = repository;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getShinobiRequest")
    @ResponsePayload
    public GetShinobiResponse getShinobi(@RequestPayload GetShinobiRequest request) {
        GetShinobiResponse response = new GetShinobiResponse();
        response.setShinobi(repository.fetchShinobi(request.getName()));
        return response;
    }

}
