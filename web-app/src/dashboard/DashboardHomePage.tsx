import React from 'react';
import { NavBarDashboard } from './utils/NavBarDashboard';
import { LicencesScrollableList } from './homepage/LicencesScrollableList';

export const DashboardHomePage = () => {
  return (
    <div>
      <NavBarDashboard />
      <div className="p-5">
        <h1 className="text-blue-600 text-2xl py-5 px-5">Ultime Richieste</h1>
        <div className="border-2 rounded-md p-1">
          <LicencesScrollableList />
        </div>
      </div>
    </div>
  );
};
