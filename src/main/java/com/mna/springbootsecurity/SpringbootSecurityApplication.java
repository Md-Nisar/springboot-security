package com.mna.springbootsecurity;

import com.mna.springbootsecurity.datasource.config.MultiDataSourceConfig;
import com.mna.springbootsecurity.datasource.demo.DualDBDemo;
import com.mna.springbootsecurity.powerbi.demo.PowerBIDemo;
import com.mna.springbootsecurity.pubsub.event.application.listener.ApplicationEnvironmentPreparedEventListener;
import com.mna.springbootsecurity.pubsub.event.application.listener.ApplicationPreparedEventListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;

@SpringBootApplication
@Slf4j
public class SpringbootSecurityApplication implements CommandLineRunner {

    @Autowired(required = false)
    private PowerBIDemo powerBIDemo;

    @Autowired
    private MultiDataSourceConfig multiDataSourceConfig;

    @Autowired(required = false)
    private DualDBDemo dualDBDemo;


    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(SpringbootSecurityApplication.class);
        application.addListeners(new ApplicationPidFileWriter());
        application.addListeners(new ApplicationEnvironmentPreparedEventListener());
        application.addListeners(new ApplicationPreparedEventListener());
        application.run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        powerBIDemo.powerBI();
        if (multiDataSourceConfig.isEnabled()) {
            dualDBDemo.useHospitalDB();
            dualDBDemo.useSchoolDB();
        }

    }

}
