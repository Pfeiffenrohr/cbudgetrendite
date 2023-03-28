package de.lechner.cbudget.rendite;

import de.lechner.cbudget.apicall.APIcall;
import de.lechner.cbudget.entities.Anlage;
import de.lechner.cbudget.entities.Konto;
import de.lechner.cbudget.entities.Rendite;
import de.lechner.cbudget.entities.RenditeBatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;



@Service
public class RenditeKontoAnlage {
   // @Autowired
   // APIcall apicall;
    public RenditeBatch computeRendite(APIcall apicall, Anlage anlage, Konto konto, Calendar calend, Calendar calbegin) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calakt = Calendar.getInstance();
        calakt = (Calendar) calend.clone();
        String enddate = formatter.format(calend.getTime());
        String startdate = formatter.format(calbegin.getTime());
        int count = 1;
        int sumcount = 0;
        Double sum = 0.0;
        Double amount = 0.0;
        Boolean gotAmount = false;
        while (calakt.after(calbegin)) {
            // Hole den aktuellen Kontostand
            String dynEnddate = formatter.format(calakt.getTime());
            String strKontostand = apicall.getAktKontostand(konto.getId(), dynEnddate);
            Double kontostand = new Double(strKontostand);
            if (!gotAmount) {
                amount = kontostand;
                gotAmount = true;
            }
            if (kontostand > -0.001 && kontostand < 0.001) {
                calakt.add(Calendar.DATE, -1);
                continue;
            }
            // LOG.info("Aktkontostand = " +kontostand );
            calakt.add(Calendar.DATE, -1);
            sum = sum + (kontostand * count);
            sumcount = sumcount + count;
            count++;
        }
        if (count == 1) {
            // LOG.info("Count = " + count);
            // continue;
        }
        Double dayAvg = 0.0;
        if (sumcount != 0.0) {
            dayAvg = sum / sumcount;
        }
        //String ruleErtrag="";
        Integer ruleID;
        Boolean ruleFromKonto;
        if (konto.getRule_id() == null || konto.getRule_id() == -1 || konto.getRule_id() == 0 )
        {
            ruleID = anlage.getRule_id();
            ruleFromKonto=false;
        }
        else
        {
            ruleID = konto.getRule_id();
            ruleFromKonto=true;
        }
        //System.out.println ("Rule_id = " + ruleID);
        String strErtrag="";
        if (! ruleFromKonto) {
            strErtrag = apicall.getErtragWithRuleID(konto.getId(), startdate, enddate,ruleID);

        }
        else
        {
            strErtrag = apicall.getErtragWithRuleID(startdate, enddate,ruleID);
        }
        Double ertrag = new Double(strErtrag);
        // String strErtragold = apicall.getErtrag(vecKonten.get(j).getId(), startdate, enddate);
        //LOG.info("Datum: "+ enddate);
        //LOG.info("Konto: "+ vecKonten.get(j).getKontoname());
        //LOG.info("Ertrag alt: "+strErtragold);
        //LOG.info("Ertrag neu: "+strErtrag);
        //Double ertrag =db.getKategorienAlleSummeWhere(startdate, enddate, where);
        //LOG.info("Ertrag " + strErtrag);
        double ertragProjahr=0.0;
        if (anlage.getName().equals("P2p"))
        {
            ertragProjahr = ertrag * (365.0 / count);
        }
        else
        {
            ertragProjahr = ertrag;
        }
        // LOG.info("ErtragproJahr =" + ertragProjahr);
        Double rendite = 0.0;
        if (dayAvg != 0.0) {
            // LOG.info("dayAvg =" + dayAvg);
            rendite = (ertragProjahr * 100) / dayAvg;
        }
        RenditeBatch rb = new RenditeBatch();
        rb.setKonto(konto.getId());
        rb.setErtrag(ertrag);
        rb.setErtragProzent(rendite);
        rb.setWertProTag(dayAvg);
        //LOG.info("Rendite =" + rendite);
       // if (rendite > -900) {
            // apicall.insertRendite(vecKonten.get(j).getId(), rendite, enddate, amount, renditeId);

            // LOG.info("Insert Rebdite  Konto: "+ vecKonten.get(j).getKontoname()+" Rendite:" +rendite +" Ertrag:"+strErtrag +" Durchschnitt "+dayAvg);
            //rendite = ertragProzent, Ertrag = ertrag, dayAvg = wertProTag
         //   apicallbatch.insertRenditeBatch(vecKonten.get(j).getId(),ertrag,rendite,dayAvg);
        return rb;
    }
}
