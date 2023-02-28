package com.techelevator.tenmo.services;



import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.util.BasicLogger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

@Service
public class AccountService {

    private final String baseUrl;
    private RestTemplate restTemplate;

    public AccountService(String baseURL) {
        this.restTemplate = new RestTemplate();
        this.baseUrl = baseURL + "account";
    }


    //TODO: Running into an issue where the restTemplate is returning 0 for AccountID and UserID
    public Account[] getBalance(AuthenticatedUser authenticatedUser) {
        HttpEntity<Void> entity = createHttpEntity(authenticatedUser);
        Account[] balance = null;

        try {
            balance = restTemplate.exchange(baseUrl + "/balance",
                    HttpMethod.GET,
                    entity,
                    Account[].class
            ).getBody();
        } catch (RestClientResponseException e) {
            System.out.println("Could not complete request. Code: " + e.getRawStatusCode());
        } catch (ResourceAccessException e) {
            System.out.println("Server network issue. Please try again.");
        }
        if (balance != null) {
            for (Account account : balance) {
                BasicLogger.log("[DEBUG]\t------\tAccount Info Retrieved: " + account.toString());
            }
        }
        return balance;
    }

    public Account[] getAccountByUserId(AuthenticatedUser authenticatedUser, int userId) {

        Account[] account = null;
        try {
            account = restTemplate.exchange(baseUrl + "account/user/" + userId,
                    HttpMethod.GET,
                    createHttpEntity(authenticatedUser),
                    Account[].class).getBody();
        } catch(RestClientResponseException e) {
            System.out.println("Could not complete request. Code: " + e.getRawStatusCode());
        } catch(ResourceAccessException e) {
            System.out.println("Server network issue. Please try again.");
        }

        return account;
    }

    private HttpEntity<Void> createHttpEntity(AuthenticatedUser authenticatedUser) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(authenticatedUser.getToken());
        return new HttpEntity<>(httpHeaders);
    }

}