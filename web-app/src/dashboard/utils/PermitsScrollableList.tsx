import React, { useEffect, useMemo, useState } from 'react';
import { apiClient } from '../../api/client';

interface PermitsListProps {
  limit: number;
}
interface UserRecord {
  name: string;
  surname: string;
  password: string;
  role: string;
  email: string;
  fiscalCode: string;
  verified: boolean;
}

interface PermitRecord {
  type: string;
  expireDate: string;
  qrCodeToken: string;
  userId: string;
}

interface ListRecord {
  permit: PermitRecord;
  user: UserRecord;
}

export const PermitsScrollableList = ({ limit = 20 }: PermitsListProps) => {
  const [permits, setPermits] = useState<PermitRecord[]>([]);
  const [records, setRecords] = useState<ListRecord[]>([]);
  const [fiscalCodeSearch, setFiscalCodeSearch] = useState('');

  useEffect(() => {
    const fetchPermits = async () => {
      const response = await apiClient.get('/admin/permit');

      console.log(response.data);

      const formattedData: PermitRecord[] = response.data.map((item) => ({
        type: item.type,
        expireDate: item.expireDate,
        qrCodeToken: item.qrCodeToken,
        userId: item.userId,
      }));

      const recordList: ListRecord[] = await Promise.all(
        formattedData.map(async (it) => {
          const userResponse = await apiClient.post('/user/info/' + it.userId);
          const formattedUserData: UserRecord = userResponse.data;

          return {
            permit: it,
            user: formattedUserData,
          };
        })
      );

      setPermits(formattedData);
      if (limit === 0) {
        setRecords(recordList);
      } else {
        setRecords(recordList.slice(0, limit));
      }
    };

    fetchPermits();
  }, []);

  const filteredRecords = useMemo(() => {
    const q = fiscalCodeSearch.trim().toUpperCase();
    if (!q) return records;
    return records.filter((record) =>
      record.user.fiscalCode?.toUpperCase().includes(q)
    );
  }, [records, fiscalCodeSearch]);

  return (
    <div className="max-h-[400px] overflow-y-auto p-2 ">
      <div className="mb-3 flex justify-end">
        <input
          type="text"
          value={fiscalCodeSearch}
          onChange={(e) => setFiscalCodeSearch(e.target.value)}
          placeholder="Cerca per codice fiscale..."
          className="w-full max-w-sm border border-gray-300 rounded px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
        />
      </div>

      {permits.length === 0 ? (
        <p className="text-center text-gray-500 py-8">
          Nessun permesso trovato.
        </p>
      ) : filteredRecords.length === 0 ? (
        <p className="text-center text-gray-500 py-8">
          Nessun risultato per il codice fiscale cercato.
        </p>
      ) : (
        <ul className="flex flex-col gap-1">
          {filteredRecords.map((record) => (
            <li
              key={record.permit.qrCodeToken}
              className="flex items-center justify-between p-3 hover:bg-gray-50 rounded-lg transition-colors cursor-pointer border border-black hover:border-gray-100"
            >
              <div className="flex flex-col">
                <span className="font-semibold text-gray-800">
                  {record.permit.qrCodeToken}
                </span>
                <span className="text-xs text-gray-500">
                  Tipo: {record.permit.type} - Scadenza:{' '}
                  {record.permit.expireDate}
                </span>
              </div>
              <div>
                {record.permit.type === 'pending' ? (
                  <span className="text-amber-300">PENDING</span>
                ) : record.permit.type === 'valid' ? (
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
