package com.dodoca.datamagic.core.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by lifei on 2017/3/13.
 */
@JsonIgnoreProperties
public class Bookmark {

    private String id;
    private String type; // "/segmentation/"
    private String name;
    private String data;
    private String time;
    @JsonProperty(value = "create_time")
    private String createTime;
    @JsonProperty(value = "user_id")
    private String userId;
    private String[] dashboards;
    @JsonProperty(value = "related_events")
    private String[] relatedEvents;
    @JsonProperty(value = "project_id")
    private String projectId;
    private String project;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String[] getDashboards() {
        return dashboards;
    }

    public void setDashboards(String[] dashboards) {
        this.dashboards = dashboards;
    }

    public String[] getRelatedEvents() {
        return relatedEvents;
    }

    public void setRelatedEvents(String[] relatedEvents) {
        this.relatedEvents = relatedEvents;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }
}
