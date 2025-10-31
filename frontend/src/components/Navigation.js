import React from 'react';
import { Link } from 'react-router-dom';
import './Navigation.css';

export default function Navigation() {
  return (
    <nav className="navbar">
      <div className="navbar-container">
        <Link to="/" className="navbar-brand">
          Todo App
        </Link>
        <ul className="navbar-menu">
          <li>
            <Link to="/" className="navbar-link">Home</Link>
          </li>
          <li>
            <Link to="/todos" className="navbar-link">Todos</Link>
          </li>
          <li>
            <Link to="/account" className="navbar-link">Account</Link>
          </li>
        </ul>
      </div>
    </nav>
  );
}