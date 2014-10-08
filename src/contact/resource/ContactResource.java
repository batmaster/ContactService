package contact.resource;

import java.net.URI;
import java.net.URISyntaxException;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import contact.entity.Contact;
import contact.entity.ContactList;
import contact.service.ContactDao;
import contact.service.DaoFactory;

/**
 * Used to handle requests from client.
 * Support request GET, POST, PUT, DELETE.
 * 
 * @author Poramate Homprakob 5510546077
 *
 */
@Path("/contacts")
@Singleton
public class ContactResource {
	
	private ContactDao dao;
	private CacheControl cache;
	private static final int NOT_FOUND = 404;
	private static final int CONFLICT = 409;
	
	/**
	 * Constructor require no parameter.
	 * Initialize DAO object.
	 */
	public ContactResource() {
		dao = DaoFactory.getInstance().getContactDao();
		cache = new CacheControl();
		cache.setMaxAge(86400);
		cache.setPrivate(true);
		System.out.println("ContactResource Created");
	}
	
	/**
	 * Get list of contact with q query.
	 * If there is q query, return all contacts that have title containing q string.
	 * If there is no q query, return all contacts.
	 * 
	 * Not use ETag because ContactList is often changed,
	 * so unneccessary to check.
	 * 
	 * @param title some part of title of contact that want to find
	 * @return contactlist containing the list of contacts
	 * @throws InterruptedException 
	 */
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public Response getContact(@QueryParam("q") String title, @Context Request request) {
		if (title == null) {
			ContactList contacts = dao.findAll();
			return Response.ok(contacts).build();
		}
		ContactList contacts = dao.findByTitle(title);
		return Response.ok(contacts).build();
	}
	
	/**
	 * Get a contact with specific id.
	 * 
	 * Support ETag.
	 * 
	 * @param id the id of contact that want to find
	 * @return contact xml if contact is found, otherwise No Content response 
	 */
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_XML)
	public Response getContact(@PathParam("id") int id, @Context Request request) {
		Contact contact = dao.find(id);
		if (contact == null) {
			return Response.status(NOT_FOUND).build();
		}
		
		EntityTag etag = new EntityTag(Integer.toString(contact.hashCode()));
		Response.ResponseBuilder builder = request.evaluatePreconditions(etag);
		// cache changed
		if (builder == null)
			builder = Response.ok(contact).tag(etag);
			
		builder.cacheControl(cache);
		return builder.build();
	}

	/**
	 * Create a contact and store in the list in DAO.
	 * 
	 * Support ETag.
	 * 
	 * @param id id of the new contact
	 * @param title title of the new contact
	 * @param name name of the new contact
	 * @param email email of the new contact
	 * @param phoneNumber phone number of the new contact
	 * @param uriInfo uri used to get absolute path
	 * @return uri to the new contact if creating success, Not Modified response if the is exist id in the list
	 * @throws URISyntaxException
	 */
	@POST
	@Consumes(MediaType.APPLICATION_XML)
	public Response postContact(Contact contact, @Context UriInfo uriInfo, @Context Request request) throws URISyntaxException {
		if (contact.getId() == 0)
			contact = dao.createContact(contact.getTitle(), contact.getName(), contact.getEmail(), contact.getPhoneNumber());
		
		EntityTag etag = new EntityTag(Integer.toString(contact.hashCode()));
		URI uri = new URI(uriInfo.getAbsolutePath().toString() + contact.getId());
		if (dao.save(contact)) {
			return Response.created(uri).cacheControl(cache).tag(etag).build();
		}
		
		return Response.status(CONFLICT).build();
	}
	
	/**
	 * Update the exist contact in the list.
	 * 
	 * Support ETag.
	 * 
	 * @param id id of the old contact in the list
	 * @param title new title for a contact
	 * @param name name for a contact
	 * @param email email for a contact
	 * @param phoneNumber phone number for a contact
	 * @param uriInfo uri used to get absolute path
	 * @return uri to the updated contact if updating success, No Content response if the is no exist id in the list
	 * @throws URISyntaxException
	 */
	@PUT
	@Path("{oldId}")
	@Consumes(MediaType.APPLICATION_XML)
	public Response putContact(Contact contact, @Context UriInfo uriInfo, @PathParam("oldId") int oldId, @Context Request request) throws URISyntaxException {
		contact.setId(oldId);
		// no old id exists
		if (dao.find(oldId) == null)
			return Response.status(NOT_FOUND).build();
		
		Contact existContact = dao.find(oldId);
		EntityTag etag = new EntityTag(Integer.toString(existContact.hashCode()));
		Response.ResponseBuilder builder = request.evaluatePreconditions(etag);
		// match then do
		if (builder == null) {
			if (dao.update(contact)) {
				EntityTag newEtag = new EntityTag(Integer.toString(contact.hashCode()));
				builder = Response.ok(contact).tag(newEtag);
			}
		}
		
		builder.cacheControl(cache);
		return builder.build();
	}
	
	/**
	 * Remove exist contact in the list with specific id.
	 * 
	 * Support ETag.
	 * 
	 * @param id id of the contact that want to remove
	 * @return OK response if success, No Content if there is no exist contact with specific id
	 */
	@DELETE
	@Path("{id}")
	public Response deleteContact(@PathParam("id") long id, @Context Request request) {
		Contact deletingContact = dao.find(id);
		// no old id exists
				if (deletingContact == null)
					return Response.status(NOT_FOUND).build();
		
		EntityTag etag = new EntityTag(Integer.toString(deletingContact.hashCode()));
		Response.ResponseBuilder builder = request.evaluatePreconditions(etag);
		// match then do
		if (builder == null) {
			if (dao.delete(id)) {
				builder = Response.ok().tag(etag);
			}
		}
			
		builder.cacheControl(cache);
		return builder.build();
	}
	
}


