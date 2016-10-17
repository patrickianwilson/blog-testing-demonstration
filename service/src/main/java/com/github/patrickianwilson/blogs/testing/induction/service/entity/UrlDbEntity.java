package com.github.patrickianwilson.blogs.testing.induction.service.entity;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import com.github.patrickianwilson.blogs.testing.induction.repositories.entities.UrlServiceEntity;

/*
 The MIT License (MIT)

 Copyright (c) 2014 Patrick Wilson

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all
 copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 SOFTWARE.
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
