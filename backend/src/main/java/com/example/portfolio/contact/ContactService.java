package com.example.portfolio.contact;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContactService {

    private static final Logger log = LoggerFactory.getLogger(ContactService.class);

    private final ContactMessageRepository repository;

    public ContactService(ContactMessageRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void receive(ContactRequest request) {
        if (request.isLikelySpam()) {
            // 봇에게는 성공처럼 보이게 응답하고 저장하지 않는다.
            log.info("Honeypot triggered, message dropped");
            return;
        }
        ContactMessage saved = repository.save(new ContactMessage(
                request.name().strip(), request.email().strip(), request.message().strip()));
        log.info("New contact message #{} from {}", saved.getId(), saved.getName());
    }

    @Transactional(readOnly = true)
    public long count() {
        return repository.count();
    }
}
