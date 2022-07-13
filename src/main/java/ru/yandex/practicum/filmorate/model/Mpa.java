package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@Data
public class Mpa {
    private long id;
    private String name;



    /*public MpaRating(long mpaRatingId, String name) {
        this.mpaRatingId = mpaRatingId;
        this.name = name;
    }*/

    public Mpa() {
    }
}
