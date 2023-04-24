package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.Collection;

@Repository
@Slf4j
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<MPA> getAllMpa() {
        return jdbcTemplate.query("SELECT * FROM mpa", (rs, rowNum) -> new MPA(
                rs.getInt("id"),
                rs.getString("mpa_name"))
        );
    }

    @Override
    public MPA getMpaById(int mpaId) {
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet("SELECT mpa_name FROM mpa WHERE id = ?", mpaId);
        if (mpaRows.next()) {
            MPA mpa = new MPA(
                    mpaId,
                    mpaRows.getString("mpa_name")
            );
            log.info("Рейтинг(MPA) найден: {}", mpa);
            return mpa;
        } else throw new NotFoundException("Рейтинг(MPA) с id= " + mpaId + " не найден");
    }
}
