package persistence;

import database.DatabaseServer;
import database.H2;
import jdbc.JdbcTemplate;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import persistence.bootstrap.ComponentScanner;
import persistence.bootstrap.InFlightMetadataCollector;
import persistence.bootstrap.MetaModel;
import persistence.bootstrap.MetaModelImpl;
import persistence.entity.H2GeneratedIdObtainStrategy;

public class H2DBTestSupport {
    private static InFlightMetadataCollector inFlightMetadataCollector;
    protected static MetaModel metaModel;
    protected static JdbcTemplate jdbcTemplate;
    private static DatabaseServer server;
    @BeforeAll
    public static void tearUp() throws Exception {
        server = new H2();
        server.start();
        jdbcTemplate = new JdbcTemplate(server.getConnection());
    }

    @AfterAll
    public static void tearDown() {
        server.stop();
    }
}
