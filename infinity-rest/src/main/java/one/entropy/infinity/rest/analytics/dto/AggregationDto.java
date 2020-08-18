package one.entropy.infinity.rest.analytics.dto;

import java.math.BigDecimal;

public class AggregationDto {

    private String eventGroup;
    private String eventType;
    private String horizon;
    private String period;
    private BigDecimal avgValue;
    private BigDecimal minValue;
    private BigDecimal maxValue;
    private BigDecimal sumValue;
    private BigDecimal meanValue;
    private BigDecimal countValue;

    public AggregationDto() {
    }

    public AggregationDto(String eventGroup, String eventType, String horizon, String period, BigDecimal avgValue, BigDecimal minValue, BigDecimal maxValue, BigDecimal sumValue, BigDecimal meanValue, BigDecimal countValue) {
        this.eventGroup = eventGroup;
        this.eventType = eventType;
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

