package com.techelevator.tenmo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techelevator.tenmo.model.Transfer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TransferControllerIntTest {
    @Autowired
    TransferController controller;

    @Autowired
    ObjectMapper mapper;

    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception{
        System.out.println("setup()...");
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void create_ValidTransfer_ShouldAddNewTransfer() throws Exception{
        final Transfer transfer = new Transfer();

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(transfer)))
                        .andExpect(status().isCreated());
    }

    private String toJson(Transfer transfer) throws JsonProcessingException {
        return mapper.writeValueAsString(transfer);
    }
}
