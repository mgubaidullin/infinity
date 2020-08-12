CREATE KEYSPACE IF NOT EXISTS infinity_ks WITH replication = {'class': 'SimpleStrategy', 'replication_factor' : 1};

CREATE TABLE IF NOT EXISTS infinity_ks.events ( key UUID, event_timestamp TIMESTAMP, type TEXT, value DECIMAL, PRIMARY KEY ((key), event_timestamp) );
