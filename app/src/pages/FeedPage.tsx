import { useEffect, useState } from 'react'
import { listPosts } from '../services/postService'
import type { Post } from '../types/post'
import PostCard from '../components/PostCard'

export default function FeedPage() {
    const [posts, setPosts] = useState<Post[]>([])
    const [page, setPage] = useState(0)
    const [isLast, setIsLast] = useState(false)
    const [loading, setLoading] = useState(false)
    const [search, setSearch] = useState('')
    const [searchInput, setSearchInput] = useState('')

    async function fetchPosts(pageNum: number, q: string, reset: boolean) {
        setLoading(true)
        try {
            const data = await listPosts(pageNum, q || undefined)
            setPosts(prev => reset ? data.content : [...prev, ...data.content])
            setIsLast(data.last)
            setPage(pageNum)
        } finally {
            setLoading(false)
        }
    }

    useEffect(() => {
        fetchPosts(0, search, true)
    }, [search])

    function handleSearch(e: React.FormEvent) {
        e.preventDefault()
        setSearch(searchInput)
    }

    function handleLoadMore() {
        fetchPosts(page + 1, search, false)
    }

    return (
        <div className="min-h-screen bg-gray-100">
            <div className="max-w-2xl mx-auto py-8 px-4">

                <h1 className="text-2xl font-bold mb-6 text-gray-900">Peddit</h1>

                {/* Busca */}
                <form onSubmit={handleSearch} className="flex gap-2 mb-6">
                    <input
                        type="text"
                        value={searchInput}
                        onChange={e => setSearchInput(e.target.value)}
                        placeholder="Buscar posts..."
                        className="flex-1 border border-gray-300 rounded px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                    <button
                        type="submit"
                        className="bg-blue-600 text-white px-4 py-2 rounded text-sm hover:bg-blue-700"
                    >
                        Buscar
                    </button>
                </form>

                {/* Lista de posts */}
                <div className="flex flex-col gap-3">
                    {posts.map(post => (
                        <PostCard key={post.id} post={post} />
                    ))}
                </div>

                {/* Estado vazio */}
                {!loading && posts.length === 0 && (
                    <p className="text-center text-gray-500 mt-12">Nenhum post encontrado.</p>
                )}

                {/* Carregar mais */}
                {!isLast && posts.length > 0 && (
                    <div className="flex justify-center mt-6">
                        <button
                            onClick={handleLoadMore}
                            disabled={loading}
                            className="bg-white border border-gray-300 text-gray-700 px-6 py-2 rounded text-sm hover:bg-gray-50 disabled:opacity-50"
                        >
                            {loading ? 'Carregando...' : 'Carregar mais'}
                        </button>
                    </div>
                )}

                {loading && posts.length === 0 && (
                    <p className="text-center text-gray-400 mt-12">Carregando...</p>
                )}

            </div>
        </div>
    )
}