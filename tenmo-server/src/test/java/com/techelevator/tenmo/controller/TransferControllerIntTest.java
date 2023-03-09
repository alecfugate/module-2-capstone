package com.techelevator.tenmo.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Transfer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class TransferControllerIntTest {


    private MockMvc mockMvc;

    @Mock
    private TransferDao mockTransferDao;

    @InjectMocks
    @Autowired
    TransferController controller;

    private ObjectMapper mapper = new ObjectMapper();

   @Test
    public void testGetTransferByTransferId() throws Exception {
        Transfer transfer = new Transfer();
        transfer.setTransferId(3001);
        when(mockTransferDao.getTransferById(3001)).thenReturn(transfer);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        MvcResult result = mockMvc.perform(get("/transfers/3001"))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        Transfer response = mapper.readValue(responseContent, Transfer.class);

        assertEquals(response.getTransferId(), 3001);
    }

    @Test
    public void testGetTransferByUserId() throws Exception {
        List<Transfer> transfers = new ArrayList<>();
        Transfer transfer = new Transfer();
        transfer.setTransferId(1);
        transfers.add(transfer);
        when(mockTransferDao.getAllTransfersForUser(anyInt())).thenReturn(transfers);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        MvcResult result = mockMvc.perform(get("/transfers/user/1001"))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        List<Transfer> response = mapper.readValue(responseContent, new TypeReference<List<Transfer>>(){});

        assertEquals(response.size(), 1);
    }

    @Test
    public void testAddTransfer() throws Exception {
        // Arrange
        Transfer transfer = new Transfer();
        transfer.setAmount(new BigDecimal(100));
        transfer.setAccountFrom(2001);
        transfer.setAccountTo(2002);
        transfer.setTransferStatus(1);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        when(mockTransferDao.createTransfer(any())).thenReturn(transfer);
        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .request(HttpMethod.POST, "/transfers/send_to/1")
                .contentType(MediaType.APPLICATION_JSON);


        requestBuilder.content(mapper.writeValueAsString(transfer));

        mockMvc.perform(requestBuilder)
                .andExpect(status().is2xxSuccessful());

    }

    @Test
    public void testGetPendingTransfersByUserId() throws Exception {
        List<Transfer> transfers = new ArrayList<>();
        Transfer transfer = new Transfer();
        transfer.setTransferId(3002);
        transfers.add(transfer);
        when(mockTransferDao.getPendingTransfersByUserId(anyInt())).thenReturn(transfers);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        MvcResult result = mockMvc.perform(get("/transfers/user/1001/pending"))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        List<Transfer> response = mapper.readValue(responseContent, new TypeReference<List<Transfer>>(){});

        assertEquals(response.size(), 1);
    }

    @Test
    public void testUpdateTransferStatus() throws Exception {
        // Arrange
        Transfer transfer = new Transfer();
        transfer.setAmount(new BigDecimal(100));
        transfer.setAccountFrom(2001);
        transfer.setAccountTo(2002);
        transfer.setTransferStatus(1);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        doNothing().when(mockTransferDao).updateTransfer(any());
        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .request(HttpMethod.PUT, "/transfers/update_status/1")
                .contentType(MediaType.APPLICATION_JSON);


        requestBuilder.content(mapper.writeValueAsString(transfer));

        mockMvc.perform(requestBuilder)
                .andExpect(status().is2xxSuccessful());
    }

}