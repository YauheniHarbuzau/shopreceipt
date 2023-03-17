package com.example.shopreceipt.repository.aop;

import org.aspectj.lang.annotation.Pointcut;

/**
 * Pointcuts for the {@link ProductAspect}
 */
public class ProductAspectPointcuts {

    @Pointcut("execution(* com.example.shopreceipt.repository.ProductRepository.findById(..))")
    protected static void findByIdMethodPointcut() {
    }

    @Pointcut("execution(* com.example.shopreceipt.repository.ProductRepository.save(..))")
    protected static void saveMethodPointcut() {
    }

    @Pointcut("execution(* com.example.shopreceipt.repository.ProductRepository.deleteById(..))")
    protected static void deleteByIdMethodPointcut() {
    }
}
