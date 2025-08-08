import React, { useState, useEffect } from 'react';
import { 
  CurrencyDollarIcon, 
  ShoppingCartIcon, 
  CubeIcon, 
  BuildingStorefrontIcon 
} from '@heroicons/react/24/outline';
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, BarChart, Bar } from 'recharts';
import StatCard from '../components/UI/StatCard';
import LoadingSpinner from '../components/UI/LoadingSpinner';
import { salesAPI, storesAPI } from '../services/api';
import { useAuth } from '../context/AuthContext';
import { format, subDays } from 'date-fns';

const Dashboard = () => {
  const { user, stores } = useAuth();
  const [loading, setLoading] = useState(true);
  const [salesData, setSalesData] = useState(null);
  const [storeData, setStoreData] = useState([]);
  const [chartData, setChartData] = useState([]);

  useEffect(() => {
    fetchDashboardData();
  }, []);

  const fetchDashboardData = async () => {
    try {
      setLoading(true);
      
      // Get date range (last 7 days)
      const endDate = new Date();
      const startDate = subDays(endDate, 7);
      
      const dateParams = {
        debut: format(startDate, 'dd-MM-yyyy HH:mm:ss'),
        fin: format(endDate, 'dd-MM-yyyy HH:mm:ss')
      };

      // Fetch sales data
      const salesResponse = await salesAPI.getSalesByDate(dateParams);
      if (salesResponse.data.success) {
        const data = JSON.parse(salesResponse.data.data);
        setSalesData(data);
        
        // Parse chart data
        if (data.data) {
          const parsedChartData = JSON.parse(data.data);
          setChartData(parsedChartData.map(item => ({
            date: format(new Date(item.jourVente), 'MMM dd'),
            sales: item.montantTTC || 0,
            quantity: item.quantite || 0,
            tickets: item.nombreTickets || 0
          })));
        }
      }

      // Fetch store data
      const storeResponse = await storesAPI.getStoresByDate({
        withDate: true,
        ...dateParams
      });
      if (storeResponse.data.success) {
        const storeInfo = JSON.parse(storeResponse.data.data);
        setStoreData(storeInfo);
      }

    } catch (error) {
      console.error('Error fetching dashboard data:', error);
    } finally {
      setLoading(false);
    }
  };

  const currentStore = stores.find(store => store.numMagasin === user?.numMagasin);
  const currentStoreData = storeData.find(store => store.numMagasin === user?.numMagasin);

  const totalSales = chartData.reduce((sum, item) => sum + item.sales, 0);
  const totalQuantity = chartData.reduce((sum, item) => sum + item.quantity, 0);
  const totalTickets = chartData.reduce((sum, item) => sum + item.tickets, 0);
  const averageTicket = totalTickets > 0 ? totalSales / totalTickets : 0;

  if (loading) {
    return (
      <div className="space-y-6">
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
          {[...Array(4)].map((_, i) => (
            <StatCard key={i} loading={true} />
          ))}
        </div>
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          <div className="card p-6">
            <LoadingSpinner size="lg" className="h-64" />
          </div>
          <div className="card p-6">
            <LoadingSpinner size="lg" className="h-64" />
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      {/* Stats Cards */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        <StatCard
          title="Total Sales"
          value={`$${totalSales.toLocaleString('en-US', { minimumFractionDigits: 2 })}`}
          change="+12.5%"
          changeType="positive"
          icon={CurrencyDollarIcon}
        />
        <StatCard
          title="Items Sold"
          value={totalQuantity.toLocaleString()}
          change="+8.2%"
          changeType="positive"
          icon={CubeIcon}
        />
        <StatCard
          title="Transactions"
          value={totalTickets.toLocaleString()}
          change="+15.3%"
          changeType="positive"
          icon={ShoppingCartIcon}
        />
        <StatCard
          title="Avg. Ticket"
          value={`$${averageTicket.toFixed(2)}`}
          change="-2.1%"
          changeType="negative"
          icon={BuildingStorefrontIcon}
        />
      </div>

      {/* Charts */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* Sales Trend */}
        <div className="card p-6">
          <h3 className="text-lg font-semibold text-gray-900 mb-4">Sales Trend (Last 7 Days)</h3>
          <ResponsiveContainer width="100%" height={300}>
            <LineChart data={chartData}>
              <CartesianGrid strokeDasharray="3 3" stroke="#f0f0f0" />
              <XAxis 
                dataKey="date" 
                stroke="#6b7280"
                fontSize={12}
              />
              <YAxis 
                stroke="#6b7280"
                fontSize={12}
                tickFormatter={(value) => `$${value.toLocaleString()}`}
              />
              <Tooltip 
                formatter={(value) => [`$${value.toLocaleString()}`, 'Sales']}
                labelStyle={{ color: '#374151' }}
                contentStyle={{ 
                  backgroundColor: 'white', 
                  border: '1px solid #e5e7eb',
                  borderRadius: '8px'
                }}
              />
              <Line 
                type="monotone" 
                dataKey="sales" 
                stroke="#0ea5e9" 
                strokeWidth={3}
                dot={{ fill: '#0ea5e9', strokeWidth: 2, r: 4 }}
                activeDot={{ r: 6, stroke: '#0ea5e9', strokeWidth: 2 }}
              />
            </LineChart>
          </ResponsiveContainer>
        </div>

        {/* Daily Transactions */}
        <div className="card p-6">
          <h3 className="text-lg font-semibold text-gray-900 mb-4">Daily Transactions</h3>
          <ResponsiveContainer width="100%" height={300}>
            <BarChart data={chartData}>
              <CartesianGrid strokeDasharray="3 3" stroke="#f0f0f0" />
              <XAxis 
                dataKey="date" 
                stroke="#6b7280"
                fontSize={12}
              />
              <YAxis 
                stroke="#6b7280"
                fontSize={12}
              />
              <Tooltip 
                formatter={(value) => [value, 'Transactions']}
                labelStyle={{ color: '#374151' }}
                contentStyle={{ 
                  backgroundColor: 'white', 
                  border: '1px solid #e5e7eb',
                  borderRadius: '8px'
                }}
              />
              <Bar 
                dataKey="tickets" 
                fill="#0ea5e9" 
                radius={[4, 4, 0, 0]}
              />
            </BarChart>
          </ResponsiveContainer>
        </div>
      </div>

      {/* Store Performance */}
      {currentStoreData && (
        <div className="card p-6">
          <h3 className="text-lg font-semibold text-gray-900 mb-4">Store Performance</h3>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
            <div className="text-center">
              <p className="text-2xl font-bold text-primary-600">
                ${currentStoreData.montantTTC?.toLocaleString('en-US', { minimumFractionDigits: 2 }) || '0.00'}
              </p>
              <p className="text-sm text-gray-600">Total Revenue</p>
            </div>
            <div className="text-center">
              <p className="text-2xl font-bold text-green-600">
                {currentStoreData.quantite?.toLocaleString() || '0'}
              </p>
              <p className="text-sm text-gray-600">Items Sold</p>
            </div>
            <div className="text-center">
              <p className="text-2xl font-bold text-blue-600">
                {currentStoreData.nombreTickets?.toLocaleString() || '0'}
              </p>
              <p className="text-sm text-gray-600">Transactions</p>
            </div>
          </div>
          
          {currentStoreData.tauxObjectif && (
            <div className="mt-4 pt-4 border-t border-gray-200">
              <div className="flex items-center justify-between">
                <span className="text-sm text-gray-600">Goal Achievement</span>
                <span className="text-sm font-medium text-gray-900">
                  {currentStoreData.tauxObjectif.toFixed(1)}%
                </span>
              </div>
              <div className="mt-2 w-full bg-gray-200 rounded-full h-2">
                <div 
                  className="bg-primary-600 h-2 rounded-full transition-all duration-300"
                  style={{ width: `${Math.min(currentStoreData.tauxObjectif, 100)}%` }}
                ></div>
              </div>
            </div>
          )}
        </div>
      )}
    </div>
  );
};

export default Dashboard;