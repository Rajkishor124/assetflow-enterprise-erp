export interface AssetCategoryRequest {
  name: string;
  description?: string | null;
  parentCategoryId?: number | null;
}

export interface AssetCategorySummaryResponse {
  id: number;
  name: string;
  parentCategoryId?: number | null;
  parentCategoryName?: string | null;
  status: string;
  createdAt: string;
}

export interface AssetCategoryDetailResponse {
  id: number;
  name: string;
  description?: string | null;
  parentCategoryId?: number | null;
  parentCategoryName?: string | null;
  status: string;
  createdAt: string;
  updatedAt: string;
}

export interface AssetDocumentRequest {
  assetId: number;
  documentType: string;
  fileUrl: string;
  fileName: string;
}

export interface AssetDocumentResponse {
  id: number;
  assetId: number;
  documentType: string;
  fileUrl: string;
  fileName: string;
  status: string;
  createdAt: string;
}

export interface AssetRequest {
  assetTag: string;
  name: string;
  description?: string | null;
  serialNumber: string;
  manufacturer?: string | null;
  model?: string | null;
  purchasePrice?: number | null;
  purchaseDate?: string | null; // ISO Date String YYYY-MM-DD
  warrantyExpiry?: string | null; // ISO Date String YYYY-MM-DD
  categoryId: number;
}

export interface AssetSummaryResponse {
  id: number;
  assetTag: string;
  name: string;
  serialNumber: string;
  manufacturer?: string | null;
  model?: string | null;
  lifecycleStatus: string;
  categoryId?: number | null;
  categoryName?: string | null;
  departmentId?: number | null;
  departmentName?: string | null;
  assignedUserId?: number | null;
  assignedUserName?: string | null;
}

export interface AssetDetailResponse {
  id: number;
  assetTag: string;
  name: string;
  description?: string | null;
  serialNumber: string;
  manufacturer?: string | null;
  model?: string | null;
  purchasePrice?: number | null;
  purchaseDate?: string | null;
  warrantyExpiry?: string | null;
  lifecycleStatus: string;
  categoryId?: number | null;
  categoryName?: string | null;
  departmentId?: number | null;
  departmentName?: string | null;
  assignedUserId?: number | null;
  assignedUserName?: string | null;
  status: string;
  createdAt: string;
  updatedAt: string;
  documents: AssetDocumentResponse[];
}
