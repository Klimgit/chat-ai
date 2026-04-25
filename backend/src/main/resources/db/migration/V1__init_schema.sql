CREATE EXTENSION IF NOT EXISTS vector;

-- 2. Таблица пользователей
CREATE TABLE users (
    id VARCHAR(255) PRIMARY KEY, -- Google ID или UUID
    email VARCHAR(255) UNIQUE NOT NULL,
    full_name VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3. Подключенные облачные диски
CREATE TABLE connected_drives (
    id SERIAL PRIMARY KEY,
    user_id VARCHAR(255) REFERENCES users(id) ON DELETE CASCADE,
    drive_type VARCHAR(50) NOT NULL, -- 'GOOGLE_DRIVE' или 'YANDEX_DISK'
    access_token TEXT NOT NULL,
    refresh_token TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 4. Документы
CREATE TABLE documents (
    id VARCHAR(255) PRIMARY KEY, -- ID файла из облака
    drive_id INT REFERENCES connected_drives(id) ON DELETE CASCADE,
    owner_id VARCHAR(255) REFERENCES users(id) ON DELETE CASCADE,
    title TEXT NOT NULL,
    status VARCHAR(50) NOT NULL, -- 'PENDING', 'INDEXED', 'FAILED'
    last_indexed_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 5. Чанки и вектора
CREATE TABLE document_chunks (
    id SERIAL PRIMARY KEY,
    document_id VARCHAR(255) REFERENCES documents(id) ON DELETE CASCADE,
    owner_id VARCHAR(255) REFERENCES users(id) ON DELETE CASCADE,
    text_content TEXT NOT NULL, -- Текст чанка
    embedding vector(1536), -- Вектор text-embedding-3-small (1536 размерность)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Индекс для быстрого векторного поиска
CREATE INDEX ON document_chunks USING hnsw (embedding vector_cosine_ops);
-- Индекс для изоляции данных
CREATE INDEX ON document_chunks (owner_id);