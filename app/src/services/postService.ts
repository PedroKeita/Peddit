import api from '../api/axios'
import type { PageResponse, Post, PostDetail } from '../types/post'

export async function listPosts(
    page: number,
    q?: string,
    communityId?: number
): Promise<PageResponse<Post>> {
    const params: Record<string, any> = { page, size: 10, sort: 'date' }
    if (q) params.q = q
    if (communityId) params.communityId = communityId

    const response = await api.get<PageResponse<Post>>('/api/posts', { params })
    return response.data
}


export async function getPostById(id: number): Promise<PostDetail> {
    const response = await api.get<PostDetail>(`/api/posts/${id}`)
    return response.data
}

export async function votePost(id: number, value: 1 | -1): Promise<{ score: number; userVote: number | null }> {
    const response = await api.post(`/api/posts/${id}/vote`, { value })
    return response.data
}