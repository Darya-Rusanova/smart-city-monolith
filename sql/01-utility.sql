SET NAMES utf8mb4;

CREATE TABLE IF NOT EXISTS utility_issue (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    category VARCHAR(255),
    address VARCHAR(255),
    status VARCHAR(255),
    description TEXT NOT NULL,
    created_at DATETIME(6),
    updated_at DATETIME(6),
    INDEX idx_utility_issue_user (user_id),
    INDEX idx_utility_issue_status (status),
    INDEX idx_utility_issue_category (category)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO utility_issue (id, user_id, category, address, status, description, created_at, updated_at) VALUES
(1, 2, 'WATER', 'ул. Ленина, 15', 'NEW', 'Прорыв трубы во дворе', NOW(), NOW()),
(2, 2, 'ROAD', 'ул. Мира, 10', 'IN_PROGRESS', 'Выбоина на дороге', NOW(), NOW()),
(3, 3, 'ELECTRICITY', 'пр. Мира, 5', 'RESOLVED', 'Не работает уличный фонарь', NOW(), NOW());