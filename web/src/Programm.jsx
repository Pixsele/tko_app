import React from 'react';
import {Link} from 'react-router-dom';
import './Programm.css';

const Programm = () => {
    return (
        <div className="home-container">
            {/* Логотип, который ведет на главную страницу */}
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
                <Link to="/programm" className="nav-link-now">
                    Программы
                </Link>
                <Link to="/kcal" className="nav-link">
                    Расчет ккал
                </Link>
            </div>
        </div>
    );
};

export default Programm;