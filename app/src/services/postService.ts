import api from '../api/axios'
import type { PageResponse, Post } from '../types/post'

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