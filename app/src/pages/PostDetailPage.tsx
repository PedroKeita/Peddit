import { useEffect, useState } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { getPostById, votePost } from '../services/postService'
import { createComment } from '../services/commentService'
import type { PostDetail } from '../types/post'
import CommentTree from '../components/CommentTree'
import { formatDate } from '../utils/formatDate'

export default function PostDetailPage() {
    const { id } = useParams<{ id: string }>()
    const navigate = useNavigate()
    const [post, setPost] = useState<PostDetail | null>(null)
    const [score, setScore] = useState(0)
    const [commentText, setCommentText] = useState('')
    const [loading, setLoading] = useState(true)
    const isLoggedIn = !!localStorage.getItem('token')

    useEffect(() => {
        async function load() {
            try {
                const data = await getPostById(Number(id))
                setPost(data)
                setScore(data.score)
            } finally {
                setLoading(false)
            }
        }
        load()
    }, [id])

    async function handleVote(value: 1 | -1) {
        if (!isLoggedIn) return
        try {
            const data = await votePost(Number(id), value)
            setScore(data.score)
        } catch {}
    }

    async function handleComment() {
        if (!commentText.trim() || !post) return
        try {
            await createComment({ content: commentText, postId: post.id })
            setCommentText('')
            const updated = await getPostById(post.id)
            setPost(updated)
        } catch {}
    }

    if (loading) return <p className="text-center mt-12 text-gray-400">Carregando...</p>
    if (!post) return <p className="text-center mt-12 text-gray-500">Post não encontrado.</p>

    return (
        <div className="min-h-screen bg-gray-100">
            <div className="max-w-2xl mx-auto py-8 px-4">

                <button
                    onClick={() => navigate('/')}
                    className="text-sm text-blue-600 hover:underline mb-4 inline-block"
                >
                    ← Voltar ao feed
                </button>

                {/* Post */}
                <div className="bg-white border border-gray-200 rounded-lg p-6 mb-6">
                    <div className="flex items-center gap-2 text-xs text-gray-500 mb-3">
                        <span className="font-medium text-blue-600">r/{post.community.name}</span>
                        <span>•</span>
                        <span>por {post.author.username}</span>
                        <span>•</span>
                        <span>{formatDate(post.createdAt)}</span>
                    </div>

                    <h1 className="text-xl font-bold text-gray-900 mb-3">{post.title}</h1>
                    <p className="text-sm text-gray-700 whitespace-pre-wrap">{post.content}</p>

                    <div className="flex items-center gap-3 mt-4 text-sm text-gray-500">
                        <button onClick={() => handleVote(1)} className="hover:text-green-600 text-lg">⬆</button>
                        <span className="font-medium">{score}</span>
                        <button onClick={() => handleVote(-1)} className="hover:text-red-500 text-lg">⬇</button>
                    </div>
                </div>

                {/* Formulário de comentário */}
                {isLoggedIn ? (
                    <div className="bg-white border border-gray-200 rounded-lg p-4 mb-6">
                        <p className="text-sm font-medium text-gray-700 mb-2">Deixe um comentário</p>
                        <textarea
                            value={commentText}
                            onChange={e => setCommentText(e.target.value)}
                            placeholder="Escreva seu comentário..."
                            rows={3}
                            className="w-full border border-gray-300 rounded px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 resize-none"
                        />
                        <div className="flex justify-end mt-2">
                            <button
                                onClick={handleComment}
                                className="bg-blue-600 text-white px-4 py-2 rounded text-sm hover:bg-blue-700"
                            >
                                Comentar
                            </button>
                        </div>
                    </div>
                ) : (
                    <div className="bg-white border border-gray-200 rounded-lg p-4 mb-6 text-center">
                        <p className="text-sm text-gray-500">
                            <button onClick={() => navigate('/login')} className="text-blue-600 hover:underline">
                                Faça login
                            </button>{' '}
                            para comentar.
                        </p>
                    </div>
                )}

                {/* Comentários */}
                <div>
                    <h2 className="text-sm font-semibold text-gray-700 mb-3">
                        {post.comments.length} comentário{post.comments.length !== 1 ? 's' : ''}
                    </h2>
                    {post.comments.length === 0 && (
                        <p className="text-sm text-gray-400">Nenhum comentário ainda. Seja o primeiro!</p>
                    )}
                    {post.comments.map(comment => (
                        <CommentTree key={comment.id} comment={comment} postId={post.id} />
                    ))}
                </div>

            </div>
        </div>
    )
}