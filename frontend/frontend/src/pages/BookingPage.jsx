import { useState, useEffect } from 'react';
import { roomApi, bookingApi } from '../services/api';

function BookingPage() {
    const [studentName, setStudentName] = useState('');
    const [studentId, setStudentId] = useState('');
    const [roomId, setRoomId] = useState('');
    const [bookingDate, setBookingDate] = useState('');
    const [availableRooms, setAvailableRooms] = useState([]);
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');
    const [loading, setLoading] = useState(false);

    useEffect(() => { loadRooms(); }, []);

    const loadRooms = async () => {
        try {
            const all = await roomApi.getAllRooms();
            setAvailableRooms(all.filter(r => r.available === true));
        } catch (err) {
            setError('Failed to load rooms');
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError('');
        setSuccess('');
        const booking = { studentName, studentId, roomId, bookingDate };
        try {
            await bookingApi.createBooking(booking);
            setSuccess('Booking created successfully!');
            setStudentName('');
            setStudentId('');
            setRoomId('');
            setBookingDate('');
            loadRooms();
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div>
            <h2>Book a Study Room</h2>

            {error && <p className="error">{error}</p>}
            {success && <p className="success">{success}</p>}

 <div className="form-box">
 <form onSubmit={handleSubmit}>
    <div className="form-group">
    <label>Student Name</label>
    <input
    type="text"
     value={studentName}
     onChange={(e) => setStudentName(e.target.value)}
     placeholder="Your full name"
    required
                        />
     </div>
 <div className="form-group">
 <label>Student ID</label>
<input
  type="text"
 value={studentId}
onChange={(e) => setStudentId(e.target.value)}
placeholder="Your student ID"
required
  />
</div>
<div className="form-group">
 <label>Select Room</label>
    <select value={roomId} onChange={(e) => setRoomId(e.target.value)} required>
      <option value="">-- choose a room --</option>
     {availableRooms.map((room) => (
        <option key={room.id} value={room.id}>
         {room.name} - {room.location} (cap: {room.capacity})
   </option>
             ))}
 </select>
 {availableRooms.length === 0 && <small style={{color: 'orange'}}>No rooms available right now</small>}
 </div>
<div className="form-group">
 <label>Booking Date</label>
     <input
    type="date"
    value={bookingDate}
    onChange={(e) => setBookingDate(e.target.value)}
    required
  />
 </div>
     <button
        type="submit"
  className="btn btn-blue"
    disabled={loading || availableRooms.length === 0}
    >
    {loading ? 'Booking...' : 'Book Room'}
      </button>
                </form>
            </div>
        </div>
    );
}

export default BookingPage;
