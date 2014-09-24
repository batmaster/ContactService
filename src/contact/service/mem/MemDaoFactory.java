package contact.service.mem;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import contact.entity.ContactList;
import contact.service.ContactDao;
import contact.service.DaoFactory;

/**
 * DaoFactory of MemContactDao.
 * Try to read an xml file and get ContactList into the dao.
 * If there is no file, try to create an empty one first.
 * And at the end of using the dao, tyr to write all Contact in ContactList to an xml file.
 * 
 * @author Poramate Homprakob 5510546077
 *
 */
public class MemDaoFactory extends DaoFactory {
	
	private static final String FILENAME = "contactlist.xml";
	
	private ContactDao dao;
	
	/**
	 * Constructor require nothing.
	 */
	public MemDaoFactory() {
		dao = new MemContactDao();
		try {
			unserialize();
		} catch (JAXBException e) {
			e.printStackTrace();
			try {
				serialize(dao.findAll());
				unserialize();
			} catch (JAXBException e1) {
				e1.printStackTrace();
			}
		}
	}

	/**
	 * @see contact.service.DaoFactory#getContactDao()
	 */
	@Override
	public ContactDao getContactDao() {
		return dao;
	}
	
	/**
	 * @see contact.service.DaoFactory#shutdown()
	 */
	@Override
	public void shutdown() {
		try {
			serialize(dao.findAll());
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Write a ContactList object to an xml file.
	 * @param contactList the list want to be serialized
	 * @throws JAXBException
	 */
	private void serialize(ContactList contactList) throws JAXBException {
		File file = new File(FILENAME);
		JAXBContext jaxb = JAXBContext.newInstance(ContactList.class);
		Marshaller marshaller = jaxb.createMarshaller();
		
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.marshal(contactList, file);

	}
	
	/**
	 * Read the xml file and get the ContactList object.
	 * @return the ContactList object from the xml file
	 * @throws JAXBException
	 */
	private ContactList unserialize() throws JAXBException {
		File file = new File(FILENAME);
		JAXBContext jaxb = JAXBContext.newInstance(ContactList.class);
		Unmarshaller unmarshaller = jaxb.createUnmarshaller();
		
		return (ContactList)unmarshaller.unmarshal(file);
	}

}
