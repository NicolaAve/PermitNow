import React from 'react';
import { NavBarDashboard } from './components/NavBarDashboard';
import { LicencesScrollableList } from './components/LicencesScrollableList';
import { PermitsScrollableList } from './components/PermitsScrollableList';
import { ServerStatus } from './components/ServerStatus';
import { ServerNumbers } from './components/ServerNumbers';

export const DashboardHomePage = () => {
  return (
    <div>
      <NavBarDashboard />
      <div className="p-5">
        <h1 className="text-blue-600 text-3xl py-5 px-5">Ultime Licenze</h1>
        <div className="border-2 rounded-md p-1">
          <LicencesScrollableList limit={10} maxHeight="400px" />
        </div>
      </div>
      <div className="p-5">
        <h1 className="text-blue-600 text-3xl py-5 px-5">
          Ultimi Permessi Richiesti
        </h1>
        <div className="border-2 rounded-md p-1">
          <PermitsScrollableList limit={10} maxHeight="400px" />
        </div>
      </div>

      <div className="flex flex-row flex-wrap items-start gap-10 w-full p-10">
        <ServerStatus />
        <ServerNumbers />
      </div>
    </div>
  );
};
