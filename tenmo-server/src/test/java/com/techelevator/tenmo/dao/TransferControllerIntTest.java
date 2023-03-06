package com.techelevator.tenmo.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techelevator.tenmo.controller.TransferController;
import com.techelevator.tenmo.model.Transfer;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TransferControllerIntTest {

        // @Mock or MockInject
        @Autowired
        TransferController controller;

        @Autowired
        ObjectMapper mapper;

        MockMvc mockMvc;

        @Before
        public void setUp() throws Exception {
        System.out.println("setup()...");
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

        @Test
        public void create_ValidTransfer_ShouldAddNewTransfer() throws Exception {
        final Transfer transfer = new Transfer(3001, 1, 1, 1001, 1002, new BigDecimal("350.00"));


        mockMvc.perform(MockMvcRequestBuilders.post("/send_to/{id}", 1001)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(transfer))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

                    // enter no values
        @Test
        public void create_InvalidTransfer_ShouldNotBeCreated() throws Exception {
        final Transfer transfer = new Transfer(0, 0, 0, 0, 0, new BigDecimal(""));

        mockMvc.perform(post("/transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(transfer)))
                .andExpect(status().isBadRequest());
    }

        @Test
        public void update_ValidTransfer_ShouldUpdateExistingTransfer() throws Exception {
        final Transfer transfer = new Transfer(3001, 1, 1, 1001, 1002, new BigDecimal("350.00"));

        //    transfer.  (); // call something

        mockMvc.perform(put("/transfers" + transfer.getTransferId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(transfer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("MY_NEW_TITLE"));  // not sure expression and expected value
    }

        @Test
        public void update_InvalidTransferShouldNotBeUpdated() throws Exception {
        final Transfer transfer = new Transfer(3001, 1, 1, 1001, 1002, new BigDecimal("350.00"));

        //    transfer.("");  // call something

        mockMvc.perform(put("/transfers" + transfer.getTransferId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(transfer))).andExpect(status().isBadRequest());
    }

        @Test
        public void update_InvalidTransferId_ShouldReturnNotFound() throws Exception {
        final Transfer transfer = new Transfer(3001, 1, 1, 1001, 1002, new BigDecimal("350.00"));

        mockMvc.perform(put("/transfers/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(transfer)))
                .andExpect(status().isNotFound());
    }

        @Test
        public void delete_ShouldReturnNoContent() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/transfers")).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        List<Transfer> allTransfers = mapper.readValue(content,List.class);

        mockMvc.perform(delete("/transfers/5")).andExpect(status().isNoContent());
        mockMvc.perform(get("/transfers")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", IsCollectionWithSize.hasSize(allTransfers.size()-1)));
    }

        private String toJson(Transfer transfer) throws JsonProcessingException {
        return mapper.writeValueAsString(transfer);
    }

}
