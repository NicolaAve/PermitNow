import React, { useState } from 'react';
import { apiClient } from '../../api/client';
import { useNavigate } from 'react-router-dom';

export const DashboardLoginPage = () => {
  const [form, setForm] = useState({
    email: '',
    password: '',
  });

  const navigate = useNavigate();
  const gotToDashboard = (componentName: string) => {
    navigate(componentName);
  };

  const [hasError, setHasError] = useState(false);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    const response = await apiClient.post('/login/admin', form);

    console.log(response.data);

    if (response.data == -1) {
      setHasError(true);
    } else {
      gotToDashboard('/dashboard');
      setHasError(false);
    }
  };

  return (
    <main className="min-h-screen bg-white grid place-items-center p-6 font-sans">
      <section className="w-full max-w-[420px] rounded-[14px] border border-gray-200 bg-white p-7 shadow-[0_10px_30px_rgba(0,0,0,0.04)]">
        <header className="mb-5">
          <h1 className="m-0 text-[1.4rem] text-gray-900">
            Accedi alla dashboard
          </h1>
          <p className="mt-2 text-[0.95rem] text-gray-500">
            Inserisci le tue credenziali per continuare.
          </p>
        </header>

        <form onSubmit={handleSubmit} className="grid gap-3.5">
          <label className="grid gap-1.5 text-[0.92rem] text-gray-700">
            Email
            <input
              type="email"
              name="email"
              placeholder="nome@dominio.it"
              required
              value={form.email}
              onChange={handleChange}
              className="h-[42px] rounded-[10px] border border-gray-300 px-3 text-[0.95rem] outline-none focus:border-blue-500 focus:ring-2 focus:ring-blue-200"
            />
          </label>

          <label className="grid gap-1.5 text-[0.92rem] text-gray-700">
            Password
            <input
              type="password"
              name="password"
              placeholder="••••••••"
              required
              value={form.password}
              onChange={handleChange}
              className="h-[42px] rounded-[10px] border border-gray-300 px-3 text-[0.95rem] outline-none focus:border-blue-500 focus:ring-2 focus:ring-blue-200"
            />
          </label>

          <div className="flex items-center justify-between">
            <label className="flex items-center gap-2 text-[0.9rem] text-gray-600">
              {/* <input type="checkbox" name="remember" />
              Ricordami */}
            </label>

            <button
              type="button"
              className="cursor-pointer border-none bg-transparent p-0 text-[0.9rem] text-blue-600 hover:text-blue-700"
            >
              Password dimenticata?
            </button>
          </div>

          <button
            type="submit"
            className="mt-1 h-11 rounded-[10px] border border-blue-700 bg-blue-600 text-[0.95rem] font-semibold text-white hover:bg-blue-700"
          >
            Accedi
          </button>
        </form>

        {hasError && (
          <div className="mt-4 text-sm text-red-600">
            Errore durante il Login, controlla che la password non sia errata
          </div>
        )}
      </section>
    </main>
  );
};
