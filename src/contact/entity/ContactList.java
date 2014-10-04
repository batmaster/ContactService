package contact.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Will be transformed to contactlist root element in xml form.
 * A List of Contact model, used to store contacts.
 * 
 * Generate hash code by using hash codes of each contact in the list to support ETag.
 * 
 * @author Poramate Homprakob 5510546077
 *
 */
@Entity
@XmlRootElement
@XmlAccessorType (XmlAccessType.FIELD)
public class ContactList implements Serializable {
	
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

	/**
	 * Check if the contact is already in list.
	 * @param contact the contact want to check
	 * @return true if the contact is already in list, otherwise false
	 */
	public boolean contains(Contact contact) {
		for (Contact c : contacts) {
			if (c.equals(contact))
				return true;
		}
		return false;
	}
	
	/**
	 * Return size of contacts in list.
	 * @return size of contacts in list
	 */
	public int size() {
		return contacts.size();
	}

	/**
	 * Override hashCode() to be generated from hash codes of each contact in the list.
	 * Used to generate ETag value.
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		for (Contact c : contacts)
			hash += c.hashCode();
		
		return hash;
	}
}
