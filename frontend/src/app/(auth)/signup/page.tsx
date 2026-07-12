'use client';

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import Link from 'next/link';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { Shield, Key, Mail, User, Eye, EyeOff, Loader2 } from 'lucide-react';
import { signupSchema, SignupInput } from '../../../lib/validators';
import { authApi } from '../../../features/auth/api';

export default function SignupPage() {
  const router = useRouter();
  const [showPassword, setShowPassword] = useState(false);
  const [errorMsg, setErrorMsg] = useState<string | null>(null);
  const [successMsg, setSuccessMsg] = useState<string | null>(null);
  const [isLoading, setIsLoading] = useState(false);

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<SignupInput>({
    resolver: zodResolver(signupSchema),
    defaultValues: {
      firstName: '',
      lastName: '',
      email: '',
      password: '',
    },
  });

  const onSubmit = async (data: SignupInput) => {
    setIsLoading(true);
    setErrorMsg(null);
    setSuccessMsg(null);
    try {
      const response = await authApi.signup(data);
      if (response.success) {
        setSuccessMsg(
          'Registration successful! Redirecting you to the login page...'
        );
        setTimeout(() => {
          router.push('/login');
        }, 2000);
      } else {
        setErrorMsg(response.message || 'Registration failed. Please try again.');
      }
    } catch (err: unknown) {
      const error = err as any;
      const errorMessage =
        error?.response?.data?.message ||
        error?.message ||
        'Network error. Please make sure the backend is running.';
      setErrorMsg(errorMessage);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="relative min-h-screen flex items-center justify-center bg-[#0d0e12] overflow-hidden px-4">
      {/* Background Glowing Gradients */}
      <div className="absolute top-[-20%] left-[-10%] w-[500px] h-[500px] rounded-full bg-indigo-500/10 blur-[120px] pointer-events-none" />
      <div className="absolute bottom-[-20%] right-[-10%] w-[500px] h-[500px] rounded-full bg-blue-500/10 blur-[120px] pointer-events-none" />

      {/* Signup Card */}
      <div className="w-full max-w-md bg-white/[0.02] border border-white/[0.06] backdrop-blur-xl rounded-2xl p-8 shadow-2xl z-10">
        
        {/* Header / Logo */}
        <div className="flex flex-col items-center mb-8">
          <div className="w-12 h-12 bg-indigo-500/10 border border-indigo-500/20 rounded-xl flex items-center justify-center mb-3">
            <Shield className="w-6 h-6 text-indigo-400" />
          </div>
          <h2 className="text-2xl font-bold text-white tracking-tight">Create Account</h2>
          <p className="text-gray-400 text-sm mt-1 text-center">
            Register as an Employee to access AssetFlow
          </p>
        </div>

        {/* Error Alert */}
        {errorMsg && (
          <div className="mb-6 p-4 rounded-xl bg-red-500/10 border border-red-500/20 text-red-400 text-sm">
            {errorMsg}
          </div>
        )}

        {/* Success Alert */}
        {successMsg && (
          <div className="mb-6 p-4 rounded-xl bg-green-500/10 border border-green-500/20 text-green-400 text-sm">
            {successMsg}
          </div>
        )}

        {/* Signup Form */}
        <form onSubmit={handleSubmit(onSubmit)} className="space-y-5">
          {/* First Name & Last Name Grid */}
          <div className="grid grid-cols-2 gap-4">
            {/* First Name field */}
            <div className="space-y-1.5">
              <label className="text-xs font-semibold text-gray-300 uppercase tracking-wider">
                First Name
              </label>
              <div className="relative">
                <User className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-gray-500" />
                <input
                  {...register('firstName')}
                  type="text"
                  placeholder="John"
                  className="w-full bg-[#14151a] border border-white/[0.08] focus:border-indigo-500/80 rounded-xl py-3 pl-9 pr-4 text-white placeholder-gray-500 outline-none transition-all duration-200 text-sm focus:ring-1 focus:ring-indigo-500/20"
                />
              </div>
              {errors.firstName && (
                <span className="text-xs text-red-400 font-medium pl-1">
                  {errors.firstName.message}
                </span>
              )}
            </div>

            {/* Last Name field */}
            <div className="space-y-1.5">
              <label className="text-xs font-semibold text-gray-300 uppercase tracking-wider">
                Last Name
              </label>
              <div className="relative">
                <User className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-gray-500" />
                <input
                  {...register('lastName')}
                  type="text"
                  placeholder="Doe"
                  className="w-full bg-[#14151a] border border-white/[0.08] focus:border-indigo-500/80 rounded-xl py-3 pl-9 pr-4 text-white placeholder-gray-500 outline-none transition-all duration-200 text-sm focus:ring-1 focus:ring-indigo-500/20"
                />
              </div>
              {errors.lastName && (
                <span className="text-xs text-red-400 font-medium pl-1">
                  {errors.lastName.message}
                </span>
              )}
            </div>
          </div>

          {/* Email field */}
          <div className="space-y-1.5">
            <label className="text-xs font-semibold text-gray-300 uppercase tracking-wider">
              Email Address
            </label>
            <div className="relative">
              <Mail className="absolute left-3 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-500" />
              <input
                {...register('email')}
                type="email"
                placeholder="you@organization.com"
                className="w-full bg-[#14151a] border border-white/[0.08] focus:border-indigo-500/80 rounded-xl py-3 pl-11 pr-4 text-white placeholder-gray-500 outline-none transition-all duration-200 text-sm focus:ring-1 focus:ring-indigo-500/20"
              />
            </div>
            {errors.email && (
              <span className="text-xs text-red-400 font-medium pl-1">
                {errors.email.message}
              </span>
            )}
          </div>

          {/* Password field */}
          <div className="space-y-1.5">
            <label className="text-xs font-semibold text-gray-300 uppercase tracking-wider">
              Password
            </label>
            <div className="relative">
              <Key className="absolute left-3 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-500" />
              <input
                {...register('password')}
                type={showPassword ? 'text' : 'password'}
                placeholder="••••••••••••"
                className="w-full bg-[#14151a] border border-white/[0.08] focus:border-indigo-500/80 rounded-xl py-3 pl-11 pr-11 text-white placeholder-gray-500 outline-none transition-all duration-200 text-sm focus:ring-1 focus:ring-indigo-500/20"
              />
              <button
                type="button"
                onClick={() => setShowPassword(!showPassword)}
                className="absolute right-3 top-1/2 -translate-y-1/2 text-gray-500 hover:text-gray-300 outline-none transition-colors"
              >
                {showPassword ? <EyeOff className="w-5 h-5" /> : <Eye className="w-5 h-5" />}
              </button>
            </div>
            {errors.password && (
              <span className="text-xs text-red-400 font-medium pl-1">
                {errors.password.message}
              </span>
            )}
          </div>

          {/* Submit Button */}
          <button
            type="submit"
            disabled={isLoading}
            className="w-full bg-indigo-600 hover:bg-indigo-500 disabled:bg-indigo-600/50 text-white font-medium rounded-xl py-3 flex items-center justify-center gap-2 cursor-pointer transition-all duration-200 shadow-lg shadow-indigo-600/20 text-sm active:scale-[0.98]"
          >
            {isLoading ? (
              <>
                <Loader2 className="w-4 h-4 animate-spin" />
                Creating account...
              </>
            ) : (
              'Create Account'
            )}
          </button>
        </form>

        {/* Footer Link */}
        <div className="mt-8 text-center text-sm text-gray-400">
          Already have an account?{' '}
          <Link
            href="/login"
            className="font-semibold text-indigo-400 hover:text-indigo-300 transition-colors"
          >
            Sign in here
          </Link>
        </div>
      </div>
    </div>
  );
}
