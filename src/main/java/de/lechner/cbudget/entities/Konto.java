package de.lechner.cbudget.entities;
public class Konto {

    private Integer id;
    private String kontoname;
    private String hidden;
    private Double upperlimit;
    private Double lowerlimit;
    private String description;
    private String mode;
    private Integer rule_id;
    
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
    
    public Konto(Integer id, String kontoname, String hidden, Double upperlimit, Double lowerlimit, String description,
            String mode, Integer rule_id) {
        super();
        this.id = id;
        this.kontoname = kontoname;
        this.hidden = hidden;
        this.upperlimit = upperlimit;
        this.lowerlimit = lowerlimit;
        this.description = description;
        this.mode = mode;
        this.rule_id = rule_id;
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

    public Integer getRule_id() {
        return rule_id;
    }

    public void setRule_id(Integer rule_id) {
        this.rule_id = rule_id;
    }   

}