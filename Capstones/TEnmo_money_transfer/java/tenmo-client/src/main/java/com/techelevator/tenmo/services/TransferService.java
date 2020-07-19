package com.techelevator.tenmo.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.User;

public class TransferService {
    private String BASE_URL;
    private RestTemplate restTemplate = new RestTemplate();

    
    //contacts the server to gain access to our methods to return client side objects/data
    public TransferService(String url) {
        this.BASE_URL = url;
     }

    
	public List<User> listOfUsers(String token) {
		List<User> users = new ArrayList<>();

		try {
			HttpEntity<?> entity = makeTokenHeader(token);
			ResponseEntity<List<User>> response = restTemplate.exchange(BASE_URL + "transfer/users", HttpMethod.GET, entity, new ParameterizedTypeReference<List<User>>(){});
			users = response.getBody();
		} catch (RestClientResponseException ex) {
			System.out.println(ex.getRawStatusCode() + " : " + ex.getStatusText());
		} catch (ResourceAccessException ex) {
			System.out.println(ex.getMessage());
		}
		return users;
		
	}
	
	public List<Transfer> listOfTransfers(String token) {
		List<Transfer> transfers = new ArrayList<>();

		try {
			HttpEntity<?> entity = makeTokenHeader(token);
			ResponseEntity<List<Transfer>> response = restTemplate.exchange(BASE_URL + "transfer/transactions", HttpMethod.GET, entity, new ParameterizedTypeReference<List<Transfer>>(){});
			transfers = response.getBody();
		} catch (RestClientResponseException ex) {
			System.out.println(ex.getRawStatusCode() + " : " + ex.getStatusText());
		} catch (ResourceAccessException ex) {
			System.out.println(ex.getMessage());
		}
		return transfers;
		
	}
	
	public Transfer findTransferById(Long transferId, String token) {
		Transfer transfer = new Transfer();

		try {
			HttpEntity<?> entity = makeTokenHeader(token);
			ResponseEntity<Transfer> response = restTemplate.exchange(BASE_URL + "transfer/transactions/" + transferId, HttpMethod.GET, entity, Transfer.class);
			transfer = response.getBody();
		} catch (RestClientResponseException ex) {
			System.out.println(ex.getRawStatusCode() + " : " + ex.getStatusText());
		} catch (ResourceAccessException ex) {
			System.out.println(ex.getMessage());
		}
		return transfer;
		
	}


	public Transfer postNewTransfer(User userFrom, User userTo, BigDecimal amount, String token) {
		Transfer transfer = new Transfer(userFrom, userTo, amount);
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setBearerAuth(token);
			HttpEntity<?> entity = new HttpEntity<>(transfer, headers);
			ResponseEntity<Transfer> response = restTemplate.exchange(BASE_URL + "transfer", HttpMethod.POST, entity, Transfer.class);
			transfer = response.getBody();
		} catch (RestClientResponseException ex) {
			System.out.println(ex.getRawStatusCode() + " : " + ex.getStatusText());
		} catch (ResourceAccessException ex) {
			System.out.println(ex.getMessage());
		}
		
		return transfer;
		
	}
	
	private HttpEntity<?> makeTokenHeader(String token) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(token);
		HttpEntity<?> entity = new HttpEntity<>(headers);
		return entity;
	}
			
			
			
    }