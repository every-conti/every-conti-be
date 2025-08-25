package com.everyconti.every_conti.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;


@Getter
@Setter
@ConfigurationProperties(prefix = "web.cors")
public class WebProperties {
    private List<String> origins;
}