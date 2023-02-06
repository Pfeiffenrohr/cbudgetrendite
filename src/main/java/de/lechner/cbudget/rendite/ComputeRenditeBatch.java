package de.lechner.cbudget.rendite;

import de.lechner.cbudget.apicall.APIcall;
import de.lechner.cbudget.apicall.APIcallBatch;
import de.lechner.cbudget.entities.Anlage;
import de.lechner.cbudget.entities.Konto;
import de.lechner.cbudget.entities.OrderRendite;
import de.lechner.cbudget.entities.Rendite;
import de.lechner.cbudget.infrastructure.SimpleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

@Service
public class ComputeRenditeBatch {
    @Autowired
    APIcall apicall = new APIcall();
    @Autowired
    APIcallBatch apicallbatch = new APIcallBatch();

    private static final Logger LOG = LoggerFactory.getLogger(SimpleService.class);

    public void compute() {
        OrderRendite orderRendite = apicallbatch.getOrderRendite();
        if (orderRendite.getFinished() == 0 )
        {
            LOG.info("Starte Berechung Rendite Batch");
            computeRendite(orderRendite);
            orderRendite.setFinished(1);
            apicallbatch.updateOrderRendite(orderRendite);
            LOG.info("Berechung Rendite Batch fertig");
        }

    }

    private void computeRendite(OrderRendite orderRendite) {
        List<Anlage> vecAnlagen = apicall.getAllAnalgen();
        List<Konto> vecKonten = apicall.getAllKonten();
        Calendar calbegin = Calendar.getInstance();
        calbegin.setTime(orderRendite.getStartdate());
        Calendar calend = Calendar.getInstance();
        calend.setTime(orderRendite.getEnddate());
        renditeProTag(vecAnlagen,vecKonten,calend,calbegin);
    }

    private Integer renditeProTag(List<Anlage> vecAnlagen, List<Konto> vecKonten, Calendar calend, Calendar calbegin) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calakt = Calendar.getInstance();
        String startdate = formatter.format(calbegin.getTime());
        String enddate = formatter.format(calend.getTime());
        int anzahl =0;
        for (int i = 0; i < vecKonten.size(); i++) {

            // LOG.info("Konto " + vecKonten.get(i).getKontoname());
        }
        for (int i = 0; i < vecAnlagen.size(); i++) {
            if (vecAnlagen.get(i).getRendite().equals("N")) {
                // LOG.debug("Keine Berechnung Anlage "+vecAnlagen.get(i).getName() );
                continue;
            }
            for (int j = 0; j < vecKonten.size(); j++) {

                if (vecKonten.get(j).getMode().equals(vecAnlagen.get(i).getName())) {
                    //LOG.info("Berechne Konto " + vecKonten.get(j).getKontoname() + " fuer Anlage "
                    // + vecAnlagen.get(i).getName());
                    int count = 1;
                    int sumcount = 0;
                    Double sum = 0.0;
                    Double amount = 0.0;
                    Boolean gotAmount = false;
                    Integer renditeId;
                    calakt = (Calendar) calend.clone();
                    Rendite renditecall = apicall.getRenditeByDateAndName(vecKonten.get(j).getId(), enddate);
                 /*  if (vecKonten.get(j).getId()==27)
                   {
                       LOG.info("Renditecall = "+ renditecall.getAmount());
                   }*/



                        while (calakt.after(calbegin)) {
                            // Hole den aktuellen Kontostand
                            String dynEnddate = formatter.format(calakt.getTime());
                            String strKontostand = apicall.getAktKontostand(vecKonten.get(j).getId(), dynEnddate);
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
                        if ((Integer)vecKonten.get(j).getRule_id() == null ||  (Integer)vecKonten.get(j).getRule_id() == -1 || (Integer)vecKonten.get(j).getRule_id() == 0 )
                        {
                            ruleID = vecAnlagen.get(i).getRule_id();
                            // System.out.println("Rule_id von Anlage");
                            // System.out.println("Konto id = "+j + " Name "+ vecKonten.get(j).getKontoname());
                            // System.out.println("RuleID from Konto: " + vecKonten.get(j).getRule_id());
                            ruleFromKonto=false;
                        }
                        else
                        {

                            ruleID = vecKonten.get(j).getRule_id();
                            ruleFromKonto=true;
                            //   System.out.println("Rule_id von Konto");
                            //   System.out.println("Rule_id =" +konto.get("rule_id"));

                        }
                        //System.out.println ("Rule_id = " + ruleID);
                        String strErtrag="";
                        if (! ruleFromKonto) {
                            strErtrag = apicall.getErtragWithRuleID(vecKonten.get(j).getId(), startdate, enddate,ruleID);

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
                        if (vecAnlagen.get(i).getName().equals("P2p"))
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

                        //LOG.info("Rendite =" + rendite);
                        if (rendite > -900) {
                           // apicall.insertRendite(vecKonten.get(j).getId(), rendite, enddate, amount, renditeId);
                            anzahl ++;
                            // LOG.info("Insert Rebdite  Konto: "+ vecKonten.get(j).getKontoname()+" Rendite:" +rendite +" Ertrag:"+strErtrag +" Durchschnitt "+dayAvg);
                            //rendite = ertragProzent, Ertrag = ertrag, dayAvg = wertProTag
                             apicallbatch.insertRenditeBatch(vecKonten.get(j).getId(),ertrag,rendite,dayAvg);
                        }

                }
            }

        }
        return anzahl;
    }
}
