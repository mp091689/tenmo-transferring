package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferStatusDto;
import com.techelevator.tenmo.model.TransferDto;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TransferService {

    public final String SERVICE_API_URL;
    private final RestTemplate restTemplate;
    private AuthenticatedUser user;

    public TransferService(String baseUrl, RestTemplate restTemplate) {
        this.SERVICE_API_URL = baseUrl + "/transfers";
        this.restTemplate = restTemplate;
    }

    public void setAuthenticatedUser(AuthenticatedUser user) {
        this.user = user;
    }

    public List<Transfer> getAll() {
        return getListByUrl(SERVICE_API_URL);
    }

    public List<Transfer> getPending() {
        return getListByUrl(SERVICE_API_URL + "/pending");
    }

    public Transfer getById(int id) {
        try {
            ResponseEntity<Transfer> response = restTemplate.exchange(SERVICE_API_URL + "/" + id, HttpMethod.GET, makeAuthEntity(), Transfer.class);
            return response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return null;
    }

    public Transfer sendBucks(int receiverId, BigDecimal amount) {
        return createTransfer(2, receiverId, amount);
    }

    public Transfer requestBucks(int receiverId, BigDecimal amount) {
        return createTransfer(1, receiverId, amount);
    }

    public Transfer approveTransfer(int transferId) {
        return touchTransferStatus(2, transferId);
    }

    public Transfer declineTransfer(int transferId) {
        return touchTransferStatus(3, transferId);
    }

    private Transfer touchTransferStatus(int statusId, int transferId) {
        Transfer transfer = getById(transferId);
        if (transfer == null || transfer.getStatusId() != 1) {
            return null;
        }

        TransferStatusDto transferApproveDto = new TransferStatusDto();
        transferApproveDto.setStatusId(statusId);

        String resourceUrl = SERVICE_API_URL + "/" + transfer.getId();
        HttpEntity<TransferStatusDto> entity = makeTransferEntity(transferApproveDto);

        try {
            ResponseEntity<Transfer> response = restTemplate.exchange(resourceUrl, HttpMethod.PUT, entity, Transfer.class);
            return response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return null;
    }

    private Transfer createTransfer(int typeId, int receiverId, BigDecimal amount) {
        TransferDto transfer = new TransferDto();
        transfer.setUserId(receiverId);
        transfer.setAmount(amount);
        transfer.setTypeId(typeId);

        try {
            return restTemplate.postForObject(SERVICE_API_URL, makeTransferEntity(transfer), Transfer.class);
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return null;
    }

    private List<Transfer> getListByUrl(String url) {
        try {
            ResponseEntity<Transfer[]> response = restTemplate.exchange(url, HttpMethod.GET, makeAuthEntity(), Transfer[].class);
            if (response.getBody() != null) {
                return Arrays.asList(response.getBody());
            }
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return new ArrayList<>();
    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(user.getToken());
        return new HttpEntity<>(headers);
    }

    private HttpEntity<TransferDto> makeTransferEntity(TransferDto transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(user.getToken());
        return new HttpEntity<>(transfer, headers);
    }

    private HttpEntity<TransferStatusDto> makeTransferEntity(TransferStatusDto transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(user.getToken());
        return new HttpEntity<>(transfer, headers);
    }
}
