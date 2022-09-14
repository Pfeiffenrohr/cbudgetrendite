package de.lechner.cbudget.apicall;


import de.lechner.cbudget.entities.Anlage;
import de.lechner.cbudget.entities.Konto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class APICallTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private APIcall apicall = new APIcall();

    @Test
    void getAllAnalgenTest() {
        Anlage anlage = new Anlage();
        anlage.setId(1);
        anlage.setName("Anlage");

        Anlage [] allAnlagen = new Anlage[1];
        allAnlagen[0] = anlage;

        Mockito.when(restTemplate.getForEntity("http://localhost:8092/anlagen", Anlage[].class))
          .thenReturn(new ResponseEntity(allAnlagen, HttpStatus.OK));

        List <Anlage> returnedAnlage = apicall.getAllAnalgen();
        assertThat(returnedAnlage.get(0).getId()).isEqualTo(1);
        assertThat(returnedAnlage.get(0).getName()).isEqualTo("Anlage");
    }
    @Test
    void getAllKontenTest() {
        Konto konto = new Konto();
        konto.setId(1);
        konto.setKontoname("Name");

        Konto[] allKonto = new Konto[1];
        allKonto[0] = konto;

        Mockito.when(restTemplate.getForEntity("http://localhost:8092/kontos", Konto[].class))
                .thenReturn(new ResponseEntity(allKonto, HttpStatus.OK));

        List <Konto> returnedKonto = apicall.getAllKonten();
        assertThat(returnedKonto.get(0).getId()).isEqualTo(1);
        assertThat(returnedKonto.get(0).getKontoname()).isEqualTo("Name");
    }
    void getErtragWithRuleIDTest() {

        Mockito.when(restTemplate.getForObject(Mockito.anyString(),String.class))
                        .thenReturn(new String("500" ));

        String result = apicall.getErtragWithRuleID(23,"2022-04-03","2022-04-04",3);
        assertThat(result.equals("500"));

    }
}
