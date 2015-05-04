package com.nrgentoo.tedtalks.tedtalks.database;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.nrgentoo.tedtalks.tedtalks.model.Talk;

import java.sql.SQLException;
import java.util.List;

/**
 * DAO for {@link Talk} class
 */
public class TalkDao extends BaseDaoImpl<Talk, Integer> {

    public TalkDao(ConnectionSource connectionSource, Class<Talk> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public List<Talk> getAllTalks() throws SQLException {
        return this.queryForAll();
    }
}
