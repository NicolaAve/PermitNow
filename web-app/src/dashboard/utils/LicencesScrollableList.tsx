import React, { useCallback, useEffect, useMemo, useState } from 'react';
import { apiClient } from '../../api/client';

interface LicencesListProps {
  limit?: number;
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

interface LicenceRecord {
  licenceId: number;
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

export const LicencesScrollableList = ({ limit = 20 }: LicencesListProps) => {
  const [licences, setLicences] = useState<LicenceRecord[]>([]);
  const [records, setRecords] = useState<ListRecord[]>([]);
  const [fiscalCodeSearch, setFiscalCodeSearch] = useState('');

  const fetchLicences = useCallback(async () => {
    const response = await apiClient.get('/admin/licence');

    const formattedData: LicenceRecord[] = (response.data ?? []).map(
      (item) => ({
        licenceId: item.licenceId,
        licenceNumber: item.licenceNumber,
        releasedBy: item.releasedBy,
        season: item.season,
        nokill: item.nokill,
        type: item.type,
        qrCode: item.qrCode,
        userId: item.userId,
      })
    );

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

    setLicences(formattedData);
    if (limit === 0) {
      setRecords(recordList);
    } else {
      setRecords(recordList.slice(0, limit));
    }
  }, [limit]);

  useEffect(() => {
    void fetchLicences();
  }, [fetchLicences]);

  const filteredRecords = useMemo(() => {
    const q = fiscalCodeSearch.trim().toUpperCase();
    if (!q) return records;
    return records.filter((record) =>
      record.user.fiscalCode?.toUpperCase().includes(q)
    );
  }, [records, fiscalCodeSearch]);

  const approveLicence = async (licenceId: number) => {
    await apiClient.get('/licence/approve/' + licenceId);
    await fetchLicences();
  };

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

      {licences.length === 0 ? (
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
                {record.licence.type === 'pending' ? (
                  <button
                    className="bg-blue-600 text-white px-4 py-2 rounded mt-4"
                    onClick={() =>
                      void approveLicence(record.licence.licenceId)
                    }
                  >
                    Approva
                  </button>
                ) : (
                  <button
                    disabled={true}
                    className="bg-gray-500 text-white px-4 py-2 rounded mt-4"
                  >
                    Approva
                  </button>
                )}
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
