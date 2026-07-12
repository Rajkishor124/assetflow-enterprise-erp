'use client';

import { useState, useEffect } from 'react';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { userApi, departmentApi } from '../api';
import { X } from 'lucide-react';

interface Props {
  userId: number | null;
  onClose: () => void;
}

export default function UserFormModal({ userId, onClose }: Props) {
  const queryClient = useQueryClient();

  const [role, setRole] = useState('');
  const [departmentId, setDepartmentId] = useState<number | null>(null);

  const { data: userData } = useQuery({
    queryKey: ['user', userId],
    queryFn: () => userApi.getById(userId!),
    enabled: !!userId,
  });

  const { data: deptData } = useQuery({
    queryKey: ['departments'],
    queryFn: () => departmentApi.getAll(),
  });
  const departments = deptData?.data?.content || [];

  useEffect(() => {
    if (userData?.data) {
      // eslint-disable-next-line react-hooks/set-state-in-effect
      setRole(userData.data.role?.name || 'EMPLOYEE');
      // eslint-disable-next-line react-hooks/set-state-in-effect
      setDepartmentId(userData.data.department?.id || null);
    }
  }, [userData]);

  const updateRoleMutation = useMutation({
    mutationFn: (data: { role: string }) => userApi.updateRole(userId!, data),
  });

  const updateMutation = useMutation({
    mutationFn: (data: any) => userApi.update(userId!, data),
  });

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!userId || !userData?.data) return;

    try {
      if (role !== userData.data.role?.name) {
        await updateRoleMutation.mutateAsync({ role });
      }

      if (departmentId !== userData.data.department?.id) {
        // Send existing user data but with updated departmentId
        const updatePayload = {
          firstName: userData.data.firstName,
          lastName: userData.data.lastName,
          email: userData.data.email,
          departmentId: departmentId,
          roleId: userData.data.role.id,
        };
        await updateMutation.mutateAsync(updatePayload);
      }

      queryClient.invalidateQueries({ queryKey: ['users'] });
      onClose();
    } catch (err) {
      console.error(err);
    }
  };

  const isPending = updateRoleMutation.isPending || updateMutation.isPending;

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
      <div className="bg-white rounded-lg shadow-xl w-full max-w-md flex flex-col">
        <div className="flex justify-between items-center p-6 border-b">
          <h2 className="text-xl font-semibold text-gray-800">
            Edit User
          </h2>
          <button onClick={onClose} className="text-gray-500 hover:text-gray-700">
            <X size={24} />
          </button>
        </div>

        <div className="p-6">
          <form id="user-form" onSubmit={handleSubmit} className="space-y-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Role *</label>
              <select
                required
                value={role}
                onChange={(e) => setRole(e.target.value)}
                className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                <option value="EMPLOYEE">EMPLOYEE</option>
                <option value="DEPT_HEAD">DEPT_HEAD</option>
                <option value="ASSET_MANAGER">ASSET_MANAGER</option>
                <option value="ADMIN">ADMIN</option>
              </select>
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Department</label>
              <select
                value={departmentId || ''}
                onChange={(e) => setDepartmentId(e.target.value ? Number(e.target.value) : null)}
                className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                <option value="">None</option>
                {departments.map((dept) => (
                  <option key={dept.id} value={dept.id}>{dept.name}</option>
                ))}
              </select>
            </div>
          </form>
        </div>

        <div className="p-6 border-t flex justify-end gap-3 bg-gray-50 rounded-b-lg">
          <button
            type="button"
            onClick={onClose}
            className="px-4 py-2 border rounded-md text-gray-600 hover:bg-gray-100 transition-colors"
          >
            Cancel
          </button>
          <button
            type="submit"
            form="user-form"
            disabled={isPending}
            className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition-colors disabled:opacity-50"
          >
            {isPending ? 'Saving...' : 'Save'}
          </button>
        </div>
      </div>
    </div>
  );
}
