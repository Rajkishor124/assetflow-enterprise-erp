'use client';

import { ClipboardList, UserCheck, RotateCcw, ArrowRightLeft, Clock } from 'lucide-react';

const features = [
  {
    icon: UserCheck,
    title: 'Assign Assets to Employees',
    description: 'Allocate company assets to team members with full tracking and accountability.',
  },
  {
    icon: RotateCcw,
    title: 'Asset Return Tracking',
    description: 'Monitor expected return dates and flag overdue assets automatically.',
  },
  {
    icon: ArrowRightLeft,
    title: 'Inter-Department Transfers',
    description: 'Seamlessly transfer assets between departments with approval workflows.',
  },
  {
    icon: Clock,
    title: 'Allocation History',
    description: 'Complete audit trail of every asset allocation, transfer, and return.',
  },
];

export default function AllocationsPage() {
  return (
    <div className="space-y-8 animate-fade-in">
      {/* Header */}
      <div className="bg-white/[0.02] border border-white/[0.06] p-8 rounded-2xl">
        <div className="flex items-center gap-4 mb-3">
          <div className="w-12 h-12 bg-indigo-500/10 border border-indigo-500/20 rounded-xl flex items-center justify-center">
            <ClipboardList className="w-6 h-6 text-indigo-400" />
          </div>
          <div>
            <h1 className="text-2xl font-bold text-white">Allocations</h1>
            <p className="text-gray-400 text-sm">Asset allocation and return management</p>
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
            <div className="w-10 h-10 bg-indigo-500/10 rounded-lg flex items-center justify-center mb-4">
              <feature.icon className="w-5 h-5 text-indigo-400" />
            </div>
            <h3 className="text-white font-semibold mb-2">{feature.title}</h3>
            <p className="text-gray-400 text-sm leading-relaxed">{feature.description}</p>
          </div>
        ))}
      </div>

      {/* Info Banner */}
      <div className="bg-white/[0.01] border border-white/[0.04] p-6 rounded-2xl text-center">
        <p className="text-gray-500 text-sm">
          The Allocations module will enable complete asset lifecycle tracking from assignment to return.
          This feature is planned for the next development milestone.
        </p>
      </div>
    </div>
  );
}
