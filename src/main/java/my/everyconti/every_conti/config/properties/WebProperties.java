package my.everyconti.every_conti.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;


@Getter
@Setter
@ConfigurationProperties(prefix = "web.cors.origin")
public class WebProperties {
    private List<String> corsOrigins;
}