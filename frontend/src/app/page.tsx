'use client';

import { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import Link from 'next/link';
import { Shield, ArrowRight, Activity, Calendar, Award } from 'lucide-react';
import { getAccessToken } from '../lib/auth';

export default function Home() {
  const router = useRouter();
  const [isClient, setIsClient] = useState(false);

  useEffect(() => {
    // eslint-disable-next-line react-hooks/set-state-in-effect
    setIsClient(true);
    const token = getAccessToken();
    if (token) {
      router.push('/dashboard');
    }
  }, [router]);

  if (!isClient) {
    return (
      <div className="min-h-screen bg-[#0d0e12] flex items-center justify-center">
        <div className="w-12 h-12 border-4 border-indigo-500/20 border-t-indigo-500 rounded-full animate-spin" />
      </div>
    );
  }



  return (
    <div className="relative min-h-screen bg-[#0d0e12] text-white overflow-hidden flex flex-col justify-between">
      {/* Background Glows */}
      <div className="absolute top-[-30%] left-[-10%] w-[600px] h-[600px] rounded-full bg-blue-500/5 blur-[150px] pointer-events-none" />
      <div className="absolute bottom-[-30%] right-[-10%] w-[600px] h-[600px] rounded-full bg-indigo-500/5 blur-[150px] pointer-events-none" />

      {/* Header */}
      <header className="w-full max-w-7xl mx-auto px-6 py-6 flex items-center justify-between z-10">
        <div className="flex items-center gap-2.5">
          <div className="w-9 h-9 bg-indigo-500/10 border border-indigo-500/20 rounded-lg flex items-center justify-center">
            <Shield className="w-5 h-5 text-indigo-400" />
          </div>
          <span className="font-bold text-lg tracking-tight">AssetFlow</span>
        </div>
        <Link
          href="/login"
          className="bg-white/5 hover:bg-white/10 border border-white/10 hover:border-white/20 px-5 py-2 rounded-xl text-sm font-semibold transition-all duration-200"
        >
          Sign In
        </Link>
      </header>

      {/* Hero Section */}
      <main className="w-full max-w-4xl mx-auto px-6 text-center z-10 my-auto py-12">
        <div className="inline-flex items-center gap-2 px-3 py-1.5 rounded-full bg-indigo-500/10 border border-indigo-500/20 text-indigo-400 text-xs font-semibold mb-6">
          <Award className="w-4 h-4" />
          Odoo Hackathon &apos;26 Virtual Round
        </div>
        
        <h1 className="text-4xl md:text-6xl font-black tracking-tight leading-[1.15] mb-6">
          Simplify & Digitize <br/>
          <span className="bg-gradient-to-r from-blue-400 via-indigo-400 to-purple-400 bg-clip-text text-transparent">
            Asset & Resource Management
          </span>
        </h1>

        <p className="text-gray-400 text-lg md:text-xl max-w-2xl mx-auto mb-10 leading-relaxed">
          Centralized enterprise ERP to track asset lifecycles, book shared resources, manage maintenance requests, and run scheduled audits.
        </p>

        <div className="flex flex-col sm:flex-row items-center justify-center gap-4">
          <Link
            href="/login"
            className="w-full sm:w-auto bg-indigo-600 hover:bg-indigo-500 text-white px-8 py-4 rounded-xl font-bold flex items-center justify-center gap-2 group transition-all duration-200 shadow-lg shadow-indigo-600/10"
          >
            Enter Platform
            <ArrowRight className="w-5 h-5 group-hover:translate-x-1 transition-transform" />
          </Link>
          <Link
            href="/signup"
            className="w-full sm:w-auto bg-white/5 hover:bg-white/10 border border-white/5 hover:border-white/10 px-8 py-4 rounded-xl font-bold transition-all duration-200"
          >
            Create Account
          </Link>
        </div>

        {/* Feature Grid */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mt-20 text-left">
          <div className="p-6 bg-white/[0.01] border border-white/[0.04] rounded-2xl">
            <Activity className="w-8 h-8 text-indigo-400 mb-4" />
            <h3 className="font-semibold text-lg mb-2">Lifecycle Tracking</h3>
            <p className="text-gray-400 text-sm leading-relaxed">
              Track assets dynamically from Available and Allocated to Under Maintenance, Lost, and Retired.
            </p>
          </div>
          <div className="p-6 bg-white/[0.01] border border-white/[0.04] rounded-2xl">
            <Calendar className="w-8 h-8 text-blue-400 mb-4" />
            <h3 className="font-semibold text-lg mb-2">Resource Bookings</h3>
            <p className="text-gray-400 text-sm leading-relaxed">
              Book rooms, vehicles, and shared equipment dynamically with conflict-free overlap validations.
            </p>
          </div>
          <div className="p-6 bg-white/[0.01] border border-white/[0.04] rounded-2xl">
            <Shield className="w-8 h-8 text-purple-400 mb-4" />
            <h3 className="font-semibold text-lg mb-2">Structured Auditing</h3>
            <p className="text-gray-400 text-sm leading-relaxed">
              Initiate audit cycles, assign auditors, verify inventory statuses, and generate discrepancy reports.
            </p>
          </div>
        </div>
      </main>

      {/* Footer */}
      <footer className="w-full max-w-7xl mx-auto px-6 py-6 text-center text-xs text-gray-500 z-10 border-t border-white/[0.04] mt-12">
        AssetFlow ERP &copy; 2026. Built with Next.js & Spring Boot for Odoo Hackathon.
      </footer>
    </div>
  );
}
