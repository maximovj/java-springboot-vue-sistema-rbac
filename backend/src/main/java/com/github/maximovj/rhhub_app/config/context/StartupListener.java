package com.github.maximovj.rhhub_app.config.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.github.maximovj.rhhub_app.config.env.PrintMessages;

import java.net.InetAddress;
import java.util.Map;

@Component
public class StartupListener {

    public static Logger logger = LoggerFactory.getLogger(StartupListener.class);

    private final Environment env;

    public StartupListener(Environment env) {
        this.env = env;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        try {
            String port = env.getProperty("server.port", "8080");
            String contextPath = env.getProperty("server.servlet.context-path", "");
            String profilesActive = env.getProperty("spring.profiles.active", "");
            String host = InetAddress.getLocalHost().getHostAddress();

            PrintMessages.logger = logger;
            PrintMessages.show(profilesActive, Map.of(
                "Aplicación iniciada correctamente ? ", true,
                "URL", "http://%s:%s%s".formatted(host, port, contextPath)
            ));

        } catch (Exception e) {
            String profilesActive = env.getProperty("spring.profiles.active", "");

            PrintMessages.logger = logger;
            PrintMessages.show(profilesActive, Map.of(
                "Aplicación iniciada (no se pudo resolver el host) ", ""
            ));
        }
    }
}

