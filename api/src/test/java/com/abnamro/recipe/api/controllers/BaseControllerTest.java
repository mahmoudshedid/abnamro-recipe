package com.abnamro.recipe.api.controllers;

import com.abnamro.recipe.api.payloads.response.ApiResponsePayload;
import com.abnamro.recipe.domain.utils.ResourceLoader;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
public class BaseControllerTest {

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;

    protected <T> T request(
            MockHttpServletRequestBuilder requestBuilder,
            String requestPayloadPath,
            HttpStatus expectedStatus,
            Class<T> responseClass) throws Exception {

        MockHttpServletResponse response = request(requestBuilder, requestPayloadPath, expectedStatus).andReturn().getResponse();
        return objectMapper.readValue(response.getContentAsString(), responseClass);
    }

    protected ResultActions request(
            MockHttpServletRequestBuilder requestBuilder,
            String requestPayloadPath,
            HttpStatus expectedStatus) throws Exception {
        String payload = ResourceLoader.getStringFromFile(requestPayloadPath);
        return request(requestBuilder
                        .content(payload)
                        .contentType(MediaType.APPLICATION_JSON),
                expectedStatus);
    }

    protected <T> T request(
            MockHttpServletRequestBuilder requestBuilder,
            Class<T> responseClass) throws Exception {
        MockHttpServletResponse response = request(requestBuilder, HttpStatus.OK).andReturn().getResponse();
        return objectMapper.readValue(response.getContentAsString(), responseClass);
    }

    protected String requestStringResponse(
            MockHttpServletRequestBuilder requestBuilder,
            HttpStatus expectedStatus) throws Exception {
        MockHttpServletResponse response = request(requestBuilder, expectedStatus).andReturn().getResponse();
        return response.getContentAsString();
    }

    protected ResultActions request(
            MockHttpServletRequestBuilder requestBuilder,
            HttpStatus expectedStatus) throws Exception {
        return mockMvc.perform(requestBuilder)
                .andExpect(status().is(expectedStatus.value()));
    }

    protected <T> List<T> requestList(
            MockHttpServletRequestBuilder requestBuilder,
            HttpStatus expectedStatus,
            Class<T> responseClass) throws Exception {
        MockHttpServletResponse response = request(requestBuilder, expectedStatus).andReturn().getResponse();
        return objectMapper.readValue(response.getContentAsString(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, responseClass));
    }

    protected <T> ApiResponsePayload<T> requestPaginated(
            MockHttpServletRequestBuilder requestBuilder,
            HttpStatus expectedStatus,
            Class<T> responseClass) throws Exception {
        MockHttpServletResponse response = request(requestBuilder, expectedStatus).andReturn().getResponse();
        return objectMapper.readValue(response.getContentAsString(),
                objectMapper.getTypeFactory().constructParametricType(ApiResponsePayload.class, responseClass));
    }

    protected <T> T requestStringPayload(
            MockHttpServletRequestBuilder requestBuilder,
            String requestPayloadPath,
            HttpStatus expectedStatus,
            Class<T> responseClass) throws Exception {

        MockHttpServletResponse response = requestStringPayload(requestBuilder, requestPayloadPath, expectedStatus).andReturn().getResponse();
        return objectMapper.readValue(response.getContentAsString(), responseClass);
    }

    protected ResultActions requestStringPayload(
            MockHttpServletRequestBuilder requestBuilder,
            String requestPayload,
            HttpStatus expectedStatus) throws Exception {
        return request(requestBuilder
                        .content(requestPayload)
                        .contentType(MediaType.APPLICATION_JSON),
                expectedStatus);
    }

}
