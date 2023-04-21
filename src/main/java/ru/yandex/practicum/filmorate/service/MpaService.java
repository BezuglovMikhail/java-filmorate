package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.Collection;

@Component
@Slf4j
public class MpaService {
    private final JdbcTemplate jdbcTemplate;

    public MpaService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Collection<MPA> getMpa() {
        return jdbcTemplate.query("SELECT * FROM mpa", (rs, rowNum) -> new MPA(
                rs.getInt("id"),
                rs.getString("mpa_name"))
        );
    }

    public MPA get(int id) {
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet("SELECT mpa_name FROM mpa WHERE id = ?", id);
        if (mpaRows.next()) {
            MPA mpa = new MPA(
                    id,
                    mpaRows.getString("mpa_name")
            );
            log.info("Рейтинг(MPA) найден: {}", mpa);
            return mpa;
        } else throw new NotFoundException("Рейтинг(MPA) с id= " + id + " не найден");
    }
}
