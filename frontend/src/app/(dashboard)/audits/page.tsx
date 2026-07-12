'use client';

import { useState } from 'react';
import { ClipboardCheck, Plus } from 'lucide-react';
import { AuditList } from '@/features/audits/components/AuditList';
import { AuditFormModal } from '@/features/audits/components/AuditFormModal';

export default function AuditsPage() {
  const [isModalOpen, setIsModalOpen] = useState(false);

  return (
    <div className="space-y-8 animate-fade-in">
      {/* Header */}
      <div className="flex justify-between items-center">
        <div className="flex items-center gap-4">
          <div className="w-12 h-12 bg-indigo-500/10 border border-indigo-500/20 rounded-xl flex items-center justify-center">
            <ClipboardCheck className="w-6 h-6 text-indigo-400" />
          </div>
          <div>
            <h1 className="text-2xl font-bold text-white">Audit Cycles</h1>
            <p className="text-gray-400 text-sm">Manage physical asset audits and verification</p>
          </div>
        </div>
        <button
          onClick={() => setIsModalOpen(true)}
          className="flex items-center gap-2 bg-blue-500 hover:bg-blue-600 text-white px-5 py-2.5 rounded-xl text-sm font-medium transition-all shadow-lg shadow-blue-500/25"
        >
          <Plus className="w-4 h-4" />
          New Audit
        </button>
      </div>

      <AuditList />
      
      <AuditFormModal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
      />
    </div>
  );
}
