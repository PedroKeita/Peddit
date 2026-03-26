export interface Author {
    id: number
    username: string
}

export interface Community {
    id: number
    name: string
}

export interface Post {
    id: number
    title: string
    contentPreview: string
    author: Author
    community: Community
    score: number
    commentCount: number
    createdAt: string
}

export interface PageResponse<T> {
    content: T[]
    totalPages: number
    totalElements: number
    number: number
    last: boolean
}