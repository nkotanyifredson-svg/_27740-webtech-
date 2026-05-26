import { useState } from 'react';
import RoomPage from './pages/RoomPage';
import BookingPage from './pages/BookingPage';
import BookingList from './pages/BookingList';
import './App.css';

function App() {
    const [tab, setTab] = useState('rooms');

    return (
        <div className="container">
            <h1>Student Study Room Booking System</h1>
            <p className="subtitle">AUCA - Book a study room easily</p>

            <div className="nav">
                <button className={tab === 'rooms' ? 'active' : ''} onClick={() => setTab('rooms')}>
                    Rooms
                </button>
                <button className={tab === 'book' ? 'active' : ''} onClick={() => setTab('book')}>
                    Book a Room
                </button>
                <button className={tab === 'bookings' ? 'active' : ''} onClick={() => setTab('bookings')}>
                    All Bookings
                </button>
            </div>

            {tab === 'rooms' && <RoomPage />}
            {tab === 'book' && <BookingPage />}
            {tab === 'bookings' && <BookingList />}
        </div>
    );
}

export default App;
