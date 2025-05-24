package ge.join2play.join2playback.config;

import ge.join2play.join2playback.model.TableHeader;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "table")
public class EventTableConfig {
    private List<TableHeader> tableColumns;

    public List<TableHeader> getTableColumns() {
        return tableColumns;
    }

    public void setTableColumns(List<TableHeader> options) {
        this.tableColumns = options;
    }
}
