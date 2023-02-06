package de.lechner.cbudget.apicall;

import de.lechner.cbudget.entities.Konto;
import de.lechner.cbudget.entities.OrderRendite;
import de.lechner.cbudget.entities.Rendite;
import de.lechner.cbudget.entities.RenditeBatch;
import de.lechner.cbudget.infrastructure.SimpleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class APIcallBatch {
    @Value("${budgetserver.host}")
    private String host;
    @Value("${budgetserver.port}")
    private String port;
    private static final Logger LOG = LoggerFactory.getLogger(SimpleService.class);
    RestTemplate restTemplate = new RestTemplate();

    public OrderRendite getOrderRendite() {
        if (host == null) {
            host = "localhost";
        }
        if (port == null) {
            port = "8092";
        }
        List<OrderRendite> list = new ArrayList<OrderRendite>();
        UriComponents uriComponents = UriComponentsBuilder.newInstance().scheme("http").host(host).port(port)
                .path("//allOrderRendite").build();
        String allOrderRendite = uriComponents.toUriString();
        ResponseEntity<OrderRendite[]> response = restTemplate.getForEntity(allOrderRendite, OrderRendite[].class);
        if (response.hasBody()) {

            OrderRendite[] orderRendite = response.getBody();
            Collections.addAll(list, orderRendite);
        }
        return list.get(0);
    }
    public void insertRenditeBatch( Integer konto, Double ertrag, Double ertragProzent, Double wertProTag) {
        RenditeBatch renditebatch = new RenditeBatch();

        renditebatch.setId(konto);
        renditebatch.setErtrag(ertrag);
        renditebatch.setKonto(konto);
        renditebatch.setErtragProzent(ertragProzent);
        renditebatch.setWertProTag(wertProTag);
        RestTemplate restTemplate = new RestTemplate();
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme("http").host(host).port(port).path("/renditebatch")
                .build();
        String uri=uriComponents.toUriString();
        // LOG.debug("URI = "+ uri);
        restTemplate.postForEntity(uri,renditebatch, RenditeBatch.class);

    }
    public void updateOrderRendite( OrderRendite orderrendite) {

        RestTemplate restTemplate = new RestTemplate();
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme("http").host(host).port(port).path("/orderRendite")
                .build();
        String uri=uriComponents.toUriString();
        // LOG.debug("URI = "+ uri);
        restTemplate.postForEntity(uri,orderrendite, OrderRendite.class);

    }
}


