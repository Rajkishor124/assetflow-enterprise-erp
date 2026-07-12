'use client';

import { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { departmentApi } from '../api';
import { Plus, Edit, Trash2 } from 'lucide-react';
import DepartmentFormModal from './DepartmentFormModal';

export default function DepartmentList() {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingDeptId, setEditingDeptId] = useState<number | null>(null);

  const queryClient = useQueryClient();

  const { data, isLoading, error } = useQuery({
    queryKey: ['departments'],
    queryFn: () => departmentApi.getAll(),
  });

  const deleteMutation = useMutation({
    mutationFn: (id: number) => departmentApi.delete(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['departments'] });
    },
  });

  const handleDelete = (id: number) => {
    if (confirm('Are you sure you want to delete this department?')) {
      deleteMutation.mutate(id);
    }
  };

  const handleEdit = (id: number) => {
    setEditingDeptId(id);
    setIsModalOpen(true);
  };

  const handleCreate = () => {
    setEditingDeptId(null);
    setIsModalOpen(true);
  };

  const departments = data?.data?.content || [];

  return (
    <div className="bg-white shadow rounded-lg p-6">
      <div className="flex justify-between items-center mb-6">
        <h2 className="text-2xl font-semibold text-gray-800">Departments</h2>
        <button
          onClick={handleCreate}
          className="flex items-center gap-2 bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-md transition-colors"
        >
          <Plus size={18} /> Add Department
        </button>
      </div>

      {isLoading ? (
        <div className="py-8 text-center text-gray-500">Loading departments...</div>
      ) : error ? (
        <div className="py-8 text-center text-red-500">Failed to load departments.</div>
      ) : departments.length === 0 ? (
        <div className="py-8 text-center text-gray-500">No departments found.</div>
      ) : (
        <div className="overflow-x-auto">
          <table className="w-full text-left border-collapse">
            <thead>
              <tr className="bg-gray-50 border-b">
                <th className="py-3 px-4 font-semibold text-gray-600">ID</th>
                <th className="py-3 px-4 font-semibold text-gray-600">Name</th>
                <th className="py-3 px-4 font-semibold text-gray-600">Parent ID</th>
                <th className="py-3 px-4 text-right font-semibold text-gray-600">Actions</th>
              </tr>
            </thead>
            <tbody>
              {departments.map((dept) => (
                <tr key={dept.id} className="border-b hover:bg-gray-50">
                  <td className="py-3 px-4">{dept.id}</td>
                  <td className="py-3 px-4 font-medium text-gray-900">{dept.name}</td>
                  <td className="py-3 px-4">{dept.parentId || 'None'}</td>
                  <td className="py-3 px-4 text-right">
                    <button
                      onClick={() => handleEdit(dept.id)}
                      className="text-gray-500 hover:text-blue-600 mx-2 transition-colors"
                      title="Edit"
                    >
                      <Edit size={18} />
                    </button>
                    <button
                      onClick={() => handleDelete(dept.id)}
                      className="text-gray-500 hover:text-red-600 transition-colors"
                      title="Delete"
                    >
                      <Trash2 size={18} />
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      {isModalOpen && (
        <DepartmentFormModal
          deptId={editingDeptId}
          onClose={() => setIsModalOpen(false)}
        />
      )}
    </div>
  );
}
