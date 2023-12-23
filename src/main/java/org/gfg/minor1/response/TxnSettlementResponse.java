package org.gfg.minor1.response;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class TxnSettlementResponse {
    private String txnId;
    private Integer settlementAmount;
}
