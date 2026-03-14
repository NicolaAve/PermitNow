import React from 'react';
import { LicencesScrollableList } from '../utils/LicencesScrollableList';
import { NavBarDashboard } from '../utils/NavBarDashboard';

export const LicenceManagementPage = () => {
  return (
    <div className="min-h-screen flex flex-col">
      <NavBarDashboard />
      <div className="flex-1 p-10">
        <LicencesScrollableList limit={0} />
      </div>
    </div>
  );
};
