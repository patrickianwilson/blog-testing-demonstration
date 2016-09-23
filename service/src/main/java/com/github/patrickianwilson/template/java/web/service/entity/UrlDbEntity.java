package com.github.patrickianwilson.template.java.web.service.entity;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import com.github.patrickianwilson.template.java.web.repositories.entities.UrlServiceEntity;

/**
 * Created by pwilson on 9/21/16.
 */
@Entity
@Table(name = "URL_Cache")
public class UrlDbEntity {

    @Id
    @Column(name = "url_id")
    private long id;

    @Column(name = "longForm")
    private String longForm;

    @Column(name = "shortForm")
    private String shortForm;

    public String getShortForm() {
        return shortForm;
    }

    public void setShortForm(String shortForm) {
        this.shortForm = shortForm;
    }

    public String getLongForm() {
        return longForm;
    }

    public void setLongForm(String longForm) {
        this.longForm = longForm;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UrlDbEntity urlDbEntity = (UrlDbEntity) o;
        return id == urlDbEntity.id &&
                Objects.equals(longForm, urlDbEntity.longForm) &&
                Objects.equals(shortForm, urlDbEntity.shortForm);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, longForm, shortForm);
    }

    public static UrlDbEntity fromServiceEntity(UrlServiceEntity serviceEntity) {
        UrlDbEntity result = new UrlDbEntity();
        result.setShortForm(serviceEntity.getShortForm());
        result.setLongForm(serviceEntity.getLongForm());

        return result;
    }

    public static UrlServiceEntity toServiceEntity(UrlDbEntity dbEntity) {

        if (dbEntity == null)
            return null;

        UrlServiceEntity result = new UrlServiceEntity();
        result.setShortForm(dbEntity.getShortForm());
        result.setLongForm(dbEntity.getLongForm());

        return result;
    }
}
