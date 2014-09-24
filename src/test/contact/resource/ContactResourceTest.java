package test.contact.resource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import javax.persistence.OrderBy;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.http.HttpMethod;
import org.junit.AfterClass;
import org.junit.Before;
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
	 * Test getting all Contact, that will get empty ContactList.
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws TimeoutException
	 */
	@Test
	public void test1GetPass() throws InterruptedException, ExecutionException, TimeoutException {
		ContentResponse con = client.GET(url);
		assertEquals(200, con.getStatus());
	}
	
	/**
	 * Test getting some contact which is not in the list.
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws TimeoutException
	 */
	@Test
	public void test2GetFail() throws InterruptedException, ExecutionException, TimeoutException {
		ContentResponse con = client.GET(url + "100");
		assertEquals(204, con.getStatus());
	}

	/**
	 * Test adding some contact.
	 * @throws Exception
	 */
	@Test
	public void test3PostPass() throws Exception {
		StringContentProvider content = new StringContentProvider(
			"<contact id=\"100\">" + 
				"<title>tit 100</title>" +
			"</contact>"
		);
		ContentResponse con = client.newRequest(url).content(content, "application/xml").method(HttpMethod.POST).send();
		assertEquals(201, con.getStatus());
		
		String get = client.GET(url + "100").getContentAsString();
		assertTrue(get.contains("tit 100"));
	}
	
	/**
	 * Test adding the same contact with the last test.
	 * @throws Exception
	 */
	@Test
	public void test4PostFail() throws Exception {
		StringContentProvider content = new StringContentProvider(
				"<contact id=\"100\">" + 
					"<title>tit 100</title>" +
				"</contact>"
		);
		ContentResponse con = client.newRequest(url).content(content, "application/xml").method(HttpMethod.POST).send();
		assertEquals(304, con.getStatus());
	}
	
	/**
	 * Test editing contact added from the last test. 
	 * @throws Exception
	 */
	@Test
	public void test5PutPass() throws Exception {
		StringContentProvider content = new StringContentProvider(
				"<contact id=\"200\">" + 
					"<title>tit 200</title>" +
				"</contact>"
			);
		ContentResponse con = client.newRequest(url + "100").content(content, "application/xml").method(HttpMethod.PUT).send();
		assertEquals(202,con.getStatus());
		
		String get = client.GET(url + "100").getContentAsString();
		assertTrue(get.contains("tit 200"));
	}
	
	/**
	 * Test editing some contact which is not in the list.
	 * @throws Exception
	 */
	@Test
	public void test6PutFail() throws Exception {
		StringContentProvider content = new StringContentProvider(
				"<contact id=\"300\">" + 
					"<title>tit 300</title>" +
				"</contact>"
			);
		ContentResponse con = client.newRequest(url + "300").content(content, "application/xml").method(HttpMethod.PUT).send();
		assertEquals(204,con.getStatus());
	}
	
	/**
	 * Test deleting contact added from the last test. 
	 * @throws Exception
	 */
	@Test
	public void test7DeletePass() throws Exception {
		ContentResponse con = client.newRequest(url + "100").method(HttpMethod.DELETE).send();
		assertEquals(200, con.getStatus());
	}
	
	/**
	 * Test deleting the same contact that was already deleted again.
	 * @throws Exception
	 */
	@Test
	public void test8DeleteFail() throws Exception {
		ContentResponse con = client.newRequest(url + "100").method(HttpMethod.DELETE).send();
		assertEquals(204, con.getStatus());
	}
}
