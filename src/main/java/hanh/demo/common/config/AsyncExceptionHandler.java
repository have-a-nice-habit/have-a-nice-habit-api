package hanh.demo.common.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
@RequiredArgsConstructor
public class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void handleUncaughtException(Throwable ex, Method method, Object... params) {
        logger.error("Uncaught asynchronous exception from: " + method.getDeclaringClass().getName() + "." + method.getName(), ex);
    }
}
