package com.example.arfishing;

public class FishDTO {

    int fish_id;
    String fish_name, fish_area, fish_scale, fish_rotation;

    @Override
    public String toString() {
        return "FishDTO{" +
                "fish_id=" + fish_id +
                ", fish_name='" + fish_name + '\'' +
                ", fish_area='" + fish_area + '\'' +
                ", fish_scale='" + fish_scale + '\'' +
                ", fish_rotation='" + fish_rotation + '\'' +
                '}';
    }
}
