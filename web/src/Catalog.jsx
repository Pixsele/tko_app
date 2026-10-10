import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import axios from 'axios';
import { useAuth } from './contexts/AuthContext.jsx';
import './Catalog.css'; // Подключи свои стили

const Catalog = () => {
    const [workouts, setWorkouts] = useState([]);
    const [filters, setFilters] = useState({
        muscleGroups: [],
        difficulty: '',
        sort: 'name',
    });

    const { user, logout } = useAuth();

    useEffect(() => {
        const fetchWorkouts = async () => {
            try {
                const params = {
                    sort: filters.sort,
                    difficulty: filters.difficulty || undefined,
                    muscleGroups: filters.muscleGroups.length > 0 ? filters.muscleGroups : undefined,
                };

                const response = await axios.get('http://85.236.187.180:8080/api/workout/page', {
                    params,
                    headers: {
                        'Authorization': `Bearer ${user.token}`,  // Добавляем токен в заголовки
                    },
                    paramsSerializer: params => {
                        const query = new URLSearchParams();
                        if (params.sort) query.append('sort', params.sort);
                        if (params.difficulty) query.append('difficulty', params.difficulty);
                        if (params.muscleGroups) {
                            params.muscleGroups.forEach(group => query.append('muscleGroups', group));
                        }
                        return query.toString();
                    }
                });

                setWorkouts(response.data.content);
            } catch (error) {
                console.error('Ошибка при загрузке тренировок:', error);
            }
        };

        fetchWorkouts();
    }, [filters]);

    const toggleMuscleGroup = (group) => {
        setFilters(prev => ({
            ...prev,
            muscleGroups: prev.muscleGroups.includes(group)
                ? prev.muscleGroups.filter(g => g !== group)
                : [...prev.muscleGroups, group]
        }));
    };

    const handleDifficultyChange = (e) => {
        setFilters(prev => ({ ...prev, difficulty: e.target.value }));
    };

    const handleSortChange = (e) => {
        setFilters(prev => ({ ...prev, sort: e.target.value }));
    };

    return (
        <div className="page-container">
            {/* Header */}
            <div className="top-bar">
                <div className="logo-container">
                    <Link to="/" className="logo">TKO <span>Fitness</span></Link>
                </div>
                <div className="navigation-links">
                    <Link to="/catalog" className="nav-link-now">Тренировки</Link>
                    <Link to="/programm" className="nav-link">Программы</Link>
                    <Link to="/kcal" className="nav-link">Расчет ккал</Link>
                </div>
                <div className="user-info">
                    <img src="/assets/Group%202.svg" alt="User Icon" />
                    <Link to="/profile" className="user-name">{user?.name}</Link>
                    <button onClick={logout} className="auth-button">Выйти</button>
                </div>
            </div>

            {/* Content */}
            <div className="main-content">
                {/* Filters */}
                <div className="filters">
                    <button className="rectangle">
                        <div>
                            <h4>Сортировка</h4>
                            <label><input type="radio" value="name" checked={filters.sort === 'name'}
                                          onChange={handleSortChange}/> По названию</label>
                            <label><input type="radio" value="popular" checked={filters.sort === 'popular'}
                                          onChange={handleSortChange}/> Популярные</label>
                            <label><input type="radio" value="favorite" checked={filters.sort === 'favorite'}
                                          onChange={handleSortChange}/> Сначала любимое</label>
                        </div>
                        <div>
                            <h4>Группа мышц</h4>
                            {['Грудные', 'Спина', 'Ноги', 'Руки', 'Ягодицы', 'Кор'].map(group => (
                                <label key={group}>
                                    <input type="checkbox" checked={filters.muscleGroups.includes(group)}
                                           onChange={() => toggleMuscleGroup(group)}/>
                                    {` ${group}`}
                                </label>
                            ))}
                        </div>
                        <div>
                            <h4>Сложность</h4>
                            {['Легкая', 'Средняя', 'Сложная'].map(level => (
                                <label key={level}>
                                    <input
                                        type="radio"
                                        name="difficulty"
                                        value={level}
                                        checked={filters.difficulty === level}
                                        onChange={handleDifficultyChange}
                                    />
                                    {` ${level}`}
                                </label>
                            ))}
                        </div>
                    </button>
                </div>

                {/* Workouts */}
                <div className="workouts-container">
                    {workouts.map(workout => (
                        <div key={workout.id} className="workout-card">
                            <h5>{workout.name}</h5>
                            <div className="like-section">
                                {workout.liked ? <img src="/assets/liked.svg" alt="like" className="liked"/> :
                                    <img src="/assets/unliked.svg" alt="like" className="unliked"/>}
                                <span>{workout.likeCount} </span>
                            </div>
                        </div>
                    ))}
                </div>
            </div>
        </div>
    );
};

export default Catalog;
