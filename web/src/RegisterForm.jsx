import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import './RegisterForm.css';
import LoginForm from './LoginForm';

const RegisterForm = ({ onSubmit, onSuccess, onSwitchToLogin }) => {
    const [formData, setFormData] = useState({
        name: '',
        username: '',
        password: '',
        gender: '',
        weight: '',
        height: '',
        birthdateDay: '',
        birthdateMonth: '',
        birthdateYear: '',
    });

    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

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

        const birthDay = `${formData.birthdateYear}-${formData.birthdateMonth}-${formData.birthdateDay}`;

        const data = {
            name: formData.name,
            login: formData.username,
            password: formData.password,
            gender: formData.gender,
            weight: parseFloat(formData.weight),
            height: parseFloat(formData.height),
            birthDay,
        };

        try {
            const response = await fetch('http://85.236.187.180:8080/api/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(data),
            });

            const result = await response.json();

            if (response.ok) {
                alert('Регистрация прошла успешно!');
                if (onSubmit) onSubmit(data);
                if (onSuccess) onSuccess(); // Закрытие модалки
            } else {
                setError(result.message || 'Ошибка при регистрации');
            }
        } catch (err) {
            setError('Произошла ошибка при отправке данных.');
        } finally {
            setLoading(false);
        }
    };

    const generateOptions = (start, end) => {
        const options = [];
        for (let i = start; i <= end; i++) {
            options.push(i < 10 ? `0${i}` : `${i}`);
        }
        return options;
    };

    return (
        <form onSubmit={handleSubmit} className="register-form">
            <h2>Присоединяйтесь к TKO Fitness</h2>

            <input
                type="text"
                name="name"
                placeholder="Имя"
                value={formData.name}
                onChange={handleChange}
                required
            />
            <input
                type="text"
                name="username"
                placeholder="Логин"
                value={formData.username}
                onChange={handleChange}
                required
            />
            <input
                type="password"
                name="password"
                placeholder="Пароль"
                value={formData.password}
                onChange={handleChange}
                required
            />

            <div className="gender-weight-height">
                <select
                    name="gender"
                    value={formData.gender}
                    onChange={handleChange}
                    required
                >
                    <option value="">Пол</option>
                    <option value="MALE">Мужской</option>
                    <option value="FEMALE">Женский</option>
                </select>
                <input
                    type="number"
                    name="height"
                    placeholder="Рост"
                    value={formData.height}
                    onChange={handleChange}
                    required
                    min="0"
                />
                <input
                    type="number"
                    name="weight"
                    placeholder="Вес"
                    value={formData.weight}
                    onChange={handleChange}
                    required
                    min="0"
                />
            </div>

            <div className="databirth">Дата рождения</div>
            <div className="birthdate-selects">
                <select
                    name="birthdateDay"
                    value={formData.birthdateDay}
                    onChange={handleChange}
                    required
                >
                    <option value="">День</option>
                    {generateOptions(1, 31).map((day) => (
                        <option key={day} value={day}>{day}</option>
                    ))}
                </select>
                <select
                    name="birthdateMonth"
                    value={formData.birthdateMonth}
                    onChange={handleChange}
                    required
                >
                    <option value="">Месяц</option>
                    {generateOptions(1, 12).map((month) => (
                        <option key={month} value={month}>{month}</option>
                    ))}
                </select>
                <select
                    name="birthdateYear"
                    value={formData.birthdateYear}
                    onChange={handleChange}
                    required
                >
                    <option value="">Год</option>
                    {generateOptions(1950, new Date().getFullYear()).reverse().map((year) => (
                        <option key={year} value={year}>{year}</option>
                    ))}
                </select>
            </div>

            {error && <div className="error">{error}</div>}

            <button type="submit" disabled={loading}>
                {loading ? 'Загрузка...' : 'Зарегистрироваться'}
            </button>
            <div className="switch-auth">
                Есть учетная запись?{' '}
                <span className="login-link" onClick={onSwitchToLogin}>Войти</span>
            </div>
        </form>
    );
};

export default RegisterForm;
