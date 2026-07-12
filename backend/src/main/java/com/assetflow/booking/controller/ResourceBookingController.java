package com.assetflow.booking.controller;

import com.assetflow.booking.dto.BookableResourceResponse;
import com.assetflow.booking.dto.ResourceBookingRequest;
import com.assetflow.booking.dto.ResourceBookingResponse;
import com.assetflow.booking.service.ResourceBookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Resource Bookings", description = "Endpoints for managing bookable resources and bookings")
public class ResourceBookingController {

    private final ResourceBookingService bookingService;

    @GetMapping("/resources")
    @Operation(summary = "Get all resources", description = "Retrieves a paginated list of all bookable resources")
    public ResponseEntity<Page<BookableResourceResponse>> getAllResources(Pageable pageable) {
        return ResponseEntity.ok(bookingService.getAllResources(pageable));
    }

    @GetMapping("/bookings")
    @Operation(summary = "Get all bookings", description = "Retrieves a paginated list of all resource bookings")
    public ResponseEntity<Page<ResourceBookingResponse>> getAllBookings(Pageable pageable) {
        return ResponseEntity.ok(bookingService.getAllBookings(pageable));
    }

    @PostMapping("/bookings")
    @Operation(summary = "Create a booking", description = "Creates a new resource booking if the time slot is available")
    public ResponseEntity<ResourceBookingResponse> createBooking(@Valid @RequestBody ResourceBookingRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.createBooking(request));
    }

    @PostMapping("/bookings/{id}/cancel")
    @Operation(summary = "Cancel a booking", description = "Cancels an active or pending booking")
    public ResponseEntity<ResourceBookingResponse> cancelBooking(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.cancelBooking(id));
    }
}
