'use client';

import { useState } from 'react';
import { CalendarRange, Plus } from 'lucide-react';
import { BookingList } from '@/features/bookings/components/BookingList';
import { BookingFormModal } from '@/features/bookings/components/BookingFormModal';

export default function BookingsPage() {
  const [isModalOpen, setIsModalOpen] = useState(false);

  return (
    <div className="space-y-8 animate-fade-in">
      {/* Header */}
      <div className="flex justify-between items-center">
        <div className="flex items-center gap-4">
          <div className="w-12 h-12 bg-blue-500/10 border border-blue-500/20 rounded-xl flex items-center justify-center">
            <CalendarRange className="w-6 h-6 text-blue-400" />
          </div>
          <div>
            <h1 className="text-2xl font-bold text-white">Resource Bookings</h1>
            <p className="text-gray-400 text-sm">Book and manage shared company resources</p>
          </div>
        </div>
        <button
          onClick={() => setIsModalOpen(true)}
          className="flex items-center gap-2 bg-blue-500 hover:bg-blue-600 text-white px-5 py-2.5 rounded-xl text-sm font-medium transition-all shadow-lg shadow-blue-500/25"
        >
          <Plus className="w-4 h-4" />
          Book Resource
        </button>
      </div>

      <BookingList />
      
      <BookingFormModal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
      />
    </div>
  );
}
