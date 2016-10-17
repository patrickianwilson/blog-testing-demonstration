package com.github.patrickianwilson.blogs.testing.induction.repositories;

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
