package com.assetflow.audits.controller;

import com.assetflow.audits.dto.AuditCycleRequest;
import com.assetflow.audits.dto.AuditCycleResponse;
import com.assetflow.audits.service.AuditCycleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/audits")
@RequiredArgsConstructor
@Tag(name = "Audits", description = "Endpoints for managing audit cycles")
public class AuditCycleController {

    private final AuditCycleService auditCycleService;

    @GetMapping
    @Operation(summary = "Get all audit cycles", description = "Retrieves a paginated list of all audit cycles")
    public ResponseEntity<Page<AuditCycleResponse>> getAllAuditCycles(Pageable pageable) {
        return ResponseEntity.ok(auditCycleService.getAllAuditCycles(pageable));
    }

    @PostMapping
    @Operation(summary = "Create audit cycle", description = "Creates a new audit cycle")
    public ResponseEntity<AuditCycleResponse> createAuditCycle(@Valid @RequestBody AuditCycleRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(auditCycleService.createAuditCycle(request));
    }

    @PostMapping("/{id}/complete")
    @Operation(summary = "Complete an audit cycle", description = "Marks an audit cycle as closed")
    public ResponseEntity<AuditCycleResponse> completeAuditCycle(@PathVariable Long id) {
        return ResponseEntity.ok(auditCycleService.completeAuditCycle(id));
    }
}
