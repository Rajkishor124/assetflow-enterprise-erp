export interface DepartmentRequest {
  name: string;
  parentId?: number | null;
}

export interface DepartmentSummaryResponse {
  id: number;
  name: string;
  parentId?: number | null;
}

export interface DepartmentDetailResponse {
  id: number;
  name: string;
  parent?: DepartmentSummaryResponse | null;
  children: DepartmentSummaryResponse[];
  status: string;
  createdAt: string;
  updatedAt: string;
  version: number;
}

export interface RoleSummaryResponse {
  id: number;
  name: string;
}

export interface UserRequest {
  firstName: string;
  lastName: string;
  email: string;
  password?: string; // Optional for updates, required for create
  departmentId?: number | null;
  roleId: number;
}

export interface UserSummaryResponse {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  departmentName?: string | null;
  roleName: string;
}

export interface UserDetailResponse {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  department?: DepartmentSummaryResponse | null;
  role: RoleSummaryResponse;
  status: string;
  createdAt: string;
  updatedAt: string;
  version: number;
}
