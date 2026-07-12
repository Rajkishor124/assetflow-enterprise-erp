'use client';

import { useEffect, useState } from 'react';
import { useRouter, usePathname } from 'next/navigation';
import Link from 'next/link';
import { 
  Shield, 
  LayoutDashboard, 
  Package, 
  ClipboardList, 
  CalendarRange, 
  Wrench, 
  FileCheck, 
  Settings, 
  LogOut, 
  Bell, 
  User as UserIcon,
  Menu,
  X
} from 'lucide-react';
import { getCurrentUser, clearAuth, User } from '../../lib/auth';

interface SidebarItem {
  name: string;
  href: string;
  icon: any;
  allowedRoles: string[];
}

const sidebarItems: SidebarItem[] = [
  { 
    name: 'Dashboard', 
    href: '/dashboard', 
    icon: LayoutDashboard, 
    allowedRoles: ['ADMIN', 'ASSET_MANAGER', 'DEPT_HEAD', 'EMPLOYEE'] 
  },
  { 
    name: 'Assets Directory', 
    href: '/assets', 
    icon: Package, 
    allowedRoles: ['ADMIN', 'ASSET_MANAGER', 'DEPT_HEAD', 'EMPLOYEE'] 
  },
  { 
    name: 'Allocations', 
    href: '/allocations', 
    icon: ClipboardList, 
    allowedRoles: ['ADMIN', 'ASSET_MANAGER', 'DEPT_HEAD', 'EMPLOYEE'] 
  },
  { 
    name: 'Resource Bookings', 
    href: '/bookings', 
    icon: CalendarRange, 
    allowedRoles: ['ADMIN', 'ASSET_MANAGER', 'DEPT_HEAD', 'EMPLOYEE'] 
  },
  { 
    name: 'Maintenance', 
    href: '/maintenance', 
    icon: Wrench, 
    allowedRoles: ['ADMIN', 'ASSET_MANAGER', 'DEPT_HEAD', 'EMPLOYEE'] 
  },
  { 
    name: 'Audit Cycles', 
    href: '/audits', 
    icon: FileCheck, 
    allowedRoles: ['ADMIN', 'ASSET_MANAGER', 'DEPT_HEAD', 'EMPLOYEE'] 
  },
  { 
    name: 'Admin Setup', 
    href: '/admin', 
    icon: Settings, 
    allowedRoles: ['ADMIN'] 
  },
];

