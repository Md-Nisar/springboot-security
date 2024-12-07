package com.mna.springbootsecurity.config.initializer;

import com.mna.springbootsecurity.base.enums.Roles;
import com.mna.springbootsecurity.domain.dao.RoleDao;
import com.mna.springbootsecurity.domain.entity.Role;
import com.mna.springbootsecurity.pubsub.message.redis.service.RedisSubscriberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class ApplicationStartupInitializer implements ApplicationRunner {

    private final ApplicationContext applicationContext;
    private final RedisSubscriberService redisSubscriberService;
    private final RoleDao roleDao;

    @Value("${application.startup-ops.enabled:false}")
    private boolean startupOpsEnabled;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (startupOpsEnabled) {
            listBeans();
            initializeRoles();
            registerRedisMessageListener();
        }

    }

    private void listBeans() {
        String[] beanNames = applicationContext.getBeanDefinitionNames();
        log.info("Beans provided by Spring Boot:: ");
        for (String beanName : beanNames) {
            System.out.println(beanName);
        }
    }

    private void initializeRoles() {

        class RoleCreator {
            @Transactional
            public void createIfNotFound(String roleName) {
                if (Boolean.FALSE.equals(roleDao.existsByName(roleName))) {
                    Role role = new Role();
                    role.setName(roleName);

                    roleDao.save(role);
                    log.info("Roles '{}' saved successfully to the DB", roleName);
                }
            }
        }

        RoleCreator roleCreator = new RoleCreator();

        roleCreator.createIfNotFound(Roles.USER.getValue());
        roleCreator.createIfNotFound(Roles.ADMIN.getValue());
    }

    public void registerRedisMessageListener() {
        if (redisSubscriberService != null) {
            log.info("Registering Redis Message Listener");
            // Register Redis Message Listener on application start up if needed
        } else {
            log.error("Redis is not configured");
        }
    }


}
