package de.lechner.cbudget.entities;

import java.util.Date;


public class Rendite {

	private Integer id;
	private Integer konto;
	private Double value;
	private Date datum;
	private Double amount;
	private Integer dirty;
	
	
	
    public Rendite() {
       
    }
    
    public Rendite(Integer id, Integer konto, Double value, Date datum, Double amount, Integer dirty) {
        super();
        this.id = id;
        this.konto = konto;
        this.value = value;
        this.datum = datum;
        this.amount = amount;
        this.dirty = dirty;
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
    public Double getValue() {
        return value;
    }
    public void setValue(Double value) {
        this.value = value;
    }
    public Date getDatum() {
        return datum;
    }
    public void setDatum(Date datum) {
        this.datum = datum;
    }
    public Double getAmount() {
        return amount;
    }
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getDirty() {
        return dirty;
    }

    public void setDirty(Integer dirty) {
        this.dirty = dirty;
    }
    
}
