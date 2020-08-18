package one.entropy.infinity.rest.analytics.storage;

import com.datastax.oss.driver.api.mapper.annotations.ClusteringColumn;
import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;

import java.math.BigDecimal;

@Entity
@CqlName("predictions")
public class Prediction {

    @PartitionKey(1)
    @CqlName("event_group")
    private String eventGroup;
    @PartitionKey(2)
    @CqlName("event_type")
    private String eventType;
    @ClusteringColumn(1)
    @CqlName("algorithm")
    private String algorithm;
    @ClusteringColumn(2)
    @CqlName("horizon")
    private String horizon;
    @ClusteringColumn(3)
    @CqlName("period")
    private String period;
    private BigDecimal avgValue;
    private BigDecimal minValue;
    private BigDecimal maxValue;
    private BigDecimal sumValue;
    private BigDecimal meanValue;
    private BigDecimal countValue;

    public Prediction() {
    }

    public Prediction(String eventGroup, String eventType, String algorithm, String horizon, String period, BigDecimal avgValue, BigDecimal minValue, BigDecimal maxValue, BigDecimal sumValue, BigDecimal meanValue, BigDecimal countValue) {
        this.eventGroup = eventGroup;
        this.eventType = eventType;
        this.algorithm = algorithm;
        this.horizon = horizon;
        this.period = period;
        this.avgValue = avgValue;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.sumValue = sumValue;
        this.meanValue = meanValue;
        this.countValue = countValue;
    }

    public String getEventGroup() {
        return eventGroup;
    }

    public void setEventGroup(String eventGroup) {
        this.eventGroup = eventGroup;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getHorizon() {
        return horizon;
    }

    public void setHorizon(String horizon) {
        this.horizon = horizon;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public BigDecimal getAvgValue() {
        return avgValue;
    }

    public void setAvgValue(BigDecimal avgValue) {
        this.avgValue = avgValue;
    }

    public BigDecimal getMinValue() {
        return minValue;
    }

    public void setMinValue(BigDecimal minValue) {
        this.minValue = minValue;
    }

    public BigDecimal getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(BigDecimal maxValue) {
        this.maxValue = maxValue;
    }

    public BigDecimal getSumValue() {
        return sumValue;
    }

    public void setSumValue(BigDecimal sumValue) {
        this.sumValue = sumValue;
    }

    public BigDecimal getMeanValue() {
        return meanValue;
    }

    public void setMeanValue(BigDecimal meanValue) {
        this.meanValue = meanValue;
    }

    public BigDecimal getCountValue() {
        return countValue;
    }

    public void setCountValue(BigDecimal countValue) {
        this.countValue = countValue;
    }
}
