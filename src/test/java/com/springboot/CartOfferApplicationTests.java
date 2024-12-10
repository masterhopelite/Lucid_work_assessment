package com.springboot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.controller.OfferRequest;
import com.springboot.controller.SegmentResponse;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CartOfferApplicationTests {

	@Test
	public void testMissingOfferDetails() throws Exception {
		OfferRequest offerRequest = new OfferRequest();
		boolean result = false;

		try {
			result = addOffer(offerRequest);
		} catch (Exception e) {
			System.out.println("Exception occurred: " + e.getMessage());
		}
		Assert.assertFalse(result);
	}

	@Test
	public void testZeroDiscount() throws Exception {
		List<String> segments = new ArrayList<>();
		segments.add("p1");
		OfferRequest offerRequest = new OfferRequest(5, "FLATX", 0, segments);
		boolean result = addOffer(offerRequest);
		Assert.assertTrue(result);
	}

	@Test
	public void testNegativeDiscount() throws Exception {
		List<String> segments = new ArrayList<>();
		segments.add("p1");
		OfferRequest offerRequest = new OfferRequest(6, "FLATX", -10, segments);
		boolean result = false;

		try {
			result = addOffer(offerRequest);
		} catch (Exception e) {
			System.out.println("Exception occurred: " + e.getMessage());
		}
		Assert.assertFalse(result);
	}

	@Test
	public void testEmptySegmentList() throws Exception {
		OfferRequest offerRequest = new OfferRequest(7, "FLATX", 10, new ArrayList<>());
		boolean result = false;

		try {
			result = addOffer(offerRequest);
		} catch (Exception e) {
			System.out.println("Exception occurred: " + e.getMessage());
		}
		Assert.assertFalse(result);
	}

	@Test
	public void testLargeNumberOfSegments() throws Exception {
		List<String> segments = new ArrayList<>();
		for (int i = 0; i < 100; i++) {
			segments.add("p" + i);
		}
		OfferRequest offerRequest = new OfferRequest(8, "FLATX", 10, segments);
		boolean result = addOffer(offerRequest);
		Assert.assertTrue(result);
	}

	@Test
	public void testInvalidSegment() throws Exception {
		List<String> segments = new ArrayList<>();
		segments.add("invalidSegment");
		OfferRequest offerRequest = new OfferRequest(4, "FLATX", 10, segments);
		boolean result = false;

		try {
			result = addOffer(offerRequest);
		} catch (Exception e) {
			System.out.println("Exception occurred: " + e.getMessage());
		}
		Assert.assertFalse(result);
	}

	@Test
	public void testServerError() throws Exception {
		String urlString = "http://localhost:9001/api/v1/invalidEndpoint";
		boolean result = false;

		try {
			URL url = new URL(urlString);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setDoOutput(true);
			con.setRequestProperty("Content-Type", "application/json");

			OfferRequest offerRequest = new OfferRequest(1, "FLATX", 10, List.of("p1"));
			ObjectMapper mapper = new ObjectMapper();
			String POST_PARAMS = mapper.writeValueAsString(offerRequest);
			OutputStream os = con.getOutputStream();
			os.write(POST_PARAMS.getBytes());
			os.flush();
			os.close();
			int responseCode = con.getResponseCode();

			Assert.assertNotEquals(responseCode, HttpURLConnection.HTTP_OK);
			result = (responseCode == HttpURLConnection.HTTP_OK);
		} catch (Exception e) {
			System.out.println("Exception occurred: " + e.getMessage());
		}
		Assert.assertFalse(result);
	}

	public boolean addOffer(OfferRequest offerRequest) throws Exception {
		String urlString = "http://localhost:9001/api/v1/offer";
		URL url = new URL(urlString);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setDoOutput(true);
		con.setRequestProperty("Content-Type", "application/json");

		ObjectMapper mapper = new ObjectMapper();

		String POST_PARAMS = mapper.writeValueAsString(offerRequest);
		OutputStream os = con.getOutputStream();
		os.write(POST_PARAMS.getBytes());
		os.flush();
		os.close();
		int responseCode = con.getResponseCode();
		System.out.println("POST Response Code :: " + responseCode);

		if (responseCode == HttpURLConnection.HTTP_OK) { // success
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			// print result
			System.out.println(response.toString());
		} else {
			System.out.println("POST request did not work.");
		}
		return true;
	}
}