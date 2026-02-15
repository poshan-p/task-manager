CREATE TABLE IF NOT EXISTS users
(
    id            bigint UNIQUE PRIMARY KEY,
    username      varchar(50) UNIQUE  NOT NULL,
    email         varchar(255) UNIQUE NOT NULL,
    password_hash varchar             NOT NULL,
    created_at    timestamptz DEFAULT now(),
    updated_at    timestamptz DEFAULT now()
);

CREATE UNIQUE INDEX IF NOT EXISTS index_users_id ON users (id);
CREATE INDEX IF NOT EXISTS ix_users_username ON users (username);
CREATE INDEX IF NOT EXISTS ix_users_email ON users (email);

CREATE TABLE IF NOT EXISTS tasks
(
    id          bigint UNIQUE PRIMARY KEY,
    user_id     bigint       NOT NULL,
    title       varchar(200) NOT NULL,
    description varchar,
    status      varchar(20) CHECK (status IN ('TODO', 'IN_PROGRESS', 'DONE')),
    priority    varchar(30) CHECK (priority IN ('LOW', 'MEDIUM', 'HIGH')),
    due_date    date,
    created_at  timestamptz DEFAULT now(),
    updated_at  timestamptz DEFAULT now(),
    CONSTRAINT fk_tasks_users_user_id_id FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE UNIQUE INDEX IF NOT EXISTS index_tasks_id ON tasks (id);
CREATE INDEX IF NOT EXISTS index_tasks_user_id ON tasks (user_id);
CREATE INDEX IF NOT EXISTS index_tasks_status ON tasks (status);
CREATE INDEX IF NOT EXISTS index_tasks_user_id_status ON tasks (user_id, status);