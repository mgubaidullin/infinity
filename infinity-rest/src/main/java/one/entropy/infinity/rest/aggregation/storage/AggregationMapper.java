package one.entropy.infinity.rest.aggregation.storage;

import com.datastax.oss.driver.api.mapper.annotations.DaoFactory;
import com.datastax.oss.driver.api.mapper.annotations.Mapper;

@Mapper
public interface AggregationMapper {
    @DaoFactory
    AggregationDao aggregationDao();
}
