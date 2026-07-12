'use client';

import { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { ClipboardCheck, Search, CheckCircle } from 'lucide-react';
import toast from 'react-hot-toast';
import { auditsApi } from '../api';

export function AuditList() {
  const [page, setPage] = useState(0);
  const queryClient = useQueryClient();

  const { data, isLoading } = useQuery({
    queryKey: ['audits', page],
    queryFn: () => auditsApi.getAllAuditCycles(page, 10),
  });

  const completeMutation = useMutation({
    mutationFn: (id: number) => auditsApi.completeAuditCycle(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['audits'] });
      toast.success('Audit cycle completed successfully');
    },
    onError: (error: Error) => {
      toast.error(error.message || 'Failed to complete audit cycle');
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
            placeholder="Search audit cycles..."
            className="w-full pl-10 pr-4 py-2 bg-white/[0.02] border border-white/[0.06] rounded-xl text-white placeholder-gray-500 focus:outline-none focus:border-blue-500 transition-colors"
          />
          <Search className="absolute left-3 top-2.5 h-5 w-5 text-gray-500" />
        </div>
      </div>

      <div className="bg-white/[0.02] border border-white/[0.06] rounded-2xl overflow-hidden">
        <table className="w-full text-left text-sm text-gray-400">
          <thead className="bg-white/[0.02] text-xs uppercase font-medium">
            <tr>
              <th className="px-6 py-4">Audit Name</th>
              <th className="px-6 py-4">Status</th>
              <th className="px-6 py-4">Start Date</th>
              <th className="px-6 py-4">End Date</th>
              <th className="px-6 py-4">Initiated By</th>
              <th className="px-6 py-4 text-right">Actions</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-white/[0.06]">
            {data?.content.map((record) => (
              <tr key={record.id} className="hover:bg-white/[0.01] transition-colors">
                <td className="px-6 py-4">
                  <div className="flex items-center gap-3">
                    <div className="w-8 h-8 rounded-lg bg-indigo-500/10 flex items-center justify-center">
                      <ClipboardCheck className="w-4 h-4 text-indigo-400" />
                    </div>
                    <div>
                      <div className="font-medium text-white">{record.name}</div>
                      {record.description && <div className="text-xs text-gray-500">{record.description}</div>}
                    </div>
                  </div>
                </td>
                <td className="px-6 py-4">
                  <span
                    className={`px-2.5 py-1 rounded-full text-xs font-medium ${
                      record.auditStatus === 'CLOSED'
                        ? 'bg-emerald-500/10 text-emerald-400 border border-emerald-500/20'
                        : 'bg-blue-500/10 text-blue-400 border border-blue-500/20'
                    }`}
                  >
                    {record.auditStatus}
                  </span>
                </td>
                <td className="px-6 py-4">
                  {new Date(record.startDate).toLocaleDateString(undefined, { 
                    month: 'short', day: 'numeric', year: 'numeric'
                  })}
                </td>
                <td className="px-6 py-4">
                  {record.endDate ? new Date(record.endDate).toLocaleDateString(undefined, { 
                    month: 'short', day: 'numeric', year: 'numeric'
                  }) : '-'}
                </td>
                <td className="px-6 py-4">
                  {record.initiatedByName}
                </td>
                <td className="px-6 py-4 text-right">
                  {record.auditStatus === 'OPEN' && (
                    <button
                      onClick={() => completeMutation.mutate(record.id)}
                      disabled={completeMutation.isPending}
                      className="inline-flex items-center gap-1 px-3 py-1.5 text-xs font-medium bg-white/[0.02] border border-white/[0.06] rounded-lg text-white hover:bg-emerald-500/10 transition-colors hover:text-emerald-400 hover:border-emerald-500/30"
                    >
                      <CheckCircle className="w-3.5 h-3.5" />
                      Close Audit
                    </button>
                  )}
                </td>
              </tr>
            ))}
            {data?.content.length === 0 && (
              <tr>
                <td colSpan={6} className="px-6 py-8 text-center text-gray-500">
                  No audit cycles found
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
