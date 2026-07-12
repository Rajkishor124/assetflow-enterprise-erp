export interface AuditCycleResponse {
  id: number;
  name: string;
  description?: string;
  startDate: string;
  endDate?: string;
  auditStatus: 'OPEN' | 'CLOSED';
  initiatedById: number;
  initiatedByName: string;
  createdAt: string;
  updatedAt: string;
}

export interface AuditCycleRequest {
  name: string;
  description?: string;
  startDate: string;
  initiatedById: number;
}

export interface PaginatedAuditCycleResponse {
  content: AuditCycleResponse[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}
