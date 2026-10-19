package ru.isu.monolith.modules.notification.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import ru.isu.monolith.modules.notification.api.NotificationApi;
import ru.isu.monolith.modules.utility.event.IssueCreatedEvent;
import ru.isu.monolith.modules.utility.event.IssueStatusUpdatedEvent;

@Component
@Slf4j
public class IssueEventListener {
    private static final String CHANNEL = "IN_APP";

    @Autowired
    private NotificationApi notificationApi;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onIssueCreated(IssueCreatedEvent event) {
        try {
            notificationApi.send(event.ownerId(), CHANNEL,
                    "Заявка создана",
                    "Ваша заявка №" + event.issueId() + " была успешно создана и принята в обработку");
        } catch (Exception e) {
            log.error("Не удалось создать уведомление о заявке №{}: {}", event.issueId(), e.getMessage());
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onIssueStatusUpdated(IssueStatusUpdatedEvent event) {
        try {
            notificationApi.send(event.ownerId(), CHANNEL,
                    "Статус заявки был изменен",
                    "Статус вашей заявки №" + event.issueId() + " был изменен на " + event.newStatusName());
        } catch (Exception e) {
            log.error("Не удалось создать уведомление о смене статуса заявки №{}: {}", event.issueId(), e.getMessage());
        }
    }
}
