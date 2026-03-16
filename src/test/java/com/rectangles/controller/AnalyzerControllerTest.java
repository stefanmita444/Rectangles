package com.rectangles.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rectangles.domain.AdjacencyResult;
import com.rectangles.domain.ContainmentResult;
import com.rectangles.domain.IntersectionResult;
import com.rectangles.domain.Point;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AnalyzerController.class)
class AnalyzerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AnalyzerService analyzerService;

    @Test
    @DisplayName("POST /analyze/intersection returns 200 with correct body")
    void testIntersectionEndpoint() throws Exception {
        when(analyzerService.analyze(any()))
                .thenReturn(new IntersectionResult(true, List.of(new Point(4, 2), new Point(2, 4))));

        Rectangle a = new Rectangle(0, 0, 4, 4);
        Rectangle b = new Rectangle(2, 2, 6, 6);
        String body = objectMapper.writeValueAsString(new IntersectionRequest(a, b));

        mockMvc.perform(post("/analyze/intersection")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.hasIntersection").value(true))
                .andExpect(jsonPath("$.intersectionPoints").isArray())
                .andExpect(jsonPath("$.intersectionPoints.length()").value(2));
    }

    @Test
    @DisplayName("POST /analyze/containment returns 200 with correct body")
    void testContainmentEndpoint() throws Exception {
        when(analyzerService.analyze(any())).thenReturn(new ContainmentResult(Status.CONTAINED));

        Rectangle a = new Rectangle(0, 0, 10, 10);
        Rectangle b = new Rectangle(2, 2, 7, 7);
        String body = objectMapper.writeValueAsString(new ContainmentRequest(a, b));

        mockMvc.perform(post("/analyze/containment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("CONTAINED"));
    }

    @Test
    @DisplayName("POST /analyze/adjacency returns 200 with correct body")
    void testAdjacencyEndpoint() throws Exception {
        when(analyzerService.analyze(any())).thenReturn(new AdjacencyResult(Type.PROPER));

        Rectangle a = new Rectangle(0, 0, 4, 4);
        Rectangle b = new Rectangle(4, 0, 8, 4);
        String body = objectMapper.writeValueAsString(new AdjacencyRequest(a, b));

        mockMvc.perform(post("/analyze/adjacency")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("PROPER"))
                .andExpect(jsonPath("$.adjacent").value(true));
    }

    @Test
    @DisplayName("POST /analyze/intersection with missing body returns 400")
    void testIntersectionEndpointValidationError() throws Exception {
        mockMvc.perform(post("/analyze/intersection")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }
}
