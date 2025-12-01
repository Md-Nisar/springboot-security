package com.mna.springbootsecurity.base.property;

import com.mna.springbootsecurity.base.constant.Environment;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@ConfigurationProperties(prefix = "application")
@Getter
@Setter
public class ApplicationProperties {

    private String name;
    private String version;
    private String environment;

    private Server server;
    private Client client;

    public boolean isLocalEnvironment() {
        return Environment.LOCAL.equals(environment);
    }

    @Data
    public static class Server {
        private String host = "localhost";
        private int port = 8080;
        private String basePath = "";

        public String getUrl() {
            return String.format(Locale.ROOT, "http://%s:%d", host, port);
        }

        public String getBaseUrl() {
            return String.format(Locale.ROOT, "http://%s:%d%s", host, port, basePath != null ? basePath : "");
        }
    }

    @Data
    public static class Client {
        private String host = "localhost";
        private int port = 4200;
        private String basePath = "";

        public String getUrl() {
            return String.format(Locale.ROOT, "http://%s:%d", host, port);
        }

        public String getBaseUrl() {
            return String.format(Locale.ROOT, "http://%s:%d%s", host, port, basePath != null ? basePath : "");
        }
    }

    @Data
    public static class Services {
        private Mail mail = new Mail();
        private Scheduling scheduling = new Scheduling();

        @Data
        public static class Mail {
            private boolean enabled = false;
        }

        @Data
        public static class Scheduling {
            private boolean enabled = false;
        }
    }
}

