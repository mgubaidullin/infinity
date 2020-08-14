package one.entropy.infinity.processor;

import com.datastax.driver.core.Cluster;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@ApplicationScoped
public class CassandraConnect {

    @ConfigProperty(name = "cassandra", defaultValue = "localhost")
    private String cassandra;

    @Named("clusterRef")
    public Cluster cluster() {
        return Cluster.builder().addContactPoint(cassandra).build();
    }
}
