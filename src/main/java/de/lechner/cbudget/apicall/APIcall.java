package de.lechner.cbudget.apicall;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import de.lechner.cbudget.entities.Anlage;
import de.lechner.cbudget.entities.Konto;
import de.lechner.cbudget.entities.Rendite;
import de.lechner.cbudget.infrastructure.SimpleService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class APIcall {
    @Value("${budgetserver.host:localhost}")
    private String host;
    @Value("${budgetserver.port:8092}")
    private String port;
    private static final Logger LOG = LoggerFactory.getLogger(SimpleService.class);
    RestTemplate restTemplate = new RestTemplate();
    public List<Anlage> getAllAnalgen() {
     //   LOG.info("Start getAllAnlagen");
     //   LOG.info("Host = "+ host);
     //   LOG.info("Port = "+ port);
     if (host == null ) {host = "localhost";}
     if (port == null ) {port = "8092";}
        List<Anlage> list = new ArrayList<Anlage>();

        UriComponents uriComponents = UriComponentsBuilder.newInstance().scheme("http").host(host).port(port)
                .path("//anlagen").build();
        String allAnlagen = uriComponents.toUriString();
        ResponseEntity<Anlage[]> response = restTemplate.getForEntity(allAnlagen, Anlage[].class);
        if (response.hasBody()) {

            Anlage[] anlage = response.getBody();
            Collections.addAll(list, anlage);
        }
        return list;
    }
    
    public List<Konto> getAllKonten() {
      //  LOG.info("Start getAllAnlagen");
       // LOG.info("Host = "+ host);
       // LOG.info("Port = "+ port);
        if (host == null ) {host = "localhost";}
        if (port == null ) {port = "8092";}
        List<Konto> list = new ArrayList<Konto>();
        UriComponents uriComponents = UriComponentsBuilder.newInstance().scheme("http").host(host).port(port)
                .path("//kontos").build();
        String allKonten = uriComponents.toUriString();
        ResponseEntity<Konto[]> response = restTemplate.getForEntity(allKonten, Konto[].class);
        if (response.hasBody()) {

            Konto[] konto = response.getBody();
            Collections.addAll(list, konto);
        }
        return list;
    }
    
    public String getAktKontostand( Integer konto, String enddate) {
       // LOG.info("Start getAktKonotstand");
           
        RestTemplate restTemplate = new RestTemplate();
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme("http").host(host).port(port).path("/transaction_get_sum")
                .queryParam("startdate", "2001-01-01")
                .queryParam("enddate", enddate)
                .queryParam("konto", konto)
                .build();
        String uri=uriComponents.toUriString();
      //  LOG.debug("URI = "+ uri);
        String result = restTemplate.getForObject(uri, String.class);
        if (result == null)
        {
        	result = "0.0";
        }
        return result;
    }
    
    public String getErtrag( Integer konto, String startdate, String enddate) {
        // LOG.info("Start getAktKonotstand");
            
         RestTemplate restTemplate = new RestTemplate();
         UriComponents uriComponents = UriComponentsBuilder.newInstance()
                 .scheme("http").host(host).port(port).path("/transaction_get_sum")
                 .queryParam("startdate", startdate)
                 .queryParam("enddate", enddate)
                 .queryParam("konto", konto)
                 .queryParam("name", "Ertrag")
                 .queryParam("categorie", "42")
                 .build();
         String uri=uriComponents.toUriString();
        // LOG.debug("URI = "+ uri);
         String result = restTemplate.getForObject(uri, String.class);
         if (result == null)
         {
         	result = "0.0";
         }
         return result;
     }
    
    public String getErtragWithRuleID( Integer konto, String startdate, String enddate, Integer ruleId) {
         RestTemplate restTemplate = new RestTemplate();
         UriComponents uriComponents = UriComponentsBuilder.newInstance()
                 .scheme("http").host(host).port(port).path("/transaction_get_sum")
                 .queryParam("startdate", startdate)
                 .queryParam("enddate", enddate)
                 .queryParam("konto", konto)
                 .queryParam("ruleid", ruleId)
                 .build();
         String uri=uriComponents.toUriString();
         //LOG.info("getErtragWithRuleID with konto");
         //LOG.info("URI = "+ uri);
         String result = restTemplate.getForObject(uri, String.class);
         if (result == null)
         {
            result = "0.0";
         }
         return result;
     }
    
    //same without konto
    public String getErtragWithRuleID(String startdate, String enddate, Integer ruleId) {
        RestTemplate restTemplate = new RestTemplate();
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme("http").host(host).port(port).path("/transaction_get_sum")
                .queryParam("startdate", startdate)
                .queryParam("enddate", enddate)
                .queryParam("ruleid", ruleId)
                .build();
        String uri=uriComponents.toUriString();
        //LOG.info("getErtragWithRuleID without konto");
        //LOG.info("URI = "+ uri);
        String result = restTemplate.getForObject(uri, String.class);
        if (result == null)
        {
           result = "0.0";
        }
        return result;
    }
    
    public Rendite getRenditeByDateAndName( Integer konto, String date) {
        // LOG.info("Start getAktKonotstand");
            
         RestTemplate restTemplate = new RestTemplate();
         Rendite rendite = new Rendite();
         UriComponents uriComponents = UriComponentsBuilder.newInstance()
                 .scheme("http").host(host).port(port).path("/renditeByDateAndKonto")
                 .queryParam("date", date)
                 .queryParam("konto", konto)
                 .build();
         String uri=uriComponents.toUriString();
         //LOG.info("URI = "+ uri);
         ResponseEntity<Rendite> response = restTemplate.getForEntity(uri, Rendite.class);
         if (response.hasBody()) {

             rendite = response.getBody();
             
         }
         else
         {
        	 rendite= null;
         }
         if (rendite == null)
         {
        	// LOG.info("Rendite for " + konto + " and "+ date + " not found");
         }
         else
         {
        // LOG.info("Get Rendite = " + rendite.getKonto());
         }
         return rendite;
     }
    
    public void insertRendite( Integer konto, Double value, String datum, Double amount, Integer renditeId) {
       Rendite rendite = new Rendite();
       if (renditeId != null)
       {
       rendite.setId(renditeId);
       }
       rendite.setKonto(konto);
       rendite.setValue(value);
       rendite.setAmount(amount);
       rendite.setDirty(0);
       try {
    	   rendite.setDatum(new SimpleDateFormat("yyyy-MM-dd").parse(datum));
			} catch (Exception e)
			{
				e.printStackTrace();
			}	
    	
         RestTemplate restTemplate = new RestTemplate();
         UriComponents uriComponents = UriComponentsBuilder.newInstance()
                 .scheme("http").host(host).port(port).path("/rendite")
                 .build();
         String uri=uriComponents.toUriString();  
        // LOG.debug("URI = "+ uri);
     	 restTemplate.postForEntity(uri,rendite, Rendite.class);
        
     }
}
