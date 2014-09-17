package resource;

import java.awt.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.glassfish.jersey.server.Uri;

import controller.ContactDao;
import entity.Contact;
import entity.ContactList;

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
	
	/**
	 * Constructor require no parameter.
	 * Initialize DAO object.
	 */
	public ContactResource() {
		dao = new ContactDao();
		System.out.println("ContactResource Created");
	}
	
	/**
	 * Get list of contact with q query.
	 * If there is q query, return all contacts that have title containing q string.
	 * If there is no q query, return all contacts.
	 * @param title some part of title of contact that want to find
	 * @return contactlist containing the list of contacts
	 */
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public Response getContact(@QueryParam("q") String title) {
		if (title == null) {
			ContactList contacts = dao.findAll();
			Response response = Response.ok(contacts).build();
			
			System.out.println("GET title all");
			System.out.println(" found " + contacts);
			System.out.println(" response" + response);
			return response;
		}
		ContactList contacts = dao.findAll();
		Response response = Response.ok(dao.find(title)).build();
		
		System.out.println("GET title " + title);
		System.out.println(" found " + contacts);
		System.out.println(" response" + response);
		return response;
	}
	
	/**
	 * Get a contact with specific id.
	 * @param id the id of contact that want to find
	 * @return contact xml if contact is found, otherwise No Content response 
	 */
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_XML)
	public Response getContact(@PathParam("id") int id) {
		Contact contact = dao.find(id);
		if (contact == null) {
			Response response = Response.noContent().build();
			
			System.out.println("GET id " + id);
			System.out.println(" found " + contact);
			System.out.println(" response" + response);
			return response;
		}
		Response response = Response.ok(contact).build();
		
		System.out.println("GET id " + id);
		System.out.println(" found " + contact);
		System.out.println(" response" + response);
		return response;
	}

	/**
	 * Create a contact and store in the list in DAO.
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
	public Response postContact(Contact contact, @Context UriInfo uriInfo) throws URISyntaxException {
		if (contact.getId() == 0)
			contact = dao.createContact(contact.getTitle(), contact.getName(), contact.getEmail(), contact.getPhoneNumber());
			
		if (dao.save(contact)) {
			URI uri = new URI(uriInfo.getAbsolutePath().toString() + contact.getId());
			Response response = Response.created(uri).build();
			
			System.out.println("POST id " + contact.getId());
			System.out.println(" make " + contact);
			System.out.println(" response" + response);

			return response;
		}
		Response response = Response.notModified().build();
		
		System.out.println("POST id " + contact.getId());
		System.out.println(" make " + contact);
		System.out.println(" response" + response);
		return response;
	}
	
	/**
	 * Update the exist contact in the list.
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
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_XML)
	public Response putContact(Contact contact, @Context UriInfo uriInfo, @PathParam("id") int id) throws URISyntaxException {
		contact.setId(id);
		if (dao.update(contact)) {
			URI uri = new URI(uriInfo.getAbsolutePath().toString() + contact.getId());
			Response response = Response.accepted(contact).build();
			
			System.out.println("PUT id " + contact.getId());
			System.out.println(" found " + contact);
			System.out.println(" response" + response);
			return response;
			
		}
		Response response = Response.noContent().build();
		
		System.out.println("PUT id " + contact.getId());
		System.out.println(" found " + contact);
		System.out.println(" response" + response);
		return response;
	}
	
	/**
	 * Remove exist contact in the list with specific id.
	 * @param id id of the contact that want to remove
	 * @return OK response if success, No Content if there is no exist contact with specific id
	 */
	@DELETE
	@Path("{id}")
	public Response deleteContact(@PathParam("id") int id) {
		Contact contact = dao.delete(id);
		if (contact == null) {
			Response response = Response.noContent().build();
			
			System.out.println("@DELETE id " + id);
			System.out.println(" found " + contact);
			System.out.println(" response" + response);
			return response;
		}
		Response response = Response.ok().build();
		
		System.out.println("@DELETE id " + id);
		System.out.println(" found " + contact);
		System.out.println(" response" + response);
		return response;
	}
}
