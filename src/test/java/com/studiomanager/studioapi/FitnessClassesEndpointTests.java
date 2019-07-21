package com.studiomanager.studioapi;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.studiomanager.studioapi.domain.Booking;
import com.studiomanager.studioapi.domain.FitnessClass;
import com.studiomanager.studioapi.services.BookingService;
import com.studiomanager.studioapi.services.FitnessClassService;
import java.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FitnessClassesEndpointTests {

	static final String URI = "http://localhost:";
	static final String CLASSES_ENDPOINT = "/classes";

	static final String SAMPLE_BOOKING = "{\"1\":{\"id\":1,\"classId\":1,\"memberName\":\"Shane O'Sullivan\",\"bookingDate\":\"2019-04-04\"}}";

	static ObjectMapper mapper;

	@LocalServerPort
	private int PORT;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	FitnessClassService fitnessClassService;

	@MockBean
	BookingService bookingService;

	static FitnessClass newFitnessClass;

	@Before
	public void setup() {
		Mockito.when(bookingService.getBookings()).thenReturn(SAMPLE_BOOKING);
		returnBookingForClass(1);
		returnNoBookingsForClass(2);

		mapper = new ObjectMapper();
		setObjectMapperUseStringsForDates();
	}

	@Test
	public void testClassesEndpointGet() {
		ResponseEntity<String> response = this.restTemplate
				.getForEntity(URI + PORT + CLASSES_ENDPOINT, String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Test
	public void testClassesEndpointPost() throws Exception {

		newFitnessClass = new FitnessClass.Builder(2)
				.withCapacity(10)
				.withClassName("Running")
				.withStartDate(LocalDate.parse("2019-04-01"))
				.withEndDate(LocalDate.parse("2019-04-05"))
				.build();

		String requestJson = mapper.writeValueAsString(newFitnessClass);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<Object> request = new HttpEntity<Object>(requestJson, headers);

		ResponseEntity<String> response = restTemplate
				.exchange(URI + PORT + CLASSES_ENDPOINT, HttpMethod.POST, request, String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(response.getBody()).isEqualTo("Fitness class created");
		assertThat(restTemplate.getForObject(URI + PORT + CLASSES_ENDPOINT,
				String.class)).contains("Pilates");
		assertThat(restTemplate.getForObject(URI + PORT + CLASSES_ENDPOINT,
				String.class)).contains("Running");
	}

	@Test
	public void testClassesEndpointDeleteWithBookingsShouldFail() throws Exception {
		ResponseEntity<String> response = this.restTemplate
				.getForEntity(URI + PORT + CLASSES_ENDPOINT, String.class);

		assertThat(restTemplate.getForObject(URI + PORT + CLASSES_ENDPOINT,
				String.class)).contains("Pilates");

		String requestJson = "";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<Object> request = new HttpEntity<>(requestJson, headers);

		deleteClass(1, request);

		assertThat(restTemplate.getForObject(URI + PORT + CLASSES_ENDPOINT,
				String.class)).contains("Pilates");
	}

	@Test
	public void testClassesEndpointDeleteWithoutBookingsShouldPass() throws Exception {
		assertThat(this.restTemplate.getForObject(URI + PORT + CLASSES_ENDPOINT,
				String.class)).contains("Pilates");

		String requestJson = "";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<Object> request = new HttpEntity<Object>(requestJson, headers);

		createNewFitnessClassWithoutBookings();

		deleteClass(1, request);
		deleteClass(2, request);

		String classes = restTemplate.getForObject(URI + PORT + CLASSES_ENDPOINT,
				String.class);

		assertThat(classes).contains("Pilates");
		assertThat(classes).doesNotContain("Running");
	}

	private void returnBookingForClass(int id) {
		Booking booking = new Booking.Builder()
				.withId(5)
				.withClassId(5)
				.withMemberName("Sean")
				.withBookingDate(LocalDate.now())
				.build();

		Mockito.when(bookingService.getBookingById(id)).thenReturn(booking);
	}

	private void returnNoBookingsForClass(int id) {
		Mockito.when(bookingService.getBookingById(id)).thenReturn(null);
	}

	private void createNewFitnessClassWithoutBookings() throws Exception {
		newFitnessClass = new FitnessClass.Builder(2)
				.withCapacity(10)
				.withClassName("Running")
				.withStartDate(LocalDate.parse("2019-04-01"))
				.withEndDate(LocalDate.parse("2019-04-05"))
				.build();

		String requestJson = mapper.writeValueAsString(newFitnessClass);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<Object> request = new HttpEntity<Object>(requestJson.toString(), headers);

		restTemplate
				.exchange(URI + PORT + CLASSES_ENDPOINT, HttpMethod.POST, request, String.class);
	}

	private void setObjectMapperUseStringsForDates() {
		mapper.registerModule(new JavaTimeModule());
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
	}

	private void deleteClass(int id, HttpEntity request) {
		this.restTemplate
				.exchange(URI + PORT + CLASSES_ENDPOINT + "/" + id, HttpMethod.DELETE, request, String.class);
	}
}
