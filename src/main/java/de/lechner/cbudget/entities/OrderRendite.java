package de.lechner.cbudget.entities;


import java.util.Date;


public class OrderRendite {


	private Integer id;
	private Date datum;
	private Date startdate;
	private Date enddate;
	private Integer ruleid;
	private Integer finished;

	public OrderRendite(Integer id, Date datum, Date startdate, Date enddate, Integer ruleid, Integer finished) {
		this.id = id;
		this.datum = datum;
		this.startdate = startdate;
		this.enddate = enddate;
		this.ruleid = ruleid;
		this.finished = finished;
	}

	public OrderRendite() {

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getDatum() {
		return datum;
	}

	public void setDatum(Date datum) {
		this.datum = datum;
	}

	public Date getStartdate() {
		return startdate;
	}

	public void setStartdate(Date startdate) {
		this.startdate = startdate;
	}

	public Date getEnddate() {
		return enddate;
	}

	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}

	public Integer getRuleid() {
		return ruleid;
	}

	public void setRuleid(Integer ruleid) {
		this.ruleid = ruleid;
	}

	public Integer getFinished() {
		return finished;
	}

	public void setFinished(Integer finished) {
		this.finished = finished;
	}
}
