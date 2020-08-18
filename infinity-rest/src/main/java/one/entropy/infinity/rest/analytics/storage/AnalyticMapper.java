package one.entropy.infinity.rest.analytics.storage;

import com.datastax.oss.driver.api.mapper.annotations.DaoFactory;
import com.datastax.oss.driver.api.mapper.annotations.Mapper;

@Mapper
public interface AnalyticMapper {
    @DaoFactory
    AnalyticDao analyticDao();
}
