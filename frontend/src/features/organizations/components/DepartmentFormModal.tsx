'use client';

import { useState, useEffect } from 'react';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { departmentApi } from '../api';
import { DepartmentRequest } from '../types';
import { X } from 'lucide-react';
import toast from 'react-hot-toast';

interface Props {
  deptId: number | null;
  onClose: () => void;
}

export default function DepartmentFormModal({ deptId, onClose }: Props) {
  const queryClient = useQueryClient();
  const isEditing = deptId !== null;

  const [formData, setFormData] = useState<DepartmentRequest>({
    name: '',
    parentId: null,
  });

  const { data: deptData } = useQuery({
    queryKey: ['department', deptId],
    queryFn: () => departmentApi.getById(deptId!),
    enabled: isEditing,
  });

  useEffect(() => {
    if (isEditing && deptData?.data) {
      const dept = deptData.data;
      // eslint-disable-next-line react-hooks/set-state-in-effect
      setFormData({
        name: dept.name || '',
        parentId: dept.parent?.id || null,
      });
    }
  }, [isEditing, deptData]);

  const mutation = useMutation({
    mutationFn: (data: DepartmentRequest) =>
      isEditing ? departmentApi.update(deptId!, data) : departmentApi.create(data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['departments'] });
      toast.success(isEditing ? 'Department updated successfully' : 'Department created successfully');
      onClose();
    },
    onError: (err: Error) => {
      toast.error(err.message || 'An error occurred');
    }
  });

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    mutation.mutate(formData);
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
      <div className="bg-white rounded-lg shadow-xl w-full max-w-md flex flex-col">
        <div className="flex justify-between items-center p-6 border-b">
          <h2 className="text-xl font-semibold text-gray-800">
            {isEditing ? 'Edit Department' : 'Add Department'}
          </h2>
          <button onClick={onClose} className="text-gray-500 hover:text-gray-700">
            <X size={24} />
          </button>
        </div>

        <div className="p-6">
          <form id="dept-form" onSubmit={handleSubmit} className="space-y-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Name *</label>
              <input
                required
                name="name"
                value={formData.name}
                onChange={handleChange}
                className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Parent Department ID</label>
              <input
                type="number"
                name="parentId"
                value={formData.parentId || ''}
                onChange={(e) => setFormData(p => ({ ...p, parentId: e.target.value ? Number(e.target.value) : null }))}
                className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>
          </form>
          {mutation.isError && (
            <div className="mt-4 p-3 bg-red-100 text-red-700 rounded-md">
              {mutation.error.message || 'An error occurred'}
            </div>
          )}
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
            form="dept-form"
            disabled={mutation.isPending}
            className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition-colors disabled:opacity-50"
          >
            {mutation.isPending ? 'Saving...' : 'Save'}
          </button>
        </div>
      </div>
    </div>
  );
}
