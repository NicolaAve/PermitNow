import axios from 'axios';

export const apiClient = axios.create({
  // cambiare l'ip col proprio ip, non cambiare la porta
  baseURL: 'http://tuo-ip-qui:8080',
  headers: {
    'Content-Type': 'application/json',
  },
  timeout: 10000,
});
