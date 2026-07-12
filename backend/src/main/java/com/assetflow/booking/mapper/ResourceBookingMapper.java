package com.assetflow.booking.mapper;

import com.assetflow.booking.dto.BookableResourceResponse;
import com.assetflow.booking.dto.ResourceBookingResponse;
import com.assetflow.booking.entity.BookableResource;
import com.assetflow.booking.entity.ResourceBooking;
import org.springframework.stereotype.Component;

@Component
public class ResourceBookingMapper {

    public BookableResourceResponse toResourceResponse(BookableResource resource) {
        if (resource == null) {
            return null;
        }
        
        return BookableResourceResponse.builder()
                .id(resource.getId())
                .name(resource.getName())
                .resourceType(resource.getResourceType())
                .description(resource.getDescription())
                .categoryId(resource.getCategory() != null ? resource.getCategory().getId() : null)
                .categoryName(resource.getCategory() != null ? resource.getCategory().getName() : null)
                .build();
    }

    public ResourceBookingResponse toBookingResponse(ResourceBooking booking) {
        if (booking == null) {
            return null;
        }
        
        return ResourceBookingResponse.builder()
                .id(booking.getId())
                .resourceId(booking.getResource() != null ? booking.getResource().getId() : null)
                .resourceName(booking.getResource() != null ? booking.getResource().getName() : null)
                .resourceType(booking.getResource() != null ? booking.getResource().getResourceType() : null)
                .userId(booking.getUser() != null ? booking.getUser().getId() : null)
                .userName(booking.getUser() != null ? booking.getUser().getFirstName() + " " + booking.getUser().getLastName() : null)
                .startTime(booking.getStartTime())
                .endTime(booking.getEndTime())
                .bookingStatus(booking.getBookingStatus())
                .purpose(booking.getPurpose())
                .build();
    }
}
