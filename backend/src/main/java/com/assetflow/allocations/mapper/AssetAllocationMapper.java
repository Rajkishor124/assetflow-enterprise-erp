package com.assetflow.allocations.mapper;

import com.assetflow.allocations.dto.AssetAllocationResponse;
import com.assetflow.allocations.entity.AssetAllocation;
import org.springframework.stereotype.Component;

@Component
public class AssetAllocationMapper {

    public AssetAllocationResponse toResponse(AssetAllocation allocation) {
        if (allocation == null) {
            return null;
        }

        return AssetAllocationResponse.builder()
                .id(allocation.getId())
                .assetId(allocation.getAsset() != null ? allocation.getAsset().getId() : null)
                .assetName(allocation.getAsset() != null ? allocation.getAsset().getName() : null)
                .assetTag(allocation.getAsset() != null ? allocation.getAsset().getAssetTag() : null)
                .userId(allocation.getUser() != null ? allocation.getUser().getId() : null)
                .userName(allocation.getUser() != null ? allocation.getUser().getFirstName() + " " + allocation.getUser().getLastName() : null)
                .departmentId(allocation.getDepartment() != null ? allocation.getDepartment().getId() : null)
                .departmentName(allocation.getDepartment() != null ? allocation.getDepartment().getName() : null)
                .allocationDate(allocation.getAllocationDate())
                .expectedReturnDate(allocation.getExpectedReturnDate())
                .actualReturnDate(allocation.getActualReturnDate())
                .allocationStatus(allocation.getAllocationStatus())
                .notes(allocation.getNotes())
                .build();
    }
}
