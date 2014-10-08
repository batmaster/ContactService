package test.contact.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.HttpMethod;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.junit.Test;

import contact.JettyMain;
import contact.entity.Contact;
import contact.service.DaoFactory;

/**
 * Unit test for testing ContactResource with ETag.
 * The tests will be in oder.
 * 
 * @author Poramate Homprakob 5510546077
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ETagTest {
	
	private static String url;
	private static HttpClient client;
	
	private static final int STATUS_OK = 200;
	private static final int STATUS_CREATED = 201;
	private static final int STATUS_NOT_FOUND = 404;
	private static final int STATUS_CONFLICT = 409;
	
	private static int runnerId = 0;
	
	@BeforeClass
	public static void setUp() throws Exception {
		JettyMain.setUp();
		
		url = JettyMain.getURL().toString() + "contacts/";
		client = new HttpClient();
		client.start();
	}
	
	@AfterClass
	public static void tearDown() throws Exception {
		JettyMain.tearDown();
	}
	
	/**
	 * Test adding contact and then get Etag.
	 * @throws Exception
	 */
	@Test
	public void test1PostWithETag() throws Exception {
		StringContentProvider content = new StringContentProvider(
			"<contact id=\"0\">" +
				"<title>tit 01</title>" +
			"</contact>"
		);
		ContentResponse con = client.newRequest(url).content(content, "application/xml").method(HttpMethod.POST).send();
		assertEquals("Add the new contact should return 201 Created", STATUS_CREATED, con.getStatus());
		runnerId++;
		
		String get = client.GET(url + runnerId).getContentAsString();
		assertTrue("Get the added contact and check the id", get.contains(String.format("id=\"%d\"", runnerId)));
		assertTrue("Also check the title", get.contains("tit 01"));
		assertTrue("Check the value of unassign attribute", !get.contains("<name>"));
		
		String location = con.getHeaders().get(HttpHeader.LOCATION).toString();
		assertEquals("Check the location header", url + runnerId, location);
		
		String etag = con.getHeaders().get("ETag");
		assertTrue("Etag should not be null", etag != null);
	}
	
	/**
	 * Test getting the contact left from the last test, that has hash code same as ETag returned from request.
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws TimeoutException
	 */
	@Test
	public void test2GetWithETag() throws InterruptedException, ExecutionException, TimeoutException {
		ContentResponse con = client.GET(url + runnerId);
		assertEquals("Get contact should return 200 OK", STATUS_OK, con.getStatus());
		
		String etag = con.getHeaders().get("ETag");
		Contact addedContact = DaoFactory.getInstance().getContactDao().find(runnerId);
		assertEquals("Etag should same as contact hash code", "\"" + addedContact.hashCode() + "\"", etag);
	}
	
	/**
	 * Test editing contact added from last test, also ETag is same as editted contact hash code. 
	 * @throws Exception
	 */
	@Test
	public void test3PutWithETag() throws Exception {
		StringContentProvider content = new StringContentProvider(
				"<contact id=\"0\">" +
					"<title>tit 200</title>" +
				"</contact>"
			);
		ContentResponse con = client.newRequest(url + runnerId).content(content, "application/xml").method(HttpMethod.PUT).send();
		assertEquals(STATUS_OK, con.getStatus());
		
		String get = client.GET(url + runnerId).getContentAsString();
		assertTrue(get.contains("tit 200"));
		
		String etag = con.getHeaders().get("ETag");
		Contact edittedContact = DaoFactory.getInstance().getContactDao().find(runnerId);
		assertEquals("Etag should same as contact hash code", "\"" + edittedContact.hashCode() + "\"", etag);
	}
	
	/**
	 * Test editting with IF_MATCH precondition.
	 * @throws Exception
	 */
	@Test
	public void test4PutWithIfMatchPass() throws Exception {
		StringContentProvider content = new StringContentProvider(
				"<contact id=\"0\">" +
					"<title>tit 300</title>" +
				"</contact>"
			);
		
		Contact contact1 = DaoFactory.getInstance().getContactDao().find(runnerId);
		ContentResponse con = client.newRequest(url + runnerId).content(content, "application/xml").header(HttpHeader.IF_MATCH, "\"" + contact1.hashCode() + "\"").method(HttpMethod.PUT).send();
		assertEquals("OK? ", STATUS_OK, con.getStatus());
		
		String get = client.GET(url + runnerId).getContentAsString();
		assertTrue("Check updated contact title", get.contains("tit 300"));
		
		String etag = con.getHeaders().get("ETag");
		Contact edittedContact = DaoFactory.getInstance().getContactDao().find(runnerId);
		assertEquals("\"" + edittedContact.hashCode() + "\"", etag);
	}
	
	/**
	 * Test editting with IF_MATCH precondition.
	 * @throws Exception
	 */
	@Test
	public void test5PutWithIfMatchFail() throws Exception {
		StringContentProvider content = new StringContentProvider(
				"<contact id=\"0\">" +
					"<title>tit 400</title>" +
				"</contact>"
			);
		
		Contact contact200 = new Contact(200);
		ContentResponse con = client.newRequest(url + "200").content(content, "application/xml").header(HttpHeader.IF_MATCH, "\"" + contact200.hashCode() + "\"").method(HttpMethod.PUT).send();
		assertEquals("Should return 404 Not Fould", STATUS_NOT_FOUND, con.getStatus());
		
		String get = client.GET(url + runnerId).getContentAsString();
		assertTrue("Check contact title have not updated", get.contains("tit 300"));
	}
}
