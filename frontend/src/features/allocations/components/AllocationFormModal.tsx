'use client';

import { useState } from 'react';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { X } from 'lucide-react';
import toast from 'react-hot-toast';
import { allocationApi } from '../api';
import { AssetAllocationRequest } from '../types';
import { apiClient } from '@/lib/api-client';
import { AssetSummaryResponse } from '@/features/assets/types';
import { UserSummaryResponse, DepartmentSummaryResponse } from '@/features/organizations/types';

interface Props {
  isOpen: boolean;
  onClose: () => void;
}

export function AllocationFormModal({ isOpen, onClose }: Props) {
  const queryClient = useQueryClient();
  const [formData, setFormData] = useState({
    assetId: '',
    assignmentType: 'user', // 'user' or 'department'
    userId: '',
    departmentId: '',
    expectedReturnDate: '',
    notes: '',
  });

  const { data: assets } = useQuery({
    queryKey: ['available-assets'],
    queryFn: async () => {
      const res = await apiClient.get('/api/v1/assets?status=AVAILABLE');
      return res.data.content || [];
    },
    enabled: isOpen,
  });

  const { data: users } = useQuery({
    queryKey: ['users'],
    queryFn: async () => {
      const res = await apiClient.get('/api/v1/users');
      return res.data.content || [];
    },
    enabled: isOpen,
  });

  const { data: departments } = useQuery({
    queryKey: ['departments'],
    queryFn: async () => {
      const res = await apiClient.get('/api/v1/departments');
      return res.data.content || [];
    },
    enabled: isOpen,
  });

  const mutation = useMutation({
    mutationFn: (data: AssetAllocationRequest) => allocationApi.allocate(data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['allocations'] });
      queryClient.invalidateQueries({ queryKey: ['assets'] });
      queryClient.invalidateQueries({ queryKey: ['available-assets'] });
      toast.success('Asset allocated successfully');
      onClose();
    },
    onError: (error: Error) => {
      toast.error(error.message || 'Failed to allocate asset');
    },
  });

  if (!isOpen) return null;

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!formData.assetId) {
      toast.error('Please select an asset');
      return;
    }
    if (formData.assignmentType === 'user' && !formData.userId) {
      toast.error('Please select a user');
      return;
    }
    if (formData.assignmentType === 'department' && !formData.departmentId) {
      toast.error('Please select a department');
      return;
    }

    mutation.mutate({
      assetId: parseInt(formData.assetId),
      userId: formData.assignmentType === 'user' ? parseInt(formData.userId) : undefined,
      departmentId: formData.assignmentType === 'department' ? parseInt(formData.departmentId) : undefined,
      allocationDate: new Date().toISOString(),
      expectedReturnDate: formData.expectedReturnDate ? new Date(formData.expectedReturnDate).toISOString() : undefined,
      notes: formData.notes || undefined,
    });
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/60 backdrop-blur-sm animate-fade-in">
      <div className="bg-[#1a1a24] border border-white/[0.08] rounded-2xl w-full max-w-md shadow-2xl overflow-hidden">
        <div className="flex justify-between items-center p-6 border-b border-white/[0.08]">
          <h2 className="text-xl font-bold text-white">Allocate Asset</h2>
          <button
            onClick={onClose}
            className="text-gray-400 hover:text-white transition-colors"
          >
            <X className="w-5 h-5" />
          </button>
        </div>

        <form onSubmit={handleSubmit} className="p-6 space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-300 mb-1">Asset *</label>
            <select
              value={formData.assetId}
              onChange={(e) => setFormData({ ...formData, assetId: e.target.value })}
              className="w-full px-4 py-2 bg-white/[0.02] border border-white/[0.08] rounded-xl text-white focus:outline-none focus:border-indigo-500"
              required
            >
              <option value="">Select Asset</option>
              {assets?.map((asset: AssetSummaryResponse) => (
                <option key={asset.id} value={asset.id}>
                  {asset.name} ({asset.assetTag})
                </option>
              ))}
            </select>
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-300 mb-1">Assign To *</label>
            <div className="flex gap-4 mb-2">
              <label className="flex items-center gap-2 text-sm text-gray-300">
                <input
                  type="radio"
                  checked={formData.assignmentType === 'user'}
                  onChange={() => setFormData({ ...formData, assignmentType: 'user', departmentId: '' })}
                  className="text-indigo-500 focus:ring-indigo-500"
                />
                User
              </label>
              <label className="flex items-center gap-2 text-sm text-gray-300">
                <input
                  type="radio"
                  checked={formData.assignmentType === 'department'}
                  onChange={() => setFormData({ ...formData, assignmentType: 'department', userId: '' })}
                  className="text-indigo-500 focus:ring-indigo-500"
                />
                Department
              </label>
            </div>
            {formData.assignmentType === 'user' ? (
              <select
                value={formData.userId}
                onChange={(e) => setFormData({ ...formData, userId: e.target.value })}
                className="w-full px-4 py-2 bg-white/[0.02] border border-white/[0.08] rounded-xl text-white focus:outline-none focus:border-indigo-500"
                required
              >
                <option value="">Select User</option>
                {users?.map((u: UserSummaryResponse) => (
                  <option key={u.id} value={u.id}>
                    {u.firstName} {u.lastName}
                  </option>
                ))}
              </select>
            ) : (
              <select
                value={formData.departmentId}
                onChange={(e) => setFormData({ ...formData, departmentId: e.target.value })}
                className="w-full px-4 py-2 bg-white/[0.02] border border-white/[0.08] rounded-xl text-white focus:outline-none focus:border-indigo-500"
                required
              >
                <option value="">Select Department</option>
                {departments?.map((d: DepartmentSummaryResponse) => (
                  <option key={d.id} value={d.id}>
                    {d.name}
                  </option>
                ))}
              </select>
            )}
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-300 mb-1">Expected Return Date</label>
            <input
              type="date"
              value={formData.expectedReturnDate}
              onChange={(e) => setFormData({ ...formData, expectedReturnDate: e.target.value })}
              className="w-full px-4 py-2 bg-white/[0.02] border border-white/[0.08] rounded-xl text-white focus:outline-none focus:border-indigo-500"
              style={{ colorScheme: 'dark' }}
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-300 mb-1">Notes</label>
            <textarea
              value={formData.notes}
              onChange={(e) => setFormData({ ...formData, notes: e.target.value })}
              className="w-full px-4 py-2 bg-white/[0.02] border border-white/[0.08] rounded-xl text-white focus:outline-none focus:border-indigo-500"
              rows={3}
            />
          </div>

          <div className="flex justify-end gap-3 pt-4 border-t border-white/[0.08]">
            <button
              type="button"
              onClick={onClose}
              className="px-4 py-2 text-sm font-medium text-gray-300 hover:text-white transition-colors"
            >
              Cancel
            </button>
            <button
              type="submit"
              disabled={mutation.isPending}
              className="px-6 py-2 bg-indigo-500 hover:bg-indigo-600 text-white text-sm font-medium rounded-xl transition-all disabled:opacity-50"
            >
              {mutation.isPending ? 'Allocating...' : 'Allocate Asset'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
