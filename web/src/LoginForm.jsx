import React, { useState } from 'react';
import {useAuth} from "./contexts/AuthContext.jsx";
import './LoginForm.css';

const LoginForm = ({ onSwitchToRegister, onSuccess }) => {
    const [formData, setFormData] = useState({
        login: '',
        password: '',
    });

    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const { login: authLogin } = useAuth();

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData((prevData) => ({
            ...prevData,
            [name]: value,
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError(null);

        try {
            const response = await fetch('http://85.236.187.180:8080/api/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(formData),
            });

            const result = await response.json();

            if (response.ok) {
                const { token, userId, name } = result;
                authLogin({ token, userId, name });
                alert('Вход успешен!');
                onSuccess();
            } else {
                setError(result.message || 'Ошибка при входе');
            }
        } catch (err) {
            setError('Произошла ошибка при отправке данных.');
        } finally {
            setLoading(false);
        }
    };


    return (
        <form onSubmit={handleSubmit} className="login-form">
            <h2>Вход</h2>
            <div>
                <input
                    type="login"
                    id="login"
                    name="login"
                    placeholder="Логин"
                    value={formData.login}
                    onChange={handleChange}
                    required
                />
            </div>
            <div>
                <input
                    type="password"
                    id="password"
                    name="password"
                    placeholder="Пароль"
                    value={formData.password}
                    onChange={handleChange}
                    required
                />
            </div>
            {error && <div className="error">{error}</div>}
            <div>
                <button type="submit" disabled={loading}>
                    {loading ? 'Загрузка...' : 'Войти'}
                </button>
            </div>
            <div className="switch-auth">
                Нет аккаунта?{' '}
                <span
                    className="register-link"
                    onClick={() => {
                        console.log('Переключение на регистрацию');
                        onSwitchToRegister();  // Переключаем на регистрацию
                    }}
                    style={{ color: '#d6ffa4', cursor: 'pointer' }}
                >
                    Зарегистрироваться
                </span>
            </div>
        </form>
    );
};

export default LoginForm;
