package com.rectangles.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rectangles.domain.AdjacencyResult;
import com.rectangles.domain.ContainmentResult;
import com.rectangles.domain.IntersectionResult;
import com.rectangles.domain.Rectangle;
import com.rectangles.domain.Status;
import com.rectangles.domain.Type;
import com.rectangles.dto.AdjacencyRequest;
import com.rectangles.dto.ContainmentRequest;
import com.rectangles.dto.IntersectionRequest;
import com.rectangles.service.AnalyzerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AnalyzerController.class)
class AnalyzerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AnalyzerService analyzerService;

    @Test
    @DisplayName("GET /analyze/intersection returns 200")
    void testIntersectionEndpoint() throws Exception {
        when(analyzerService.analyze(any())).thenReturn(new IntersectionResult(false, List.of()));

        Rectangle r = new Rectangle(0, 0, 4, 4);
        String body = objectMapper.writeValueAsString(new IntersectionRequest(r, r));

        mockMvc.perform(get("/analyze/intersection")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /analyze/containment returns 200")
    void testContainmentEndpoint() throws Exception {
        when(analyzerService.analyze(any())).thenReturn(new ContainmentResult(Status.NO_CONTAINMENT));

        Rectangle r = new Rectangle(0, 0, 4, 4);
        String body = objectMapper.writeValueAsString(new ContainmentRequest(r, r));

        mockMvc.perform(get("/analyze/containment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /analyze/adjacency returns 200")
    void testAdjacencyEndpoint() throws Exception {
        when(analyzerService.analyze(any())).thenReturn(new AdjacencyResult(Type.NOT_ADJACENT));

        Rectangle r = new Rectangle(0, 0, 4, 4);
        String body = objectMapper.writeValueAsString(new AdjacencyRequest(r, r));

        mockMvc.perform(get("/analyze/adjacency")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk());
    }
}
