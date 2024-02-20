package de.lechner.cbudget.rendite;

import de.lechner.cbudget.apicall.APIcall;
import de.lechner.cbudget.entities.Anlage;
import de.lechner.cbudget.entities.Konto;
import de.lechner.cbudget.entities.RenditeBatch;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Calendar;

@ExtendWith(MockitoExtension.class)
public class RenditeKontoAnlageTest {

    //private RestTemplate restTemplate;


    @Mock
    @Autowired
    private APIcall apicall;

    @Autowired
    @InjectMocks
    RenditeKontoAnlage renditeKontoAnlage;

    @Test
    void renditeKontoTest() {
        Anlage anlage = new Anlage();
        anlage.setRendite("Y");
        anlage.setName("P2p");
        Konto konto = new Konto();
        konto.setId(1);
        konto.setKontoname("test");
        konto.setMode("Fonds");
        konto.setRule_id(3);

        Calendar calbegin = Calendar.getInstance();
        Calendar calend = Calendar.getInstance();
        calbegin.add(Calendar.DATE, -5);

        RenditeKontoAnlage renditeKontoAnlage = new RenditeKontoAnlage();
        Mockito.when(apicall.getAktKontostand(Mockito.anyInt(),Mockito.anyString())).thenReturn("100","101","102","103","104");
        Mockito.when(apicall.getErtragWithRuleID(Mockito.anyString(),Mockito.anyString(),Mockito.anyInt())).thenReturn("4");
        RenditeBatch  rb = renditeKontoAnlage.computeRendite(apicall,anlage,konto,calend,calbegin);
        System.out.println("Done");
        assertTrue(rb.getErtrag()==4.0);
        //assertThat(5.0,rb.getErtrag());
    }
}
