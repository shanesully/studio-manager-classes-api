package com.studiomanager.studioapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.studiomanager.studioapi.domain.Booking;
import com.studiomanager.studioapi.domain.FitnessClass;
import com.studiomanager.studioapi.services.impl.BookingServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FitnessClassesEndpointTests {

	static final String BASE_URI = "http://localhost:";
	static final String CLASSES_ENDPOINT = "/classes";
	static final String SAMPLE_BOOKING = "{\"1\":{\"id\":1,\"classId\":1,\"memberName\":\"Shane O'Sullivan\",\"bookingDate\":\"2019-04-04\"}}";

	static ObjectMapper mapper;

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@MockBean
	BookingServiceImpl bookingService;

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
				.getForEntity(BASE_URI + port + CLASSES_ENDPOINT, String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Test
	public void classesEndpointShouldReturnDefaultResult() {
		assertThat(getClasses()).contains("Pilates");
	}

	@Test
	public void testClassesEndpointPost() throws Exception {

		newFitnessClass = new FitnessClass();

		newFitnessClass.setId(2);
		newFitnessClass.setCapacity(10);
		newFitnessClass.setClassName("Running");
		newFitnessClass.setStartDate(LocalDate.parse("2019-04-01"));
		newFitnessClass.setEndDate(LocalDate.parse("2019-04-05"));

		String requestJson = mapper.writeValueAsString(newFitnessClass);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<Object> request = new HttpEntity<Object>(requestJson, headers);

		ResponseEntity<String> response = createClass(request);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(response.getBody()).isEqualTo("Fitness class created");
		assertThat(getClasses()).contains("Pilates");
		assertThat(getClasses()).contains("Running");
	}

	@Test
	public void testClassesEndpointDeleteWithBookingsShouldFail() throws Exception {
		assertThat(getClasses()).contains("Pilates");

		String requestJson = "";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<Object> request = new HttpEntity<>(requestJson, headers);

		deleteClass(1, request);

		assertThat(getClasses()).contains("Pilates");
	}

	@Test
	public void testClassesEndpointDeleteWithoutBookingsShouldPass() throws Exception {
		assertThat(this.restTemplate.getForObject(BASE_URI + port + CLASSES_ENDPOINT,
				String.class)).contains("Pilates");

		String requestJson = "";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<Object> request = new HttpEntity<Object>(requestJson, headers);

		createNewFitnessClassWithoutBookings();

		deleteClass(1, request);
		deleteClass(2, request);

		String classes = getClasses();

		assertThat(classes).contains("Pilates");
		assertThat(classes).doesNotContain("Running");
	}

	private void returnBookingForClass(int id) {
		Mockito.when(bookingService.getBookingById(id)).thenReturn(new Booking());
	}

	private void returnNoBookingsForClass(int id) {
		Mockito.when(bookingService.getBookingById(id)).thenReturn(null);
	}

	private void createNewFitnessClassWithoutBookings() throws Exception {
		newFitnessClass = new FitnessClass();

		newFitnessClass.setId(2);
		newFitnessClass.setCapacity(10);
		newFitnessClass.setClassName("Running");
		newFitnessClass.setStartDate(LocalDate.parse("2019-04-01"));
		newFitnessClass.setEndDate(LocalDate.parse("2019-04-05"));

		String requestJson = mapper.writeValueAsString(newFitnessClass);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<Object> request = new HttpEntity<Object>(requestJson.toString(), headers);

		createClass(request);
	}

	private void setObjectMapperUseStringsForDates() {
		mapper.registerModule(new JavaTimeModule());
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
	}

	private ResponseEntity<String> createClass(HttpEntity<Object> request) {
		return this.restTemplate
				.exchange(BASE_URI + port + CLASSES_ENDPOINT, HttpMethod.POST, request, String.class);
	}

	private String getClasses() {
		return this.restTemplate.getForObject(BASE_URI + port + CLASSES_ENDPOINT,
				String.class);
	}

	private void deleteClass(int id, HttpEntity request) {
		this.restTemplate
				.exchange(BASE_URI + port + CLASSES_ENDPOINT +"/" + id, HttpMethod.DELETE, request, String.class);
	}
}
