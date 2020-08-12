CREATE KEYSPACE IF NOT EXISTS infinity_ks WITH replication = {'class': 'SimpleStrategy', 'replication_factor' : 1};

CREATE TABLE IF NOT EXISTS infinity_ks.events ( event_group TEXT, event_type TEXT, event_timestamp TIMESTAMP, value DECIMAL, PRIMARY KEY ((event_group, event_type), event_timestamp) );
