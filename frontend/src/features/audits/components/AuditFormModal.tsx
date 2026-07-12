'use client';

import { useState } from 'react';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { X } from 'lucide-react';
import toast from 'react-hot-toast';
import { auditsApi } from '../api';
import { AuditCycleRequest } from '../types';
import { apiClient } from '@/lib/api-client';
import { UserSummaryResponse } from '@/features/organizations/types';

interface Props {
  isOpen: boolean;
  onClose: () => void;
}

export function AuditFormModal({ isOpen, onClose }: Props) {
  const queryClient = useQueryClient();
  const [formData, setFormData] = useState({
    name: '',
    description: '',
    startDate: '',
    initiatedById: '',
  });

  const { data: users } = useQuery({
    queryKey: ['users'],
    queryFn: async () => {
      const res = await apiClient.get('/api/v1/users?page=0&size=100');
      return res.data.content || [];
    },
    enabled: isOpen,
  });

  const mutation = useMutation({
    mutationFn: (data: AuditCycleRequest) => auditsApi.createAuditCycle(data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['audits'] });
      toast.success('Audit cycle created');
      onClose();
    },
    onError: (error: Error) => {
      toast.error(error.message || 'Failed to create audit cycle');
    },
  });

  if (!isOpen) return null;

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!formData.name || !formData.startDate || !formData.initiatedById) {
      toast.error('Please fill in all required fields');
      return;
    }

    mutation.mutate({
      name: formData.name,
      description: formData.description || undefined,
      startDate: new Date(formData.startDate).toISOString(),
      initiatedById: parseInt(formData.initiatedById),
    });
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/60 backdrop-blur-sm animate-fade-in">
      <div className="bg-[#1a1a24] border border-white/[0.08] rounded-2xl w-full max-w-md shadow-2xl overflow-hidden">
        <div className="flex justify-between items-center p-6 border-b border-white/[0.08]">
          <h2 className="text-xl font-bold text-white">New Audit Cycle</h2>
          <button
            onClick={onClose}
            className="text-gray-400 hover:text-white transition-colors"
          >
            <X className="w-5 h-5" />
          </button>
        </div>

        <form onSubmit={handleSubmit} className="p-6 space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-300 mb-1">Audit Name *</label>
            <input
              type="text"
              value={formData.name}
              onChange={(e) => setFormData({ ...formData, name: e.target.value })}
              className="w-full px-4 py-2 bg-white/[0.02] border border-white/[0.08] rounded-xl text-white focus:outline-none focus:border-blue-500"
              placeholder="E.g., Q3 Equipment Audit"
              required
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-300 mb-1">Description</label>
            <textarea
              value={formData.description}
              onChange={(e) => setFormData({ ...formData, description: e.target.value })}
              className="w-full px-4 py-2 bg-white/[0.02] border border-white/[0.08] rounded-xl text-white focus:outline-none focus:border-blue-500"
              rows={3}
              placeholder="Audit scope and instructions..."
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-300 mb-1">Initiated By *</label>
            <select
              value={formData.initiatedById}
              onChange={(e) => setFormData({ ...formData, initiatedById: e.target.value })}
              className="w-full px-4 py-2 bg-white/[0.02] border border-white/[0.08] rounded-xl text-white focus:outline-none focus:border-blue-500"
              required
            >
              <option value="">Select User</option>
              {users?.map((u: UserSummaryResponse) => (
                <option key={u.id} value={u.id}>
                  {u.firstName} {u.lastName}
                </option>
              ))}
            </select>
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-300 mb-1">Start Date *</label>
            <input
              type="date"
              value={formData.startDate}
              onChange={(e) => setFormData({ ...formData, startDate: e.target.value })}
              className="w-full px-4 py-2 bg-white/[0.02] border border-white/[0.08] rounded-xl text-white focus:outline-none focus:border-blue-500"
              style={{ colorScheme: 'dark' }}
              required
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
              className="px-6 py-2 bg-blue-500 hover:bg-blue-600 text-white text-sm font-medium rounded-xl transition-all disabled:opacity-50"
            >
              {mutation.isPending ? 'Submitting...' : 'Create Audit'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
