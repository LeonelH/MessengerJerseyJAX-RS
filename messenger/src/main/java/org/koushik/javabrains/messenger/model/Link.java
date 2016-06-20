package org.koushik.javabrains.messenger.model;

// This has not root because its not a root element because its a member attribute the message class
public class Link {
	
	private String link;
	//rel is the relation is such as link to its self or it could be profile or comments etc..
	private String rel;
	
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getRel() {
		return rel;
	}
	public void setRel(String rel) {
		this.rel = rel;
	}
}
