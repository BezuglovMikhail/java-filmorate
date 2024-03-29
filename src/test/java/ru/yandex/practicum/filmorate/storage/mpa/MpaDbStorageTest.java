package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.MPA;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql({"/schema.sql", "/data.sql"})
class MpaDbStorageTest {

    private final MpaDbStorage mpaStorage;

    @Test
    void getMpaTest() {
        Optional<MPA> mpaOptional = Optional.of(mpaStorage.getMpaById(1));

        assertThat(mpaOptional)
                .isPresent()
                .hasValueSatisfying((mpa ->
                        assertThat(mpa)
                                .hasFieldOrPropertyWithValue("id", 1)
                                .hasFieldOrPropertyWithValue("name", "G"))
                );
    }

    @Test
    void getMpaFalseTest() {

        NotFoundException ex = assertThrows(NotFoundException.class, new Executable() {
            @Override
            public void execute() throws IOException {
                mpaStorage.getMpaById(1000);
            }
        });

        assertEquals("Рейтинг(MPA) с id= 1000 не найден", ex.getMessage());
    }
}
