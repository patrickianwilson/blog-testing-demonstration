package com.github.patrickianwilson.template.java.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import com.github.patrickianwilson.template.java.web.controllers.exceptions.BadJSONInputException;
import com.github.patrickianwilson.template.java.web.controllers.exceptions.ServerError;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

/**
 * Created by pwilson on 9/20/16.
 */
@Provider
public class JsonMessageBodyHandler implements MessageBodyReader<Object>, MessageBodyWriter<Object> {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
       return mediaType.isCompatible(MediaType.APPLICATION_JSON_TYPE);
    }

    @Override
    public Object readFrom(Class<Object> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException, WebApplicationException {

        try(InputStreamReader in =  new InputStreamReader(entityStream)) {
            return gson.fromJson(in, type);
        } catch (JsonSyntaxException e) {
            throw new BadJSONInputException(e);
        } catch (JsonIOException e) {
            throw new ServerError();
        }
    }

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    @Override
    public long getSize(Object o, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    @Override
    public void writeTo(Object o, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
        try (OutputStreamWriter out = new OutputStreamWriter(entityStream)) {
            httpHeaders.put("Content-Type", Lists.newArrayList((Object) MediaType.APPLICATION_JSON));

            gson.toJson(o, out);
        } catch (JsonIOException e) {
            throw new ServerError();
        }
    }
}
