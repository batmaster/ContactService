package test.contact.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import contact.entity.Contact;
import contact.entity.ContactList;
import contact.service.ContactDao;
import contact.service.DaoFactory;
import contact.service.jpa.JpaContactDao;
import contact.service.jpa.JpaDaoFactory;
import contact.service.mem.MemContactDao;

/**
 * Unit test for testing ContactDao.
 * Used to test find save update delete handling.
 * The tests will be in oder.
 * 
 * @author Poramate Homprakob 5510546077
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ContactDaoTest {
	
	private static ContactDao dao;
	private static Contact contact1;
	private static Contact contact2;
	
	@BeforeClass
	public static void setUp() {
		dao = DaoFactory.getInstance().getContactDao();
		
		contact1 = dao.createContact(1, "title1", null, null, null);
		contact2 = dao.createContact(2, "title2", null, null, null);
	}
	
	/**
	 * Test finding all contact, but should return empty ContactList.
	 */
	@Test
	public void test1FindAllEmptyList() {
		ContactList contacts = dao.findAll();
		assertTrue("Find all at least should return a not null empty ContactList", contacts != null);
		assertEquals("Empty ContactList has size 0", 0, contacts.size());
	}
	
	/**
	 * Test finding by using id, but id does not exist yet.
	 */
	@Test
	public void test2FindByIdFail() {
		Contact contact = dao.find(1);
		assertNull("Get not exist contact by id should return null", contact);	
	}
	
	/**
	 * Test finding by using title, but id does not exist yet.
	 */
	@Test
	public void test3FindByTitleFail() {
		ContactList contacts = dao.findByTitle("title");
		assertTrue("Get not exist contact by title should return empty Contactlist", contacts != null);
		assertEquals("Empty ContactList has size 0", 0, contacts.size());
	}
	
	/**
	 * Test adding new contact that does not exist in the list.
	 */
	@Test
	public void test4SavePass() {
		assertTrue("Save a new contact to the list successfully should return true", dao.save(contact1));
		
		Contact contact = dao.find(1);
		assertEquals("Try to get title of contact id 1", contact1.getTitle(), contact.getTitle());
	}
	
	/**
	 * Test adding exist contact.
	 */
	@Test
	public void test5SaveFail() {
		assertFalse("Save the same contact should return false", dao.save(contact1));
	}
	
	/**
	 * Test finding contact with id.
	 */
	@Test
	public void test6FindByIdPass() {
		Contact contact = dao.find(1);
		assertEquals("Try to get id of contact id 1", contact1.getId(), contact.getId());
	}
	
	/**
	 * Test finding contact with title.
	 */
	@Test
	public void test7FindByTitlePass() {
		Contact contact = dao.find(1);
		assertEquals("Try to get title of contact id 1", contact1.getTitle(), contact.getTitle());
	}
	
	/**
	 * Test editing exist contact.
	 */
	@Test
	public void test8UpdatePass() {
		contact2.setId(contact1.getId());
		assertTrue("Update the contact should return true", dao.update(contact2));
		
		Contact contact = dao.find(contact2.getId());
		assertEquals("Updated contact id should not be changed", contact1.getId(), contact.getId());
		assertTrue("The title of updated contact should be changed", !contact1.getTitle().equals(contact.getTitle()));
	}
	
	/**
	 * Test deleting existing contact.
	 */
	@Test
	public void test9DeletePass() {
		assertTrue("Delete existing contact with correct id should return true", dao.delete(contact2.getId()));
	}
	
	/**
	 * Test deleting the contact that does not in the list.
	 */
	@Test
	public void test10DeleteFail() {
		assertFalse("Delete not exist contact should return false", dao.delete(contact2.getId()));
	}
	
	/**
	 * Test editing the contact that is not in the list.
	 */
	@Test
	public void test11UpdateFail() {
		assertFalse("Update the contact that is not in the list should return false", dao.update(contact1));
	}
}