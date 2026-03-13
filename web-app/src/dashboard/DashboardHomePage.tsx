import React from 'react';
import { NavBarDashboard } from './utils/NavBarDashboard';

export const DashboardHomePage = () => {
  return (
    <div>
      <NavBarDashboard />
      <div>
        <h1 className="text-blue-600 text-2xl py-10 px-5">Ultime Richieste</h1>
      </div>
    </div>
  );
};
