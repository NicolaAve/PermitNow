import { DashboardHomePage } from './dashboard/DashboardHomePage';
import { LicenceManagementPage } from './dashboard/LicenceManagement/LicenceManagementPage';
import { DashboardLoginPage } from './dashboard/login/DashboardLoginPage';
import { HomePage } from './HomePage/HomePage';
import { createBrowserRouter, RouterProvider } from 'react-router-dom';

const router = createBrowserRouter([
  {
    path: '/',
    element: <HomePage />, // Pagina di benvenuto indipendente
  },
  {
    path: '/dashboard/login',
    element: <DashboardLoginPage />, // Pagina della dashboard indipendente
  },
  {
    path: '/dashboard',
    element: <DashboardHomePage />, // Pagina della dashboard indipendente
  },
  {
    path: '/dashboard/licences',
    element: <LicenceManagementPage />, // Pagina della dashboard indipendente
  },
]);

function App() {
  return <RouterProvider router={router} />;
}

export default App;
