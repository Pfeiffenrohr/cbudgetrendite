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
		Calendar calAnfang = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calend = Calendar.getInstance();
		Calendar calbegin = Calendar.getInstance();
		calAnfang.add(Calendar.YEAR, -6);
		List<Anlage> vecAnlagen = apicall.getAllAnalgen();
		List<Konto> vecKonten = apicall.getAllKonten();
		
        calend.add(Calendar.DATE, -1);
        calbegin.add(Calendar.YEAR, -1);
        while (calend.after(calAnfang))
        {
        	LOG.info("Berechne Ertrag von " + formatter.format(calbegin.getTime()) +" bis " + 
        formatter.format(calend.getTime()));
		renditeProTag(vecAnlagen, vecKonten, calend, calbegin);
		calend.add(Calendar.DATE, -1);
		calbegin.add(Calendar.DATE, -1);
        }
        
	}

	private void renditeProTag(List<Anlage> vecAnlagen, List<Konto> vecKonten, Calendar calend, Calendar calbegin) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calakt = Calendar.getInstance();
		String startdate = formatter.format(calbegin.getTime());
		String enddate = formatter.format(calend.getTime());
        
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
					LOG.info("Berechne Konto " + vecKonten.get(j).getKontoname() + " fuer Anlage "
							+ vecAnlagen.get(i).getName());
					int count = 1;
					int sumcount = 0;
					Double sum = 0.0;
					Double amount =0.0;
					Boolean gotAmount=false;
					calakt = (Calendar) calend.clone();
					if (apicall.getRenditeByDateAndName(vecKonten.get(j).getId(), enddate) == null) {
						while (calakt.after(calbegin)) {
							// Hole den aktuellen Kontostand
							String dynEnddate = formatter.format(calakt.getTime());
							String strKontostand = apicall.getAktKontostand(vecKonten.get(j).getId(), dynEnddate);
							Double kontostand = new Double(strKontostand);
							if (! gotAmount) {
							    amount=kontostand;
							    gotAmount=true;
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
							//LOG.info("Count = " + count);
							//continue;
						}
						Double dayAvg = 0.0;
						if (sumcount != 0.0)
						{
						  dayAvg = sum / sumcount;
						}
						String strErtrag = apicall.getErtrag(vecKonten.get(j).getId(), startdate, enddate);
						Double ertrag = new Double(strErtrag);
						//LOG.info("Ertrag " + strErtrag);
						Double ertragProjahr = ertrag * (365.0 / count);
						//LOG.info("ErtragproJahr =" + ertragProjahr);
						Double rendite =0.0;
						if (dayAvg != 0.0) {
							//LOG.info("dayAvg =" + dayAvg);
							rendite = (ertragProjahr * 100) / dayAvg;
						}
							
							//LOG.info("Rendite =" + rendite);
							if (rendite > -900)
							{
							apicall.insertRendite(vecKonten.get(j).getId(), rendite, enddate,amount);
							}
						//} else {
						//	LOG.info("Keine Rendite,. da kein Ertrag ");
						// }

					
				} else {
				/*	LOG.info("Already computed"  + vecKonten.get(j).getKontoname() + " fuer Anlage "
							+ vecAnlagen.get(i).getName());*/
				}
				}
			}

		}

	}

}
