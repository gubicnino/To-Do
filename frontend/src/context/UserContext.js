import React, { createContext, useContext, useState } from 'react';
import { clearUserFromStorage, loadUserFromStorage, saveUserToStorage } from '../services/auth';

const UserContext = createContext(null);

export function UserContextProvider({ children }) {

    const [currentUser, setCurrentUser] = useState(() => 
        loadUserFromStorage()
    );


    const login = (user) => {
        setCurrentUser(user);
        saveUserToStorage(user);
    };

    const logout = () => {
        setCurrentUser(null);
        clearUserFromStorage();
    }
    const isLoggedIn = Boolean(currentUser);

    return (
        <UserContext.Provider value={{ currentUser, isLoggedIn, login, logout }}>
            {children}
        </UserContext.Provider>
    );
}
// Custom hook for easy access
export function useUser() {
  const context = useContext(UserContext);
  if (!context) {
    throw new Error('useUser must be used within UserContextProvider');
  }
  return context;
}