package com.kirillova.gymcrmsystem.to;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
public class TraineeTo extends BaseTo {

    @NotBlank
    private String username;

    @NotBlank
    @Size(min = 1, max = 128)
    private String firstName;

    @NotBlank
    @Size(min = 1, max = 128)
    private String lastName;

    @NotNull
    private LocalDate birthday;

    @Size(min = 5, max = 128)
    private String address;

    @NotBlank
    private boolean isActive;

    @ToString.Exclude
    private List<TrainerTo> trainerList;

    public TraineeTo(Integer id) {
        super(id);
    }
}
