const BASE_URL = 'http://localhost:8089/api';

export const roomApi = {
    getAllRooms: async () => {
        const res = await fetch(`${BASE_URL}/rooms`);
        if (!res.ok) throw new Error('Failed to get rooms');
        return res.json();
    },

    getRoomById: async (id) => {
        const res = await fetch(`${BASE_URL}/rooms/${id}`);
        if (!res.ok) throw new Error('Room not found');
        return res.json();
    },

    createRoom: async (room) => {
        const res = await fetch(`${BASE_URL}/rooms`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(room)
        });
        if (!res.ok) throw new Error('Failed to create room');
        return res.json();
    },

    updateRoom: async (id, room) => {
        const res = await fetch(`${BASE_URL}/rooms/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(room)
        });
        if (!res.ok) throw new Error('Failed to update room');
        return res.json();
    },

    deleteRoom: async (id) => {
        const res = await fetch(`${BASE_URL}/rooms/${id}`, { method: 'DELETE' });
        if (!res.ok) throw new Error('Failed to delete room');
    }
};

export const bookingApi = {
    getAllBookings: async () => {
        const res = await fetch(`${BASE_URL}/bookings`);
        if (!res.ok) throw new Error('Failed to get bookings');
        return res.json();
    },

    createBooking: async (booking) => {
        const res = await fetch(`${BASE_URL}/bookings`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(booking)
        });
        if (!res.ok) {
            const msg = await res.text();
            throw new Error(msg || 'Failed to create booking');
        }
        return res.json();
    },

    releaseBooking: async (id) => {
        const res = await fetch(`${BASE_URL}/bookings/${id}`, {
            method: 'PUT'
        });
        if (!res.ok) throw new Error('Failed to release booking');
        return res.json();
    },

    cancelBooking: async (id) => {
        const res = await fetch(`${BASE_URL}/bookings/${id}`, {
            method: 'DELETE'
        });
        if (!res.ok) throw new Error('Failed to cancel booking');
    }
};
