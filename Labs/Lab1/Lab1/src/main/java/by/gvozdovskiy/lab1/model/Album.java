package by.gvozdovskiy.lab1.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Album {
    private String nameOfAlbum;
    private String group;
}
