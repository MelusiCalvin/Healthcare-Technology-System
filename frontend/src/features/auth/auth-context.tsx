"use client";

import { createContext, useContext, useEffect, useMemo, useState, type ReactNode } from "react";
import { authApi, type AuthenticatedUser, type AuthSession } from "@/features/auth/auth-api";

const STORAGE_KEY = "ubuntu-health-display-user";
const SESSION_KEY = "ubuntu-health-auth-session";

type StoredSession = AuthSession;

interface AuthContextValue {
  user: AuthenticatedUser | null;
  loading: boolean;
  signIn: (session: AuthSession) => void;
  signOut: () => Promise<void>;
  hasRole: (...roles: string[]) => boolean;
}

const AuthContext = createContext<AuthContextValue | undefined>(undefined);

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<AuthenticatedUser | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    try {
      const stored = window.sessionStorage.getItem(SESSION_KEY);
      if (stored) {
        const parsed = JSON.parse(stored) as StoredSession;
        if (parsed?.user?.username && parsed.accessToken && parsed.refreshToken) {
          setUser(parsed.user);
        }
      }
    } catch {
      window.sessionStorage.removeItem(SESSION_KEY);
    } finally {
      setLoading(false);
    }
  }, []);

  const value = useMemo<AuthContextValue>(() => ({
    user,
    loading,
    signIn: (session) => {
      setUser(session.user);
      window.sessionStorage.setItem(SESSION_KEY, JSON.stringify(session));
    },
    signOut: async () => {
      const stored = window.sessionStorage.getItem(SESSION_KEY);
      window.sessionStorage.removeItem(SESSION_KEY);
      window.sessionStorage.removeItem(STORAGE_KEY);
      setUser(null);
      if (stored) {
        try {
          await authApi.logout((JSON.parse(stored) as StoredSession).refreshToken);
        } catch {
        }
      }
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
