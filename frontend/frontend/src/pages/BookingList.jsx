import { useState, useEffect } from 'react';
import { bookingApi, roomApi } from '../services/api';

function BookingList() {
    const [bookings, setBookings] = useState([]);
    const [rooms, setRooms] = useState({});
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    useEffect(() => { loadData(); }, []);

    const loadData = async () => {
        try {
            const bookingData = await bookingApi.getAllBookings();
            const roomData = await roomApi.getAllRooms();
            const roomMap = {};
            roomData.forEach(r => { roomMap[r.id] = r; });
            setBookings(bookingData);
            setRooms(roomMap);
        } catch (err) {
            setError('Failed to load data');
        } finally {
            setLoading(false);
        }
    };

    const handleRelease = async (id) => {
        try {
            await bookingApi.releaseBooking(id);
            loadData();
        } catch (err) {
            alert('Could not release booking');
        }
    };

    const handleCancel = async (id) => {
        if (!window.confirm('Cancel this booking?')) return;
        try {
            await bookingApi.cancelBooking(id);
            loadData();
        } catch (err) {
            alert('Could not cancel booking');
        }
    };

    if (loading) return <p>Loading bookings...</p>;
    if (error) return <p className="error">{error}</p>;

    return (
        <div>
            <h2>All Bookings</h2>
            {bookings.length === 0 ? (
                <p>No bookings yet.</p>
            ) : (
                <table>
                    <thead>
                        <tr>
                            <th>Student Name</th>
                            <th>Student ID</th>
                            <th>Room</th>
                            <th>Date</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        {bookings.map((b) => (
                            <tr key={b.id}>
                                <td>{b.studentName}</td>
                                <td>{b.studentId}</td>
                                <td>{rooms[b.roomId] ? rooms[b.roomId].name : 'N/A'}</td>
                                <td>{b.bookingDate}</td>
                                <td>
                                    <span className={b.released ? 'status-released' : 'status-active'}>
                                        {b.released ? 'Released' : 'Active'}
                                    </span>
                                </td>
                                <td>
                                    {!b.released && (
                                        <div className="actions">
                                            <button className="btn btn-blue" onClick={() => handleRelease(b.id)}>
                                                Release
                                            </button>
                                            <button className="btn btn-red" onClick={() => handleCancel(b.id)}>
                                                Cancel
                                            </button>
                                        </div>
                                    )}
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            )}
        </div>
    );
}

export default BookingList;
