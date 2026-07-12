'use client';

import { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { assetApi } from '../api';
import { Plus, Edit, Trash2, Search, Box } from 'lucide-react';
import AssetFormModal from './AssetFormModal';
import toast from 'react-hot-toast';

export default function AssetList() {
  const [page, setPage] = useState(0);
  const [search, setSearch] = useState('');
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingAssetId, setEditingAssetId] = useState<number | null>(null);

  const queryClient = useQueryClient();

  const { data, isLoading, error } = useQuery({
    queryKey: ['assets', page, search],
    queryFn: () => assetApi.getAll(page, 10, search),
  });

  const deleteMutation = useMutation({
    mutationFn: (id: number) => assetApi.delete(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['assets'] });
      toast.success('Asset deleted successfully');
    },
    onError: () => {
      toast.error('Failed to delete asset');
    },
  });

  const handleDelete = (id: number) => {
    if (confirm('Are you sure you want to delete this asset?')) {
      deleteMutation.mutate(id);
    }
  };

  const handleEdit = (id: number) => {
    setEditingAssetId(id);
    setIsModalOpen(true);
  };

  const handleCreate = () => {
    setEditingAssetId(null);
    setIsModalOpen(true);
  };

  const assets = data?.data?.content || [];
  const totalPages = data?.data?.totalPages || 1;

  if (isLoading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-500"></div>
      </div>
    );
  }

  if (error) {
    return <div className="py-8 text-center text-red-500">Failed to load assets.</div>;
  }

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <div className="relative w-64">
          <input
            type="text"
            placeholder="Search assets..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            className="w-full pl-10 pr-4 py-2 bg-white/[0.02] border border-white/[0.06] rounded-xl text-white placeholder-gray-500 focus:outline-none focus:border-blue-500 transition-colors"
          />
          <Search className="absolute left-3 top-2.5 h-5 w-5 text-gray-500" />
        </div>
        <button
          onClick={handleCreate}
          className="flex items-center gap-2 bg-blue-500 hover:bg-blue-600 text-white px-5 py-2.5 rounded-xl text-sm font-medium transition-all shadow-lg shadow-blue-500/25"
        >
          <Plus size={18} /> Add Asset
        </button>
      </div>

      <div className="bg-white/[0.02] border border-white/[0.06] rounded-2xl overflow-hidden">
        <table className="w-full text-left text-sm text-gray-400">
          <thead className="bg-white/[0.02] text-xs uppercase font-medium">
            <tr>
              <th className="px-6 py-4">Asset</th>
              <th className="px-6 py-4">Category</th>
              <th className="px-6 py-4">Status</th>
              <th className="px-6 py-4 text-right">Actions</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-white/[0.06]">
            {assets.map((asset) => (
              <tr key={asset.id} className="hover:bg-white/[0.01] transition-colors">
                <td className="px-6 py-4">
                  <div className="flex items-center gap-3">
                    <div className="w-8 h-8 rounded-lg bg-blue-500/10 flex items-center justify-center">
                      <Box className="w-4 h-4 text-blue-400" />
                    </div>
                    <div>
                      <div className="font-medium text-white">{asset.name}</div>
                      <div className="text-xs text-gray-500">{asset.assetTag}</div>
                    </div>
                  </div>
                </td>
                <td className="px-6 py-4">{asset.categoryName || '-'}</td>
                <td className="px-6 py-4">
                  <span
                    className={`px-2.5 py-1 rounded-full text-xs font-medium ${
                      asset.lifecycleStatus === 'AVAILABLE'
                        ? 'bg-emerald-500/10 text-emerald-400 border border-emerald-500/20'
                        : asset.lifecycleStatus === 'ALLOCATED'
                        ? 'bg-purple-500/10 text-purple-400 border border-purple-500/20'
                        : asset.lifecycleStatus === 'UNDER_MAINTENANCE'
                        ? 'bg-orange-500/10 text-orange-400 border border-orange-500/20'
                        : 'bg-gray-500/10 text-gray-400 border border-gray-500/20'
                    }`}
                  >
                    {asset.lifecycleStatus}
                  </span>
                </td>
                <td className="px-6 py-4 text-right">
                  <button
                    onClick={() => handleEdit(asset.id)}
                    className="text-gray-400 hover:text-blue-400 mx-2 transition-colors"
                    title="Edit"
                  >
                    <Edit size={16} />
                  </button>
                  <button
                    onClick={() => handleDelete(asset.id)}
                    className="text-gray-400 hover:text-red-400 transition-colors"
                    title="Delete"
                  >
                    <Trash2 size={16} />
                  </button>
                </td>
              </tr>
            ))}
            {assets.length === 0 && (
              <tr>
                <td colSpan={4} className="px-6 py-8 text-center text-gray-500">
                  No assets found
                </td>
              </tr>
            )}
          </tbody>
        </table>

        {totalPages > 1 && (
          <div className="px-6 py-4 border-t border-white/[0.06] flex items-center justify-between">
            <span className="text-sm text-gray-500">
              Showing page {page + 1} of {totalPages}
            </span>
            <div className="flex gap-2">
              <button
                onClick={() => setPage((p) => Math.max(0, p - 1))}
                disabled={page === 0}
                className="px-3 py-1.5 text-sm bg-white/[0.02] border border-white/[0.06] rounded-lg text-white disabled:opacity-50 hover:bg-white/[0.06] transition-colors"
              >
                Previous
              </button>
              <button
                onClick={() => setPage((p) => Math.min(totalPages - 1, p + 1))}
                disabled={page >= totalPages - 1}
                className="px-3 py-1.5 text-sm bg-white/[0.02] border border-white/[0.06] rounded-lg text-white disabled:opacity-50 hover:bg-white/[0.06] transition-colors"
              >
                Next
              </button>
            </div>
          </div>
        )}
      </div>

      {isModalOpen && (
        <AssetFormModal
          assetId={editingAssetId}
          onClose={() => setIsModalOpen(false)}
        />
      )}
    </div>
  );
}
