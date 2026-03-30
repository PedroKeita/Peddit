import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import api from '../api/axios'

interface Community {
    id: number
    name: string
    description: string
}

interface PageResponse<T> {
    content: T[]
}

export default function CreatePostPage() {
    const navigate = useNavigate()
    const isLoggedIn = !!localStorage.getItem('token')

    const [title, setTitle] = useState('')
    const [content, setContent] = useState('')
    const [communityId, setCommunityId] = useState<number | ''>('')
    const [communities, setCommunities] = useState<Community[]>([])
    const [loading, setLoading] = useState(false)
    const [errors, setErrors] = useState<Record<string, string>>({})

    useEffect(() => {
        if (!isLoggedIn) {
            navigate('/login')
            return
        }
        async function fetchCommunities() {
            try {
                const res = await api.get<PageResponse<Community>>('/api/communities', {
                    params: { page: 0, size: 100 },
                })
                setCommunities(res.data.content)
            } catch {}
        }
        fetchCommunities()
    }, [])

    async function handleSubmit(e: React.FormEvent) {
        e.preventDefault()
        setErrors({})

        const localErrors: Record<string, string> = {}
        if (!title.trim()) localErrors.title = 'Título é obrigatório'
        if (!content.trim()) localErrors.content = 'Conteúdo é obrigatório'
        if (!communityId) localErrors.communityId = 'Selecione uma comunidade'
        if (Object.keys(localErrors).length > 0) {
            setErrors(localErrors)
            return
        }

        setLoading(true)
        try {
            const res = await api.post('/api/posts', {
                title: title.trim(),
                content: content.trim(),
                communityId,
            })
            navigate(`/posts/${res.data.id}`)
        } catch (err: any) {
            const data = err.response?.data
            if (data?.errors) {
                setErrors(data.errors)
            } else if (data?.error) {
                setErrors({ form: data.error })
            } else {
                setErrors({ form: 'Erro ao criar post. Tente novamente.' })
            }
        } finally {
            setLoading(false)
        }
    }

    return (
        <div className="min-h-screen bg-gray-100">
            <div className="max-w-2xl mx-auto py-8 px-4">

                <button
                    onClick={() => navigate('/')}
                    className="text-sm text-blue-600 hover:underline mb-6 inline-block"
                >
                    ← Voltar ao feed
                </button>

                <div className="bg-white border border-gray-200 rounded-lg p-6">
                    <h1 className="text-xl font-bold text-gray-900 mb-6">Criar novo post</h1>

                    {errors.form && (
                        <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-2 rounded mb-4 text-sm">
                            {errors.form}
                        </div>
                    )}

                    <form onSubmit={handleSubmit} className="flex flex-col gap-5">

                        <div>
                            <label className="block text-sm font-medium text-gray-700 mb-1">
                                Comunidade
                            </label>
                            <select
                                value={communityId}
                                onChange={e => setCommunityId(Number(e.target.value))}
                                className={`w-full border rounded px-3 py-2 text-sm bg-white focus:outline-none focus:ring-2 focus:ring-blue-500 ${
                                    errors.communityId ? 'border-red-400' : 'border-gray-300'
                                }`}
                            >
                                <option value="">Selecione uma comunidade...</option>
                                {communities.map(c => (
                                    <option key={c.id} value={c.id}>
                                        r/{c.name}
                                    </option>
                                ))}
                            </select>
                            {errors.communityId && (
                                <p className="text-xs text-red-500 mt-1">{errors.communityId}</p>
                            )}
                        </div>

                        <div>
                            <label className="block text-sm font-medium text-gray-700 mb-1">
                                Título
                            </label>
                            <input
                                type="text"
                                value={title}
                                onChange={e => setTitle(e.target.value)}
                                maxLength={300}
                                placeholder="Dê um título ao seu post..."
                                className={`w-full border rounded px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 ${
                                    errors.title ? 'border-red-400' : 'border-gray-300'
                                }`}
                            />
                            <div className="flex justify-between mt-1">
                                {errors.title ? (
                                    <p className="text-xs text-red-500">{errors.title}</p>
                                ) : (
                                    <span />
                                )}
                                <span className="text-xs text-gray-400">{title.length}/300</span>
                            </div>
                        </div>

                        <div>
                            <label className="block text-sm font-medium text-gray-700 mb-1">
                                Conteúdo
                            </label>
                            <textarea
                                value={content}
                                onChange={e => setContent(e.target.value)}
                                placeholder="Escreva seu post aqui..."
                                rows={8}
                                className={`w-full border rounded px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 resize-none ${
                                    errors.content ? 'border-red-400' : 'border-gray-300'
                                }`}
                            />
                            {errors.content && (
                                <p className="text-xs text-red-500 mt-1">{errors.content}</p>
                            )}
                        </div>

                        <div className="flex justify-end gap-3 pt-2">
                            <button
                                type="button"
                                onClick={() => navigate('/')}
                                className="px-4 py-2 text-sm text-gray-700 border border-gray-300 rounded hover:bg-gray-50"
                            >
                                Cancelar
                            </button>
                            <button
                                type="submit"
                                disabled={loading}
                                className="px-5 py-2 text-sm bg-blue-600 text-white rounded font-medium hover:bg-blue-700 disabled:opacity-50"
                            >
                                {loading ? 'Publicando...' : 'Publicar post'}
                            </button>
                        </div>

                    </form>
                </div>
            </div>
        </div>
    )
}