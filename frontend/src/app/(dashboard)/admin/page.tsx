'use client';

import DepartmentList from '@/features/organizations/components/DepartmentList';
import UserList from '@/features/organizations/components/UserList';

export default function AdminPage() {
  return (
    <div className="space-y-8">
      <div>
        <h1 className="text-3xl font-bold text-gray-900 mb-6">Admin Setup</h1>
        <p className="text-gray-500 mb-8">Manage departments, users, and system configuration.</p>
      </div>

      <DepartmentList />
      
      <UserList />
    </div>
  );
}
