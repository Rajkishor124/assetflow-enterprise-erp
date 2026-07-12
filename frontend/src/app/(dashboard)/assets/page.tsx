'use client';

import AssetList from '@/features/assets/components/AssetList';
import { Box } from 'lucide-react';

export default function AssetsPage() {
  return (
    <div className="space-y-8 animate-fade-in">
      {/* Header */}
      <div className="flex justify-between items-center">
        <div className="flex items-center gap-4">
          <div className="w-12 h-12 bg-blue-500/10 border border-blue-500/20 rounded-xl flex items-center justify-center">
            <Box className="w-6 h-6 text-blue-400" />
          </div>
          <div>
            <h1 className="text-2xl font-bold text-white">Assets Directory</h1>
            <p className="text-gray-400 text-sm">Manage and track your organization&apos;s physical assets</p>
          </div>
        </div>
      </div>
      
      <AssetList />
    </div>
  );
}
