'use client';

import { FileCheck, Search, Users, AlertTriangle, Clock } from 'lucide-react';

const features = [
  {
    icon: Search,
    title: 'Inventory Audits',
    description: 'Conduct full or partial inventory audits with barcode scanning and location verification.',
  },
  {
    icon: Users,
    title: 'Audit Assignments',
    description: 'Assign audit tasks to team members with deadlines, checklists, and progress tracking.',
  },
  {
    icon: AlertTriangle,
    title: 'Discrepancy Reports',
    description: 'Flag and investigate discrepancies between expected and actual asset records.',
  },
  {
    icon: FileCheck,
    title: 'Compliance Reports',
    description: 'Generate compliance reports with full audit trails for regulatory requirements.',
  },
];

export default function AuditsPage() {
  return (
    <div className="space-y-8 animate-fade-in">
      {/* Header */}
      <div className="bg-white/[0.02] border border-white/[0.06] p-8 rounded-2xl">
        <div className="flex items-center gap-4 mb-3">
          <div className="w-12 h-12 bg-purple-500/10 border border-purple-500/20 rounded-xl flex items-center justify-center">
            <FileCheck className="w-6 h-6 text-purple-400" />
          </div>
          <div>
            <h1 className="text-2xl font-bold text-white">Audit Cycles</h1>
            <p className="text-gray-400 text-sm">Inventory audits and compliance reporting</p>
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
            <div className="w-10 h-10 bg-purple-500/10 rounded-lg flex items-center justify-center mb-4">
              <feature.icon className="w-5 h-5 text-purple-400" />
            </div>
            <h3 className="text-white font-semibold mb-2">{feature.title}</h3>
            <p className="text-gray-400 text-sm leading-relaxed">{feature.description}</p>
          </div>
        ))}
      </div>

      {/* Info Banner */}
      <div className="bg-white/[0.01] border border-white/[0.04] p-6 rounded-2xl text-center">
        <p className="text-gray-500 text-sm">
          The Audit Cycles module will provide comprehensive inventory audit capabilities with
          assignment workflows and discrepancy management. This feature is planned for the next development milestone.
        </p>
      </div>
    </div>
  );
}
