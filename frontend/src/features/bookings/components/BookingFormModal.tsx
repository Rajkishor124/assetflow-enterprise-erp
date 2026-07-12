'use client';

import { useState } from 'react';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { X } from 'lucide-react';
import toast from 'react-hot-toast';
import { bookingApi } from '../api';
import { ResourceBookingRequest, BookableResourceResponse } from '../types';
import { apiClient } from '@/lib/api-client';
import { UserSummaryResponse } from '@/features/organizations/types';

interface Props {
  isOpen: boolean;
  onClose: () => void;
}

export function BookingFormModal({ isOpen, onClose }: Props) {
  const queryClient = useQueryClient();
  const [formData, setFormData] = useState({
    resourceId: '',
    userId: '',
    startTime: '',
    endTime: '',
    purpose: '',
  });

  const { data: resources } = useQuery({
    queryKey: ['resources'],
    queryFn: async () => {
      const res = await bookingApi.getAllResources(0, 100);
      return res.content || [];
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

  const mutation = useMutation({
    mutationFn: (data: ResourceBookingRequest) => bookingApi.createBooking(data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['bookings'] });
      toast.success('Resource booked successfully');
      onClose();
    },
    onError: (error: Error) => {
      toast.error(error.message || 'Failed to book resource');
    },
  });

  if (!isOpen) return null;

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!formData.resourceId) {
      toast.error('Please select a resource');
      return;
    }
    if (!formData.userId) {
      toast.error('Please select a user');
      return;
    }
    if (!formData.startTime || !formData.endTime) {
      toast.error('Please select start and end times');
      return;
    }

    const start = new Date(formData.startTime);
    const end = new Date(formData.endTime);

    if (start >= end) {
      toast.error('End time must be after start time');
      return;
    }

    mutation.mutate({
      resourceId: parseInt(formData.resourceId),
      userId: parseInt(formData.userId),
      startTime: start.toISOString(),
      endTime: end.toISOString(),
      purpose: formData.purpose || undefined,
    });
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/60 backdrop-blur-sm animate-fade-in">
      <div className="bg-[#1a1a24] border border-white/[0.08] rounded-2xl w-full max-w-md shadow-2xl overflow-hidden">
        <div className="flex justify-between items-center p-6 border-b border-white/[0.08]">
          <h2 className="text-xl font-bold text-white">Book Resource</h2>
          <button
            onClick={onClose}
            className="text-gray-400 hover:text-white transition-colors"
          >
            <X className="w-5 h-5" />
          </button>
        </div>

        <form onSubmit={handleSubmit} className="p-6 space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-300 mb-1">Resource *</label>
            <select
              value={formData.resourceId}
              onChange={(e) => setFormData({ ...formData, resourceId: e.target.value })}
              className="w-full px-4 py-2 bg-white/[0.02] border border-white/[0.08] rounded-xl text-white focus:outline-none focus:border-blue-500"
              required
            >
              <option value="">Select Resource</option>
              {resources?.map((resource: BookableResourceResponse) => (
                <option key={resource.id} value={resource.id}>
                  {resource.name} ({resource.resourceType})
                </option>
              ))}
            </select>
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-300 mb-1">User *</label>
            <select
              value={formData.userId}
              onChange={(e) => setFormData({ ...formData, userId: e.target.value })}
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

          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-gray-300 mb-1">Start Time *</label>
              <input
                type="datetime-local"
                value={formData.startTime}
                onChange={(e) => setFormData({ ...formData, startTime: e.target.value })}
                className="w-full px-4 py-2 bg-white/[0.02] border border-white/[0.08] rounded-xl text-white focus:outline-none focus:border-blue-500"
                style={{ colorScheme: 'dark' }}
                required
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-300 mb-1">End Time *</label>
              <input
                type="datetime-local"
                value={formData.endTime}
                onChange={(e) => setFormData({ ...formData, endTime: e.target.value })}
                className="w-full px-4 py-2 bg-white/[0.02] border border-white/[0.08] rounded-xl text-white focus:outline-none focus:border-blue-500"
                style={{ colorScheme: 'dark' }}
                required
              />
            </div>
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-300 mb-1">Purpose</label>
            <textarea
              value={formData.purpose}
              onChange={(e) => setFormData({ ...formData, purpose: e.target.value })}
              className="w-full px-4 py-2 bg-white/[0.02] border border-white/[0.08] rounded-xl text-white focus:outline-none focus:border-blue-500"
              rows={3}
              placeholder="E.g., Client meeting, Project team sync..."
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
              {mutation.isPending ? 'Booking...' : 'Book Resource'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
