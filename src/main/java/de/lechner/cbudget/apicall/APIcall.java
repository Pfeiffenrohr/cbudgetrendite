package de.lechner.cbudget.apicall;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import de.lechner.cbudget.entities.Anlage;
import de.lechner.cbudget.entities.Konto;
import de.lechner.cbudget.infrastructure.SimpleService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class APIcall {
    @Value("${budgetserver.host}")
    private String host;
    @Value("${budgetserver.port}")
    private String port;
    private static final Logger LOG = LoggerFactory.getLogger(SimpleService.class);
    
    public List<Anlage> getAllAnalgen() {
        LOG.info("Start getAllAnlagen");
        LOG.info("Host = "+ host);
        LOG.info("Port = "+ port);        
        List<Anlage> list = new ArrayList<Anlage>();
        RestTemplate restTemplate = new RestTemplate();
        UriComponents uriComponents = UriComponentsBuilder.newInstance().scheme("http").host(host).port(port)
                .path("//anlagen").build();
        String allAnlagen = uriComponents.toUriString();
        ResponseEntity<Anlage[]> response = restTemplate.getForEntity(allAnlagen, Anlage[].class);
        if (response.hasBody()) {

            Anlage[] anlage = response.getBody();
            for (int i = 0; i < anlage.length; i++) {
                list.add(anlage[i]);
            }
        }
        return list;
    }
    
    public List<Konto> getAllKonten() {
        LOG.info("Start getAllAnlagen");
        LOG.info("Host = "+ host);
        LOG.info("Port = "+ port);        
        List<Konto> list = new ArrayList<Konto>();
        RestTemplate restTemplate = new RestTemplate();
        UriComponents uriComponents = UriComponentsBuilder.newInstance().scheme("http").host(host).port(port)
                .path("//kontos").build();
        String allKonten = uriComponents.toUriString();
        ResponseEntity<Konto[]> response = restTemplate.getForEntity(allKonten, Konto[].class);
        if (response.hasBody()) {

            Konto[] konto = response.getBody();
            for (int i = 0; i < konto.length; i++) {
                list.add(konto[i]);
            }
        }
        return list;
    }
    
    public String getAktKontostand( Integer konto, String enddate) {
        LOG.info("Start getAktKonotstand");
           
        RestTemplate restTemplate = new RestTemplate();
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme("http").host(host).port(port).path("/transaction_get_sum")
                .queryParam("startdate", "2011-01-01")
                .queryParam("enddate", enddate)
                .queryParam("konto", konto)
                .build();
        String uri=uriComponents.toUriString();
        String result = restTemplate.getForObject(uri, String.class);
        return result;
    }
}
