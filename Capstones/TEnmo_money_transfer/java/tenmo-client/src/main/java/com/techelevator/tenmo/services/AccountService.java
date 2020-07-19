package com.techelevator.tenmo.services;
import java.math.BigDecimal;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;



public class AccountService {
    private String BASE_URL;
    private RestTemplate restTemplate = new RestTemplate();

    public AccountService(String url) {
        this.BASE_URL = url;
    }
    
    //reference balance in Account controller to provide clinent side balance value to check for history, and for transfers
	public BigDecimal getBalance(String token) {
		BigDecimal balance = null;

		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setBearerAuth(token);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<BigDecimal> response = restTemplate.exchange(BASE_URL + "balance", HttpMethod.GET, entity,
					BigDecimal.class);
			balance = response.getBody();
		} catch (RestClientResponseException ex) {
			System.out.println(ex.getRawStatusCode() + " : " + ex.getStatusText());
		} catch (ResourceAccessException ex) {
			System.out.println(ex.getMessage());
		}
		return balance;
	}

}
