SET NAMES utf8mb4;

CREATE TABLE IF NOT EXISTS notification_message (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    channel VARCHAR(255),
    title VARCHAR(150) NOT NULL,
    message TEXT NOT NULL,
    status VARCHAR(255),
    created_at DATETIME(6),
    INDEX idx_notification_message_user_id (user_id),
    INDEX idx_notification_message_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO notification_message (id, user_id, channel, title, message, status, created_at) VALUES
(1, 2, 'IN_APP', 'Заявка создана', 'Ваша заявка №1 была успешно создана и принята в обработку', 'SENT', NOW()),
(2, 2, 'IN_APP', 'Заявка создана', 'Ваша заявка №2 была успешно создана и принята в обработку', 'SENT', NOW()),
(3, 3, 'IN_APP', 'Заявка создана', 'Ваша заявка №3 была успешно создана и принята в обработку', 'SENT', NOW()),
(4, 2, 'IN_APP', 'Статус заявки был изменен', 'Статус вашей заявки №2 был изменен на В работе', 'SENT', NOW()),
(5, 3, 'IN_APP', 'Статус заявки был изменен', 'Статус вашей заявки №3 был изменен на Решена', 'SENT', NOW());