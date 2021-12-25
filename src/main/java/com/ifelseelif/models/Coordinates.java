package com.ifelseelif.models;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Coordinates {
    private Float x;

    private float y; //Поле не может быть null
}