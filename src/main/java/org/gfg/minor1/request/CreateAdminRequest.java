package org.gfg.minor1.request;

import lombok.*;
import org.gfg.minor1.models.Admin;
import org.gfg.minor1.models.Student;
import org.gfg.minor1.models.StudentType;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CreateAdminRequest {
    private String name;
    private String address;

    @NotBlank
    private String contact;
    private String email;

    private String password;

    public Admin to(){
        return Admin.builder()
                .name(this.name)
                .email(this.email)
                .contact(this.contact)
                .address(this.address)
                .build();
    }
}
