package ru.gloomyana.config;

import org.aeonbits.owner.Config;

@Config.Sources({"classpath:base-url.properties"})

public interface ApiConfig extends Config {
    @Key("baseUrl")
    String baseUrl();
}
