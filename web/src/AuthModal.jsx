import React from 'react';
import LoginForm from './LoginForm';
import RegisterForm from './RegisterForm';

const AuthModal = ({ mode, onSuccess , onSwitchMode }) => {
    return (
        <div className="modal-overlay">
            <div className="auth-modal">
                {mode === 'login' ? (
                    <LoginForm onSwitchToRegister={() => onSwitchMode('register')}
                               onSuccess={ onSuccess }
                    />
                ) : (
                    <RegisterForm onSwitchToLogin={() => onSwitchMode('login')}

                    onSuccess={ onSuccess }/>
                )}
            </div>
        </div>
    );
};

export default AuthModal;
