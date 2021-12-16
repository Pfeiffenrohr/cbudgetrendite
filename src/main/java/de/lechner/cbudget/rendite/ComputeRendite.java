package de.lechner.cbudget.rendite;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.lechner.cbudget.apicall.APIcall;
import de.lechner.cbudget.entities.Anlage;
import de.lechner.cbudget.entities.Konto;
import de.lechner.cbudget.infrastructure.SimpleService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ComputeRendite {
    @Autowired
    APIcall apicall = new APIcall();
    
    private static final Logger LOG = LoggerFactory.getLogger(SimpleService.class);
    
    public void rendite() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String akt_datum = formatter.format(cal.getTime());
        cal.set(Calendar.DAY_OF_YEAR, 1);
        String start_datum = formatter.format(cal.getTime());
        List<Anlage> vecAnlagen= apicall.getAllAnalgen();
        List<Konto>  vecKonten = apicall.getAllKonten();
        for (int i=0; i<vecKonten.size();i++)
        {
                      
           // LOG.info("Konto " + vecKonten.get(i).getKontoname());
        }
        for (int i=0; i<vecAnlagen.size();i++)
        {
            if (vecAnlagen.get(i).getRendite().equals("N"))
            {
              //  LOG.debug("Keine Berechnung für Anlage "+vecAnlagen.get(i).getName() );
                continue;
            }
            for (int j=0; j<vecKonten.size();j++) {
                
                if (vecKonten.get(j).getMode().equals(vecAnlagen.get(i).getName()))
                {
                    LOG.info("Berechne Konto "+ vecKonten.get(j).getKontoname() +" für Anlage "+ vecAnlagen.get(i).getName());
                }
            }
           

        }
    }

}
