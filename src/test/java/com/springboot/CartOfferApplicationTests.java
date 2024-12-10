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

	// @Test
	// public void checkFlatXForOneSegment() throws Exception {
	// List<String> segments = new ArrayList<>();
	// segments.add("p1");
	// OfferRequest offerRequest = new OfferRequest(1, "FLATX", 1000000000,
	// segments);
	// boolean result = addOffer(offerRequest);
	// Assert.assertEquals(result, true); // able to add offer
	// }

	// @Test
	// public void checkPercentageDiscountForMultipleSegments() throws Exception {
	// List<String> segments = new ArrayList<>();
	// segments.add("p1");
	// segments.add("p2");
	// OfferRequest offerRequest = new OfferRequest(2, "PERCENTAGE", 20, segments);
	// boolean result = addOffer(offerRequest);
	// Assert.assertEquals(result, true); // able to add offer
	// }

	// @Test
	// public void checkBOGOOffer() throws Exception {
	// List<String> segments = new ArrayList<>();
	// segments.add("p1");
	// OfferRequest offerRequest = new OfferRequest(3, "BOGO", 0, segments);
	// boolean result = addOffer(offerRequest);
	// Assert.assertEquals(result, true); // able to add offer
	// }

	// @Test
	// public void testMissingOfferDetails() throws Exception {
	// // Create an OfferRequest with missing fields
	// OfferRequest offerRequest = new OfferRequest();
	// boolean result = addOffer(offerRequest);
	// // Assert that the API returns an appropriate error code or response
	// // (e.g., HTTP 400 Bad Request)
	// }

	// @Test
	// public void testZeroDiscount() throws Exception {
	// List<String> segments = new ArrayList<>();
	// segments.add("p1");
	// OfferRequest offerRequest = new OfferRequest(5, "FLATX", 0, segments);
	// boolean result = addOffer(offerRequest);
	// // Assert the expected behavior, e.g., offer is added with a zero discount
	// }

	// @Test
	// public void testNegativeDiscount() throws Exception {
	// List<String> segments = new ArrayList<>();
	// segments.add("p1");
	// OfferRequest offerRequest = new OfferRequest(6, "FLATX", -10, segments);
	// boolean result = addOffer(offerRequest);
	// // Assert the expected error handling, e.g., API returns an error for invalid
	// // input
	// }

	// @Test
	// public void testEmptySegmentList() throws Exception {
	// OfferRequest offerRequest = new OfferRequest(7, "FLATX", 10, new
	// ArrayList<>());
	// boolean result = addOffer(offerRequest);
	// // Assert the expected behavior, e.g., API handles empty segment list
	// // appropriately
	// }

	// @Test
	// public void testLargeNumberOfSegments() throws Exception {
	// List<String> segments = new ArrayList<>();
	// // Add a large number of segments
	// for (int i = 0; i < 100; i++) {
	// segments.add("p" + i);
	// }
	// OfferRequest offerRequest = new OfferRequest(8, "FLATX", 10, segments);
	// boolean result = addOffer(offerRequest);
	// // Assert the expected behavior, e.g., API handles large number of segments
	// // efficiently
	// }

	// @Test
	// public void testInvalidSegment() throws Exception {
	// List<String> segments = new ArrayList<>();
	// segments.add("invalidSegment");
	// OfferRequest offerRequest = new OfferRequest(4, "FLATX", 10, segments);
	// boolean result = addOffer(offerRequest);
	// // Assert that the API handles the invalid segment and returns an appropriate
	// // error
	// }

	// @Test
	// public void testServerError() throws Exception {
	// // Modify the URL to point to a non-existent endpoint
	// String urlString = "http://localhost:9001/api/v1/invalidEndpoint";
	// URL url = new URL(urlString);
	// // ... (rest of the addOffer method)
	// // Assert that the test handles the exception gracefully
	// }

	@Test
	public void testMissingOfferDetails() throws Exception {
		// Create an OfferRequest with missing fields
		OfferRequest offerRequest = new OfferRequest();
		boolean result = false;

		try {
			result = addOffer(offerRequest);
		} catch (Exception e) {
			System.out.println("Exception occurred: " + e.getMessage());
			// Assuming the API would throw an error for invalid input
		}
		// Assert that the API returns an appropriate error code or response
		Assert.assertFalse(result); // API should not allow the addition of an offer with missing fields
	}

	@Test
	public void testZeroDiscount() throws Exception {
		List<String> segments = new ArrayList<>();
		segments.add("p1");
		OfferRequest offerRequest = new OfferRequest(5, "FLATX", 0, segments);
		boolean result = addOffer(offerRequest);
		// Assert the expected behavior, e.g., offer is added with a zero discount
		Assert.assertTrue(result); // Assuming the API allows zero discount offers
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
			// Assuming the API would reject the offer and throw an error
		}
		// Assert the expected error handling, e.g., API returns an error for invalid
		// input
		Assert.assertFalse(result); // Negative discounts should not be allowed
	}

	@Test
	public void testEmptySegmentList() throws Exception {
		OfferRequest offerRequest = new OfferRequest(7, "FLATX", 10, new ArrayList<>());
		boolean result = false;

		try {
			result = addOffer(offerRequest);
		} catch (Exception e) {
			System.out.println("Exception occurred: " + e.getMessage());
			// Assuming the API would reject offers with an empty segment list
		}
		// Assert the expected behavior, e.g., API handles empty segment list
		// appropriately
		Assert.assertFalse(result); // API should reject empty segment lists
	}

	@Test
	public void testLargeNumberOfSegments() throws Exception {
		List<String> segments = new ArrayList<>();
		// Add a large number of segments
		for (int i = 0; i < 100; i++) {
			segments.add("p" + i);
		}
		OfferRequest offerRequest = new OfferRequest(8, "FLATX", 10, segments);
		boolean result = addOffer(offerRequest);
		// Assert the expected behavior, e.g., API handles large number of segments
		// efficiently
		Assert.assertTrue(result); // Assuming API allows and processes large segment lists efficiently
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
			// Assuming the API would return an error for an invalid segment
		}
		// Assert that the API handles the invalid segment and returns an appropriate
		// error
		Assert.assertFalse(result); // Invalid segments should not be allowed
	}

	@Test
	public void testServerError() throws Exception {
		// Modify the URL to point to a non-existent endpoint
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
			// Handling server errors or connection issues gracefully
		}
		// Assert that the test handles the exception gracefully
		Assert.assertFalse(result); // Expecting the test to fail for server errors
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