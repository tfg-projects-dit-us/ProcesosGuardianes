package com.procesosguardianes.generacioncalendario;

/**
 * This class was automatically generated by the data modeler tool.
 */

public class Calendar implements java.io.Serializable {

	static final long serialVersionUID = 1L;

	@org.kie.api.definition.type.Label("mes")
	private java.lang.Integer month;

	@org.kie.api.definition.type.Label(value = "año")
	private java.lang.Integer year;

	public Calendar() {
	}

	public java.lang.Integer getMonth() {
		return this.month;
	}

	public void setMonth(java.lang.Integer month) {
		this.month = month;
	}

	public java.lang.Integer getYear() {
		return this.year;
	}

	public void setYear(java.lang.Integer year) {
		this.year = year;
	}

	public Calendar(java.lang.Integer month, java.lang.Integer year) {
		this.month = month;
		this.year = year;
	}

}