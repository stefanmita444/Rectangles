package com.rectangles.service;

import com.rectangles.domain.AdjacencyResult;
import com.rectangles.domain.ContainmentResult;
import com.rectangles.domain.IntersectionResult;
import com.rectangles.domain.Status;
import com.rectangles.domain.Type;
import com.rectangles.dto.AdjacencyRequest;
import com.rectangles.dto.ContainmentRequest;
import com.rectangles.dto.IntersectionRequest;
import com.rectangles.service.strategy.AdjacencyStrategy;
import com.rectangles.service.strategy.ContainmentStrategy;
import com.rectangles.service.strategy.IntersectionStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.clearInvocations;

@ExtendWith(MockitoExtension.class)
class AnalyzerServiceTest {

    @Mock private IntersectionStrategy intersectionStrategy;
    @Mock private ContainmentStrategy containmentStrategy;
    @Mock private AdjacencyStrategy adjacencyStrategy;

    private AnalyzerService analyzerService;

    @BeforeEach
    void setUp() {
        when(intersectionStrategy.getSupportedRequestType()).thenReturn(IntersectionRequest.class);
        when(containmentStrategy.getSupportedRequestType()).thenReturn(ContainmentRequest.class);
        when(adjacencyStrategy.getSupportedRequestType()).thenReturn(AdjacencyRequest.class);
        analyzerService = new AnalyzerService(List.of(intersectionStrategy, containmentStrategy, adjacencyStrategy));
        clearInvocations(intersectionStrategy, containmentStrategy, adjacencyStrategy);
    }

    @Test
    @DisplayName("IntersectionRequest routes to IntersectionStrategy")
    void testRoutesToIntersectionStrategy() {
        IntersectionRequest request = mock(IntersectionRequest.class);
        IntersectionResult expected = new IntersectionResult(false, List.of());
        when(intersectionStrategy.analyze(request)).thenReturn(expected);

        var result = analyzerService.analyze(request);

        assertEquals(expected, result);
        verify(intersectionStrategy).analyze(request);
        verifyNoInteractions(containmentStrategy, adjacencyStrategy);
    }

    @Test
    @DisplayName("ContainmentRequest routes to ContainmentStrategy")
    void testRoutesToContainmentStrategy() {
        ContainmentRequest request = mock(ContainmentRequest.class);
        ContainmentResult expected = new ContainmentResult(Status.CONTAINED);
        when(containmentStrategy.analyze(request)).thenReturn(expected);

        var result = analyzerService.analyze(request);

        assertEquals(expected, result);
        verify(containmentStrategy).analyze(request);
        verifyNoInteractions(intersectionStrategy, adjacencyStrategy);
    }

    @Test
    @DisplayName("AdjacencyRequest routes to AdjacencyStrategy")
    void testRoutesToAdjacencyStrategy() {
        AdjacencyRequest request = mock(AdjacencyRequest.class);
        AdjacencyResult expected = new AdjacencyResult(Type.PROPER);
        when(adjacencyStrategy.analyze(request)).thenReturn(expected);

        var result = analyzerService.analyze(request);

        assertEquals(expected, result);
        verify(adjacencyStrategy).analyze(request);
        verifyNoInteractions(intersectionStrategy, containmentStrategy);
    }

    @Test
    @DisplayName("Null request returns null")
    void testNullRequestReturnsNull() {
        assertNull(analyzerService.analyze(null));
    }

    @Test
    @DisplayName("Unknown request type returns null")
    void testUnknownRequestTypeReturnsNull() {
        var unknownRequest = mock(com.rectangles.dto.Request.class);
        assertNull(analyzerService.analyze(unknownRequest));
        verifyNoInteractions(intersectionStrategy, containmentStrategy, adjacencyStrategy);
    }
}