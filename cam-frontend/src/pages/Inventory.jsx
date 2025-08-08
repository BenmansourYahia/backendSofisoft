import React, { useState, useEffect } from 'react';
import { MagnifyingGlassIcon, FunnelIcon, CubeIcon } from '@heroicons/react/24/outline';
import { productsAPI } from '../services/api';
import LoadingSpinner from '../components/UI/LoadingSpinner';

const STOCK_FILTERS = {
  ALL: 2, // TAG_STOCK_AVEC_ZERO
  NON_ZERO: 1, // TAG_STOCK_DIFF_ZERO
  ZERO: 3, // TAG_STOCK_EQAL_ZERO
  NEGATIVE: 4, // TAG_STOCK_INF_ZERO
  POSITIVE: 5, // TAG_STOCK_SUP_ZERO
};

const Inventory = () => {
  const [loading, setLoading] = useState(true);
  const [inventory, setInventory] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [stockFilter, setStockFilter] = useState(STOCK_FILTERS.ALL);
  const [currentPage, setCurrentPage] = useState(1);
  const itemsPerPage = 50;

  useEffect(() => {
    fetchInventory();
  }, [stockFilter, currentPage]);

  const fetchInventory = async () => {
    try {
      setLoading(true);
      
      const params = {
        from: (currentPage - 1) * itemsPerPage + 1,
        to: currentPage * itemsPerPage,
        stockBy: stockFilter
      };

      const response = await productsAPI.getGlobalStock(params);
      if (response.data.success) {
        const data = JSON.parse(response.data.data);
        setInventory(data);
      }
    } catch (error) {
      console.error('Error fetching inventory:', error);
    } finally {
      setLoading(false);
    }
  };

  const filteredInventory = inventory.filter(item =>
    item.designation?.toLowerCase().includes(searchTerm.toLowerCase()) ||
    item.codeProduit?.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const getStockStatus = (quantity) => {
    if (quantity > 0) {
      return { color: 'text-green-600', bg: 'bg-green-100', label: 'In Stock' };
    } else if (quantity === 0) {
      return { color: 'text-yellow-600', bg: 'bg-yellow-100', label: 'Out of Stock' };
    } else {
      return { color: 'text-red-600', bg: 'bg-red-100', label: 'Negative Stock' };
    }
  };

  const handleFilterChange = (filter) => {
    setStockFilter(filter);
    setCurrentPage(1);
  };

  const handlePageChange = (page) => {
    setCurrentPage(page);
  };

  if (loading) {
    return (
      <div className="space-y-6">
        <div className="card p-6">
          <LoadingSpinner size="lg" className="h-64" />
        </div>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      {/* Header and Filters */}
      <div className="card p-6">
        <div className="flex flex-col lg:flex-row lg:items-center lg:justify-between space-y-4 lg:space-y-0">
          <h2 className="text-xl font-semibold text-gray-900">Inventory Management</h2>
          
          <div className="flex flex-col sm:flex-row space-y-2 sm:space-y-0 sm:space-x-4">
            {/* Search */}
            <div className="relative">
              <MagnifyingGlassIcon className="absolute left-3 top-1/2 transform -translate-y-1/2 w-5 h-5 text-gray-400" />
              <input
                type="text"
                placeholder="Search products..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="input-field pl-10 w-full sm:w-64"
              />
            </div>
            
            {/* Stock Filter */}
            <select
              value={stockFilter}
              onChange={(e) => handleFilterChange(Number(e.target.value))}
              className="input-field"
            >
              <option value={STOCK_FILTERS.ALL}>All Products</option>
              <option value={STOCK_FILTERS.NON_ZERO}>Non-Zero Stock</option>
              <option value={STOCK_FILTERS.POSITIVE}>Positive Stock</option>
              <option value={STOCK_FILTERS.ZERO}>Out of Stock</option>
              <option value={STOCK_FILTERS.NEGATIVE}>Negative Stock</option>
            </select>
          </div>
        </div>
      </div>

      {/* Stock Summary Cards */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-6">
        <div className="card p-6">
          <div className="flex items-center">
            <div className="w-12 h-12 bg-green-100 rounded-lg flex items-center justify-center">
              <CubeIcon className="w-6 h-6 text-green-600" />
            </div>
            <div className="ml-4">
              <p className="text-sm font-medium text-gray-600">In Stock</p>
              <p className="text-2xl font-bold text-green-600">
                {filteredInventory.filter(item => item.quantite > 0).length}
              </p>
            </div>
          </div>
        </div>
        
        <div className="card p-6">
          <div className="flex items-center">
            <div className="w-12 h-12 bg-yellow-100 rounded-lg flex items-center justify-center">
              <CubeIcon className="w-6 h-6 text-yellow-600" />
            </div>
            <div className="ml-4">
              <p className="text-sm font-medium text-gray-600">Out of Stock</p>
              <p className="text-2xl font-bold text-yellow-600">
                {filteredInventory.filter(item => item.quantite === 0).length}
              </p>
            </div>
          </div>
        </div>
        
        <div className="card p-6">
          <div className="flex items-center">
            <div className="w-12 h-12 bg-red-100 rounded-lg flex items-center justify-center">
              <CubeIcon className="w-6 h-6 text-red-600" />
            </div>
            <div className="ml-4">
              <p className="text-sm font-medium text-gray-600">Negative Stock</p>
              <p className="text-2xl font-bold text-red-600">
                {filteredInventory.filter(item => item.quantite < 0).length}
              </p>
            </div>
          </div>
        </div>
        
        <div className="card p-6">
          <div className="flex items-center">
            <div className="w-12 h-12 bg-blue-100 rounded-lg flex items-center justify-center">
              <CubeIcon className="w-6 h-6 text-blue-600" />
            </div>
            <div className="ml-4">
              <p className="text-sm font-medium text-gray-600">Total Products</p>
              <p className="text-2xl font-bold text-blue-600">
                {filteredInventory.length}
              </p>
            </div>
          </div>
        </div>
      </div>

      {/* Inventory Table */}
      <div className="card p-6">
        <div className="flex items-center justify-between mb-4">
          <h3 className="text-lg font-semibold text-gray-900">
            Inventory ({filteredInventory.length})
          </h3>
          
          {/* Pagination Info */}
          <div className="text-sm text-gray-500">
            Page {currentPage} • Items {(currentPage - 1) * itemsPerPage + 1}-{currentPage * itemsPerPage}
          </div>
        </div>
        
        <div className="overflow-x-auto">
          <table className="min-w-full divide-y divide-gray-200">
            <thead className="bg-gray-50">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Product
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Code
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Stock Quantity
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Status
                </th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {filteredInventory.map((item, index) => {
                const status = getStockStatus(item.quantite);
                return (
                  <tr key={`${item.codeProduit}-${index}`} className="hover:bg-gray-50">
                    <td className="px-6 py-4">
                      <div className="text-sm font-medium text-gray-900">
                        {item.designation}
                      </div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <span className="text-sm text-gray-500 font-mono">
                        {item.codeProduit}
                      </span>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <span className={`text-sm font-medium ${status.color}`}>
                        {item.quantite.toLocaleString()}
                      </span>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <span className={`inline-flex px-2 py-1 text-xs font-medium rounded-full ${status.bg} ${status.color}`}>
                        {status.label}
                      </span>
                    </td>
                  </tr>
                );
              })}
            </tbody>
          </table>
          
          {filteredInventory.length === 0 && (
            <div className="text-center py-12">
              <div className="text-gray-400 mb-2">
                <CubeIcon className="w-12 h-12 mx-auto" />
              </div>
              <p className="text-gray-500">No products found</p>
              <p className="text-sm text-gray-400">Try adjusting your search or filters</p>
            </div>
          )}
        </div>
        
        {/* Pagination */}
        <div className="flex items-center justify-between mt-6">
          <button
            onClick={() => handlePageChange(currentPage - 1)}
            disabled={currentPage === 1}
            className="btn-secondary disabled:opacity-50 disabled:cursor-not-allowed"
          >
            Previous
          </button>
          
          <div className="flex space-x-2">
            {[...Array(5)].map((_, i) => {
              const page = currentPage - 2 + i;
              if (page < 1) return null;
              
              return (
                <button
                  key={page}
                  onClick={() => handlePageChange(page)}
                  className={`px-3 py-2 text-sm font-medium rounded-lg ${
                    page === currentPage
                      ? 'bg-primary-600 text-white'
                      : 'text-gray-700 hover:bg-gray-100'
                  }`}
                >
                  {page}
                </button>
              );
            })}
          </div>
          
          <button
            onClick={() => handlePageChange(currentPage + 1)}
            className="btn-secondary"
          >
            Next
          </button>
        </div>
      </div>
    </div>
  );
};

export default Inventory;