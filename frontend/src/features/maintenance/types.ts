export interface MaintenanceRecordResponse {
  id: number;
  assetId: number;
  assetName: string;
  assetTag: string;
  reportedById: number;
  reportedByName: string;
  title: string;
  description?: string;
  priority: 'LOW' | 'MEDIUM' | 'HIGH';
  maintenanceStatus: 'PENDING' | 'APPROVED' | 'IN_PROGRESS' | 'RESOLVED' | 'CLOSED';
  scheduledDate?: string;
  resolutionDate?: string;
  createdAt: string;
  updatedAt: string;
}

export interface MaintenanceRecordRequest {
  assetId: number;
  reportedById: number;
  title: string;
  description?: string;
  priority: 'LOW' | 'MEDIUM' | 'HIGH';
  scheduledDate?: string;
}

export interface PaginatedMaintenanceResponse {
  content: MaintenanceRecordResponse[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}
