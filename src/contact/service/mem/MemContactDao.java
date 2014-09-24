package contact.service.mem;

import java.util.HashMap;
import java.util.Map;

import contact.entity.Contact;
import contact.entity.ContactList;
import contact.service.ContactDao;

/**
 * A Data Access Object, used to handle with data operation.
 * Contains a list of contact in a form of pair of id and the contact.
 * 
 * @author Poramate Homprakob 5510546077
 *
 */
public class MemContactDao extends ContactDao {
	
	/** The map used to store list of contact in a form of pair of id and the contact. */
	private Map<Long, Contact> map;
	
	/**
	 * Constructor require nothing.
	 * Initialize the list of contact.
	 */
	public MemContactDao() {
		map = new HashMap<Long, Contact>();
	}

	/**
	 * @see contact.service.ContactDao#find(long)
	 */
	@Override
	public Contact find(long id) {
		return map.get(id);
	}
	
	/**
	 * @see contact.service.ContactDao#findAll()
	 */
	@Override
	public ContactList findAll() {
		return new ContactList(map.values());
	}
	
	/**
	 * @see contact.service.ContactDao#findByTitle(java.lang.String)
	 */
	@Override
	public ContactList findByTitle(String title) {
		ContactList contacts = new ContactList();
		for (Contact c : map.values()) {
			if (c.getTitle().contains(title))
				contacts.add(c);
		}
		
		return contacts;
	}
	
	/**
	 * @see contact.service.ContactDao#delete(long)
	 */
	@Override
	public boolean delete(long id) {
		return map.remove(id) != null;
	}
	
	/**
	 * @see contact.service.ContactDao#save(contact.entity.Contact)
	 */
	@Override
	public boolean save(Contact contact) {
		if (find(contact.getId()) != null)
			return false;
		
		map.put(contact.getId(), contact);
		return true;
	}
	
	/**
	 * @see contact.service.ContactDao#update(contact.entity.Contact)
	 */
	@Override
	public boolean update(Contact contact) {
		if (find(contact.getId()) == null)
			return false;
		
		map.put(contact.getId(), contact);
		return true;
	}
}
