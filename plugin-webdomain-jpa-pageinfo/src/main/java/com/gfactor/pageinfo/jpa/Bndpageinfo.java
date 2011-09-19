package com.gfactor.pageinfo.jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQuery(name = "QueryByName_Ver_Entrypoint", 
		    query = "SELECT bndpage FROM Bndpageinfo bndpage " +
		    		"WHERE bndpage.bundle_name = :bndName " +
		    		"and bndpage.bundle_version = :bndVer " +
		    		"and bndpage.entry_point = :bndEntryPoint")
		    		
@Entity
@Table(name="bndpageinfo")
public class Bndpageinfo implements Serializable{
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	 
	@Column(name="bundle_name")
	private String bundle_name;
	
	@Column(name="bundle_version")
	private String bundle_version;
	
	@Column(name="entry_point")
	private String entry_point;
	
	@Column(name="class_name")
	private String class_name;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	



	public String getBundle_name() {
		return bundle_name;
	}

	public void setBundle_name(String bundle_name) {
		this.bundle_name = bundle_name;
	}

	public String getBundle_version() {
		return bundle_version;
	}

	public void setBundle_version(String bundle_version) {
		this.bundle_version = bundle_version;
	}

	public String getEntry_point() {
		return entry_point;
	}

	public void setEntry_point(String entry_point) {
		this.entry_point = entry_point;
	}

	public String getClass_name() {
		return class_name;
	}

	public void setClass_name(String class_name) {
		this.class_name = class_name;
	}
	
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("print Bndpageinfo object : \n");
		sb.append("id               : "+ id + "\n");
		sb.append("bnudle_name      : "+ bundle_name + "\n");
		sb.append("bundle_version   : "+ bundle_version + "\n");
		sb.append("entry_point      : "+ entry_point + "\n");
		sb.append("class_name       : "+ class_name + "\n");
		return sb.toString();
	}
}
