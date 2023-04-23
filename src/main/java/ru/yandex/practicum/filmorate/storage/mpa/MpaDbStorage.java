package ru.yandex.practicum.filmorate.storage.mpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.MPA;

@Repository
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

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
        throw new NotFoundException("Рейтинга с id = " + id + " нет.");
    }
}
