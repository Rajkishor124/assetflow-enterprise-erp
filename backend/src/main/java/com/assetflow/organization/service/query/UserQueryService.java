package com.assetflow.organization.service.query;

import com.assetflow.exception.ResourceNotFoundException;
import com.assetflow.organization.dto.response.UserDetailResponse;
import com.assetflow.organization.dto.response.UserSummaryResponse;
import com.assetflow.organization.entity.User;
import com.assetflow.organization.mapper.UserMapper;
import com.assetflow.organization.repository.UserRepository;
import com.assetflow.shared.enums.RecordStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.assetflow.shared.service.BaseQueryService;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserQueryService extends BaseQueryService<User, Long, UserRepository> {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    protected UserRepository getRepository() {
        return userRepository;
    }

    @Override
    protected String getResourceName() {
        return "User";
    }

    public Page<UserSummaryResponse> findAllActive(Pageable pageable) {
        return userRepository.findAll((root, query, cb) -> 
            cb.equal(root.get("status"), RecordStatus.ACTIVE), pageable)
            .map(userMapper::toSummaryResponse);
    }

    public UserDetailResponse findActiveById(Long id) {
        User user = findActiveEntityById(id);
        return userMapper.toDetailResponse(user);
    }
}
