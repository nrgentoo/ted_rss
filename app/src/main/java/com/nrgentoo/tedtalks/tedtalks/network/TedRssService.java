package com.nrgentoo.tedtalks.tedtalks.network;

import android.util.Log;

import com.nrgentoo.tedtalks.tedtalks.database.HelperFactory;
import com.nrgentoo.tedtalks.tedtalks.database.TalkDao;
import com.nrgentoo.tedtalks.tedtalks.model.Talk;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
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
                .flatMap(talks1 -> getTalksFromDb());
    }

    public Observable<List<Talk>> getNewTalks() {
        return getTalksFromRss()
                .map(talkList -> {
                    Collections.sort(talkList, Collections.reverseOrder(new DateComparator()));
                    return talkList;
                });
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
                List<Talk> talks = HelperFactory.getHelper().getTalkDao().getAllTalks();
                Collections.sort(talks, Collections.reverseOrder(new DateComparator()));
                return Observable.just(talks);
            } catch (SQLException e) {
                e.printStackTrace();
                return Observable.just(Collections.EMPTY_LIST);
            }
        });
    }

    private Observable<List<Talk>> getTalksFromRss() {
        return getTedRss().getTalks()
                .flatMap(rss -> Observable.just(rss.talks))
                .map(talkList -> {
                    try {
                        TalkDao talkDao = HelperFactory.getHelper().getTalkDao();

                        int i = 0;
                        Iterator<Talk> itr = talkList.iterator();
                        while (itr.hasNext()) {
                            Talk newTalk = itr.next();

                            // if id not found in the db, then...
                            if (!talkDao.idExists((int) newTalk.getTalkId())) {
                                // ...init unix pubDate time...
                                newTalk.initUnixPubDate();
                                // ...and store talk in db
                                talkDao.create(newTalk);
                            } else {
                                itr.remove();
                            }
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }

                    return talkList;
                });
    }

    private class DateComparator implements Comparator<Talk> {

        @Override
        public int compare(Talk lhs, Talk rhs) {
            if (lhs.getUnixPubDate() > rhs.getUnixPubDate()) return 1;
            if (lhs.getUnixPubDate() < rhs.getUnixPubDate()) return -1;
            return 0;
        }
    }
}
