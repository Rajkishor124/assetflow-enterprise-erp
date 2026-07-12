package com.assetflow.shared.specification;

import com.assetflow.shared.enums.RecordStatus;
import org.springframework.data.jpa.domain.Specification;

public class BaseSpecification {

    public static <T> Specification<T> isStatus(RecordStatus status) {
        return (root, query, cb) -> cb.equal(root.get("status"), status);
    }
    
    public static <T> Specification<T> isActive() {
        return isStatus(RecordStatus.ACTIVE);
    }
}
