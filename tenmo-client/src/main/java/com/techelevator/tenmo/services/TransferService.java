package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.util.BasicLogger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;


@Service
public class TransferService {

    private String baseUrl;
    private RestTemplate restTemplate = new RestTemplate();

    public TransferService(String url) {
        this.baseUrl = url + "transfers";
    }

    public String createTransfer(AuthenticatedUser authenticatedUser, Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authenticatedUser.getToken());
        HttpEntity<Transfer> entity = new HttpEntity(transfer, headers);

        String url = baseUrl + "/send_to/" + transfer.getTransferID();
        String message = "";
        if(transfer.getTransferType()==2) {
            message = "Your transfer is complete.";
        }
        else {
            message = "Your request is complete";
        }

        try {
            BasicLogger.log(transfer.toString());
            restTemplate.exchange(url, HttpMethod.POST, entity, Transfer.class);
        } catch(RestClientResponseException e) {
            if (e.getMessage().contains("Insufficient Funds")) {
                message="You don't have enough money for that transaction.";
            } else {
                message="Could not complete request. Code: " + e.getRawStatusCode();
            }
        } catch(ResourceAccessException e) {
            message="Server network issue. Please try again.";
        }
        return message;
    }

    public String updateTransfer(AuthenticatedUser authenticatedUser, Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authenticatedUser.getToken());
        HttpEntity<Transfer> entity = new HttpEntity(transfer, headers);

        String url = baseUrl + "/update_status/" + transfer.getTransferID();
        String message = "Your transaction is complete";

        try {
            restTemplate.exchange(url, HttpMethod.PUT, entity, Transfer.class);
        } catch(RestClientResponseException e) {
            if (e.getMessage().contains("Insufficient Funds")) {
                message = "You don't have enough money for that transaction.";
            } else {
                message = "Could not complete request. Code: " + e.getRawStatusCode();
            }
        } catch(ResourceAccessException e) {
            message = "Server network issue, please try again.";
        }
        return message;
    }

    public Transfer[] getTransfersFromUserId(AuthenticatedUser authenticatedUser) {
        Transfer[] transfers = null;
        try {
            transfers = restTemplate.exchange(baseUrl + "/transfers/user/" + authenticatedUser.getUser().getId(),
                    HttpMethod.GET,
                    makeEntity(authenticatedUser),
                    Transfer[].class
            ).getBody();
        } catch(RestClientResponseException e) {
            System.out.println("Could not complete request. Code: " + e.getRawStatusCode());
        } catch(ResourceAccessException e) {
            System.out.println("Server network issue. Please try again.");
        }
        return transfers;
    }



    public Transfer[] getPendingTransfersByUserId(AuthenticatedUser authenticatedUser) {
        Transfer[] transfers = null;
        try {
            transfers = restTemplate.exchange(baseUrl + "/transfers/user/" + authenticatedUser.getUser().getId() + "/pending",
                    HttpMethod.GET,
                    makeEntity(authenticatedUser),
                    Transfer[].class
            ).getBody();
        } catch(RestClientResponseException e) {
            System.out.println("Could not complete request. Code: " + e.getRawStatusCode());
        } catch(ResourceAccessException e) {
            System.out.println("Server network issue. Please try again.");
        }
        return transfers;
    }

    private HttpEntity makeEntity(AuthenticatedUser authenticatedUser) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authenticatedUser.getToken());
        HttpEntity entity = new HttpEntity(headers);
        return entity;
    }

}
