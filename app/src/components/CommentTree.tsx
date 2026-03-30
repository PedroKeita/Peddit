import { useState } from 'react'
import type { Comment } from '../types/post'
import { voteComment, createComment } from '../services/commentService'
import { formatDate } from '../utils/formatDate'

interface Props {
    comment: Comment
    postId: number
    depth?: number
}

export default function CommentTree({ comment, postId, depth = 0 }: Props) {
    const [score, setScore] = useState(comment.score)
    const [replying, setReplying] = useState(false)
    const [replyText, setReplyText] = useState('')
    const [replies, setReplies] = useState<Comment[]>(comment.replies)
    const isLoggedIn = !!localStorage.getItem('token')

    async function handleVote(value: 1 | -1) {
        if (!isLoggedIn) return
        try {
            const data = await voteComment(comment.id, value)
            setScore(data.score)
        } catch {}
    }

    async function handleReply() {
        if (!replyText.trim()) return
        try {
            await createComment({ content: replyText, postId, parentId: comment.id })
            setReplyText('')
            setReplying(false)
            setReplies(prev => [...prev, {
                id: Date.now(),
                content: replyText,
                author: { id: 0, username: localStorage.getItem('username') ?? 'você' },
                score: 0,
                replies: [],
                createdAt: new Date().toISOString(),
            }])
        } catch {}
    }

    return (
        <div className={`${depth > 0 ? 'ml-6 border-l-2 border-gray-100 pl-4' : ''} mt-3`}>
            <div className="bg-white rounded p-3 border border-gray-200">
                <div className="flex items-center gap-2 text-xs text-gray-500 mb-1">
                    <span className="font-medium text-gray-800">{comment.author.username}</span>
                    <span>•</span>
                    <span>{formatDate(comment.createdAt)}</span>
                </div>

                <p className="text-sm text-gray-800">{comment.content}</p>

                <div className="flex items-center gap-3 mt-2 text-xs text-gray-500">
                    <button onClick={() => handleVote(1)} className="hover:text-green-600">⬆</button>
                    <span>{score}</span>
                    <button onClick={() => handleVote(-1)} className="hover:text-red-500">⬇</button>
                    {isLoggedIn && (
                        <button
                            onClick={() => setReplying(!replying)}
                            className="hover:text-blue-600 ml-2"
                        >
                            Responder
                        </button>
                    )}
                </div>

                {replying && (
                    <div className="mt-2 flex gap-2">
                        <input
                            value={replyText}
                            onChange={e => setReplyText(e.target.value)}
                            placeholder="Sua resposta..."
                            className="flex-1 border border-gray-300 rounded px-2 py-1 text-sm focus:outline-none focus:ring-1 focus:ring-blue-500"
                        />
                        <button
                            onClick={handleReply}
                            className="bg-blue-600 text-white px-3 py-1 rounded text-sm hover:bg-blue-700"
                        >
                            Enviar
                        </button>
                    </div>
                )}
            </div>

            {replies.map(reply => (
                <CommentTree key={reply.id} comment={reply} postId={postId} depth={depth + 1} />
            ))}
        </div>
    )
}