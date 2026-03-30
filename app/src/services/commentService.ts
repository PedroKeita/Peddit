import api from '../api/axios'

export async function createComment(data: {
    content: string
    postId: number
    parentId?: number
}): Promise<void> {
    await api.post('/api/comments', data)
}

export async function voteComment(id: number, value: 1 | -1): Promise<{ score: number; userVote: number | null }> {
    const response = await api.post(`/api/comments/${id}/vote`, { value })
    return response.data
}