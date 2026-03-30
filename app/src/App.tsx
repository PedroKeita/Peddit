import { Routes, Route } from 'react-router-dom'
import LoginPage from './pages/LoginPage'
import RegisterPage from './pages/RegisterPage'
import FeedPage from './pages/FeedPage'
import PostDetailPage from './pages/PostDetailPage'


function NotFound() {
    return <h1 className="text-3xl font-bold p-8">404 — Página não encontrada</h1>
}

export default function App() {
    return (
        <Routes>
            <Route path="/" element={<FeedPage />} />
            <Route path="/login" element={<LoginPage />} />
            <Route path="/register" element={<RegisterPage />} />
            <Route path="/posts/:id" element={<PostDetailPage />} />
            <Route path="*" element={<NotFound />} />
        </Routes>
    )
}