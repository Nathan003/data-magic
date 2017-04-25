package com.dodoca.datamagic.utils.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by admin on 2017/3/28.
 */
@JsonIgnoreProperties
public class EventProperty {
    private String cname;
    @JsonProperty(value = "property_id")
    private String propertyId;
    @JsonProperty(value = "is_in_use")
    private  String isInUse;
    private  String unit;

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

    public String getIsInUse() {
        return isInUse;
    }

    public void setIsInUse(String isInUse) {
        this.isInUse = isInUse;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
