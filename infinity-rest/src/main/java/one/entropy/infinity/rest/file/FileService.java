package one.entropy.infinity.rest.file;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import one.entropy.infinity.rest.event.dto.EventDto;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.OnOverflow;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class FileService {

    @Inject
    @OnOverflow(OnOverflow.Strategy.BUFFER)
    @Channel("events")
    Emitter<EventDto> emitterForEvents;

    public int process(InputStream stream) throws IOException, CsvException {
        List<String[]> list = getData(stream);
        list.forEach(s -> {
            ZonedDateTime t = Instant.parse(s[0]).atZone(ZoneOffset.UTC);
            BigDecimal value = new BigDecimal(s[3]);
            EventDto eventDto = new EventDto(UUID.randomUUID().toString(), s[1], s[2], t, value);
            emitterForEvents.send(eventDto);
        });
        return list.size();
    }
    private List<String[]> getData(InputStream stream) throws IOException, CsvException {
        try (Reader reader = new InputStreamReader(stream)){
            try (CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build()){
                return csvReader.readAll();
            }
        }
    }
}
