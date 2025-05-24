package ge.join2play.join2playback.config;

import ge.join2play.join2playback.model.Option;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "filter")
public class FilterConfig {
    private List<Option> filterOptions;

    public List<Option> getFilterOptions() {
        return filterOptions;
    }

    public void setFilterOptions(List<Option> options) {
        this.filterOptions = options;
    }
}