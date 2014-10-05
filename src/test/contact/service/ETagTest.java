package test.contact.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import javax.persistence.OrderBy;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.HttpMethod;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.junit.Test;

import contact.JettyMain;
import contact.entity.Contact;
import contact.entity.ContactList;
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
	
	@BeforeClass
	public static void setUp() throws Exception {
		JettyMain.setUp();
		
		url = JettyMain.getURL().toString() + "contacts/";
		System.out.println("   >> " + url);
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
			"<contact id=\"100\">" +
				"<title>tit 100</title>" +
			"</contact>"
		);
		ContentResponse con = client.newRequest(url).content(content, "application/xml").method(HttpMethod.POST).send();
		assertEquals(201, con.getStatus());
		
		String get = client.GET(url + "100").getContentAsString();
		assertTrue(get.contains("tit 100"));
		
		String etag = con.getHeaders().get("ETag");
		assertTrue(etag != null);
	}
	
	/**
	 * Test getting the contact left from the last test, that has hash code same as ETag returned from request.
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws TimeoutException
	 */
	@Test
	public void test2GetWithETag() throws InterruptedException, ExecutionException, TimeoutException {
		ContentResponse con = client.GET(url + "100");
		assertEquals(200, con.getStatus());
		
		String etag = con.getHeaders().get("ETag");
		Contact addedContact = DaoFactory.getInstance().getContactDao().find(100);
		assertEquals("\"" + addedContact.hashCode() + "\"", etag);
	}
	
	/**
	 * Test editing contact added from last test, also ETag is same as editted contact hash code. 
	 * @throws Exception
	 */
	@Test
	public void test3PutWithETag() throws Exception {
		StringContentProvider content = new StringContentProvider(
				"<contact id=\"200\">" +
					"<title>tit 200</title>" +
				"</contact>"
			);
		ContentResponse con = client.newRequest(url + "100").content(content, "application/xml").method(HttpMethod.PUT).send();
		assertEquals(202, con.getStatus());
		
		String get = client.GET(url + "100").getContentAsString();
		assertTrue(get.contains("tit 200"));
		
		String etag = con.getHeaders().get("ETag");
		Contact edittedContact = DaoFactory.getInstance().getContactDao().find(100);
		assertEquals("\"" + edittedContact.hashCode() + "\"", etag);
	}
	
	/**
	 * Test editting with IF_MATCH precondition.
	 * @throws Exception
	 */
	@Test
	public void test4PutWithIfMatchPass() throws Exception {
		StringContentProvider content = new StringContentProvider(
				"<contact id=\"300\">" +
					"<title>tit 300</title>" +
				"</contact>"
			);
		
		Contact contact100 = DaoFactory.getInstance().getContactDao().find(100);
		ContentResponse con = client.newRequest(url + "100").content(content, "application/xml").header(HttpHeader.IF_MATCH, "\"" + contact100.hashCode() + "\"").method(HttpMethod.PUT).send();
		assertEquals("OK? ", 202, con.getStatus());
		
		String get = client.GET(url + "100").getContentAsString();
		assertTrue(get.contains("tit 300"));
		
		String etag = con.getHeaders().get("ETag");
		Contact edittedContact = DaoFactory.getInstance().getContactDao().find(100);
		assertEquals("\"" + edittedContact.hashCode() + "\"", etag);
	}
	
	/**
	 * Test editting with IF_MATCH precondition.
	 * @throws Exception
	 */
	@Test
	public void test5PutWithIfMatchFail() throws Exception {
		StringContentProvider content = new StringContentProvider(
				"<contact id=\"300\">" +
					"<title>tit 300</title>" +
				"</contact>"
			);
		
		Contact contact200 = new Contact(200);
		ContentResponse con = client.newRequest(url + "200").content(content, "application/xml").header(HttpHeader.IF_MATCH, "\"" + contact200.hashCode() + "\"").method(HttpMethod.PUT).send();
		assertEquals(204, con.getStatus());
	}
}
