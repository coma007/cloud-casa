package com.casa.app.user.unregistered;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CredentialsDTO {
    public String username;
    public String password;
}
