"use client";

import { createContext, useContext, useEffect, useMemo, useState, type ReactNode } from "react";
import type { AuthenticatedUser } from "@/features/auth/auth-api";
import { AUTH_SESSION_STORAGE_KEY } from "@/features/auth/auth-session";

interface AuthContextValue {
  user: AuthenticatedUser | null;
  loading: boolean;
  signIn: (user: AuthenticatedUser) => void;
  signOut: () => void;
  hasRole: (...roles: string[]) => boolean;
}

const AuthContext = createContext<AuthContextValue | undefined>(undefined);

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<AuthenticatedUser | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    try {
      const stored = window.sessionStorage.getItem(AUTH_SESSION_STORAGE_KEY);
      if (stored) {
        const parsed = JSON.parse(stored) as AuthenticatedUser;
        if (parsed?.id && parsed.username && parsed.accessToken && parsed.refreshToken && Array.isArray(parsed.roles)) {
          setUser(parsed);
        } else {
          window.sessionStorage.removeItem(AUTH_SESSION_STORAGE_KEY);
        }
      }
    } catch {
      window.sessionStorage.removeItem(AUTH_SESSION_STORAGE_KEY);
    } finally {
      setLoading(false);
    }
  }, []);

  const value = useMemo<AuthContextValue>(() => ({
    user,
    loading,
    signIn: (nextUser) => {
      setUser(nextUser);
      window.sessionStorage.setItem(AUTH_SESSION_STORAGE_KEY, JSON.stringify(nextUser));
    },
    signOut: () => {
      setUser(null);
      window.sessionStorage.removeItem(AUTH_SESSION_STORAGE_KEY);
    },
    hasRole: (...roles) => roles.some((role) => user?.roles.includes(role)),
  }), [user, loading]);

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error("useAuth must be used inside AuthProvider");
  }
  return context;
}
