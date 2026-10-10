import React from 'react';
import {Link} from 'react-router-dom';
import './Kcal.css';

const Kcal = () => {
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


                <Link to="/programm" className="nav-link">
                    Программы
                </Link>


                <Link to="/kcal" className="nav-link">
                    Расчет ккал
                </Link>

            </div>
        </div>
    );
};

export default Kcal;