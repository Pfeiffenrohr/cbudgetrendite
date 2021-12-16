package de.lechner.cbudget.entities;
public class Konto {

    private Integer id;
    private String kontoname;
    private String hidden;
    private Double upperlimit;
    private Double lowerlimit;
    private String description;
    private String mode;    
    
    public Konto(Integer id, String kontoname, String hidden,  Double upperlimit, Double lowerlimit, String description, String mode) {
        super();
        this.id = id;
        this.kontoname = kontoname;
        this.hidden = hidden;
        this.upperlimit = upperlimit;
        this.lowerlimit = lowerlimit;
        this.description = description;
        this.mode = mode;
    }
    
    public Konto() {
        
    }
    
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getKontoname() {
        return kontoname;
    }
    public void setKontoname(String kontoname) {
        this.kontoname = kontoname;
    }
    
    public String getHidden() {
        return hidden;
    }

    public void setHidden(String hidden) {
        this.hidden = hidden;
    }

    public Double getUpperlimit() {
        return upperlimit;
    }
    public void setUpperlimit(Double upperlimit) {
        this.upperlimit = upperlimit;
    }
    public Double getLowerlimit() {
        return lowerlimit;
    }
    public void setLowerlimit(Double lowerlimit) {
        this.lowerlimit = lowerlimit;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getMode() {
        return mode;
    }
    public void setMode(String mode) {
        this.mode = mode;
    }   

}