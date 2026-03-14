import React from 'react';
import { NavBarDashboard } from './utils/NavBarDashboard';
import { LicencesScrollableList } from './utils/LicencesScrollableList';
import { PermitsScrollableList } from './utils/PermitsScrollableList';

export const DashboardHomePage = () => {
  return (
    <div>
      <NavBarDashboard />
      <div className="p-5">
        <h1 className="text-blue-600 text-2xl py-5 px-5">Ultime Licenze</h1>
        <div className="border-2 rounded-md p-1">
          <LicencesScrollableList limit={10} />
        </div>
      </div>
      <div className="p-5">
        <h1 className="text-blue-600 text-2xl py-5 px-5">
          Ultimi Permessi Richiesti
        </h1>
        <div className="border-2 rounded-md p-1">
          <PermitsScrollableList limit={10} />
        </div>
      </div>
    </div>
  );
};
