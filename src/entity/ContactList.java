package entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType (XmlAccessType.FIELD)
public class ContactList {
	
	@XmlElement(name = "contact")
	private List<Contact> contacts;
	
	public ContactList() {
		contacts = new ArrayList<Contact>();
	}
	
	public ContactList(Collection<Contact> contacts) {
		this.contacts = new ArrayList<Contact>(contacts);
	}

	public void add(Contact contact) {
		contacts.add(contact);
	}

}
