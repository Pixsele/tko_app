import React, { createContext, useContext, useState, useEffect } from 'react';

const AuthContext = createContext();

export const useAuth = () => useContext(AuthContext);

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(() => {
        let savedUser = null;
        try {
            const raw = localStorage.getItem('user');
            if (raw) savedUser = JSON.parse(raw);
        } catch (e) {
            console.error('Ошибка при чтении пользователя из localStorage:', e);
        }
        return savedUser;
    });

    const login = ({ token, userId, name }) => {
        const userData = { token, userId, name };
        setUser(userData);
        localStorage.setItem('user', JSON.stringify(userData));
    };

    const logout = () => {
        setUser(null);
        localStorage.removeItem('user');
    };

    return (
        <AuthContext.Provider value={{ user, login, logout, isAuthenticated: !!user }}>
            {children}
        </AuthContext.Provider>
    );
};
