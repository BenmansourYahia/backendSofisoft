import axios from 'axios';

// Create axios instance with base configuration
const api = axios.create({
  baseURL: 'http://localhost:8080',
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor for debugging
api.interceptors.request.use(
  (config) => {
    console.log('API Request:', config.method?.toUpperCase(), config.url, config.data);
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response interceptor for error handling
api.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    console.error('API Error:', error.response?.data || error.message);
    return Promise.reject(error);
  }
);

// API endpoints
export const authAPI = {
  login: (credentials) => api.post('/Login', credentials),
  checkServer: (number) => api.post('/checkServer', number),
};

export const storesAPI = {
  getStores: () => api.post('/getMagasins'),
  getStoresByDate: (params) => api.post('/getMagasinsInfoByDate', params),
  comparePeriodsForStore: (params) => api.post('/getComparePeriode', params),
};

export const salesAPI = {
  getBestSalesProducts: (params) => api.post('/bestSalesPrds', params),
  getSalesByDate: (params) => api.post('/getInfosByDate', params),
  getDailySalesInfo: (params) => api.post('/getInfosDay', params),
  getSalesLines: (params) => api.post('/getLineVentes', params),
};

export const productsAPI = {
  getProductsSold: (params) => api.post('/getPrdsVendus', params),
  getProductDimensions: (params) => api.post('/getDimsPrdVendus', params),
  searchProductStock: (params) => api.post('/StockByProduct', params),
  getGlobalStock: (params) => api.post('/GlobalStock', params),
};

export const configAPI = {
  getParameters: () => api.post('/getParam'),
};

export default api;