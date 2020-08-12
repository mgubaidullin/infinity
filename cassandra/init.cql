CREATE KEYSPACE IF NOT EXISTS infinity_ks WITH replication = {'class': 'SimpleStrategy', 'replication_factor' : 1};

CREATE TABLE IF NOT EXISTS infinity_ks.events ( event_group TEXT, event_type TEXT, event_timestamp TIMESTAMP, value DECIMAL, PRIMARY KEY ((event_group, event_type), event_timestamp)) WITH CLUSTERING ORDER BY (event_timestamp DESC) ;

CREATE TABLE IF NOT EXISTS infinity_ks.aggregations ( event_group TEXT, event_type TEXT, agg_type TEXT, agg_period TEXT, agg_time TEXT, value DECIMAL, PRIMARY KEY ((event_group, event_type), agg_type, agg_period, agg_time)) WITH CLUSTERING ORDER BY (agg_type ASC, agg_period ASC, agg_time DESC) ;