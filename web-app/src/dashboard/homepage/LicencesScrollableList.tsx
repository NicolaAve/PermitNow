import React, { useEffect, useState } from 'react';
import { apiClient } from '../../api/client';

export const LicencesScrollableList = () => {
  interface UserRecord {
    name: string;
    surname: string;
    password: string;
    role: string;
    email: string;
    fiscalCode: string;
    verified: boolean;
  }

  interface LicenceRecord {
    licenceNumber: string;
    releasedBy: string;
    season: string;
    nokill: boolean;
    type: string;
    qrCode: string;
    userId: string;
  }

  interface ListRecord {
    licence: LicenceRecord;
    user: UserRecord;
  }

  const [permits, setPermits] = useState<LicenceRecord[]>([]);
  const [records, setRecords] = useState<ListRecord[]>([]);

  useEffect(() => {
    const fetchLicences = async () => {
      const response = await apiClient.get('/admin/licence');

      console.log(response.data);

      const formattedData: LicenceRecord[] = response.data.map((item) => ({
        licenceNumber: item.licenceNumber,
        releasedBy: item.releasedBy, // Uniamo nome e cognome
        season: item.season,
        nokill: item.nokill,
        type: item.type,
        qrCode: item.qrCode,
        userId: item.userId,
      }));

      const recordList: ListRecord[] = await Promise.all(
        formattedData.map(async (it) => {
          const userResponse = await apiClient.post('/user/info/' + it.userId);
          const formattedUserData: UserRecord = userResponse.data;

          return {
            licence: it,
            user: formattedUserData,
          };
        })
      );

      setPermits(formattedData);
      setRecords(recordList);
    };

    fetchLicences();
  }, []);

  return (
    <div className="max-h-[400px] overflow-y-auto p-2">
      {permits.length === 0 ? (
        <p className="text-center text-gray-500 py-8">
          Nessun permesso trovato.
        </p>
      ) : (
        <ul className="flex flex-col gap-1">
          {records.map((record) => (
            <li
              key={record.licence.licenceNumber}
              className="flex items-center justify-between p-3 hover:bg-gray-50 rounded-lg transition-colors cursor-pointer border border-black hover:border-gray-100"
            >
              <div className="flex flex-col">
                <span className="font-semibold text-gray-800">
                  {record.licence.licenceNumber}
                </span>
                <span className="text-xs text-gray-500">
                  {record.licence.releasedBy} - {record.licence.season} -{' '}
                  {!record.licence.nokill ? 'NOKILL' : 'KILL'}
                </span>
              </div>
              <div>
                {record.licence.type === 'pending' ? (
                  <span className="text-amber-300">PENDING</span>
                ) : record.licence.type === 'valid' ? (
                  <span className="text-green-500">VALID</span>
                ) : null}
              </div>
              <div>
                {record.user.verified ? (
                  <span>{record.user.fiscalCode}</span>
                ) : (
                  <span className="text-amber-300">
                    {record.user.fiscalCode}
                  </span>
                )}
              </div>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
};
