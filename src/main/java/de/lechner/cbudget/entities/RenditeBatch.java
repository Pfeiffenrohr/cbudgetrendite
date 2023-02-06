package de.lechner.cbudget.entities;



    public class RenditeBatch {
        private Integer id;
        private Integer konto;
        private Double wertProTag;
        private Double ertrag;
        private Double ertragProzent;

        public RenditeBatch(Integer id, Integer konto, Double wertProTag, Double ertrag, Double ertragProzent) {
            this.id = id;
            this.konto = konto;
            this.wertProTag = wertProTag;
            this.ertrag = ertrag;
            this.ertragProzent = ertragProzent;
        }
        public RenditeBatch() {

        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getKonto() {
            return konto;
        }

        public void setKonto(Integer konto) {
            this.konto = konto;
        }

        public Double getWertProTag() {
            return wertProTag;
        }

        public void setWertProTag(Double wertProTag) {
            this.wertProTag = wertProTag;
        }

        public Double getErtrag() {
            return ertrag;
        }

        public void setErtrag(Double ertrag) {
            this.ertrag = ertrag;
        }

        public Double getErtragProzent() {
            return ertragProzent;
        }

        public void setErtragProzent(Double ertragProzent) {
            this.ertragProzent = ertragProzent;
        }
    }
