import { apiClient } from './client';
import type { LoginJson, RegisterJson } from '../types/models';

export const authApi = {
  login: async (credentials: LoginJson): Promise<any> => {
    const response = await apiClient.post('/login', credentials);
    return response.data;
  },

  register: async (userData: RegisterJson): Promise<any> => {
    const response = await apiClient.post('/register', userData);
    return response.data;
  },
};
