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

@Path("/contacts")
@Singleton
public class ContactResource {
	
	private ContactDao dao;
	
	public ContactResource() {
		dao = new ContactDao();
		System.out.println("ContactResource Created");
	}
	
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
	
	@POST
	@Consumes(MediaType.APPLICATION_XML)
	/// แก้ @FormParam id
	public Response postContact(@FormParam("id") int id, @FormParam("title") String title, @FormParam("name") String name, @FormParam("email") String email, @FormParam("phonenumber") String phoneNumber, @Context UriInfo uriInfo) throws URISyntaxException {
		Contact contact = dao.createContact(id, title, name, email, phoneNumber);
		if (dao.save(contact)) {
			URI uri = new URI(uriInfo.getAbsolutePath().toString() + contact.getId());
			Response response = Response.created(uri).build();
			
			System.out.println("POST id " + id);
			System.out.println(" make " + contact);
			System.out.println(" response" + response);
			return response;
		}
		Response response = Response.notModified().build();
		
		System.out.println("POST id " + id);
		System.out.println(" make " + contact);
		System.out.println(" response" + response);
		return response;
	}
	
	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_XML)
	public Response putContact(@PathParam("id") int id, @FormParam("title") String title, @FormParam("name") String name, @FormParam("email") String email, @FormParam("phonenumber") String phoneNumber, @Context UriInfo uriInfo) throws URISyntaxException {
		Contact contact = dao.createContact(id, title, name, email, phoneNumber);
		if (dao.update(contact)) {
			URI uri = new URI(uriInfo.getAbsolutePath().toString() + contact.getId());
			Response response = Response.ok(uri).build();
			
			System.out.println("PUT id " + id);
			System.out.println(" found " + contact);
			System.out.println(" response" + response);
			return response;
			
		}
		Response response = Response.noContent().build();
		
		System.out.println("PUT id " + id);
		System.out.println(" found " + contact);
		System.out.println(" response" + response);
		return response;
	}
	
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
