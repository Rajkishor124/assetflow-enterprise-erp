'use client';

import { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { assetApi } from '../api';
import { Plus, Edit, Trash2, Search } from 'lucide-react';
import AssetFormModal from './AssetFormModal';

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

  return (
    <div className="bg-white shadow rounded-lg p-6">
      <div className="flex justify-between items-center mb-6">
        <h2 className="text-2xl font-semibold text-gray-800">Assets</h2>
        <button
          onClick={handleCreate}
          className="flex items-center gap-2 bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-md transition-colors"
        >
          <Plus size={18} /> Add Asset
        </button>
      </div>

      <div className="mb-4 relative">
        <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400" size={18} />
        <input
          type="text"
          placeholder="Search assets..."
          value={search}
          onChange={(e) => setSearch(e.target.value)}
          className="w-full pl-10 pr-4 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
        />
      </div>

      {isLoading ? (
        <div className="py-8 text-center text-gray-500">Loading assets...</div>
      ) : error ? (
        <div className="py-8 text-center text-red-500">Failed to load assets.</div>
      ) : assets.length === 0 ? (
        <div className="py-8 text-center text-gray-500">No assets found.</div>
      ) : (
        <div className="overflow-x-auto">
          <table className="w-full text-left border-collapse">
            <thead>
              <tr className="bg-gray-50 border-b">
                <th className="py-3 px-4 font-semibold text-gray-600">Tag</th>
                <th className="py-3 px-4 font-semibold text-gray-600">Name</th>
                <th className="py-3 px-4 font-semibold text-gray-600">Category</th>
                <th className="py-3 px-4 font-semibold text-gray-600">Status</th>
                <th className="py-3 px-4 text-right font-semibold text-gray-600">Actions</th>
              </tr>
            </thead>
            <tbody>
              {assets.map((asset) => (
                <tr key={asset.id} className="border-b hover:bg-gray-50">
                  <td className="py-3 px-4">{asset.assetTag}</td>
                  <td className="py-3 px-4 font-medium text-gray-900">{asset.name}</td>
                  <td className="py-3 px-4">{asset.categoryName || 'N/A'}</td>
                  <td className="py-3 px-4">
                    <span className="px-2 py-1 text-xs font-medium bg-blue-100 text-blue-800 rounded-full">
                      {asset.lifecycleStatus}
                    </span>
                  </td>
                  <td className="py-3 px-4 text-right">
                    <button
                      onClick={() => handleEdit(asset.id)}
                      className="text-gray-500 hover:text-blue-600 mx-2 transition-colors"
                      title="Edit"
                    >
                      <Edit size={18} />
                    </button>
                    <button
                      onClick={() => handleDelete(asset.id)}
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
        <AssetFormModal
          assetId={editingAssetId}
          onClose={() => setIsModalOpen(false)}
        />
      )}
    </div>
  );
}
