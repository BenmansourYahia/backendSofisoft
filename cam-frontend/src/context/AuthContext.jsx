import React, { createContext, useContext, useState, useEffect } from 'react';
import { authAPI } from '../services/api';

const AuthContext = createContext();

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [stores, setStores] = useState([]);
  const [parameters, setParameters] = useState([]);
  const [loading, setLoading] = useState(true);
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  useEffect(() => {
    // Check if user is already logged in
    const savedUser = localStorage.getItem('cam_user');
    const savedStores = localStorage.getItem('cam_stores');
    const savedParams = localStorage.getItem('cam_parameters');
    
    if (savedUser) {
      setUser(JSON.parse(savedUser));
      setStores(savedStores ? JSON.parse(savedStores) : []);
      setParameters(savedParams ? JSON.parse(savedParams) : []);
      setIsAuthenticated(true);
    }
    setLoading(false);
  }, []);

  const login = async (credentials) => {
    try {
      setLoading(true);
      const response = await authAPI.login(credentials);
      
      if (response.data.success) {
        const data = JSON.parse(response.data.data);
        
        setUser(data.user);
        setStores(data.mags || []);
        setParameters(data.param || []);
        setIsAuthenticated(true);
        
        // Save to localStorage
        localStorage.setItem('cam_user', JSON.stringify(data.user));
        localStorage.setItem('cam_stores', JSON.stringify(data.mags || []));
        localStorage.setItem('cam_parameters', JSON.stringify(data.param || []));
        
        return { success: true };
      } else {
        return { success: false, message: response.data.message };
      }
    } catch (error) {
      console.error('Login error:', error);
      return { 
        success: false, 
        message: error.response?.data?.message || 'Login failed. Please try again.' 
      };
    } finally {
      setLoading(false);
    }
  };

  const logout = () => {
    setUser(null);
    setStores([]);
    setParameters([]);
    setIsAuthenticated(false);
    localStorage.removeItem('cam_user');
    localStorage.removeItem('cam_stores');
    localStorage.removeItem('cam_parameters');
  };

  const value = {
    user,
    stores,
    parameters,
    loading,
    isAuthenticated,
    login,
    logout,
  };

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
};