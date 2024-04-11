package database;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.io.IOException;

/**
 * @author Shao Yibo
 * @description
 * @date 2024/4/11
 */
@EnableAutoConfiguration
@Configuration
@ComponentScan
public class DataBaseTestAutoConfiguration {
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource source) {
        return new JdbcTemplate(source);
    }
    @Bean public SqlScript sqlScript() throws IOException {
        return new SqlScript();
    }
}
