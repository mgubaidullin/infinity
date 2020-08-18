package one.entropy.infinity.aggregator;

import org.apache.spark.sql.*;
import org.apache.spark.sql.types.DataTypes;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Aggregator {
    private static final Logger LOGGER = Logger.getLogger(Aggregator.class.getName());


    public static Dataset<Row> aggregate(Dataset<Row> dataset, ChronoUnit horizon) {
        LOGGER.log(Level.INFO, "Aggregate events for {0}", horizon);

        Column[] groupBy = createGroupBy(horizon);
        Map<String, Column> withColumns = createWithColumn(horizon);

        Dataset<Row> agg = dataset.groupBy(groupBy).agg(
                functions.round(functions.avg("value"), 4).alias("avg_value"),
                functions.round(functions.min("value"), 4).alias("min_value"),
                functions.round(functions.max("value"), 4).alias("max_value"),
                functions.round(functions.mean("value"), 4).alias("mean_value"),
                functions.round(functions.count("value"), 4).alias("count_value"),
                functions.round(functions.sum("value"), 4).alias("sum_value"));

        for (Map.Entry<String, Column> entry : withColumns.entrySet()) {
            agg = agg.withColumn(entry.getKey(), entry.getValue());
        }
        agg.show();
        return agg;
    }

    private static Map<String, Column> createWithColumn(ChronoUnit horizon) {
        Map<String, Column> withColumn = new HashMap<>();
        withColumn.put("horizon", functions.lit(horizon.name()));

        if (horizon.equals(ChronoUnit.YEARS)) {
            withColumn.put("period", new Column("event_year").cast(DataTypes.StringType));
        } else if (horizon.equals(ChronoUnit.MONTHS)) {
            withColumn.put("period", functions.concat_ws("-",
                    new Column("event_year").cast(DataTypes.StringType),
                    functions.lpad(new Column("event_month").cast(DataTypes.StringType), 2, "0")
            ));
        } else if (horizon.equals(ChronoUnit.DAYS)) {
            withColumn.put("period", functions.concat_ws("-",
                    new Column("event_year").cast(DataTypes.StringType),
                    functions.lpad(new Column("event_month").cast(DataTypes.StringType), 2, "0"),
                    functions.lpad(new Column("event_day").cast(DataTypes.StringType), 2, "0")
            ));
        } else if (horizon.equals(ChronoUnit.HOURS)) {
            withColumn.put("period", functions.concat_ws("-",
                    new Column("event_year").cast(DataTypes.StringType),
                    functions.lpad(new Column("event_month").cast(DataTypes.StringType), 2, "0"),
                    functions.lpad(new Column("event_day").cast(DataTypes.StringType), 2, "0"),
                    functions.lpad(new Column("event_hour").cast(DataTypes.StringType), 2, "0")
            ));
        } else if (horizon.equals(ChronoUnit.MINUTES)) {
            withColumn.put("period", functions.concat_ws("-",
                    new Column("event_year").cast(DataTypes.StringType),
                    functions.lpad(new Column("event_month").cast(DataTypes.StringType), 2, "0"),
                    functions.lpad(new Column("event_day").cast(DataTypes.StringType), 2, "0"),
                    functions.lpad(new Column("event_hour").cast(DataTypes.StringType), 2, "0"),
                    functions.lpad(new Column("event_minute").cast(DataTypes.StringType), 2, "0")
            ));
        } else if (horizon.equals(ChronoUnit.SECONDS)) {
            withColumn.put("period", functions.concat_ws("-",
                    new Column("event_year").cast(DataTypes.StringType),
                    functions.lpad(new Column("event_month").cast(DataTypes.StringType), 2, "0"),
                    functions.lpad(new Column("event_day").cast(DataTypes.StringType), 2, "0"),
                    functions.lpad(new Column("event_hour").cast(DataTypes.StringType), 2, "0"),
                    functions.lpad(new Column("event_minute").cast(DataTypes.StringType), 2, "0"),
                    functions.lpad(new Column("event_second").cast(DataTypes.StringType), 2, "0")
            ));
        }
        return withColumn;
    }

    private static Column[] createGroupBy(ChronoUnit horizon) {
        List<Column> groupBy = new ArrayList<>();
        groupBy.add(new Column("event_group"));
        groupBy.add(new Column("event_type"));
        groupBy.add(new Column("event_year"));
        if (horizon.equals(ChronoUnit.MONTHS)) {
            groupBy.add(new Column("event_month"));
        } else if (horizon.equals(ChronoUnit.DAYS)) {
            groupBy.add(new Column("event_month"));
            groupBy.add(new Column("event_day"));
        } else if (horizon.equals(ChronoUnit.HOURS)) {
            groupBy.add(new Column("event_month"));
            groupBy.add(new Column("event_day"));
            groupBy.add(new Column("event_hour"));
        } else if (horizon.equals(ChronoUnit.MINUTES)) {
            groupBy.add(new Column("event_month"));
            groupBy.add(new Column("event_day"));
            groupBy.add(new Column("event_hour"));
            groupBy.add(new Column("event_minute"));
        } else if (horizon.equals(ChronoUnit.SECONDS)) {
            groupBy.add(new Column("event_month"));
            groupBy.add(new Column("event_day"));
            groupBy.add(new Column("event_hour"));
            groupBy.add(new Column("event_minute"));
            groupBy.add(new Column("event_second"));
        }
        return groupBy.toArray(new Column[groupBy.size()]);
    }
}
