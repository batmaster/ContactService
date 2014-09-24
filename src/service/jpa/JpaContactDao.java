package service.jpa;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import java.util.logging.Logger;

import jersey.repackaged.com.google.common.collect.Lists;
import entity.Contact;
import entity.ContactList;
import service.ContactDao;

/**
 * Data access object for saving and retrieving contacts,
 * using JPA.
 * To get an instance of this class use:
 * <p>
 * <tt>
 * dao = DaoFactory.getInstance().getContactDao()
 * </tt>
 * 
 * @author jim
 */
public class JpaContactDao extends ContactDao {
	
	/** the EntityManager for accessing JPA persistence services. */
	private final EntityManager em;
	
	/**
	 * constructor with injected EntityManager to use.
	 * @param em an EntityManager for accessing JPA services.
	 */
	public JpaContactDao(EntityManager em) {
		this.em = em;
		createTestContact( );
	}
	
	/** add contacts for testing. */
	private void createTestContact( ) {
		long id = 101;
		if (find(id) == null) {
			Contact test = new Contact("Test contact", "Joe Experimental", "none@testing.com", "000000");
			test.setId(id);
			save(test);
		}
		id++;
		if (find(id) == null) {
			Contact test2 = new Contact("Another Test contact", "Testosterone", "testee@foo.com", "111111");
			test2.setId(id);
			save(test2);
		}
	}

	/**
	 * @see service.ContactDao#find(long)
	 */
	@Override
	public Contact find(long id) {
		return em.find(Contact.class, id);
	}

	/**
	 * @see service.ContactDao#findAll()
	 */
	@Override
	public ContactList findAll() {
		Query query = em.createQuery("SELECT c FROM Contact c");
		ContactList result = new ContactList(query.getResultList());
		return result;
	}

	/**
	 * @see service.ContactDao#findByTitle(java.lang.String)
	 */
	@Override
	public ContactList findByTitle(String titlestr) {
		Query query = em.createQuery("SELECT c FROM Contact c WHERE LOWER(c.title) LIKE :title");
		query.setParameter("title", "%" + titlestr.toLowerCase() + "%");
		ContactList result = new ContactList(Lists.newArrayList( query.getResultList() ));
		return result;
	}

	/**
	 * @see service.ContactDao#delete(long)
	 */
	@Override
	public boolean delete(long id) {
		EntityTransaction tx  = em.getTransaction();
		tx.begin();
		Contact contact = find(id);
		if (contact == null) {
			tx.commit();
			return false;
		}
		em.remove(contact);
		tx.commit();
		return true;
	}
	
	/**
	 * @see service.ContactDao#save(entity.Contact)
	 */
	@Override
	public boolean save(Contact contact) {
		if (contact == null) throw new IllegalArgumentException("Can't save a null contact");
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			em.persist(contact);
			tx.commit();
			return true;
		} catch (EntityExistsException ex) {
			Logger.getLogger(this.getClass().getName()).warning(ex.getMessage());
			if (tx.isActive()) try { tx.rollback(); } catch(Exception e) {}
			return false;
		}
	}

	/**
	 * @see service.ContactDao#update(entity.Contact)
	 */
	@Override
	public boolean update(Contact update) {
		EntityTransaction tx  = em.getTransaction();
		tx.begin();
		if (find(update.getId()) == null) {
			tx.commit();
			return false;
		}
		
		Contact contact = find(update.getId());
		contact.setTitle(update.getTitle());
		contact.setName(update.getName());
		contact.setEmail(update.getEmail());
		contact.setPhoneNumber(update.getPhoneNumber());
		tx.commit();
		return true;
	}
}
