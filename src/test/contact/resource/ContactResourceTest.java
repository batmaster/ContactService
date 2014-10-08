package test.contact.resource;

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

/**
 * Unit test for testing ContactResource.
 * The tests will be in oder.
 * 
 * @author Poramate Homprakob 5510546077
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ContactResourceTest {
	
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
	 * Test getting all Contact, that will get empty ContactList.
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws TimeoutException
	 */
	@Test
	public void test1GetAllPass() throws InterruptedException, ExecutionException, TimeoutException {
		ContentResponse con = client.GET(url);
		assertEquals("Get all contacts in the list that is still empty but return empty ContactList should also return 200 OK", STATUS_OK, con.getStatus());
	}
	
	/**
	 * Test getting some contact which is not in the list.
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws TimeoutException
	 */
	@Test
	public void test2GetFail() throws InterruptedException, ExecutionException, TimeoutException {
		ContentResponse con = client.GET(url + runnerId);
		assertEquals("Try to get contact that doesnot exist should return 404 Not Found", STATUS_NOT_FOUND, con.getStatus());
	}

	/**
	 * Test adding some contact.
	 * And assign 0 for id to let dao generate it.
	 * @throws Exception
	 */
	@Test
	public void test3PostPass() throws Exception {
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
	}
	
	/**
	 * Test getting the new Contact.
	 * Also test location header.
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws TimeoutException
	 */
	@Test
	public void test4GetPass() throws InterruptedException, ExecutionException, TimeoutException {
		ContentResponse con = client.GET(url + runnerId);
		assertEquals("Get the existing contact should return 200 OK", STATUS_OK, con.getStatus());
	}
	
	/**
	 * Test adding the same contact with the last test.
	 * @throws Exception
	 */
	@Test
	public void test5PostFail() throws Exception {
		StringContentProvider content = new StringContentProvider(
				"<contact id=\"1\">" +
					"<title>tit 100</title>" +
				"</contact>"
		);
		ContentResponse con = client.newRequest(url).content(content, "application/xml").method(HttpMethod.POST).send();
		assertEquals("Add the contact that the id is already in the list should return 409 Conflict", STATUS_CONFLICT, con.getStatus());
	}
	
	/**
	 * Test editing contact added from the last test. 
	 * @throws Exception
	 */
	@Test
	public void test6PutPass() throws Exception {
		StringContentProvider content = new StringContentProvider(
				"<contact id=\"0\">" +
					"<title>tit 200</title>" +
				"</contact>"
			);
		ContentResponse con = client.newRequest(url + runnerId).content(content, "application/xml").method(HttpMethod.PUT).send();
		assertEquals("Update the exist contact that has the same id with id path (not from the new contact id) should return 200 OK", STATUS_OK, con.getStatus());
		
		String get = client.GET(url + runnerId).getContentAsString();
		assertTrue("Check the updated contact id", get.contains(String.format("id=\"%d\"", runnerId)));
		assertTrue("Check the updated contact title", get.contains("tit 200"));
	}
	
	/**
	 * Test editing some contact which is not in the list.
	 * @throws Exception
	 */
	@Test
	public void test7PutFail() throws Exception {
		StringContentProvider content = new StringContentProvider(
				"<contact id=\"0\">" +
					"<title>tit 300</title>" +
				"</contact>"
			);
		ContentResponse con = client.newRequest(url + "300").content(content, "application/xml").method(HttpMethod.PUT).send();
		assertEquals("Try to update not exist contact should return 404 Not Found", STATUS_NOT_FOUND, con.getStatus());
	}
	
	/**
	 * Test deleting contact added from the last test. 
	 * @throws Exception
	 */
	@Test
	public void test8DeletePass() throws Exception {
		ContentResponse con = client.newRequest(url + runnerId).method(HttpMethod.DELETE).send();
		assertEquals("Delete the contact in the list correctly should return 200 OK", STATUS_OK, con.getStatus());
	}
	
	/**
	 * Test deleting the same contact that was already deleted again.
	 * @throws Exception
	 */
	@Test
	public void test9DeleteFail() throws Exception {
		ContentResponse con = client.newRequest(url + "100").method(HttpMethod.DELETE).send();
		assertEquals("Delete not exist contact should return 404 Not fould", STATUS_NOT_FOUND, con.getStatus());
	}
}
