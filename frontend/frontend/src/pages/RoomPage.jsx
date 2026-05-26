import { useState, useEffect } from 'react';
import { roomApi } from '../services/api';

function RoomPage() {
    const [rooms, setRooms] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [showForm, setShowForm] = useState(false);
    const [editingRoom, setEditingRoom] = useState(null);
    const [name, setName] = useState('');
    const [capacity, setCapacity] = useState('');
    const [location, setLocation] = useState('');
    const [available, setAvailable] = useState(true);

    useEffect(() => { loadRooms(); }, []);

    const loadRooms = async () => {
        try {
            const data = await roomApi.getAllRooms();
            setRooms(data);
        } catch (err) {
            setError('Could not load rooms');
        } finally {
            setLoading(false);
        }
    };

    const openAddForm = () => {
        setEditingRoom(null);
        setName('');
        setCapacity('');
        setLocation('');
        setAvailable(true);
        setShowForm(true);
    };

    const openEditForm = (room) => {
        setEditingRoom(room);
        setName(room.name);
        setCapacity(room.capacity);
        setLocation(room.location);
        setAvailable(room.available);
        setShowForm(true);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        const roomData = { name, capacity: parseInt(capacity), location, available };
        try {
            if (editingRoom) {
                await roomApi.updateRoom(editingRoom.id, roomData);
            } else {
                await roomApi.createRoom(roomData);
            }
            setShowForm(false);
            loadRooms();
        } catch (err) {
            setError('Failed to save room');
        }
    };

    const handleDelete = async (id) => {
        if (!window.confirm('Delete this room?')) return;
        try {
            await roomApi.deleteRoom(id);
            loadRooms();
        } catch (err) {
            alert('Could not delete room');
        }
    };

    if (loading) return <p>Loading...</p>;

    return (
        <div>
            <div className="page-header">
                <h2>Rooms</h2>
                {!showForm && (
                    <button className="btn btn-green" onClick={openAddForm}>+ Add Room</button>
                )}
            </div>

            {error && <p className="error">{error}</p>}

            {showForm && (
                <div className="form-box">
                    <h3>{editingRoom ? 'Edit Room' : 'Add New Room'}</h3>
                    <form onSubmit={handleSubmit}>
                        <div className="form-group">
                            <label>Room Name</label>
                            <input
                                type="text"
                                value={name}
                                onChange={(e) => setName(e.target.value)}
                                placeholder="e.g. Room A"
                                required
                            />
                        </div>
                        <div className="form-group">
                            <label>Capacity</label>
                            <input
                                type="number"
                                value={capacity}
                                onChange={(e) => setCapacity(e.target.value)}
                                placeholder="e.g. 10"
                                min="1"
                                required
                            />
                        </div>
                        <div className="form-group">
                            <label>Location</label>
                            <input
                                type="text"
                                value={location}
                                onChange={(e) => setLocation(e.target.value)}
                                placeholder="e.g. Block B, Floor 2"
                                required
                            />
                        </div>
                        <div className="form-group">
                            <label>
                                <input
                                    type="checkbox"
                                    checked={available}
                                    onChange={(e) => setAvailable(e.target.checked)}
                                />
                                {' '}Available
                            </label>
                        </div>
                        <div className="actions">
                            <button type="submit" className="btn btn-blue">
                                {editingRoom ? 'Update' : 'Save'}
                            </button>
                            <button type="button" className="btn btn-gray" onClick={() => setShowForm(false)}>
                                Cancel
                            </button>
                        </div>
                    </form>
                </div>
            )}

            {rooms.length === 0 ? (
                <p>No rooms found.</p>
            ) : (
                <table>
                    <thead>
                        <tr>
                            <th>Name</th>
                            <th>Capacity</th>
                            <th>Location</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        {rooms.map((room) => (
                            <tr key={room.id}>
                                <td>{room.name}</td>
                                <td>{room.capacity}</td>
                                <td>{room.location}</td>
                                <td>
                                    <span className={room.available ? 'status-available' : 'status-booked'}>
                                        {room.available ? 'Available' : 'Booked'}
                                    </span>
                                </td>
                                <td>
                                    <div className="actions">
                                        <button className="btn btn-orange" onClick={() => openEditForm(room)}>
                                            Edit
                                        </button>
                                        <button className="btn btn-red" onClick={() => handleDelete(room.id)}>
                                            Delete
                                        </button>
                                    </div>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            )}
        </div>
    );
}

export default RoomPage;
