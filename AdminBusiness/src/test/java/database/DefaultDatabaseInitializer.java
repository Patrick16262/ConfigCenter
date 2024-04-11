package database;

import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author Shao Yibo
 * @description
 * @date 2024/4/11
 */
@Component
public class DefaultDatabaseInitializer {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private SqlScript script;

    @PostConstruct
    public void initializeDatabase() throws Exception {
        System.out.println("Initialize database");

        String sqls = script.getReader()
                .lines()
                .peek(str -> System.out.println("Sql ready to execute : " + str))
                .collect(Collectors.joining());
        Thread someThread = getAWeiredThread();
        jdbcTemplate.execute(sqls);
        someThread.interrupt();
        someThread.join();
        System.out.print("Successfully initialized database");
    }

    @NotNull
    private static Thread getAWeiredThread() {
        Thread someThread = new Thread(() -> {
            System.out.println("Initializing");
            int seconds = 0;
            while (true) {
                try {
                    System.out.print(".");
                    TimeUnit.MILLISECONDS.sleep(10);
                    seconds += 10;
                } catch (InterruptedException e) {
                    System.out.println("\ntime usage " + (double)seconds /1000 + "s");
                    return;
                }
            }
        });
        someThread.start();
        return someThread;
    }
}
