package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */
@Data
@AllArgsConstructor
public class Item  {

    private Integer id;

    private String name;

    private String description;

    @NotNull
    private Boolean available;

    private Integer owner;

    //private request;



    public Item() {
    }
}
