'use client';

import { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { userApi } from '../api';
import { Edit } from 'lucide-react';
import UserFormModal from './UserFormModal';

export default function UserList() {
  const [page, setPage] = useState(0);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingUserId, setEditingUserId] = useState<number | null>(null);

  const { data, isLoading, error } = useQuery({
    queryKey: ['users', page],
    queryFn: () => userApi.getAll(page, 10),
  });

  const handleEdit = (id: number) => {
    setEditingUserId(id);
    setIsModalOpen(true);
  };

  const users = data?.data?.content || [];
  const totalPages = data?.data?.totalPages || 1;

  return (
    <div className="bg-white shadow rounded-lg p-6">
      <div className="flex justify-between items-center mb-6">
        <h2 className="text-2xl font-semibold text-gray-800">Users</h2>
      </div>

      {isLoading ? (
        <div className="py-8 text-center text-gray-500">Loading users...</div>
      ) : error ? (
        <div className="py-8 text-center text-red-500">Failed to load users.</div>
      ) : users.length === 0 ? (
        <div className="py-8 text-center text-gray-500">No users found.</div>
      ) : (
        <div className="overflow-x-auto">
          <table className="w-full text-left border-collapse">
            <thead>
              <tr className="bg-gray-50 border-b">
                <th className="py-3 px-4 font-semibold text-gray-600">ID</th>
                <th className="py-3 px-4 font-semibold text-gray-600">Name</th>
                <th className="py-3 px-4 font-semibold text-gray-600">Email</th>
                <th className="py-3 px-4 font-semibold text-gray-600">Role</th>
                <th className="py-3 px-4 font-semibold text-gray-600">Department</th>
                <th className="py-3 px-4 text-right font-semibold text-gray-600">Actions</th>
              </tr>
            </thead>
            <tbody>
              {users.map((user) => (
                <tr key={user.id} className="border-b hover:bg-gray-50">
                  <td className="py-3 px-4">{user.id}</td>
                  <td className="py-3 px-4 font-medium text-gray-900">{user.firstName} {user.lastName}</td>
                  <td className="py-3 px-4">{user.email}</td>
                  <td className="py-3 px-4">
                    <span className="px-2 py-1 text-xs font-medium bg-purple-100 text-purple-800 rounded-full">
                      {user.roleName}
                    </span>
                  </td>
                  <td className="py-3 px-4">{user.departmentName || 'None'}</td>
                  <td className="py-3 px-4 text-right">
                    <button
                      onClick={() => handleEdit(user.id)}
                      className="text-gray-500 hover:text-blue-600 mx-2 transition-colors"
                      title="Edit Role / Department"
                    >
                      <Edit size={18} />
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      {totalPages > 1 && (
        <div className="flex justify-between items-center mt-6">
          <button
            disabled={page === 0}
            onClick={() => setPage(p => p - 1)}
            className="px-4 py-2 border rounded text-gray-600 disabled:opacity-50"
          >
            Previous
          </button>
          <span className="text-gray-600">
            Page {page + 1} of {totalPages}
          </span>
          <button
            disabled={page >= totalPages - 1}
            onClick={() => setPage(p => p + 1)}
            className="px-4 py-2 border rounded text-gray-600 disabled:opacity-50"
          >
            Next
          </button>
        </div>
      )}

      {isModalOpen && (
        <UserFormModal
          userId={editingUserId}
          onClose={() => setIsModalOpen(false)}
        />
      )}
    </div>
  );
}
