package com.assetflow.organization.validation;

import com.assetflow.exception.InvalidStateException;
import org.springframework.stereotype.Component;

@Component
public class DepartmentHierarchyValidator {
    
    public void validateNotSelfParent(Long departmentId, Long parentId) {
        if (departmentId != null && departmentId.equals(parentId)) {
            throw new InvalidStateException("A department cannot be its own parent.");
        }
    }
}