export default function DashboardLayout({ children }: { children: React.ReactNode }) {
  const router = useRouter();
  const pathname = usePathname();
  const [currentUser, setCurrentUserVal] = useState<User | null>(null);
  const [isSidebarOpen, setIsSidebarOpen] = useState(false);
  const [notificationCount, setNotificationCount] = useState(3); // Mock notifications count

  useEffect(() => {
    const user = getCurrentUser();
    if (!user) {
      router.push('/login');
    } else {
      setCurrentUserVal(user);
    }
  }, [router]);

  const handleSignOut = () => {
    clearAuth();
    router.push('/login');
  };

  if (!currentUser) {
    return (
      <div className="min-h-screen bg-[#0d0e12] flex items-center justify-center">
        <div className="w-12 h-12 border-4 border-indigo-500/20 border-t-indigo-500 rounded-full animate-spin" />
      </div>
    );
  }

  // Filter sidebar items based on user role
  const allowedItems = sidebarItems.filter(item => 
    item.allowedRoles.includes(currentUser.role)
  );

  return (
    <div className="min-h-screen bg-[#090a0f] text-gray-100 flex">
      {/* Sidebar - Desktop */}
      <aside className="hidden md:flex flex-col w-64 bg-[#0d0e14] border-r border-white/[0.04] p-5 shrink-0 justify-between">
        <div className="space-y-8">
          {/* Logo */}
          <div className="flex items-center gap-2.5 px-2">
            <div className="w-9 h-9 bg-indigo-500/10 border border-indigo-500/20 rounded-lg flex items-center justify-center">
              <Shield className="w-5 h-5 text-indigo-400" />
            </div>
            <span className="font-bold text-lg tracking-tight">AssetFlow</span>
          </div>

          {/* Navigation Links */}
          <nav className="space-y-1">
            {allowedItems.map((item) => {
              const isActive = pathname === item.href || pathname.startsWith(item.href + '/');
              return (
                <Link
                  key={item.name}
                  href={item.href}
                  className={`flex items-center gap-3 px-3 py-2.5 rounded-xl text-sm font-semibold transition-all duration-150 ${
                    isActive 
                      ? 'bg-indigo-600 text-white shadow-lg shadow-indigo-600/15' 
                      : 'text-gray-400 hover:bg-white/[0.03] hover:text-gray-200'
                  }`}
                >
                  <item.icon className="w-5 h-5 shrink-0" />
                  {item.name}
                </Link>
              );
            })}
          </nav>
        </div>

        {/* User Card & Logout */}
        <div className="pt-4 border-t border-white/[0.04] space-y-4">
          <div className="flex items-center gap-3 px-2">
            <div className="w-9 h-9 rounded-full bg-indigo-600/10 border border-indigo-600/20 flex items-center justify-center text-indigo-400 font-bold uppercase text-sm">
              {currentUser.name.substring(0, 2)}
            </div>
            <div className="min-w-0">
              <p className="text-sm font-semibold text-white truncate">{currentUser.name}</p>
              <p className="text-[10px] font-bold text-indigo-400 uppercase tracking-wider">
                {currentUser.role.replace('_', ' ')}
              </p>
            </div>
          </div>
          <button
            onClick={handleSignOut}
            className="w-full flex items-center gap-3 px-3 py-2.5 rounded-xl text-sm font-semibold text-red-400 hover:bg-red-500/5 transition-colors cursor-pointer"
          >
            <LogOut className="w-5 h-5 shrink-0" />
            Sign Out
          </button>
        </div>
      </aside>

      {/* Mobile Drawer Sidebar */}
      {isSidebarOpen && (
        <div className="fixed inset-0 bg-black/60 z-50 md:hidden animate-fade-in" onClick={() => setIsSidebarOpen(false)}>
          <aside 
            className="flex flex-col w-64 bg-[#0d0e14] border-r border-white/[0.04] p-5 h-full justify-between animate-slide-in"
            onClick={(e) => e.stopPropagation()}
          >
            <div className="space-y-8">
              <div className="flex items-center justify-between">
                <div className="flex items-center gap-2.5">
                  <div className="w-9 h-9 bg-indigo-500/10 border border-indigo-500/20 rounded-lg flex items-center justify-center">
                    <Shield className="w-5 h-5 text-indigo-400" />
                  </div>
                  <span className="font-bold text-lg tracking-tight">AssetFlow</span>
                </div>
                <button onClick={() => setIsSidebarOpen(false)} className="text-gray-400 hover:text-white">
                  <X className="w-6 h-6" />
                </button>
              </div>

              <nav className="space-y-1">
                {allowedItems.map((item) => {
                  const isActive = pathname === item.href || pathname.startsWith(item.href + '/');
                  return (
                    <Link
                      key={item.name}
                      href={item.href}
                      onClick={() => setIsSidebarOpen(false)}
                      className={`flex items-center gap-3 px-3 py-2.5 rounded-xl text-sm font-semibold transition-all duration-150 ${
                        isActive 
                          ? 'bg-indigo-600 text-white shadow-lg shadow-indigo-600/15' 
                          : 'text-gray-400 hover:bg-white/[0.03] hover:text-gray-200'
                      }`}
                    >
                      <item.icon className="w-5 h-5 shrink-0" />
                      {item.name}
                    </Link>
                  );
                })}
              </nav>
            </div>

            <div className="pt-4 border-t border-white/[0.04] space-y-4">
              <div className="flex items-center gap-3 px-2">
                <div className="w-9 h-9 rounded-full bg-indigo-600/10 border border-indigo-600/20 flex items-center justify-center text-indigo-400 font-bold uppercase text-sm">
                  {currentUser.name.substring(0, 2)}
                </div>
                <div className="min-w-0">
                  <p className="text-sm font-semibold text-white truncate">{currentUser.name}</p>
                  <p className="text-[10px] font-bold text-indigo-400 uppercase tracking-wider">
                    {currentUser.role.replace('_', ' ')}
                  </p>
                </div>
              </div>
              <button
                onClick={handleSignOut}
                className="w-full flex items-center gap-3 px-3 py-2.5 rounded-xl text-sm font-semibold text-red-400 hover:bg-red-500/5 transition-colors cursor-pointer"
              >
                <LogOut className="w-5 h-5 shrink-0" />
                Sign Out
              </button>
            </div>
          </aside>
        </div>
      )}

      {/* Main Content Area */}
      <div className="flex-1 flex flex-col min-w-0 overflow-y-auto">
        {/* Header */}
        <header className="h-16 border-b border-white/[0.04] bg-[#0d0e14]/50 backdrop-blur-md px-6 flex items-center justify-between sticky top-0 z-40">
          <div className="flex items-center gap-4">
            <button 
              onClick={() => setIsSidebarOpen(true)}
              className="md:hidden text-gray-400 hover:text-white outline-none"
            >
              <Menu className="w-6 h-6" />
            </button>
            <h1 className="text-lg font-bold text-white capitalize">
              {pathname === '/dashboard' ? 'Dashboard Summary' : pathname.split('/')[1]?.replace('-', ' ')}
            </h1>
          </div>

          {/* Top Actions */}
          <div className="flex items-center gap-4">
            {/* Notification Bell */}
            <button className="relative w-9 h-9 rounded-xl bg-white/[0.02] border border-white/[0.06] hover:bg-white/[0.05] flex items-center justify-center transition-colors cursor-pointer">
              <Bell className="w-5 h-5 text-gray-300" />
              {notificationCount > 0 && (
                <span className="absolute -top-1.5 -right-1.5 w-5 h-5 bg-indigo-600 rounded-full flex items-center justify-center text-[10px] font-bold text-white border-2 border-[#090a0f] animate-pulse">
                  {notificationCount}
                </span>
              )}
            </button>

            {/* Profile Dropdown Indicator */}
            <div className="flex items-center gap-2 pl-2 border-l border-white/[0.08]">
              <div className="w-8 h-8 rounded-full bg-white/[0.04] flex items-center justify-center">
                <UserIcon className="w-4 h-4 text-gray-400" />
              </div>
              <span className="hidden sm:inline text-sm font-semibold text-gray-300">
                {currentUser.name}
              </span>
            </div>
          </div>
        </header>

        {/* Content body */}
        <main className="p-6 md:p-8 flex-1">
          {children}
        </main>
      </div>
    </div>
  );
}
