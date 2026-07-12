package com.assetflow.booking.service;

import com.assetflow.booking.dto.BookableResourceResponse;
import com.assetflow.booking.dto.ResourceBookingRequest;
import com.assetflow.booking.dto.ResourceBookingResponse;
import com.assetflow.booking.entity.BookableResource;
import com.assetflow.booking.entity.ResourceBooking;
import com.assetflow.booking.enums.BookingStatus;
import com.assetflow.booking.mapper.ResourceBookingMapper;
import com.assetflow.booking.repository.BookableResourceRepository;
import com.assetflow.booking.repository.ResourceBookingRepository;
import com.assetflow.exception.ResourceNotFoundException;
import com.assetflow.organization.entity.User;
import com.assetflow.organization.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ResourceBookingService {

    private final BookableResourceRepository resourceRepository;
    private final ResourceBookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ResourceBookingMapper mapper;

    @Transactional(readOnly = true)
    public Page<BookableResourceResponse> getAllResources(Pageable pageable) {
        return resourceRepository.findAll(pageable)
                .map(mapper::toResourceResponse);
    }

    @Transactional(readOnly = true)
    public Page<ResourceBookingResponse> getAllBookings(Pageable pageable) {
        return bookingRepository.findAll(pageable)
                .map(mapper::toBookingResponse);
    }

    @Transactional
    public ResourceBookingResponse createBooking(ResourceBookingRequest request) {
        BookableResource resource = resourceRepository.findById(request.getResourceId())
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found"));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (request.getStartTime().isAfter(request.getEndTime())) {
            throw new IllegalArgumentException("Start time cannot be after end time");
        }

        if (request.getStartTime().isBefore(Instant.now())) {
            throw new IllegalArgumentException("Cannot book in the past");
        }

        // Check for conflicts
        // Find any ACTIVE or PENDING booking that overlaps with the requested time
        boolean hasConflict = bookingRepository.findAll().stream()
                .filter(b -> b.getResource().getId().equals(resource.getId()))
                .filter(b -> b.getBookingStatus() == BookingStatus.CONFIRMED || b.getBookingStatus() == BookingStatus.PENDING)
                .anyMatch(b -> 
                        (request.getStartTime().isBefore(b.getEndTime()) && request.getEndTime().isAfter(b.getStartTime()))
                );

        if (hasConflict) {
            throw new IllegalArgumentException("The resource is already booked during this time period");
        }

        ResourceBooking booking = new ResourceBooking();
        booking.setResource(resource);
        booking.setUser(user);
        booking.setStartTime(request.getStartTime());
        booking.setEndTime(request.getEndTime());
        booking.setPurpose(request.getPurpose());
        booking.setBookingStatus(BookingStatus.CONFIRMED); // Auto-approve for MVP

        return mapper.toBookingResponse(bookingRepository.save(booking));
    }

    @Transactional
    public ResourceBookingResponse cancelBooking(Long id) {
        ResourceBooking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (booking.getBookingStatus() == BookingStatus.COMPLETED || booking.getBookingStatus() == BookingStatus.CANCELLED) {
            throw new IllegalArgumentException("Cannot cancel a completed or already cancelled booking");
        }

        booking.setBookingStatus(BookingStatus.CANCELLED);
        return mapper.toBookingResponse(bookingRepository.save(booking));
    }
}
