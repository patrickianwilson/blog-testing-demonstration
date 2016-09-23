package com.github.patrickianwilson.template.java.web.repositories.entities;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Id;

/**
 * Created by pwilson on 9/22/16.
 */

public class UrlServiceEntity {

    private String longForm;
    private String shortForm;

    public UrlServiceEntity() {
    }

    public UrlServiceEntity(String longForm, String shortForm) {
        this.longForm = longForm;
        this.shortForm = shortForm;
    }

    public String getLongForm() {
        return longForm;
    }

    public void setLongForm(String longForm) {
        this.longForm = longForm;
    }

    public String getShortForm() {
        return shortForm;
    }

    public void setShortForm(String shortForm) {
        this.shortForm = shortForm;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UrlServiceEntity that = (UrlServiceEntity) o;
        return Objects.equals(longForm, that.longForm) &&
                Objects.equals(shortForm, that.shortForm);
    }

    @Override
    public int hashCode() {
        return Objects.hash(longForm, shortForm);
    }
}
