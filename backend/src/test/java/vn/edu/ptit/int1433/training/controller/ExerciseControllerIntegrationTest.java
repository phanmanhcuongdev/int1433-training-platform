package vn.edu.ptit.int1433.training.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import vn.edu.ptit.int1433.training.AbstractPostgresIntegrationTest;

@AutoConfigureMockMvc
class ExerciseControllerIntegrationTest extends AbstractPostgresIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void listReturnsOk() throws Exception {
        mockMvc.perform(get("/api/v1/exercises"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.totalItems").value(10))
            .andExpect(jsonPath("$.items", hasSize(10)));
    }

    @Test
    void filteringWorks() throws Exception {
        mockMvc.perform(get("/api/v1/exercises").queryParam("technology", "TCP").queryParam("level", "L2"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.totalItems").value(1))
            .andExpect(jsonPath("$.items[0].id").value("tcp-character-normalize-001"));
    }

    @Test
    void detailReturnsOk() throws Exception {
        mockMvc.perform(get("/api/v1/exercises/tcp-character-normalize-001"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value("tcp-character-normalize-001"))
            .andExpect(jsonPath("$.sourceLabel").value("OBSERVED"))
            .andExpect(jsonPath("$.evaluationMode").value("NETWORK_CHALLENGE"));
    }

    @Test
    void starterDownloadReturnsZipForNetworkExercise() throws Exception {
        mockMvc.perform(get("/api/v1/exercises/tcp-byte-prime-sum-001/starter"))
            .andExpect(status().isOk())
            .andExpect(header().string("Content-Type", containsString("application/zip")))
            .andExpect(header().string("Content-Disposition", containsString("tcp-byte-prime-sum-001-starter.zip")));
    }

    @Test
    void unknownDetailReturns404() throws Exception {
        mockMvc.perform(get("/api/v1/exercises/not-found"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.code").value("EXERCISE_NOT_FOUND"))
            .andExpect(jsonPath("$.message", containsString("not-found")));
    }

    @Test
    void invalidPaginationReturns400() throws Exception {
        mockMvc.perform(get("/api/v1/exercises").queryParam("size", "0"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("BAD_REQUEST"));
    }
}
