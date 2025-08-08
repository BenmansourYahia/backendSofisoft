import React from 'react';
import { Bars3Icon, BellIcon } from '@heroicons/react/24/outline';
import { useAuth } from '../../context/AuthContext';

const Header = ({ onMenuClick, title = 'Dashboard' }) => {
  const { user, stores } = useAuth();
  
  const currentStore = stores.find(store => store.numMagasin === user?.numMagasin);

  return (
    <header className="bg-white shadow-soft border-b border-gray-200">
      <div className="flex items-center justify-between px-4 py-4">
        <div className="flex items-center space-x-4">
          <button
            onClick={onMenuClick}
            className="p-2 rounded-lg text-gray-600 hover:bg-gray-100 lg:hidden"
          >
            <Bars3Icon className="w-6 h-6" />
          </button>
          <div>
            <h1 className="text-2xl font-bold text-gray-900">{title}</h1>
            {currentStore && (
              <p className="text-sm text-gray-500">
                {currentStore.nomMagasin} ({currentStore.codeMagasin})
              </p>
            )}
          </div>
        </div>

        <div className="flex items-center space-x-4">
          <button className="p-2 rounded-lg text-gray-600 hover:bg-gray-100 relative">
            <BellIcon className="w-6 h-6" />
            <span className="absolute top-1 right-1 w-2 h-2 bg-red-500 rounded-full"></span>
          </button>
          
          <div className="flex items-center space-x-3">
            <div className="w-8 h-8 bg-primary-100 rounded-full flex items-center justify-center">
              <span className="text-primary-600 font-medium text-sm">
                {user?.nom?.charAt(0)?.toUpperCase() || 'U'}
              </span>
            </div>
            <div className="hidden sm:block">
              <p className="text-sm font-medium text-gray-900">{user?.nom}</p>
              <p className="text-xs text-gray-500">Administrator</p>
            </div>
          </div>
        </div>
      </div>
    </header>
  );
};

export default Header;