import React, {useEffect, useState} from 'react';
import {Link} from 'react-router-dom';
import axios from 'axios';
import { useAuth } from './contexts/AuthContext.jsx';
import './Profile.css';

const Profile = () => {
    const { user, logout } = useAuth();
    const [profileData, setProfileData] = useState(null);
    const [showEdit, setShowEdit] = useState(false);

    useEffect(() => {
        const fetchProfile = async () => {
            try {
                const response = await axios.get(`http://85.236.187.180:8080/api/user/${user.userId}`, {
                    headers: {
                        Authorization: `Bearer ${user.token}`
                    }
                });
                setProfileData(response.data);
            } catch (error) {
                console.error('Ошибка при загрузке профиля:', error);
            }
        };

        fetchProfile();
    }, [user]);

    const toggleEdit = () => setShowEdit(!showEdit);

    if (!profileData) return <div>Загрузка профиля...</div>;
    return (
        <div className="home-container">
            {/* Логотип, который ведет на главную страницу */}
            <div className="top-bar">
                <div className="logo-container">
                    <Link to="/" className="logo">
                        TKO Fitness {/* Логотип — это текст */}
                    </Link>
                </div>

                {/* Три кнопки, ведущие на другие страницы */}
                <div className="navigation-links">

                    <Link to="/catalog" className="nav-link">
                        Тренировки
                    </Link>


                    <Link to="/programm" className="nav-link">
                        Программы
                    </Link>


                    <Link to="/kcal" className="nav-link">
                        Расчет ккал
                    </Link>
                </div>
                <div className="user-info">
                    <img src="/assets/Group%203.svg" alt="User Icon"/>
                    <Link to="/profile" className="user-name-p">{user?.name}</Link>
                    <button onClick={logout} className="auth-button">Выйти</button>
                </div>
            </div>
            <button className="main">
                <div className="profile-content">
                    <img
                        src="/assets/image%202.svg" // можно заменить на profileData.avatar, если сервер отдает URL
                        alt="Avatar"
                        className="profile-avatar"
                    />
                    <div className="profile-info">
                        <p><strong>Имя:</strong> {profileData.name}</p>
                        <p><strong>Логин:</strong> {profileData.login}</p>
                        <p><strong>Рост:</strong> {profileData.height} см</p>
                        <p><strong>Вес:</strong> {profileData.weight} кг</p>
                        <p><strong>Дата рождения:</strong> {profileData.birthDay}</p>
                        <p><strong>Пол:</strong> {profileData.gender}</p>

                        <span onClick={toggleEdit} className="edit-link">Изменить данные</span>
                    </div>
                </div>
            </button>
        </div>
    );
};

export default Profile;