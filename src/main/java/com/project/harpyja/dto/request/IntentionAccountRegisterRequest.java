package com.project.harpyja.dto.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class IntentionAccountRegisterRequest {
    private String email;
    private String password;

}