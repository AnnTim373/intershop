package ru.practicum.intershop;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.TestPropertySource;

@TestConfiguration(proxyBeanMethods = false)
@TestPropertySource(locations = "classpath:application-test.yml")
public class TestContext {

}
