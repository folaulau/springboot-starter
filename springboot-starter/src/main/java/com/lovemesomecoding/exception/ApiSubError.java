package com.lovemesomecoding.exception;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@JsonInclude(value = Include.NON_NULL)
@ToString
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ApiSubError implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * error message
     */
    private String            message;

    /*
     * ============= Form specific fields ============
     */

    /**
     * Name of the object. i.e User<br>
     * This is important when validating multiple objects within the same request body
     */
    private String            object;

    /**
     * Name of the field. i.e firstName<br>
     */
    private String            field;

    /**
     * Value that is invalid. i.e For firstName, "" <br>
     */
    private Object            rejectedValue;

    public ApiSubError(String message) {
        this.message = message;
    }

}
