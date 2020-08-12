package one.entropy.infinity.processor.storage;

import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.Update;

@Dao
public interface EventDao {
    @Update
    void update(Event event);

}

