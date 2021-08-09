package com.example.candy.controller.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.Email;

@Getter
public class JoinRequestDto {

    @Email @ApiModelProperty
    private String email;
    @ApiModelProperty
    private boolean emailCheck;
    @ApiModelProperty
    private String password;
    @ApiModelProperty
    private String parentPassword;
    @ApiModelProperty
    private String name;
    @ApiModelProperty
    private String phone;
    @ApiModelProperty
    private String birth;

    public JoinRequestDto() {}

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("email", email)
                .append("emailCheck", emailCheck)
                .append("password", password)
                .append("parentPassword", parentPassword)
                .append("name", name)
                .append("phone", phone)
                .append("birth", birth)
                .toString();
    }
}
