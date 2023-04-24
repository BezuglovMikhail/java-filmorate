package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.Collection;

@Component
@Slf4j
public class MpaService {
    private MpaStorage mpaStorage;

    @Autowired
    public MpaService(MpaDbStorage mpaDbStorage) {
        this.mpaStorage = mpaDbStorage;
    }

    public Collection<MPA> getAllMpa() {
        return mpaStorage.getAllMpa();
    }

    public MPA getMpaById(int mpaId) {
        return mpaStorage.getMpaById(mpaId);
    }
}
