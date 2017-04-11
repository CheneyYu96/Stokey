package org.xeon.stockey.po;

import java.io.Serializable;

/**
 * Created by nians on 2016/6/12.
 */
public class ActionPO implements Serializable{
    private String userID;
    private String name;
    private String description;
    private String definition;

    public ActionPO() {

    }

    public ActionPO(String userID, String name, String description, String definition) {
        this.userID = userID;
        this.name = name;
        this.description = description;
        this.definition = definition;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }
}
