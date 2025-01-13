import React from 'react';
import { Routes, Route } from 'react-router-dom';
import Home from '../pages/Home';
import Search from '../pages/Search';
import Login from '../pages/Login';
import Signup from '../pages/Signup';
import SearchPlace from '../pages/SearchPlace';

function AppRoutes() {
  return (
    <Routes>
      <Route path="/" element={<Home />} />
      <Route path="/search" element={<Search />} />
      <Route path="/login" element={<Login />} />
      <Route path="/signup" element={<Signup />} />
      <Route path="/search-place" element={<SearchPlace />} />
    </Routes>
  );
}

export default AppRoutes;
