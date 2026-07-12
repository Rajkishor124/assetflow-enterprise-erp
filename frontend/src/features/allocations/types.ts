export interface AssetAllocationResponse {
  id: number;
  assetId: number;
  assetName: string;
  assetTag: string;
  userId?: number;
  userName?: string;
  departmentId?: number;
  departmentName?: string;
  allocationDate: string;
  expectedReturnDate?: string;
  actualReturnDate?: string;
  allocationStatus: 'ACTIVE' | 'RETURNED' | 'CANCELLED';
  notes?: string;
}

export interface AssetAllocationRequest {
  assetId: number;
  userId?: number;
  departmentId?: number;
  allocationDate: string;
  expectedReturnDate?: string;
  notes?: string;
}

export interface PaginatedAllocationResponse {
  content: AssetAllocationResponse[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}
