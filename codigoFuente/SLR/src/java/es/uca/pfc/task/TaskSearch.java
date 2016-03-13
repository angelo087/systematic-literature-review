package es.uca.pfc.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TaskSearch 
{
	private List<TaskReference> references = new ArrayList<TaskReference>();
	
	private String guidSlr;
	private TaskEngineSearch engine;
	private String component;
	private String operator;
	private String terminos = "";
	private Date fecha = new Date();
	private String startYear = "";
	private String endYear = "";
	int maxTotal = 0;
	public List<TaskReference> getReferences() {
		return references;
	}
	public void setReferences(List<TaskReference> references) {
		this.references = references;
	}
	public String getGuidSlr() {
		return guidSlr;
	}
	public void setGuidSlr(String guidSlr) {
		this.guidSlr = guidSlr;
	}
	public TaskEngineSearch getEngine() {
		return engine;
	}
	public void setEngine(TaskEngineSearch engine) {
		this.engine = engine;
	}
	public String getComponent() {
		return component;
	}
	public void setComponent(String component) {
		this.component = component;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getTerminos() {
		return terminos;
	}
	public void setTerminos(String terminos) {
		this.terminos = terminos;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public String getStartYear() {
		return startYear;
	}
	public void setStartYear(String startYear) {
		this.startYear = startYear;
	}
	public String getEndYear() {
		return endYear;
	}
	public void setEndYear(String endYear) {
		this.endYear = endYear;
	}
	public int getMaxTotal() {
		return maxTotal;
	}
	public void setMaxTotal(int maxTotal) {
		this.maxTotal = maxTotal;
	}
}
