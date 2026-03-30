import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import api from '../api/axios'
import type { UserResponse } from '../types/auth'
import type { Post, PageResponse } from '../types/post'
import PostCard from '../components/PostCard'
import { formatDate } from '../utils/formatDate'

export default function ProfilePage() {
    const navigate = useNavigate()
    const isLoggedIn = !!localStorage.getItem('token')

    const [user, setUser] = useState<UserResponse | null>(null)
    const [posts, setPosts] = useState<Post[]>([])
    const [loadingUser, setLoadingUser] = useState(true)
    const [loadingPosts, setLoadingPosts] = useState(true)

    useEffect(() => {
        if (!isLoggedIn) {
            navigate('/login')
            return
        }
        fetchUser()
    }, [])

    async function fetchUser() {
        try {
            const res = await api.get<UserResponse>('/api/users/me')
            setUser(res.data)
            fetchPosts(res.data.username)
        } catch {
            navigate('/login')
        } finally {
            setLoadingUser(false)
        }
    }

    async function fetchPosts(username: string) {
        try {
            const res = await api.get<PageResponse<Post>>('/api/posts', {
                params: { page: 0, size: 100, sort: 'date' },
            })
            const userPosts = res.data.content.filter(
                p => p.author.username === username
            )
            setPosts(userPosts)
        } catch {
        } finally {
            setLoadingPosts(false)
        }
    }

    function handleLogout() {
        localStorage.removeItem('token')
        localStorage.removeItem('username')
        localStorage.removeItem('role')
        navigate('/login')
    }

    if (loadingUser) {
        return (
            <div className="min-h-screen bg-gray-100 flex items-center justify-center">
                <p className="text-gray-400">Carregando perfil...</p>
            </div>
        )
    }

    if (!user) return null

    return (
        <div className="min-h-screen bg-gray-100">
            <div className="max-w-2xl mx-auto py-8 px-4">

                <button
                    onClick={() => navigate('/')}
                    className="text-sm text-blue-600 hover:underline mb-6 inline-block"
                >
                    ← Voltar ao feed
                </button>

                <div className="bg-white border border-gray-200 rounded-lg p-6 mb-6">
                    <div className="flex items-start justify-between">
                        <div className="flex items-center gap-4">
                            <div className="w-14 h-14 rounded-full bg-blue-600 flex items-center justify-center text-white text-xl font-bold select-none">
                                {user.username.charAt(0).toUpperCase()}
                            </div>
                            <div>
                                <h1 className="text-lg font-bold text-gray-900">
                                    u/{user.username}
                                </h1>
                                <p className="text-sm text-gray-500">{user.email}</p>
                                <div className="flex items-center gap-2 mt-1">
                                    <span className={`text-xs px-2 py-0.5 rounded-full font-medium ${
                                        user.role === 'ADMIN'
                                            ? 'bg-red-100 text-red-700'
                                            : 'bg-blue-100 text-blue-700'
                                    }`}>
                                        {user.role === 'ADMIN' ? '👑 Admin' : '👤 Usuário'}
                                    </span>
                                    <span className="text-xs text-gray-400">
                                        Membro desde {formatDate(user.createdAt)}
                                    </span>
                                </div>
                            </div>
                        </div>

                        <button
                            onClick={handleLogout}
                            className="text-sm text-gray-500 border border-gray-300 px-3 py-1.5 rounded hover:bg-gray-50 hover:text-red-600 transition"
                        >
                            Sair
                        </button>
                    </div>
                </div>

                <div>
                    <h2 className="text-sm font-semibold text-gray-700 mb-3">
                        {loadingPosts
                            ? 'Carregando posts...'
                            : `${posts.length} post${posts.length !== 1 ? 's' : ''} publicado${posts.length !== 1 ? 's' : ''}`
                        }
                    </h2>

                    {!loadingPosts && posts.length === 0 && (
                        <div className="bg-white border border-gray-200 rounded-lg p-8 text-center">
                            <p className="text-gray-400 text-sm">Você ainda não publicou nenhum post.</p>
                            <button
                                onClick={() => navigate('/posts/new')}
                                className="mt-3 text-sm text-blue-600 hover:underline"
                            >
                                Criar primeiro post →
                            </button>
                        </div>
                    )}

                    {loadingPosts && (
                        <p className="text-center text-gray-400 text-sm mt-6">Carregando...</p>
                    )}

                    <div className="flex flex-col gap-3">
                        {posts.map(post => (
                            <PostCard key={post.id} post={post} />
                        ))}
                    </div>
                </div>

            </div>
        </div>
    )
}