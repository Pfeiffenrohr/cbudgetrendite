package de.lechner.cbudget.infrastructure;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
    
  //TODO Klasse ist notwendig, da ich nicht wei�, wie ich einen String in JPA ausf�hre :( Sehr schmutzig
    public class DBHelper {
        
        public Connection con = null;
        public boolean debug=true;
        
        public boolean dataBaseConnect(String username,String password, String connectString) {
            if (debug) System.out.println("Verbinde mich zur Datenbank");
               try {
                   try {
                       //Class.forName("org.gjt.mm.mysql.Driver").newInstance(); // DB-
                                                                               // Treiber
                                                                       // laden
                       Class.forName("org.postgresql.Driver").newInstance();
                                                                               
                   } catch (Exception E) {
                       System.err
                               .println("Konnte MySQL Datenbank-Treiber nicht laden!");
                       return false;
                   }
                   //String url = "jdbc:mysql://192.168.2.8/budget_test";
                   //con = DriverManager.getConnection(connectString, username, password); // Verbindung
                   if (debug) System.out.println("Try to connect with "+connectString);                                            // herstellen
                   
                        //con = DriverManager.getConnection("jdbc:postgresql://192.168.2.28:5432/budget", "budget", "budget");
                       con = DriverManager.getConnection(connectString, username, password);
                   //DriverManager.getConnection("jdbc:postgresql://localhost:5432/budget?user=budget&password=");
                       
                   if (debug) System.out.println("Verbindung erstellt");
               } catch (Exception e) {
                   e.printStackTrace();
                   System.err.println("Treiber fuer postgres nicht gefunden");
                   return false;
               }
               return true;
           }
        
        /**
         * 
         * @param rule_id
         * @return
         */
        public String getRuleCommand(Integer rule_id) {
            Vector vec = new Vector();
            try {

                PreparedStatement stmt;
                ResultSet res = null;
                stmt = con
                        .prepareStatement("select command from rules where rule_id = "+rule_id);
                if (debug) System.out.println("select command from rules where rule_id = "+rule_id);
                res = stmt.executeQuery();
                while (res.next()) {                    
                    return( (String) res.getString("command"));
                    
                }
            } catch (SQLException e) {
                System.err.println("Konnte Select-Anweisung nicht ausführen" + e);
                return "";
            }
            if (debug) System.out.println("Select-Anweisung ausgeführt");
            // return summe/(float)getAnz(tag,monat,year);
            return "";
        }
        
        public double getKategorienAlleSummeWhere(String startdatum, String enddatum,String where) {
            double sum=0.0;
            try {
                PreparedStatement stmt;
                ResultSet res = null;
                String str_stm="select sum(wert) as summe from transaktionen where datum >=  '"+startdatum+"'"+" and datum <=  '"+enddatum+"'"+" and "+where;
                if (debug)System.out.println(str_stm);
                stmt = con
                .prepareStatement(str_stm);
               res = stmt.executeQuery();
            while (res.next()) {
                sum=(res.getDouble("summe"));
            }
            } catch (SQLException e) {
                System.err.println("Konnte Select-Anweisung nicht ausführen" + e);
                return sum;
            }
            if (debug) System.out.println("Select-Anweisung ausgeführt");
            // return summe/(float)getAnz(tag,monat,year);
            return sum;
        }
        
        public boolean closeConnection() {
            if (con != null) {
                try {
                    con.close();
                    if (debug) System.out.println("Verbindung beendet");
                } catch (Exception e) {
                    System.err.println("Konnte Verbindung nicht beenden!!");
                    return false;
                }
            }
            return true;
        }

    }

