package com.evinram.informationrev2;

import java.util.Date;

public class SubCategory
{
    private String main_category;
    private String sub_category;
    private String full_description;
    private Date created;
    private Date updated;
    private String objectId;

    public String getMain_category()
    {
        return main_category;
    }

    public void setMain_category(String main_category)
    {
        this.main_category = main_category;
    }

    public String getSub_category()
    {
        return sub_category;
    }

    public void setSub_category(String sub_category)
    {
        this.sub_category = sub_category;
    }

    public String getFull_description()
    {
        return full_description;
    }

    public void setFull_description(String full_description)
    {
        this.full_description = full_description;
    }

    public Date getCreated()
    {
        return created;
    }

    public void setCreated(Date created)
    {
        this.created = created;
    }

    public Date getUpdated()
    {
        return updated;
    }

    public void setUpdated(Date updated)
    {
        this.updated = updated;
    }

    public String getObjectId()
    {
        return objectId;
    }

    public void setObjectId(String objectId)
    {
        this.objectId = objectId;
    }
}
