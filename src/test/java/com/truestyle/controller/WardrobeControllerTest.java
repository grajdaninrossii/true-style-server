package com.truestyle.controller;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

class WardrobeControllerTest {


//    @Test
//    public void addUsersStuffInWardrobe() throws Exception {
//        MockMultipartFile employeeJson = new MockMultipartFile("employee", null,
//                "application/json", "{\"name\": \"Emp Name\"}".getBytes());
//
//        MockMvc mockMvc = webAppContextSetup(wac).build();
//
//        mockMvc.perform(multipart("/requestpart/employee")
//                        .file(A_FILE)
//                        .file(employeeJson))
//                .andExpect(status().isOk());
//    }
}