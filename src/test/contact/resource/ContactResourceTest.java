package test.contact.resource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.http.HttpMethod;
import org.junit.Before;
import org.junit.Test;

import contact.JettyMain;

public class ContactResourceTest {
	
	private static String url;
	private static HttpClient client;
	
	@Before
	public void setUp() throws Exception {
		System.out.println(":");
		JettyMain.main(null);
		
		
		url = JettyMain.getURL() + "contacts/";
		client = new HttpClient();
		client.start();
	}
	
	@Test
	public void getPass() throws InterruptedException, ExecutionException, TimeoutException{
		ContentResponse con = client.GET(url);
		assertEquals(200, con.getStatus());
	}
	
	@Test
	public void getFail() throws InterruptedException, ExecutionException, TimeoutException{
		ContentResponse con = client.GET(url + "100");
		assertEquals(204, con.getStatus());
	}

	@Test
	public void postPass() throws Exception{
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
	
	@Test
	public void postFail() throws Exception{
		StringContentProvider content = new StringContentProvider(
				"<contact id=\"100\">" + 
					"<title>tit 100</title>" +
				"</contact>"
		);
		ContentResponse con = client.newRequest(url).content(content, "application/xml").method(HttpMethod.POST).send();
		assertEquals(304, con.getStatus());
	}
	
	@Test
	public void putPass() throws Exception{
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
	
	@Test
	public void putFail() throws Exception{
		StringContentProvider content = new StringContentProvider(
				"<contact id=\"300\">" + 
					"<title>tit 300</title>" +
				"</contact>"
			);
		ContentResponse con = client.newRequest(url + "300").content(content, "application/xml").method(HttpMethod.PUT).send();
		assertEquals(204,con.getStatus());
	}
	
	@Test
	public void deletePass() throws Exception{
		ContentResponse con = client.newRequest(url + "100").method(HttpMethod.DELETE).send();
		assertEquals(200, con.getStatus());
	}
	
	@Test
	public void deleteFail() throws Exception{
		ContentResponse con = client.newRequest(url + "100").method(HttpMethod.DELETE).send();
		assertEquals(204, con.getStatus());
	}
}
