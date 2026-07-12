package com.assetflow.assets.service.command;

import com.assetflow.assets.dto.request.AssetRequest;
import com.assetflow.assets.entity.Asset;
import com.assetflow.assets.entity.AssetCategory;
import com.assetflow.assets.enums.AssetStatus;
import com.assetflow.assets.mapper.AssetMapper;
import com.assetflow.assets.repository.AssetRepository;
import com.assetflow.assets.service.query.AssetCategoryQueryService;
import com.assetflow.assets.service.query.AssetQueryService;
import com.assetflow.exception.DuplicateResourceException;
import com.assetflow.exception.InvalidStateException;
import com.assetflow.organization.entity.Department;
import com.assetflow.organization.entity.User;
import com.assetflow.organization.service.query.DepartmentQueryService;
import com.assetflow.organization.service.query.UserQueryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AssetCommandServiceTest {

    @Mock
    private AssetRepository assetRepository;

    @Mock
    private AssetMapper assetMapper;

    @Mock
    private AssetQueryService assetQueryService;

    @Mock
    private AssetCategoryQueryService assetCategoryQueryService;

    @Mock
    private UserQueryService userQueryService;

    @Mock
    private DepartmentQueryService departmentQueryService;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private AssetCommandServiceImpl assetCommandService;

    private AssetRequest request;
    private Asset asset;

    @BeforeEach
    void setUp() {
        request = new AssetRequest();
        request.setAssetTag("TAG-123");
        request.setSerialNumber("SN-123");
        request.setCategoryId(1L);

        asset = new Asset();
        asset.setId(1L);
        asset.setAssetTag("TAG-123");
        asset.setSerialNumber("SN-123");
        asset.setLifecycleStatus(AssetStatus.AVAILABLE);
    }

    @Test
    void createAsset_ShouldThrowDuplicateResourceException_WhenTagExists() {
        when(assetRepository.existsByAssetTag("TAG-123")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> assetCommandService.createAsset(request));
    }

    @Test
    void allocateAsset_ShouldThrowInvalidStateException_WhenAssetNotAvailable() {
        asset.setLifecycleStatus(AssetStatus.RETIRED);
        when(assetQueryService.findActiveEntityById(1L)).thenReturn(asset);

        assertThrows(InvalidStateException.class, () -> assetCommandService.allocateAsset(1L, 1L, null));
    }

    @Test
    void retireAsset_ShouldThrowInvalidStateException_WhenAssetAlreadyRetired() {
        asset.setLifecycleStatus(AssetStatus.RETIRED);
        when(assetQueryService.findActiveEntityById(1L)).thenReturn(asset);

        assertThrows(InvalidStateException.class, () -> assetCommandService.retireAsset(1L));
    }

    @Test
    void allocateAsset_ShouldChangeStatusToAllocated() {
        asset.setLifecycleStatus(AssetStatus.AVAILABLE);
        when(assetQueryService.findActiveEntityById(1L)).thenReturn(asset);
        
        User user = new User();
        user.setId(1L);
        when(userQueryService.findActiveEntityById(1L)).thenReturn(user);

        assetCommandService.allocateAsset(1L, 1L, null);

        assertEquals(AssetStatus.ALLOCATED, asset.getLifecycleStatus());
        verify(assetRepository).save(asset);
        verify(eventPublisher).publishEvent(any(com.assetflow.assets.event.AssetAllocatedEvent.class));
    }
}
