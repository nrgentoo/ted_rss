package com.nrgentoo.tedtalks.tedtalks.network;

import com.nrgentoo.tedtalks.tedtalks.model.Talk;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

import retrofit.RestAdapter;
import retrofit.converter.SimpleXMLConverter;
import retrofit.http.GET;
import rx.Observable;

/**
 * Service class for retrieving TED's rss feed. (This is not an Android service)
 */
public enum TedRssService {
    INSTANCE;

    // --------------------------------------------------------------------------------------------
    //      FIELDS
    // --------------------------------------------------------------------------------------------

    private static String END_POINT = "http://www.ted.com";
    private TedRss tedRss;

    // --------------------------------------------------------------------------------------------
    //      PUBLIC METHODS
    // --------------------------------------------------------------------------------------------

    /**
     * Get talks from rss feed
     *
     * @return list of talks
     */
    public Observable<List<Talk>> getTalks() {
        return getTedRss().getTalks()
                .flatMap(rss -> Observable.just(rss.talks));
    }

    // --------------------------------------------------------------------------------------------
    //      OTHER METHODS
    // --------------------------------------------------------------------------------------------

    TedRss getTedRss() {
        if (tedRss == null) {
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(TedRssService.END_POINT)
                    .setConverter(new SimpleXMLConverter())
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .build();
            tedRss = restAdapter.create(TedRss.class);
        }

        return tedRss;
    }

    interface TedRss {
        @GET("/talks/rss")
        Observable<Rss> getTalks();
    }

    @Root(name = "rss", strict = false)
    static class Rss {
        @Path("channel")
        @ElementList(name = "item", inline = true)
        public List<Talk> talks = new ArrayList<>();
    }


}
