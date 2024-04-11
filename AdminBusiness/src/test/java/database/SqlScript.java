package database;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author Shao Yibo
 * @description
 * @date 2024/4/11
 */
public class SqlScript {
    /**
     * Create a new SqlScriptResource.
     *
     */
    private static final String path = "../scripts/sql/Initialize.sql";
    public BufferedReader getReader() {
        try {
            return Files.newBufferedReader(Path.of(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
