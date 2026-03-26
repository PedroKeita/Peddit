import { Routes, Route } from 'react-router-dom'
import LoginPage from './pages/LoginPage'
import RegisterPage from './pages/RegisterPage'

function Home() {
    return <h1 className="text-3xl font-bold p-8">Peddit </h1>
}

function NotFound() {
    return <h1 className="text-3xl font-bold p-8">404 — Página não encontrada</h1>
}

export default function App() {
    return (
        <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/login" element={<LoginPage />} />
            <Route path="/register" element={<RegisterPage />} />
            <Route path="*" element={<NotFound />} />
        </Routes>
    )
}