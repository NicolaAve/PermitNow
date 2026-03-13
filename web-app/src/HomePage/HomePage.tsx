import React from 'react';
import { useNavigate } from 'react-router-dom';

export const HomePage = () => {
  const navigate = useNavigate();
  const gotToDashboard = () => {
    navigate('/dashboard/login');
  };
  return (
    <div>
      HomePage
      <div>
        <button
          onClick={gotToDashboard}
          className="bg-blue-600 text-white px-4 py-2 rounded mt-4"
        >
          Go to admin Dashboard
        </button>
      </div>
    </div>
  );
};
