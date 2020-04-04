package com.evinram.informationrev2;

import java.util.Date;

public class Category
{
    private String main_category;
    private String sub_categories;
    private String photo;
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

    public String getSub_categories()
    {
        return sub_categories;
    }

    public void setSub_categories(String sub_categories)
    {
        this.sub_categories = sub_categories;
    }

    public String getPhoto()
    {
        return photo;
    }

    public void setPhoto(String photo)
    {
        this.photo = photo;
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
