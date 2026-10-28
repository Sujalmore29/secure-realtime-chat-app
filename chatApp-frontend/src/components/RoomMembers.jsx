import React, { use, useEffect, useState } from 'react'
import { addRoomMember, getRoomMembers, removeRoomMember } from '../services/RoomService';

const RoomMembers = ({roomId, currentUser, ownerUsername}) => {

useEffect(() => {
    loadMembers();
}, [roomId]);
    const [members, setMembers] = useState([]);
    const [newUsername, setNewUsername] = useState("");
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");
    const [notice, setNotice] = useState("");

    const isOwner = currentUser === ownerUsername;

    const handleAddMember = async(e) => {
        e.preventDefault();

        const username = newUsername.trim();
        if(!username) return;

        try{
            setLoading(true);
            setError("");
            setNotice("");

            await addRoomMember(roomId, username);
            setNewUsername("");
            setNotice(`${username} added to the room`);
            loadMembers();
        } catch(err) {
            setError(err.response?.data?.message || "Failed to add member");
            console.log("Error adding member", err);
        } finally {
            setLoading(false);
        }
    }


    const loadMembers = async () => {
            try {
                const membersList = await getRoomMembers(roomId);
                setMembers(membersList);
            } catch (err) {
                setError(err.response?.data?.message || "Failed to load members");
                console.log("Error loading members", err);
            }
        };

    const handleRemoveMember = async(username) => {
        if (!window.confirm(`Remove ${username} from this room?`)
        ) {
            return;
        }

    try {
      setError("");
      setNotice("");

      await removeRoomMember(roomId, username);
      setNotice(`${username} removed from the room`);
      await loadMembers();
    } catch (err) {
      setError(
        err.response?.data?.message ||
          "Unable to remove member"
      );
    }
  }
  return (
    <section className='rounded-xl bg-white p-4 shadow dark:bg-gray-800'>
        <h2 className='mb-3 text-lg font-semibold'>
            RoomMembers
        </h2>

        <p className="mb-3 text-sm text-gray-500">
            Owner: {ownerUsername}
        </p>

        {error && (
            <p role="alert" className="mb-2 text-sm text-red-500">
                {error}
            </p>
        )}

        {notice && (
            <p role="status" className="mb-2 text-sm text-green-600">
                {notice}
            </p>
        )}

        <ul className='mb-4 space-y-2'>
            {members.map((username) => (
                <li key={username} className='flex items-center justify-between gap-2'>
                    <span>
                        {username}
                        {username === ownerUsername && " (Owner) "}
                    </span>
                    {isOwner && username !== ownerUsername && (
                        <button
                            type="button"
                            onClick={() => handleRemoveMember(username)}
                            className="rounded bg-red-500 px-3 py-1 text-sm text-white hover:bg-red-600">
                                Remove
                        </button>
                )}
                </li>
            ))}
        </ul>

        {isOwner && (
            <form onSubmit={handleAddMember}
            className='flex flex-wrap gap-2'>
                <input 
                  value={newUsername}
                  onChange={(e) => setNewUsername(e.target.value)}
                  placeholder='Registered username'
                  aria-label='Username to add'
                  className='min-w-0 flex-1 rounded border px-3 py-2 dark:border-gray-600 dark:bg-gray-700' />

                  <button
                    type='submit'
                    disabled={loading || !newUsername.trim()}
                    className='rounded bg-blue-600 px-4 py-2 text-white disabled:opacity-50 hover:bg-blue-700'>
                        {loading ? "Adding..." : "Add Member"}
                  </button>
            </form>
        )}
    </section>
  );
}
export default RoomMembers