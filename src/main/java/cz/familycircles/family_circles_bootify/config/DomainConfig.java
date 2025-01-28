package cz.familycircles.family_circles_bootify.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EntityScan("cz.familycircles.family_circles_bootify.domain")
@EnableJpaRepositories("cz.familycircles.family_circles_bootify.repos")
@EnableTransactionManagement
public class DomainConfig {
}
