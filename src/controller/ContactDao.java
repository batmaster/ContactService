package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entity.Contact;
import entity.ContactList;

public class ContactDao {
	
	private Map<Integer, Contact> map;
	private static int runnerId = 0;
	
	public ContactDao() {
		map = new HashMap<Integer, Contact>();
	}

	public Contact find(int id) {
		return map.get(id);
	}
	
	public ContactList find(String title) {
		ContactList contacts = new ContactList();
		for (Contact c : map.values()) {
			if (c.getTitle().contains(title))
				contacts.add(c);
		}
		
		return contacts;
	}
	
	public ContactList findAll() {
		return new ContactList(map.values());
	}
	
	public Contact delete(int id) {
		return map.remove(id);
	}
	
	public boolean save(Contact contact) {
		if (find(contact.getId()) != null)
			return false;
		
		map.put(contact.getId(), contact);
		return true;
	}
	
	public boolean update(Contact contact) {
		if (find(contact.getId()) == null)
			return false;
		
		map.put(contact.getId(), contact);
		return true;
	}
	
	public Contact createContact(String title, String name, String email, String phoneNumber) {
		return createContact(runnerId, title, name, email, phoneNumber);
	}
	
	public Contact createContact(int id, String title, String name, String email, String phoneNumber) {
		runnerId++;
		if (find(id) != null)
			return createContact(id, title, name, email, phoneNumber);
		return new Contact(id, title, name, email, phoneNumber);
	}
}
