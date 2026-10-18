package com.learning.product_service.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Times every call into the controller, processor, use case and service layers.
 * <p>
 * Performance: {@code product.layer.duration} (timer), tagged with {@code layer}, {@code class},
 * {@code method} and {@code outcome} ({@code success} / {@code error}), plus {@code exception} on errors.
 * <p>
 * Business: {@code product.usecase.executions} (counter), one per use case execution, tagged with
 * {@code usecase} and {@code outcome} ({@code success}, {@code rejected} for expected business errors such as
 * not-found or duplicates, {@code error} for anything unexpected).
 */
@Aspect
@Component
public class LayerMetricsAspect {

    private static final String LAYER_TIMER = "product.layer.duration";
    private static final String USECASE_COUNTER = "product.usecase.executions";

    private final MeterRegistry registry;

    public LayerMetricsAspect(MeterRegistry registry) {
        this.registry = registry;
    }

    @Pointcut("within(com.learning.product_service.web.controller..*)")
    void controllerLayer() {
    }

    @Pointcut("within(com.learning.product_service.core.processor..*) && execution(* process(..))")
    void processorLayer() {
    }

    @Pointcut("within(com.learning.product_service.core.usecase..*) && execution(* execute(..))")
    void usecaseLayer() {
    }

    @Pointcut("within(com.learning.product_service.core.service..*)")
    void serviceLayer() {
    }

    @Around("controllerLayer()")
    public Object controller(ProceedingJoinPoint pjp) throws Throwable {
        return time("controller", pjp);
    }

    @Around("processorLayer()")
    public Object processor(ProceedingJoinPoint pjp) throws Throwable {
        return time("processor", pjp);
    }

    @Around("usecaseLayer()")
    public Object usecase(ProceedingJoinPoint pjp) throws Throwable {
        var sample = Timer.start(registry);
        String outcome = "success";
        Throwable failure = null;
        try {
            return pjp.proceed();
        } catch (Throwable t) {
            failure = t;
            outcome = isBusinessError(t) ? "rejected" : "error";
            throw t;
        } finally {
            record(sample, "usecase", pjp, failure);
            registry.counter(USECASE_COUNTER,
                    "usecase", pjp.getTarget().getClass().getSimpleName(),
                    "outcome", outcome).increment();
        }
    }

    @Around("serviceLayer()")
    public Object service(ProceedingJoinPoint pjp) throws Throwable {
        return time("service", pjp);
    }

    /** Domain exceptions (not found, duplicate, rule violation...) are expected outcomes, not faults. */
    private static boolean isBusinessError(Throwable t) {
        return t.getClass().getPackageName().equals("com.learning.product_service.exception");
    }

    private Object time(String layer, ProceedingJoinPoint pjp) throws Throwable {
        var sample = Timer.start(registry);
        Throwable failure = null;
        try {
            return pjp.proceed();
        } catch (Throwable t) {
            failure = t;
            throw t;
        } finally {
            record(sample, layer, pjp, failure);
        }
    }

    private void record(Timer.Sample sample, String layer, ProceedingJoinPoint pjp, Throwable failure) {
        sample.stop(Timer.builder(LAYER_TIMER)
                .description("Time spent in an application layer")
                .tag("layer", layer)
                .tag("class", pjp.getTarget().getClass().getSimpleName())
                .tag("method", pjp.getSignature().getName())
                .tag("outcome", failure == null ? "success" : "error")
                .tag("exception", failure == null ? "none" : failure.getClass().getSimpleName())
                .publishPercentileHistogram()
                .register(registry));
    }
}
