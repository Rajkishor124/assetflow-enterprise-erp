package com.assetflow.assets.specification;

import com.assetflow.assets.entity.Asset;
import com.assetflow.assets.enums.AssetStatus;
import org.springframework.data.jpa.domain.Specification;

public class AssetSpecification {
    public static Specification<Asset> hasCategory(Long categoryId) {
        return (root, query, cb) -> categoryId == null ? null : cb.equal(root.join("category").get("id"), categoryId);
    }

    public static Specification<Asset> hasStatus(AssetStatus status) {
        return (root, query, cb) -> status == null ? null : cb.equal(root.get("lifecycleStatus"), status);
    }

    public static Specification<Asset> isAssignedTo(Long userId) {
        return (root, query, cb) -> userId == null ? null : cb.equal(root.join("assignedUser").get("id"), userId);
    }

    public static Specification<Asset> search(String q) {
        return (root, query, cb) -> {
            if (q == null || q.isBlank()) return null;
            String pattern = "%" + q.toLowerCase() + "%";
            return cb.or(
                cb.like(cb.lower(root.get("name")), pattern),
                cb.like(cb.lower(root.get("assetTag")), pattern),
                cb.like(cb.lower(root.get("serialNumber")), pattern)
            );
        };
    }

    public static Specification<Asset> visibleToUser(Long userId, boolean isEmployeeOnly) {
        return (root, query, cb) -> {
            if (!isEmployeeOnly) return null;
            return cb.or(
                cb.equal(root.get("lifecycleStatus"), AssetStatus.AVAILABLE),
                cb.equal(root.join("assignedUser").get("id"), userId)
            );
        };
    }
}
