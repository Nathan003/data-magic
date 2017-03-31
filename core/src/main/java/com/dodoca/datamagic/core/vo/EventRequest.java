package com.dodoca.datamagic.core.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by admin on 2017/3/28.
 */
@JsonIgnoreProperties
public class EventRequest {
    private String visible;
    @JsonProperty(value = "event_id")
    private String eventId;
    private String cname;
    private String[] tag;
    private List<EventProperty> eventProperty;

    public String getVisible() {
        return visible;
    }

    public void setVisible(String visible) {
        this.visible = visible;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String[] getTag() {
        return tag;
    }

    public void setTag(String[] tag) {
        this.tag = tag;
    }


    public List<EventProperty> getEventProperty() {
        return eventProperty;
    }

    public void setEventProperty(List<EventProperty> eventProperty) {
        this.eventProperty = eventProperty;
    }
}
