package com.github.patrickianwilson.template.java.web.repositories;

import com.github.patrickianwilson.template.java.web.repositories.entities.UrlServiceEntity;

/**
 * Created by pwilson on 9/20/16.
 */
public interface URLRepository {

    /**
     * lookup an url Entity based on the longform of the URL (ie = the unshortened url).
     * @param longForm
     * @return the url entity (if found) or null otherwise.
     */
    UrlServiceEntity findByLongForm(String longForm);

    /**
     * Cache a valid URL entity into the database.
     * @param entity a pre validated entity to cache in the database.  Long form should already be check for length and format.  Smae goes for shortForm.
     */
    void persist(UrlServiceEntity entity);

}
