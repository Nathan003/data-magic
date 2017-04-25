package com.dodoca.datamagic.utils.model;

import com.dodoca.datamagic.utils.vo.Item ;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by lifei on 2017/3/13.
 */
@JsonIgnoreProperties
public class Dashboard {

    private String id;
    private String name;
    @JsonProperty(value = "create_time")
    private String createTime;
    @JsonProperty(value = "user_id")
    private String userId;
    @JsonProperty(value = "is_default")
    private String isDefault;
    @JsonProperty(value = "is_public")
    private String isPublic;// 0表示仅自己可见，1表示所有用户可见
    private String username;
    @JsonProperty(value = "project_id")
    private String projectId;
    private String project;
    private List<Item> items;
    private String config;

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    public String getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(String isPublic) {
        this.isPublic = isPublic;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}

