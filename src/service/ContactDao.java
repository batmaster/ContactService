package service;

import entity.Contact;
import entity.ContactList;

/**
 * An abstract for contact dao contains method for handling
 * with contact.
 * 
 * @author Poramate Homprakob 5510546077
 *
 */
public abstract class ContactDao {

	/** A runner id for auto assign id to a new contact. */
	private static int runnerId = 1;

	/**
	 * Get the exist contact in the list with specific id.
	 * @param id id of the contact that want to find
	 * @return contact with specific id, null if not found
	 */
	public abstract Contact find(long id);
	
	/**
	 * Get list of all contacts.
	 * @return list of all contacts
	 */
	public abstract ContactList findAll();
	
	/**
	 * Get list of contacts whose title contains a specific string 
	 * @param title the specific string
	 * @return contactlist of contact
	 */
	public abstract ContactList findByTitle(String name);
	
	/**
	 * Remove contact in the list with specific id.
	 * @param id id of contact
	 * @return the removed contact if success, null if there is no exist contact to be removed
	 */
	public abstract boolean delete(long id);
	
	/**
	 * Add a new contact to the list.
	 * @param contact the new contact
	 * @return true if adding success, false if there is exist id
	 */
	public abstract boolean save(Contact contact);
	
	/**
	 * Update the exist contact in the list.
	 * @param contact the new contact that want to replace to the list by the same id
	 * @return true if updating success, false if there is no exist id
	 */
	public abstract boolean update(Contact update);
	
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
