'use client';

import { useState } from 'react';
import { Wrench, Plus } from 'lucide-react';
import { MaintenanceList } from '@/features/maintenance/components/MaintenanceList';
import { MaintenanceFormModal } from '@/features/maintenance/components/MaintenanceFormModal';

export default function MaintenancePage() {
  const [isModalOpen, setIsModalOpen] = useState(false);

  return (
    <div className="space-y-8 animate-fade-in">
      {/* Header */}
      <div className="flex justify-between items-center">
        <div className="flex items-center gap-4">
          <div className="w-12 h-12 bg-orange-500/10 border border-orange-500/20 rounded-xl flex items-center justify-center">
            <Wrench className="w-6 h-6 text-orange-400" />
          </div>
          <div>
            <h1 className="text-2xl font-bold text-white">Maintenance</h1>
            <p className="text-gray-400 text-sm">Track and manage asset maintenance requests</p>
          </div>
        </div>
        <button
          onClick={() => setIsModalOpen(true)}
          className="flex items-center gap-2 bg-blue-500 hover:bg-blue-600 text-white px-5 py-2.5 rounded-xl text-sm font-medium transition-all shadow-lg shadow-blue-500/25"
        >
          <Plus className="w-4 h-4" />
          Report Issue
        </button>
      </div>

      <MaintenanceList />
      
      <MaintenanceFormModal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
      />
    </div>
  );
}
