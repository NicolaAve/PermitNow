import React, { useEffect, useState } from 'react';
import { apiClient } from '../../api/client';

export const ServerNumbers = () => {
  const [permitStatus, setPermitStatus] = useState('');
  const [licenceStatus, setLicenceStatus] = useState('');
  const [newsStatus, setNewsStatus] = useState('');
  useEffect(() => {
    const fetchStatuses = async () => {
      const permitResponse = await apiClient.get('/status/permit');
      if (permitResponse.data != 'FA') {
        setPermitStatus(permitResponse.data);
      } else {
        setPermitStatus('FA');
      }

      const licenceResponse = await apiClient.get('/status/licence');
      if (licenceResponse.data != 'FA') {
        setLicenceStatus(licenceResponse.data);
      } else {
        setLicenceStatus('FA');
      }

      const newsResponse = await apiClient.get('/status/news');
      if (newsResponse.data != 'FA') {
        setNewsStatus(newsResponse.data);
      } else {
        setNewsStatus('FA');
      }
    };

    fetchStatuses();
  });

  return (
    <div className="border-2 rounded-sm">
      <h1 className="text-blue-600 text-3xl pt-5 px-5">Numeri dei Servizi</h1>
      <div className="grid grid-cols-1 sm:grid-cols-2 gap-x-5 gap-y-5 w-full mt-6">
        <span className="flex flex-col flex-wrap items-start gap-4 w-full ">
          <div className="flex flex-col justify-center items-center p-4">
            <h1 className="text-blue-300 text-2xl">
              Numero di Permessi Attivi
            </h1>
            <label className="text-green-500 text-2xl">{permitStatus}</label>
            <div className="flex flex-col justify-center items-center p-4">
              <h1 className="text-blue-300 text-2xl">
                Numero di Licenze Attive
              </h1>
              <label className="text-green-500 text-2xl">{licenceStatus}</label>
            </div>
          </div>
        </span>
        <span className="flex flex-col flex-wrap items-start gap-4 w-full ">
          <div className="flex flex-col justify-center items-center p-4">
            <h1 className="text-blue-300 text-2xl">Numero di Notizie Attive</h1>
            <label className="text-green-500 text-2xl">{newsStatus}</label>
          </div>
        </span>
      </div>
    </div>
  );
};
