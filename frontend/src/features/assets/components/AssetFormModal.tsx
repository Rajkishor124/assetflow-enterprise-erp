'use client';

import { useState, useEffect } from 'react';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { assetApi, assetCategoryApi } from '../api';
import { AssetRequest } from '../types';
import { X } from 'lucide-react';

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
      onClose();
    },
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

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
      <div className="bg-white rounded-lg shadow-xl w-full max-w-2xl max-h-[90vh] flex flex-col">
        <div className="flex justify-between items-center p-6 border-b">
          <h2 className="text-xl font-semibold text-gray-800">
            {isEditing ? 'Edit Asset' : 'Add New Asset'}
          </h2>
          <button onClick={onClose} className="text-gray-500 hover:text-gray-700">
            <X size={24} />
          </button>
        </div>

        <div className="p-6 overflow-y-auto flex-grow">
          <form id="asset-form" onSubmit={handleSubmit} className="space-y-4">
            <div className="grid grid-cols-2 gap-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Asset Tag *</label>
                <input
                  required
                  name="assetTag"
                  value={formData.assetTag}
                  onChange={handleChange}
                  className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Name *</label>
                <input
                  required
                  name="name"
                  value={formData.name}
                  onChange={handleChange}
                  className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Category *</label>
                <select
                  required
                  name="categoryId"
                  value={formData.categoryId || ''}
                  onChange={handleChange}
                  className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                >
                  <option value="" disabled>Select a category</option>
                  {categories.map((cat) => (
                    <option key={cat.id} value={cat.id}>{cat.name}</option>
                  ))}
                </select>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Serial Number *</label>
                <input
                  required
                  name="serialNumber"
                  value={formData.serialNumber}
                  onChange={handleChange}
                  className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>
              <div className="col-span-2">
                <label className="block text-sm font-medium text-gray-700 mb-1">Description</label>
                <textarea
                  name="description"
                  value={formData.description || ''}
                  onChange={handleChange}
                  rows={3}
                  className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Manufacturer</label>
                <input
                  name="manufacturer"
                  value={formData.manufacturer || ''}
                  onChange={handleChange}
                  className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Model</label>
                <input
                  name="model"
                  value={formData.model || ''}
                  onChange={handleChange}
                  className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Purchase Date</label>
                <input
                  type="date"
                  name="purchaseDate"
                  value={formData.purchaseDate || ''}
                  onChange={handleChange}
                  className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Warranty Expiry</label>
                <input
                  type="date"
                  name="warrantyExpiry"
                  value={formData.warrantyExpiry || ''}
                  onChange={handleChange}
                  className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>
            </div>
          </form>
          {mutation.isError && (
            <div className="mt-4 p-3 bg-red-100 text-red-700 rounded-md">
            {/* @ts-expect-error Axios error type */}
            {mutation.error?.response?.data?.message || mutation.error.message || 'An error occurred'}
            </div>
          )}
        </div>

        <div className="p-6 border-t flex justify-end gap-3 bg-gray-50 rounded-b-lg">
          <button
            type="button"
            onClick={onClose}
            className="px-4 py-2 border rounded-md text-gray-600 hover:bg-gray-100 transition-colors"
          >
            Cancel
          </button>
          <button
            type="submit"
            form="asset-form"
            disabled={mutation.isPending}
            className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition-colors disabled:opacity-50"
          >
            {mutation.isPending ? 'Saving...' : 'Save Asset'}
          </button>
        </div>
      </div>
    </div>
  );
}
