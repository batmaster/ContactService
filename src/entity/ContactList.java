package entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Will be transformed to contactlist root element in xml form.
 * A List of Contact model, used to store contacts.
 * 
 * @author Poramate Homprakob 5510546077
 *
 */
@XmlRootElement
@XmlAccessorType (XmlAccessType.FIELD)
public class ContactList {
	
	/** Will be transformed to be contacts element within contactlist element. */
	@XmlElement(name = "contact")
	private List<Contact> contacts;
	
	/**
	 * Constructor require nothing.
	 * Create List<Contact> that will be used for storing contact list
	 */
	public ContactList() {
		contacts = new ArrayList<Contact>();
	}
	
	/**
	 * Constructor require list of contact that unmarshall from xml.
	 * @param contacts list of contact
	 */
	public ContactList(Collection<Contact> contacts) {
		this.contacts = new ArrayList<Contact>(contacts);
	}

	/**
	 * Add a contact to the list.
	 * @param contact a contact that want to add to the list
	 */
	public void add(Contact contact) {
		contacts.add(contact);
	}

}
