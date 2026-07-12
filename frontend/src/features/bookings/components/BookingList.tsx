'use client';

import { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { CalendarRange, Search, XCircle } from 'lucide-react';
import toast from 'react-hot-toast';
import { bookingApi } from '../api';

export function BookingList() {
  const [page, setPage] = useState(0);
  const queryClient = useQueryClient();

  const { data, isLoading } = useQuery({
    queryKey: ['bookings', page],
    queryFn: () => bookingApi.getAllBookings(page, 10),
  });

  const cancelMutation = useMutation({
    mutationFn: (id: number) => bookingApi.cancelBooking(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['bookings'] });
      toast.success('Booking cancelled successfully');
    },
    onError: (error: Error) => {
      toast.error(error.message || 'Failed to cancel booking');
    },
  });

  if (isLoading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-500"></div>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <div className="relative w-64">
          <input
            type="text"
            placeholder="Search bookings..."
            className="w-full pl-10 pr-4 py-2 bg-white/[0.02] border border-white/[0.06] rounded-xl text-white placeholder-gray-500 focus:outline-none focus:border-blue-500 transition-colors"
          />
          <Search className="absolute left-3 top-2.5 h-5 w-5 text-gray-500" />
        </div>
      </div>

      <div className="bg-white/[0.02] border border-white/[0.06] rounded-2xl overflow-hidden">
        <table className="w-full text-left text-sm text-gray-400">
          <thead className="bg-white/[0.02] text-xs uppercase font-medium">
            <tr>
              <th className="px-6 py-4">Resource</th>
              <th className="px-6 py-4">Booked By</th>
              <th className="px-6 py-4">Start Time</th>
              <th className="px-6 py-4">End Time</th>
              <th className="px-6 py-4">Status</th>
              <th className="px-6 py-4 text-right">Actions</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-white/[0.06]">
            {data?.content.map((booking) => (
              <tr key={booking.id} className="hover:bg-white/[0.01] transition-colors">
                <td className="px-6 py-4">
                  <div className="flex items-center gap-3">
                    <div className="w-8 h-8 rounded-lg bg-blue-500/10 flex items-center justify-center">
                      <CalendarRange className="w-4 h-4 text-blue-400" />
                    </div>
                    <div>
                      <div className="font-medium text-white">{booking.resourceName}</div>
                      <div className="text-xs text-gray-500">{booking.resourceType || 'Resource'}</div>
                    </div>
                  </div>
                </td>
                <td className="px-6 py-4">
                  <div className="font-medium text-white">{booking.userName}</div>
                </td>
                <td className="px-6 py-4">
                  {new Date(booking.startTime).toLocaleString(undefined, { 
                    month: 'short', day: 'numeric', hour: '2-digit', minute: '2-digit' 
                  })}
                </td>
                <td className="px-6 py-4">
                  {new Date(booking.endTime).toLocaleString(undefined, { 
                    month: 'short', day: 'numeric', hour: '2-digit', minute: '2-digit' 
                  })}
                </td>
                <td className="px-6 py-4">
                  <span
                    className={`px-2.5 py-1 rounded-full text-xs font-medium ${
                      booking.bookingStatus === 'CONFIRMED'
                        ? 'bg-emerald-500/10 text-emerald-400 border border-emerald-500/20'
                        : booking.bookingStatus === 'CANCELLED'
                        ? 'bg-red-500/10 text-red-400 border border-red-500/20'
                        : 'bg-gray-500/10 text-gray-400 border border-gray-500/20'
                    }`}
                  >
                    {booking.bookingStatus}
                  </span>
                </td>
                <td className="px-6 py-4 text-right">
                  {(booking.bookingStatus === 'CONFIRMED' || booking.bookingStatus === 'PENDING') && (
                    <button
                      onClick={() => cancelMutation.mutate(booking.id)}
                      disabled={cancelMutation.isPending}
                      className="inline-flex items-center gap-1 px-3 py-1.5 text-xs font-medium bg-white/[0.02] border border-white/[0.06] rounded-lg text-white hover:bg-white/[0.06] transition-colors hover:text-red-400 hover:border-red-500/30"
                    >
                      <XCircle className="w-3.5 h-3.5" />
                      Cancel
                    </button>
                  )}
                </td>
              </tr>
            ))}
            {data?.content.length === 0 && (
              <tr>
                <td colSpan={6} className="px-6 py-8 text-center text-gray-500">
                  No bookings found
                </td>
              </tr>
            )}
          </tbody>
        </table>
        
        {data && data.totalPages > 1 && (
          <div className="px-6 py-4 border-t border-white/[0.06] flex items-center justify-between">
            <span className="text-sm text-gray-500">
              Showing page {page + 1} of {data.totalPages}
            </span>
            <div className="flex gap-2">
              <button
                onClick={() => setPage(p => Math.max(0, p - 1))}
                disabled={page === 0}
                className="px-3 py-1.5 text-sm bg-white/[0.02] border border-white/[0.06] rounded-lg text-white disabled:opacity-50 hover:bg-white/[0.06] transition-colors"
              >
                Previous
              </button>
              <button
                onClick={() => setPage(p => Math.min(data.totalPages - 1, p + 1))}
                disabled={page >= data.totalPages - 1}
                className="px-3 py-1.5 text-sm bg-white/[0.02] border border-white/[0.06] rounded-lg text-white disabled:opacity-50 hover:bg-white/[0.06] transition-colors"
              >
                Next
              </button>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}
