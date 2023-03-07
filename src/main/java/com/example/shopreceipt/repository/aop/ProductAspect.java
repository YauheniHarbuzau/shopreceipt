package com.example.shopreceipt.repository.aop;

import com.example.shopreceipt.entity.Product;
import com.example.shopreceipt.repository.ProductRepository;
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
 * AOP for the {@link ProductRepository}
 *
 * @see ProductAspectPointcuts
 */
@Component
@Aspect
public class ProductAspect {

    private final Factory<Long, Product> factory;
    private final Cache<Long, Product> cache;

    public ProductAspect(
            @Value("${cache.type}") String cacheType,
            @Value("${cache.capacity}") Integer cacheCapacity
    ) {
        this.factory = new CacheFactory<>();
        this.cache = factory.initCache(cacheType, cacheCapacity);
    }

    @Around("ProductAspectPointcuts.findByIdMethodPointcut()")
    public Optional<Product> aroundFindByIdMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        Long id = (Long) joinPoint.getArgs()[0];
        if (cache.get(id).isPresent()) {
            return cache.get(id);
        }
        Optional<Product> product = (Optional<Product>) joinPoint.proceed();
        product.ifPresent(c -> cache.put(c.getId(), c));
        addXml(product.orElseThrow(), "findById");
        return product;
    }

    @Around("ProductAspectPointcuts.saveMethodPointcut()")
    public Product aroundSaveMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        Product product = (Product) joinPoint.proceed();
        cache.put(product.getId(), product);
        return product;
    }

    @Around("ProductAspectPointcuts.deleteByIdMethodPointcut()")
    public Object aroundDeleteByIdMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        Long id = (Long) joinPoint.getArgs()[0];
        if (cache.get(id).isPresent()) {
            cache.remove(id);
        }
        return joinPoint.proceed();
    }
}