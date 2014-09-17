package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entity.Contact;
import entity.ContactList;

/**
 * A Data Access Object, used to handle with data operation.
 * Contains a list of contact in a form of pair of id and the contact.
 * 
 * @author Poramate Homprakob 5510546077
 *
 */
public class ContactDao {
	
	/** The map used to store list of contact in a form of pair of id and the contact. */
	private Map<Integer, Contact> map;
	
	/** A runner id for auto assign id to a new contact. */
	private static int runnerId = 1;
	
	/**
	 * Constructor require nothing.
	 * Initialize the list of contact.
	 */
	public ContactDao() {
		map = new HashMap<Integer, Contact>();
	}

	/**
	 * Get the exist contact in the list with specific id.
	 * @param id id of the contact that want to find
	 * @return contact with specific id, null if not found
	 */
	public Contact find(int id) {
		return map.get(id);
	}
	
	/**
	 * Get list of contacts whose title contains a specific string 
	 * @param title the specific string
	 * @return contactlist of contact
	 */
	public ContactList find(String title) {
		ContactList contacts = new ContactList();
		for (Contact c : map.values()) {
			if (c.getTitle().contains(title))
				contacts.add(c);
		}
		
		return contacts;
	}
	
	/**
	 * Get list of all contacts.
	 * @return list of all contacts
	 */
	public ContactList findAll() {
		return new ContactList(map.values());
	}
	
	/**
	 * Remove contact in the list with specific id.
	 * @param id id of contact
	 * @return the removed contact if success, null if there is no exist contact to be removed
	 */
	public Contact delete(int id) {
		return map.remove(id);
	}
	
	/**
	 * Add a new contact to the list.
	 * @param contact the new contact
	 * @return true if adding success, false if there is exist id
	 */
	public boolean save(Contact contact) {
		if (find(contact.getId()) != null)
			return false;
		
		map.put(contact.getId(), contact);
		return true;
	}
	
	/**
	 * Update the exist contact in the list.
	 * @param contact the new contact that want to replace to the list by the same id
	 * @return true if updating success, false if there is no exist id
	 */
	public boolean update(Contact contact) {
		if (find(contact.getId()) == null)
			return false;
		
		map.put(contact.getId(), contact);
		return true;
	}
	
	/**
	 * Create and return a contact with the information parameter. But no id.
	 * @param title title of the contact
	 * @param name name of the contact
	 * @param email email of the contact
	 * @param phoneNumber phone number of the contact
	 * @return the created contact
	 */
	public Contact createContact(String title, String name, String email, String phoneNumber) {
		return createContact(runnerId, title, name, email, phoneNumber);
	}
	
	/**
	 * Create and return a contact with the information parameter. Also with id.
	 * If the id has been used, the new id is auto assign.
	 * @param id id of the contact
	 * @param title title of the contact
	 * @param name name of the contact
	 * @param email email of the contact
	 * @param phoneNumber phone number of the contact
	 * @return the created contact
	 */
	public Contact createContact(int id, String title, String name, String email, String phoneNumber) {
		runnerId++;
		if (find(id) != null || id == 0)
			return createContact(runnerId, title, name, email, phoneNumber);
		return new Contact(id, title, name, email, phoneNumber);
	}
}
