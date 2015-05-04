package com.nrgentoo.tedtalks.tedtalks.network;

import android.util.Log;

import com.nrgentoo.tedtalks.tedtalks.database.HelperFactory;
import com.nrgentoo.tedtalks.tedtalks.model.Talk;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
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

    private static final String TAG = TedRssService.class.getSimpleName();

    // --------------------------------------------------------------------------------------------
    //      FIELDS
    // --------------------------------------------------------------------------------------------

    private static String END_POINT = "http://www.ted.com";
    private TedRss tedRss;

    // --------------------------------------------------------------------------------------------
    //      PUBLIC METHODS
    // --------------------------------------------------------------------------------------------

    /**
     * Get talks from rss feed and cache them to database
     *
     * @return list of talks
     */
    public Observable<List<Talk>> getTalks() {
        return getTalksFromRss()
                .doOnNext(talks -> {
                    try {
                        for (Talk newTalk : talks) {
                            // store talks in db
                            HelperFactory.getHelper().getTalkDao().createOrUpdate(newTalk);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                })
                .flatMap(talks1 -> getTalksFromDb());
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

    // --------------------------------------------------------------------------------------------
    //      PRIVATE METHODS
    // --------------------------------------------------------------------------------------------
    private Observable<List<Talk>> getTalksFromDb() {
        return Observable.defer(() -> {
            try {
                return Observable.just(HelperFactory.getHelper().getTalkDao().getAllTalks());
            } catch (SQLException e) {
                e.printStackTrace();
                return Observable.just(Collections.EMPTY_LIST);
            }
        });
    }

    private Observable<List<Talk>> getTalksFromRss() {
        return getTedRss().getTalks()
                .flatMap(rss -> Observable.just(rss.talks));
    }
}
