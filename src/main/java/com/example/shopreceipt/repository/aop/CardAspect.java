package com.example.shopreceipt.repository.aop;

import com.example.shopreceipt.entity.Card;
import com.example.shopreceipt.repository.CardRepository;
import com.example.shopreceipt.repository.cache.Cache;
import com.example.shopreceipt.repository.cache.factory.CacheFactory;
import com.example.shopreceipt.repository.cache.factory.Factory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.example.shopreceipt.util.XmlCreating.addXml;

/**
 * AOP for the {@link CardRepository}
 *
 * @see CardAspectPointcuts
 */
@Component
@Aspect
public class CardAspect {

    private final Factory<Long, Card> factory;
    private final Cache<Long, Card> cache;

    public CardAspect(
            @Value("${cache.type}") String cacheType,
            @Value("${cache.capacity}") Integer cacheCapacity
    ) {
        this.factory = new CacheFactory<>();
        this.cache = factory.initCache(cacheType, cacheCapacity);
    }

    @Around("CardAspectPointcuts.findByIdMethodPointcut()")
    public Optional<Card> aroundFindByIdMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        Long id = (Long) joinPoint.getArgs()[0];
        if (cache.get(id).isPresent()) {
            return cache.get(id);
        }
        Optional<Card> card = (Optional<Card>) joinPoint.proceed();
        card.ifPresent(c -> cache.put(c.getId(), c));
        addXml(card.orElseThrow(), "findById");
        return card;
    }

    @Around("CardAspectPointcuts.saveMethodPointcut()")
    public Card aroundSaveMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        Card card = (Card) joinPoint.proceed();
        cache.put(card.getId(), card);
        return card;
    }

    @Around("CardAspectPointcuts.deleteByIdMethodPointcut()")
    public Object aroundDeleteByIdMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        Long id = (Long) joinPoint.getArgs()[0];
        if (cache.get(id).isPresent()) {
            cache.remove(id);
        }
        return joinPoint.proceed();
    }
}