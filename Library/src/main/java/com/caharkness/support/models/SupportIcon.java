package com.caharkness.support.models;

public class SupportIcon
{
    private String id;

    public void setId(String s)
    {
        id = s;
    }

    public String getId()
    {
        if (id == null)
            setId("");

        return id;
    }

    private String name;

    public void setName(String s)
    {
        name = s;
    }

    public String getName()
    {
        if (name == null)
            setName("");

        return id;
    }

    private Integer resource;

    public void setResource(Integer s)
    {
        resource = s;
    }

    public Integer getResource()
    {
        if (resource == null)
            setResource(0);

        return resource;
    }
}
