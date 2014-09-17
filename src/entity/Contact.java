package entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Will be transformed to contact element in xml form.
 * 
 * @author Poramate Homprakob 5510546077
 *
 */
@XmlRootElement
@XmlAccessorType (XmlAccessType.FIELD)
public class Contact {
	
	/**
	 * An attribute of the contact.
	 * E.g. <contact id="200">
	 */
	@XmlAttribute
	private int id;
	
	/** Elements of the contact. */
	@XmlElement
	private String title;
	private String name;
	private String email;
	private String phoneNumber;
	
	/**
	 * Constructor require nothing.
	 */
	public Contact() {
		
	}
	
	/**
	 * Constructor require contact information parameter.
	 * @param id id of a contact
	 * @param title title of a contact
	 * @param name name of a contact
	 * @param email email of a contact
	 * @param phoneNumber phone number of a contact
	 */
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
	
	public void setId(int id) {
		this.id = id;
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
	
	public String toString() {
		return String.format("%d %s %s", id, title, name);
	}
}
