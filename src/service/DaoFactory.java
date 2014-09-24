package service;

import service.jpa.JpaDaoFactory;

public abstract class DaoFactory {
	
	private static DaoFactory factory;
	
	protected DaoFactory() {
		
	}
	
	public static DaoFactory getInstance() {
//		if (factory == null)
//			factory = new MemDaoFactory();
		
		if (factory == null)
			factory = new JpaDaoFactory();
		return factory;
	}
	
	public abstract ContactDao getContactDao();
	
	public abstract void shutdown();

}
