'use client';

import { useEffect, useState } from 'react';
import Link from 'next/link';
import { 
  PackageCheck, 
  PackageOpen, 
  Wrench, 
  CalendarRange, 
  RefreshCw, 
  AlertTriangle,
  PlusCircle,
  CalendarDays,
  Hammer,
  Clock,
  ArrowRight
} from 'lucide-react';
import { getCurrentUser, User } from '../../../lib/auth';

interface KpiData {
  available: number;
  allocated: number;
  maintenance: number;
  bookings: number;
  transfers: number;
  overdue: number;
}

interface OverdueReturn {
  id: number;
  assetTag: string;
  assetName: string;
  assignedTo: string;
  expectedDate: string;
}

export default function DashboardPage() {
  const [currentUser, setCurrentUserVal] = useState<User | null>(null);
  const [kpis, setKpis] = useState<KpiData>({
    available: 12,
    allocated: 28,
    maintenance: 3,
    bookings: 5,
    transfers: 2,
    overdue: 4,
  });

  const [overdueList, setOverdueList] = useState<OverdueReturn[]>([
    { id: 1, assetTag: 'AF-0024', assetName: 'MacBook Pro 16"', assignedTo: 'Priya Sharma', expectedDate: '2026-07-10' },
    { id: 2, assetTag: 'AF-0112', assetName: 'iPad Air Gen 5', assignedTo: 'Rajesh Kumar', expectedDate: '2026-07-09' },
    { id: 3, assetTag: 'AF-0098', assetName: 'Wacom Cintiq Pro', assignedTo: 'Neha Gupta', expectedDate: '2026-07-05' },
  ]);

  useEffect(() => {
    const user = getCurrentUser();
    if (user) {
      setCurrentUserVal(user);
    }
  }, []);

  const kpiConfig = [
    { name: 'Assets Available', value: kpis.available, icon: PackageCheck, color: 'text-emerald-400', bg: 'bg-emerald-500/10' },
    { name: 'Assets Allocated', value: kpis.allocated, icon: PackageOpen, color: 'text-blue-400', bg: 'bg-blue-500/10' },
    { name: 'Maintenance Today', value: kpis.maintenance, icon: Wrench, color: 'text-amber-400', bg: 'bg-amber-500/10' },
    { name: 'Active Bookings', value: kpis.bookings, icon: CalendarRange, color: 'text-purple-400', bg: 'bg-purple-500/10' },
    { name: 'Pending Transfers', value: kpis.transfers, icon: RefreshCw, color: 'text-indigo-400', bg: 'bg-indigo-500/10' },
    { name: 'Overdue Returns', value: kpis.overdue, icon: AlertTriangle, color: 'text-red-400', bg: 'bg-red-500/10' },
  ];

  return (
    <div className="space-y-8 animate-fade-in">
      {/* Welcome Banner */}
      <div className="flex flex-col sm:flex-row justify-between sm:items-center gap-4 bg-white/[0.01] border border-white/[0.04] p-6 rounded-2xl">
        <div>
          <h2 className="text-xl md:text-2xl font-bold text-white">
            Welcome back, {currentUser?.name}!
          </h2>
          <p className="text-gray-400 text-sm mt-1">
            Here's what's happening in your organization today.
          </p>
        </div>
        <div className="inline-flex items-center gap-2 px-3 py-1 bg-indigo-500/10 border border-indigo-500/20 text-indigo-400 text-xs font-semibold rounded-lg self-start sm:self-center">
          Role: {currentUser?.role.replace('_', ' ')}
        </div>
      </div>

      {/* KPI Cards Grid */}
      <div className="grid grid-cols-2 lg:grid-cols-3 xl:grid-cols-6 gap-4">
        {kpiConfig.map((kpi) => (
          <div key={kpi.name} className="bg-white/[0.02] border border-white/[0.06] p-5 rounded-2xl flex flex-col justify-between hover:border-white/[0.1] transition-all duration-200">
            <div className="flex justify-between items-start mb-4">
              <span className="text-xs font-semibold text-gray-400 tracking-wider leading-relaxed">
                {kpi.name}
              </span>
              <div className={`w-8 h-8 rounded-lg flex items-center justify-center ${kpi.bg}`}>
                <kpi.icon className={`w-4 h-4 ${kpi.color}`} />
              </div>
            </div>
            <span className="text-2xl md:text-3xl font-bold text-white tracking-tight">
              {kpi.value}
            </span>
          </div>
        ))}
      </div>

      {/* Quick Actions & Overdue return Panel */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        
        {/* Left 2 Columns: Quick Actions & Operations */}
        <div className="lg:col-span-2 space-y-6">
          <div className="bg-white/[0.02] border border-white/[0.06] p-6 rounded-2xl">
            <h3 className="text-lg font-bold text-white mb-5 flex items-center gap-2">
              Quick Actions
            </h3>
            <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
              {currentUser?.role !== 'EMPLOYEE' && (
                <Link 
                  href="/assets?action=new"
                  className="flex flex-col items-center justify-center p-5 bg-indigo-500/5 hover:bg-indigo-500/10 border border-indigo-500/15 hover:border-indigo-500/30 rounded-xl transition-all duration-200 gap-3 group"
                >
                  <PlusCircle className="w-8 h-8 text-indigo-400" />
                  <span className="text-sm font-semibold text-white group-hover:text-indigo-300 transition-colors">
                    Register Asset
                  </span>
                </Link>
              )}
              <Link 
                href="/bookings"
                className="flex flex-col items-center justify-center p-5 bg-blue-500/5 hover:bg-blue-500/10 border border-blue-500/15 hover:border-blue-500/30 rounded-xl transition-all duration-200 gap-3 group"
              >
                <CalendarDays className="w-8 h-8 text-blue-400" />
                <span className="text-sm font-semibold text-white group-hover:text-blue-300 transition-colors">
                  Book Resource
                </span>
              </Link>
              <Link 
                href="/maintenance"
                className="flex flex-col items-center justify-center p-5 bg-amber-500/5 hover:bg-amber-500/10 border border-amber-500/15 hover:border-amber-500/30 rounded-xl transition-all duration-200 gap-3 group"
              >
                <Hammer className="w-8 h-8 text-amber-400" />
                <span className="text-sm font-semibold text-white group-hover:text-amber-300 transition-colors">
                  Raise Maintenance
                </span>
              </Link>
            </div>
          </div>

          {/* Department Utilization Summary Mock */}
          <div className="bg-white/[0.02] border border-white/[0.06] p-6 rounded-2xl">
            <h3 className="text-lg font-bold text-white mb-5">
              Department-wise Asset Summary
            </h3>
            <div className="space-y-4">
              <div>
                <div className="flex justify-between text-sm mb-1.5 font-medium">
                  <span className="text-gray-300">Engineering (IT)</span>
                  <span className="text-indigo-400">18 Assets Allocated</span>
                </div>
                <div className="w-full bg-[#14151e] h-2.5 rounded-full overflow-hidden">
                  <div className="bg-indigo-500 h-full rounded-full" style={{ width: '65%' }} />
                </div>
              </div>
              <div>
                <div className="flex justify-between text-sm mb-1.5 font-medium">
                  <span className="text-gray-300">Operations & Logistics</span>
                  <span className="text-indigo-400">8 Assets Allocated</span>
                </div>
                <div className="w-full bg-[#14151e] h-2.5 rounded-full overflow-hidden">
                  <div className="bg-blue-500 h-full rounded-full" style={{ width: '30%' }} />
                </div>
              </div>
              <div>
                <div className="flex justify-between text-sm mb-1.5 font-medium">
                  <span className="text-gray-300">Human Resources (HR)</span>
                  <span className="text-indigo-400">2 Assets Allocated</span>
                </div>
                <div className="w-full bg-[#14151e] h-2.5 rounded-full overflow-hidden">
                  <div className="bg-purple-500 h-full rounded-full" style={{ width: '10%' }} />
                </div>
              </div>
            </div>
          </div>
        </div>

        {/* Right 1 Column: Overdue Returns Alerts */}
        <div className="space-y-6">
          <div className="bg-white/[0.02] border border-white/[0.06] p-6 rounded-2xl flex flex-col justify-between h-full">
            <div>
              <div className="flex items-center gap-2 mb-4 text-red-400">
                <AlertTriangle className="w-5 h-5" />
                <h3 className="text-lg font-bold text-white">Overdue Return Alerts</h3>
              </div>
              <p className="text-xs text-gray-400 mb-5">
                The expected return date for the following allocations has passed.
              </p>

              <div className="space-y-4">
                {overdueList.map((item) => (
                  <div key={item.id} className="p-4 bg-[#1b1215] border border-red-500/10 rounded-xl space-y-2">
                    <div className="flex justify-between items-center">
                      <span className="font-semibold text-white text-sm">{item.assetName}</span>
                      <span className="px-2 py-0.5 rounded text-[10px] font-bold bg-red-500/20 text-red-400 uppercase">
                        {item.assetTag}
                      </span>
                    </div>
                    <div className="flex justify-between text-xs text-gray-400">
                      <span>Held by: {item.assignedTo}</span>
                      <span className="flex items-center gap-1 text-red-400/90 font-medium">
                        <Clock className="w-3.5 h-3.5" />
                        Due {new Date(item.expectedDate).toLocaleDateString('en-US', { month: 'short', day: 'numeric' })}
                      </span>
                    </div>
                  </div>
                ))}
              </div>
            </div>

            <Link 
              href="/allocations?filter=overdue"
              className="mt-6 flex items-center justify-center gap-2 w-full bg-white/[0.02] border border-white/[0.06] hover:bg-white/[0.04] text-sm font-semibold rounded-xl py-3 text-indigo-400 transition-colors"
            >
              View All Allocations
              <ArrowRight className="w-4 h-4" />
            </Link>
          </div>
        </div>

      </div>
    </div>
  );
}
