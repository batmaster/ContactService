package entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType (XmlAccessType.FIELD)
public class Contact {
	
	@XmlAttribute
	private int id;
	
	@XmlElement
	private String title;
	private String name;
	private String email;
	private String phoneNumber;
	
	public Contact() {
		
	}
	
	public Contact(int id, String title, String name, String email, String phoneNumber) {
		this.id = id;
		this.title = title;
		this.name = name;
		this.email = email;
		this.phoneNumber = phoneNumber;
	}
	
	public int getId() {
		return id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getName() {
		return name;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}

}
