package com.assetflow.maintenance.controller;

import com.assetflow.maintenance.dto.MaintenanceRecordRequest;
import com.assetflow.maintenance.dto.MaintenanceRecordResponse;
import com.assetflow.maintenance.service.MaintenanceService;
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
@RequestMapping("/api/v1/maintenance")
@RequiredArgsConstructor
@Tag(name = "Maintenance", description = "Endpoints for managing asset maintenance requests")
public class MaintenanceController {

    private final MaintenanceService maintenanceService;

    @GetMapping
    @Operation(summary = "Get all maintenance requests", description = "Retrieves a paginated list of all maintenance requests")
    public ResponseEntity<Page<MaintenanceRecordResponse>> getAllMaintenanceRequests(Pageable pageable) {
        return ResponseEntity.ok(maintenanceService.getAllMaintenanceRequests(pageable));
    }

    @PostMapping
    @Operation(summary = "Create maintenance request", description = "Creates a new maintenance request for an asset")
    public ResponseEntity<MaintenanceRecordResponse> createMaintenanceRequest(@Valid @RequestBody MaintenanceRecordRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(maintenanceService.createMaintenanceRequest(request));
    }

    @PostMapping("/{id}/complete")
    @Operation(summary = "Complete a maintenance request", description = "Marks a maintenance request as resolved and makes the asset available")
    public ResponseEntity<MaintenanceRecordResponse> completeMaintenance(@PathVariable Long id) {
        return ResponseEntity.ok(maintenanceService.completeMaintenance(id));
    }
}
