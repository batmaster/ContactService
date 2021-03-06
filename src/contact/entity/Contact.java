package contact.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Will be transformed to contact element in xml form.
 * 
 * Generate hash code by using attributes value to support ETag.
 * 
 * @author Poramate Homprakob 5510546077
 *
 */
@Entity
@XmlRootElement
@XmlAccessorType (XmlAccessType.FIELD)
public class Contact implements Serializable {
	
	/**
	 * An attribute of the contact.
	 * Each contact has unique id.
	 * E.g. <contact id="200">
	 */
	@Id
	@XmlAttribute
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	/** Elements of the contact. */
	@XmlElement(required = true)
	@NotNull
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
	 * Constructor require only id and another will be null.
	 * @param id id of a contact
	 */
	public Contact(long id) {
		this(id, null, null, null, null);
	}
	
	/**
	 * Constructor require contact information parameter but without id.
	 * @param title title of a contact
	 * @param name name of a contact
	 * @param email email of a contact
	 * @param phoneNumber phone number of a contact
	 */
	public Contact(String title, String name, String email, String phoneNumber) {
		this(0, title, name, email, phoneNumber);
	}
	
	/**
	 * Constructor require contact information parameter.
	 * @param id id of a contact
	 * @param title title of a contact
	 * @param name name of a contact
	 * @param email email of a contact
	 * @param phoneNumber phone number of a contact
	 */
	public Contact(long id, String title, String name, String email, String phoneNumber) {
		this.id = id;
		this.title = title;
		this.name = name;
		this.email = email;
		this.phoneNumber = phoneNumber;
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	@Override
	public String toString() {
		String xml = "";
		xml += String.format("<contact id=\"%d\">", id);
		
		if (title != null)
			xml += String.format("\n\t<title>%s</title>", title);
		
		if (name != null)
			xml += String.format("\n\t<name>%s</name>", name);
		
		if (email != null)
			xml += String.format("\n\t<email>%s</email>", email);
		
		if (phoneNumber != null)
			xml += String.format("\n\t<phoneNumber>%s</phoneNumber>", phoneNumber);
		
		xml += "\n</contact>";
		return xml;
	}

	/**
	 * Override hashCode() to be generated from attributes value.
	 * Used to generate ETag value.
	 */
	@Override
	public int hashCode() {
		return (id + title + name + email + phoneNumber).hashCode();
	}
}
