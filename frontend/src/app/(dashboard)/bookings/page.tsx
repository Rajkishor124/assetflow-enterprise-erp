'use client';

import { CalendarRange, Building2, Car, Monitor, Clock } from 'lucide-react';

const features = [
  {
    icon: Building2,
    title: 'Book Meeting Rooms',
    description: 'Reserve conference rooms and meeting spaces with real-time availability checks.',
  },
  {
    icon: Car,
    title: 'Book Company Vehicles',
    description: 'Schedule company vehicles for business travel with driver assignment and mileage tracking.',
  },
  {
    icon: Monitor,
    title: 'Book Shared Equipment',
    description: 'Reserve projectors, cameras, and other shared equipment across departments.',
  },
  {
    icon: CalendarRange,
    title: 'Calendar Integration',
    description: 'View all bookings in a unified calendar with conflict detection and reminders.',
  },
];

export default function BookingsPage() {
  return (
    <div className="space-y-8 animate-fade-in">
      {/* Header */}
      <div className="bg-white/[0.02] border border-white/[0.06] p-8 rounded-2xl">
        <div className="flex items-center gap-4 mb-3">
          <div className="w-12 h-12 bg-blue-500/10 border border-blue-500/20 rounded-xl flex items-center justify-center">
            <CalendarRange className="w-6 h-6 text-blue-400" />
          </div>
          <div>
            <h1 className="text-2xl font-bold text-white">Resource Bookings</h1>
            <p className="text-gray-400 text-sm">Book and manage shared company resources</p>
          </div>
        </div>
        <div className="mt-4 inline-flex items-center gap-2 px-3 py-1.5 bg-amber-500/10 border border-amber-500/20 text-amber-400 text-xs font-semibold rounded-lg">
          <Clock className="w-3.5 h-3.5" />
          Coming Soon — Under Development
        </div>
      </div>

      {/* Feature Cards */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        {features.map((feature) => (
          <div
            key={feature.title}
            className="bg-white/[0.02] border border-white/[0.06] p-6 rounded-2xl hover:border-white/[0.1] transition-all duration-200"
          >
            <div className="w-10 h-10 bg-blue-500/10 rounded-lg flex items-center justify-center mb-4">
              <feature.icon className="w-5 h-5 text-blue-400" />
            </div>
            <h3 className="text-white font-semibold mb-2">{feature.title}</h3>
            <p className="text-gray-400 text-sm leading-relaxed">{feature.description}</p>
          </div>
        ))}
      </div>

      {/* Info Banner */}
      <div className="bg-white/[0.01] border border-white/[0.04] p-6 rounded-2xl text-center">
        <p className="text-gray-500 text-sm">
          The Resource Bookings module will provide a complete reservation system for shared company resources
          including rooms, vehicles, and equipment. This feature is planned for the next development milestone.
        </p>
      </div>
    </div>
  );
}
