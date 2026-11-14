package com.example.portfolio.stats;

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

/** 서버가 켜져 있는 동안의 방문 수. 여러 요청이 동시에 와도 안전하도록 AtomicLong을 사용한다. */
@Component
public class VisitCounter {

    private final AtomicLong visits = new AtomicLong();

    public long increment() {
        return visits.incrementAndGet();
    }

    public long current() {
        return visits.get();
    }
}
