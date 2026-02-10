package com.scm.forms;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactSearchForm {

    @NotBlank(message = "Field is required")
    private String field;

    @NotBlank(message = "Name/Phone/Email no. is required")
    private String value;

}
