package ge.join2play.join2playback.model;

import java.util.List;

public class SetupResponse {
    private List<TableHeader> columns;
    private List<Option> sportTypeOptions;
    private List<Option> filterOptions;
    private List<EventResponse> events;

    public SetupResponse(List<TableHeader> columns, List<Option> sportTypeOptions, List<Option> filterOptions, List<EventResponse> events) {
        this.columns = columns;
        this.sportTypeOptions = sportTypeOptions;
        this.filterOptions = filterOptions;
        this.events = events;
    }

    public List<TableHeader> getColumns() {
        return columns;
    }

    public void setColumns(List<TableHeader> columns) {
        this.columns = columns;
    }

    public List<Option> getSportTypeOptions() {
        return sportTypeOptions;
    }

    public void setSportTypeOptions(List<Option> sportTypeOptions) {
        this.sportTypeOptions = sportTypeOptions;
    }

    public List<Option> getFilterOptions() {
        return filterOptions;
    }

    public void setFilterOptions(List<Option> filterOptions) {
        this.filterOptions = filterOptions;
    }

    public List<EventResponse> getEvents() {
        return events;
    }

    public void setEvents(List<EventResponse> events) {
        this.events = events;
    }
}
