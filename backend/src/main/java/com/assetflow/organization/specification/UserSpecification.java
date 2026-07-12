package com.assetflow.organization.specification;

import com.assetflow.organization.entity.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {
    public static Specification<User> hasRole(String roleName) {
        return (root, query, cb) -> roleName == null || roleName.isBlank() ? null : cb.equal(root.join("role").get("name"), roleName);
    }

    public static Specification<User> hasDepartment(Long deptId) {
        return (root, query, cb) -> deptId == null ? null : cb.equal(root.join("department").get("id"), deptId);
    }
}
