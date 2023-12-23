package org.gfg.minor1.request;


import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CreateTxnRequest {
    @NotBlank(message = "Student contact must not be blank")
    private String studentContact;
    @NotBlank(message = "BookNo must not be blank")
    private String bookNo;
    @NotNull(message = "Paid Amount must not be null")
    @Positive(message = "Amount should be positive")
    private Integer paidAmount;

}
