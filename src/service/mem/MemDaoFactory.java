package service.mem;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import entity.ContactList;
import service.ContactDao;
import service.DaoFactory;

public class MemDaoFactory extends DaoFactory {
	
	private static final String FILENAME = "contactlist.xml";
	
	private MemContactDao dao;
	
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

	@Override
	public ContactDao getContactDao() {
		return dao;
	}

	@Override
	public void shutdown() {
		try {
			serialize(dao.findAll());
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
	
	private void serialize(ContactList contactList) throws JAXBException {
		File file = new File(FILENAME);
		JAXBContext jaxb = JAXBContext.newInstance(ContactList.class);
		Marshaller marshaller = jaxb.createMarshaller();
		
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.marshal(contactList, file);

	}
	
	private ContactList unserialize() throws JAXBException {
		File file = new File(FILENAME);
		JAXBContext jaxb = JAXBContext.newInstance(ContactList.class);
		Unmarshaller unmarshaller = jaxb.createUnmarshaller();
		
		return (ContactList)unmarshaller.unmarshal(file);
	}

}
