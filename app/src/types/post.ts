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

export interface Comment {
    id: number
    content: string
    author: Author
    score: number
    replies: Comment[]
    createdAt: string
}

export interface PostDetail {
    id: number
    title: string
    content: string
    author: Author
    community: Community
    score: number
    comments: Comment[]
    createdAt: string
}