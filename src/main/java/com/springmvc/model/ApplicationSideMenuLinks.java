package com.springmvc.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "application_side_menus_links")
public class ApplicationSideMenuLinks extends CommonMaster implements Serializable {
	
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="ApplicationSideMenuLinks_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	@Column(name = "side_link")
	private String sideLink;
	@ManyToOne
	@JoinColumn(name = "app_sid_menu")
	private ApplictionSideMenus appsideMenu;
	private boolean rootMenu;
	@Column(name = "display")
	private char display = 'T';
	@Column(name="codeDesc")
	private String code;

	public ApplicationSideMenuLinks() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ApplicationSideMenuLinks(String sideLink, ApplictionSideMenus appsideMenu, boolean rootMenu, char display,
			String code) {
		super();
		this.sideLink = sideLink;
		this.appsideMenu = appsideMenu;
		this.rootMenu = rootMenu;
		this.display = display;
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public boolean isRootMenu() {
		return rootMenu;
	}

	public void setRootMenu(boolean rootMenu) {
		this.rootMenu = rootMenu;
	}

	
	public String getSideLink() {
		return sideLink;
	}

	public void setSideLink(String sideLink) {
		this.sideLink = sideLink;
	}

	public ApplictionSideMenus getAppsideMenu() {
		return appsideMenu;
	}

	public void setAppsideMenu(ApplictionSideMenus appsideMenu) {
		this.appsideMenu = appsideMenu;
	}

	public char getDisplay() {
		return display;
	}

	public void setDisplay(char display) {
		this.display = display;
	}

}
