package de.lechner.cbudget.entities;

    public class Anlage {

 
        private Integer id;
        private String name;
        private String beschreibung;
        private String rendite;
        private Integer rule_id;
        

        public Anlage(Integer id, String name, String beschreibung, String rendite, Integer rule_id) {
            super();
            this.id = id;
            this.name = name;
            this.beschreibung = beschreibung;
            this.rendite = rendite;
            this.rule_id = rule_id;
        }
        
        
        
        public Anlage() {
          
        }



        public Integer getId() {
            return id;
        }
        public void setId(Integer id) {
            this.id = id;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getBeschreibung() {
            return beschreibung;
        }
        public void setBeschreibung(String beschreibung) {
            this.beschreibung = beschreibung;
        }
        public String getRendite() {
            return rendite;
        }
        public void setRendite(String rendite) {
            this.rendite = rendite;
        }
        public Integer getRule_id() {
            return rule_id;
        }
        public void setRule_id(Integer rule_id) {
            this.rule_id = rule_id;
        }
      
        
}