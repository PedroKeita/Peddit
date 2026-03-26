import { useNavigate } from 'react-router-dom'
import type { Post } from '../types/post'
import { formatDate } from '../utils/formatDate'

interface Props {
    post: Post
}

export default function PostCard({ post }: Props) {
    const navigate = useNavigate()

    return (
        <div
            onClick={() => navigate(`/posts/${post.id}`)}
            className="bg-white border border-gray-200 rounded-lg p-4 cursor-pointer hover:border-blue-400 transition"
        >
            <div className="flex items-center gap-2 text-xs text-gray-500 mb-2">
                <span className="font-medium text-blue-600">r/{post.community.name}</span>
                <span>•</span>
                <span>por {post.author.username}</span>
                <span>•</span>
                <span>{formatDate(post.createdAt)}</span>
            </div>

            <h2 className="text-base font-semibold text-gray-900 mb-1">{post.title}</h2>
            <p className="text-sm text-gray-600 line-clamp-2">{post.contentPreview}</p>

            <div className="flex items-center gap-4 mt-3 text-xs text-gray-500">
                <span>⬆ {post.score} pontos</span>
                <span>💬 {post.commentCount} comentários</span>
            </div>
        </div>
    )
}