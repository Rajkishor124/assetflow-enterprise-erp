'use client';

import { Wrench, AlertCircle, History, CalendarClock, Clock } from 'lucide-react';

const features = [
  {
    icon: AlertCircle,
    title: 'Maintenance Requests',
    description: 'Submit and track maintenance requests for any company asset with priority levels.',
  },
  {
    icon: History,
    title: 'Repair History',
    description: 'Complete service history for every asset including costs, vendors, and resolution times.',
  },
  {
    icon: CalendarClock,
    title: 'Scheduled Maintenance',
    description: 'Set up preventive maintenance schedules with automated reminders and work orders.',
  },
  {
    icon: Wrench,
    title: 'Vendor Management',
    description: 'Track external repair vendors, service contracts, and warranty information.',
  },
];

export default function MaintenancePage() {
  return (
    <div className="space-y-8 animate-fade-in">
      {/* Header */}
      <div className="bg-white/[0.02] border border-white/[0.06] p-8 rounded-2xl">
        <div className="flex items-center gap-4 mb-3">
          <div className="w-12 h-12 bg-amber-500/10 border border-amber-500/20 rounded-xl flex items-center justify-center">
            <Wrench className="w-6 h-6 text-amber-400" />
          </div>
          <div>
            <h1 className="text-2xl font-bold text-white">Maintenance</h1>
            <p className="text-gray-400 text-sm">Asset maintenance requests and repair tracking</p>
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
            <div className="w-10 h-10 bg-amber-500/10 rounded-lg flex items-center justify-center mb-4">
              <feature.icon className="w-5 h-5 text-amber-400" />
            </div>
            <h3 className="text-white font-semibold mb-2">{feature.title}</h3>
            <p className="text-gray-400 text-sm leading-relaxed">{feature.description}</p>
          </div>
        ))}
      </div>

      {/* Info Banner */}
      <div className="bg-white/[0.01] border border-white/[0.04] p-6 rounded-2xl text-center">
        <p className="text-gray-500 text-sm">
          The Maintenance module will streamline repair workflows, preventive maintenance scheduling,
          and vendor coordination. This feature is planned for the next development milestone.
        </p>
      </div>
    </div>
  );
}
