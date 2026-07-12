'use client';

import { useState, useEffect } from 'react';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { assetApi, assetCategoryApi } from '../api';
import { AssetRequest } from '../types';
import { X } from 'lucide-react';
import toast from 'react-hot-toast';

interface Props {
  assetId: number | null;
  onClose: () => void;
}

export default function AssetFormModal({ assetId, onClose }: Props) {
  const queryClient = useQueryClient();
  const isEditing = assetId !== null;

  const [formData, setFormData] = useState<AssetRequest>({
    assetTag: '',
    name: '',
    description: '',
    serialNumber: '',
    manufacturer: '',
    model: '',
    purchasePrice: null,
    purchaseDate: '',
    warrantyExpiry: '',
    categoryId: 0,
  });

  const { data: categoriesData } = useQuery({
    queryKey: ['asset-categories'],
    queryFn: () => assetCategoryApi.getAll(0, 100),
  });
  const categories = categoriesData?.data?.content || [];

  const { data: assetData } = useQuery({
    queryKey: ['asset', assetId],
    queryFn: () => assetApi.getById(assetId!),
    enabled: isEditing,
  });

  useEffect(() => {
    if (isEditing && assetData?.data) {
      const asset = assetData.data;
      // eslint-disable-next-line react-hooks/set-state-in-effect
      setFormData({
        assetTag: asset.assetTag || '',
        name: asset.name || '',
        description: asset.description || '',
        serialNumber: asset.serialNumber || '',
        manufacturer: asset.manufacturer || '',
        model: asset.model || '',
        purchasePrice: asset.purchasePrice || null,
        purchaseDate: asset.purchaseDate || '',
        warrantyExpiry: asset.warrantyExpiry || '',
        categoryId: asset.categoryId || 0,
      });
    }
  }, [isEditing, assetData]);

  const mutation = useMutation({
    mutationFn: (data: AssetRequest) =>
      isEditing ? assetApi.update(assetId!, data) : assetApi.create(data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['assets'] });
      toast.success(isEditing ? 'Asset updated successfully' : 'Asset created successfully');
      onClose();
    },
    onError: (err: Error) => {
      toast.error(err.message || 'An error occurred');
    }
  });

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    mutation.mutate(formData);
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: name === 'categoryId' || name === 'purchasePrice' ? (value ? Number(value) : null) : value,
    }));
  };

  if (!categories) return null;

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/60 backdrop-blur-sm animate-fade-in">
      <div className="bg-[#1a1a24] border border-white/[0.08] rounded-2xl w-full max-w-2xl max-h-[90vh] flex flex-col shadow-2xl overflow-hidden">
        <div className="flex justify-between items-center p-6 border-b border-white/[0.08]">
          <h2 className="text-xl font-bold text-white">
            {isEditing ? 'Edit Asset' : 'Add New Asset'}
          </h2>
          <button onClick={onClose} className="text-gray-400 hover:text-white transition-colors">
            <X className="w-5 h-5" />
          </button>
        </div>

        <div className="p-6 overflow-y-auto flex-grow">
          <form id="asset-form" onSubmit={handleSubmit} className="space-y-4">
            <div className="grid grid-cols-2 gap-4">
              <div>
                <label className="block text-sm font-medium text-gray-300 mb-1">Asset Tag *</label>
                <input
                  required
                  name="assetTag"
                  value={formData.assetTag}
                  onChange={handleChange}
                  className="w-full px-4 py-2 bg-white/[0.02] border border-white/[0.08] rounded-xl text-white focus:outline-none focus:border-blue-500"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-300 mb-1">Name *</label>
                <input
                  required
                  name="name"
                  value={formData.name}
                  onChange={handleChange}
                  className="w-full px-4 py-2 bg-white/[0.02] border border-white/[0.08] rounded-xl text-white focus:outline-none focus:border-blue-500"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-300 mb-1">Category *</label>
                <select
                  required
                  name="categoryId"
                  value={formData.categoryId || ''}
                  onChange={handleChange}
                  className="w-full px-4 py-2 bg-white/[0.02] border border-white/[0.08] rounded-xl text-white focus:outline-none focus:border-blue-500"
                >
                  <option value="" disabled>Select a category</option>
                  {categories.map((cat: { id: number; name: string }) => (
                    <option key={cat.id} value={cat.id}>{cat.name}</option>
                  ))}
                </select>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-300 mb-1">Serial Number *</label>
                <input
                  required
                  name="serialNumber"
                  value={formData.serialNumber}
                  onChange={handleChange}
                  className="w-full px-4 py-2 bg-white/[0.02] border border-white/[0.08] rounded-xl text-white focus:outline-none focus:border-blue-500"
                />
              </div>
              <div className="col-span-2">
                <label className="block text-sm font-medium text-gray-300 mb-1">Description</label>
                <textarea
                  name="description"
                  value={formData.description || ''}
                  onChange={handleChange}
                  rows={3}
                  className="w-full px-4 py-2 bg-white/[0.02] border border-white/[0.08] rounded-xl text-white focus:outline-none focus:border-blue-500"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-300 mb-1">Manufacturer</label>
                <input
                  name="manufacturer"
                  value={formData.manufacturer || ''}
                  onChange={handleChange}
                  className="w-full px-4 py-2 bg-white/[0.02] border border-white/[0.08] rounded-xl text-white focus:outline-none focus:border-blue-500"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-300 mb-1">Model</label>
                <input
                  name="model"
                  value={formData.model || ''}
                  onChange={handleChange}
                  className="w-full px-4 py-2 bg-white/[0.02] border border-white/[0.08] rounded-xl text-white focus:outline-none focus:border-blue-500"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-300 mb-1">Purchase Date</label>
                <input
                  type="date"
                  name="purchaseDate"
                  value={formData.purchaseDate || ''}
                  onChange={handleChange}
                  className="w-full px-4 py-2 bg-white/[0.02] border border-white/[0.08] rounded-xl text-white focus:outline-none focus:border-blue-500"
                  style={{ colorScheme: 'dark' }}
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-300 mb-1">Warranty Expiry</label>
                <input
                  type="date"
                  name="warrantyExpiry"
                  value={formData.warrantyExpiry || ''}
                  onChange={handleChange}
                  className="w-full px-4 py-2 bg-white/[0.02] border border-white/[0.08] rounded-xl text-white focus:outline-none focus:border-blue-500"
                  style={{ colorScheme: 'dark' }}
                />
              </div>
            </div>
          </form>
          {mutation.isError && (
            <div className="mt-4 p-3 bg-red-500/10 border border-red-500/20 text-red-400 rounded-xl">
              {mutation.error.message || 'An error occurred'}
            </div>
          )}
        </div>

        <div className="p-6 border-t border-white/[0.08] flex justify-end gap-3">
          <button
            type="button"
            onClick={onClose}
            className="px-4 py-2 text-sm font-medium text-gray-300 hover:text-white transition-colors"
          >
            Cancel
          </button>
          <button
            type="submit"
            form="asset-form"
            disabled={mutation.isPending}
            className="px-6 py-2 bg-blue-500 hover:bg-blue-600 text-white text-sm font-medium rounded-xl transition-all disabled:opacity-50"
          >
            {mutation.isPending ? 'Saving...' : 'Save Asset'}
          </button>
        </div>
      </div>
    </div>
  );
}
