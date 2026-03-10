import { apiClient } from './client';
import type { LoginJson, RegisterJson } from '../types/models';

export const authApi = {
  login: async (credentials: LoginJson) => {
    const response = await apiClient.post('/login', credentials);
    return response.data;
  },

  register: async (userData: RegisterJson) => {
    const response = await apiClient.post('/register', userData);
    return response.data;
  },
};
