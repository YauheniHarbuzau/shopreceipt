package com.example.shopreceipt.repository.aop;

import org.aspectj.lang.annotation.Pointcut;

/**
 * Pointcuts for the {@link CardAspect}
 */
public class CardAspectPointcuts {

    @Pointcut("execution(* com.example.shopreceipt.repository.CardRepository.findById(..))")
    protected static void findByIdMethodPointcut() {
    }

    @Pointcut("execution(* com.example.shopreceipt.repository.CardRepository.save(..))")
    protected static void saveMethodPointcut() {
    }

    @Pointcut("execution(* com.example.shopreceipt.repository.CardRepository.deleteById(..))")
    protected static void deleteByIdMethodPointcut() {
    }
}
