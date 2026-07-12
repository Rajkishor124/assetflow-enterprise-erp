'use client';

import { useState } from 'react';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { X } from 'lucide-react';
import toast from 'react-hot-toast';
import { maintenanceApi } from '../api';
import { MaintenanceRecordRequest } from '../types';
import { apiClient } from '@/lib/api-client';
import { AssetSummaryResponse } from '@/features/assets/types';
import { UserSummaryResponse } from '@/features/organizations/types';

interface Props {
  isOpen: boolean;
  onClose: () => void;
}

export function MaintenanceFormModal({ isOpen, onClose }: Props) {
  const queryClient = useQueryClient();
  const [formData, setFormData] = useState({
    assetId: '',
    reportedById: '',
    title: '',
    description: '',
    priority: 'MEDIUM',
    scheduledDate: '',
  });

  const { data: assets } = useQuery({
    queryKey: ['assets'],
    queryFn: async () => {
      const res = await apiClient.get('/api/v1/assets?page=0&size=100');
      return res.data.content || [];
    },
    enabled: isOpen,
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
    mutationFn: (data: MaintenanceRecordRequest) => maintenanceApi.createMaintenanceRequest(data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['maintenance'] });
      queryClient.invalidateQueries({ queryKey: ['assets'] });
      toast.success('Maintenance request created');
      onClose();
    },
    onError: (error: Error) => {
      toast.error(error.message || 'Failed to create maintenance request');
    },
  });

  if (!isOpen) return null;

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!formData.assetId || !formData.reportedById || !formData.title) {
      toast.error('Please fill in all required fields');
      return;
    }

    mutation.mutate({
      assetId: parseInt(formData.assetId),
      reportedById: parseInt(formData.reportedById),
      title: formData.title,
      description: formData.description || undefined,
      priority: formData.priority as 'LOW' | 'MEDIUM' | 'HIGH',
      scheduledDate: formData.scheduledDate ? new Date(formData.scheduledDate).toISOString() : undefined,
    });
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/60 backdrop-blur-sm animate-fade-in">
      <div className="bg-[#1a1a24] border border-white/[0.08] rounded-2xl w-full max-w-md shadow-2xl overflow-hidden">
        <div className="flex justify-between items-center p-6 border-b border-white/[0.08]">
          <h2 className="text-xl font-bold text-white">Report Maintenance Issue</h2>
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
              className="w-full px-4 py-2 bg-white/[0.02] border border-white/[0.08] rounded-xl text-white focus:outline-none focus:border-blue-500"
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
            <label className="block text-sm font-medium text-gray-300 mb-1">Reported By *</label>
            <select
              value={formData.reportedById}
              onChange={(e) => setFormData({ ...formData, reportedById: e.target.value })}
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
            <label className="block text-sm font-medium text-gray-300 mb-1">Issue Title *</label>
            <input
              type="text"
              value={formData.title}
              onChange={(e) => setFormData({ ...formData, title: e.target.value })}
              className="w-full px-4 py-2 bg-white/[0.02] border border-white/[0.08] rounded-xl text-white focus:outline-none focus:border-blue-500"
              placeholder="E.g., Screen flickering, Oil leak..."
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
              placeholder="Describe the issue in detail..."
            />
          </div>

          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-gray-300 mb-1">Priority</label>
              <select
                value={formData.priority}
                onChange={(e) => setFormData({ ...formData, priority: e.target.value })}
                className="w-full px-4 py-2 bg-white/[0.02] border border-white/[0.08] rounded-xl text-white focus:outline-none focus:border-blue-500"
              >
                <option value="LOW">Low</option>
                <option value="MEDIUM">Medium</option>
                <option value="HIGH">High</option>
              </select>
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-300 mb-1">Scheduled Date</label>
              <input
                type="date"
                value={formData.scheduledDate}
                onChange={(e) => setFormData({ ...formData, scheduledDate: e.target.value })}
                className="w-full px-4 py-2 bg-white/[0.02] border border-white/[0.08] rounded-xl text-white focus:outline-none focus:border-blue-500"
                style={{ colorScheme: 'dark' }}
              />
            </div>
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
              {mutation.isPending ? 'Submitting...' : 'Submit Request'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
