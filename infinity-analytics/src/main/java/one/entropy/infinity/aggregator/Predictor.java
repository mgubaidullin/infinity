package one.entropy.infinity.aggregator;

import com.github.signaflo.timeseries.TimeSeries;
import com.github.signaflo.timeseries.forecast.Forecast;
import com.github.signaflo.timeseries.model.arima.Arima;
import com.github.signaflo.timeseries.model.arima.ArimaOrder;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.*;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Predictor {

    private static final Map<ChronoUnit, String> FORMATS = Map.of(
            ChronoUnit.YEARS, "yyyy",
            ChronoUnit.MONTHS, "yyyy-MM",
            ChronoUnit.DAYS, "yyyy-MM-DD",
            ChronoUnit.HOURS, "yyyy-MM-DD-HH",
            ChronoUnit.MINUTES, "yyyy-MM-DD-HH-mm",
            ChronoUnit.SECONDS, "yyyy-MM-DD-HH-mm-ss"
    );

    public static Dataset<Row> forecast(Dataset<Row> aggregation, ChronoUnit horizon, SparkSession spark) throws ParseException {
        Row header = aggregation
                .select("event_group", "event_type", "period")
                .groupBy("event_group", "event_type")
                .agg(functions.max("period")).javaRDD().first();

        String eventGroup = header.getString(0);
        String eventType = header.getString(1);
        String period = header.getString(2);

        JavaRDD<Row> rdd = aggregation.select("avg_value", "min_value", "max_value", "mean_value", "sum_value", "count_value", "period")
                .sort(functions.asc("period")).javaRDD();
        List<Map<String, Double>> list = rdd.map(row ->
                Map.of(
                        "avg_value", row.getDecimal(0).doubleValue(),
                        "min_value", row.getDecimal(1).doubleValue(),
                        "max_value", row.getDecimal(2).doubleValue(),
                        "mean_value", row.getDecimal(3).doubleValue(),
                        "sum_value", row.getDecimal(4).doubleValue(),
                        "count_value", Long.valueOf(row.getLong(5)).doubleValue()
                )).collect();
        double[] avgValues = list.stream().mapToDouble(value -> value.get("avg_value")).toArray();
        double[] minValues = list.stream().mapToDouble(value -> value.get("min_value")).toArray();
        double[] maxValues = list.stream().mapToDouble(value -> value.get("max_value")).toArray();
        double[] meanValues = list.stream().mapToDouble(value -> value.get("mean_value")).toArray();
        double[] sumValues = list.stream().mapToDouble(value -> value.get("sum_value")).toArray();
        double[] countValues = list.stream().mapToDouble(value -> value.get("count_value")).toArray();

        ArimaOrder modelOrder = ArimaOrder.order(0, 1, 1, 0, 1, 1);
        int steps = 6;

        List<Double> avgForecast = Arima.model(TimeSeries.from(avgValues), modelOrder).forecast(steps).pointEstimates().asList();
        List<Double> minForecast = Arima.model(TimeSeries.from(minValues), modelOrder).forecast(steps).pointEstimates().asList();
        List<Double> maxForecast = Arima.model(TimeSeries.from(maxValues), modelOrder).forecast(steps).pointEstimates().asList();
        List<Double> meanForecast = Arima.model(TimeSeries.from(meanValues), modelOrder).forecast(steps).pointEstimates().asList();
        List<Double> sumForecast = Arima.model(TimeSeries.from(sumValues), modelOrder).forecast(steps).pointEstimates().asList();
        List<Double> countForecast = Arima.model(TimeSeries.from(countValues), modelOrder).forecast(steps).pointEstimates().asList();

        List<Row> forecasts = new ArrayList<>(steps);
        for (int i = 0; i < steps; i++) {
            period = addPeriod(period, horizon);
            Row row = RowFactory.create(eventGroup, eventType, "ARIMA", horizon.name(), period,
                    BigDecimal.valueOf(avgForecast.get(i)),
                    BigDecimal.valueOf(maxForecast.get(i)),
                    BigDecimal.valueOf(minForecast.get(i)),
                    BigDecimal.valueOf(meanForecast.get(i)),
                    BigDecimal.valueOf(sumForecast.get(i)),
                    BigDecimal.valueOf(countForecast.get(i)));
            forecasts.add(row);
        }
        return spark.createDataFrame(forecasts, getStructType());
    }

    private static StructType getStructType(){
        List<org.apache.spark.sql.types.StructField> fields = new ArrayList<>();
        fields.add(DataTypes.createStructField("event_group", DataTypes.StringType, true));
        fields.add(DataTypes.createStructField("event_type", DataTypes.StringType, true));
        fields.add(DataTypes.createStructField("algorithm", DataTypes.StringType, true));
        fields.add(DataTypes.createStructField("horizon", DataTypes.StringType, true));
        fields.add(DataTypes.createStructField("period", DataTypes.StringType, true));
        fields.add(DataTypes.createStructField("avg_value", DataTypes.createDecimalType(), true));
        fields.add(DataTypes.createStructField("max_value", DataTypes.createDecimalType(), true));
        fields.add(DataTypes.createStructField("min_value", DataTypes.createDecimalType(), true));
        fields.add(DataTypes.createStructField("mean_value", DataTypes.createDecimalType(), true));
        fields.add(DataTypes.createStructField("sum_value", DataTypes.createDecimalType(), true));
        fields.add(DataTypes.createStructField("count_value", DataTypes.createDecimalType(), true));
        return DataTypes.createStructType(fields);
    }

    private static String addPeriod(String period, ChronoUnit horizon) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(FORMATS.get(horizon));
        Date data = formatter.parse(period);
        ZonedDateTime zdt = data.toInstant().atZone(ZoneOffset.UTC).plus(1, horizon);
        Date newDate = Date.from(zdt.toInstant());
        return formatter.format(newDate);
    }
}
