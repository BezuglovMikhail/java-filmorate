package ru.yandex.practicum.filmorate.storage.mpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;

@Repository
public class MpaDbStorage implements MpaStorage{
    private final JdbcTemplate jdbcTemplate;
    //private final Logger log = LoggerFactory.getLogger(MpaDbStorage.class);

    @Autowired
    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public MPA getMpa(int id) {
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet("SELECT mpa_name FROM mpa WHERE id = ?", id);
        if (mpaRows.next()) {
            return new MPA(id, mpaRows.getString("mpa_name"));
        }
        return null;
    }
}
