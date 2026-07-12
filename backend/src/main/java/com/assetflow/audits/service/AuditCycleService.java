package com.assetflow.audits.service;

import com.assetflow.audits.dto.AuditCycleRequest;
import com.assetflow.audits.dto.AuditCycleResponse;
import com.assetflow.audits.entity.AuditCycle;
import com.assetflow.audits.enums.AuditStatus;
import com.assetflow.audits.mapper.AuditMapper;
import com.assetflow.audits.repository.AuditCycleRepository;
import com.assetflow.exception.ResourceNotFoundException;
import com.assetflow.organization.entity.User;
import com.assetflow.organization.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuditCycleService {

    private final AuditCycleRepository auditCycleRepository;
    private final UserRepository userRepository;
    private final AuditMapper mapper;

    @Transactional(readOnly = true)
    public Page<AuditCycleResponse> getAllAuditCycles(Pageable pageable) {
        return auditCycleRepository.findAll(pageable)
                .map(mapper::toResponse);
    }

    @Transactional
    public AuditCycleResponse createAuditCycle(AuditCycleRequest request) {
        User initiator = userRepository.findById(request.getInitiatedById())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        AuditCycle cycle = new AuditCycle();
        cycle.setName(request.getName());
        cycle.setDescription(request.getDescription());
        cycle.setStartDate(request.getStartDate());
        cycle.setEndDate(request.getEndDate());
        cycle.setInitiatedBy(initiator);
        cycle.setAuditStatus(AuditStatus.OPEN);

        return mapper.toResponse(auditCycleRepository.save(cycle));
    }

    @Transactional
    public AuditCycleResponse completeAuditCycle(Long id) {
        AuditCycle cycle = auditCycleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Audit cycle not found"));

        if (cycle.getAuditStatus() == AuditStatus.CLOSED) {
            throw new IllegalArgumentException("Audit cycle is already closed");
        }

        cycle.setAuditStatus(AuditStatus.CLOSED);
        cycle.setEndDate(Instant.now());

        return mapper.toResponse(auditCycleRepository.save(cycle));
    }
}
