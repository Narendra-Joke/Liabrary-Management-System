package org.gfg.minor1.controller;

import org.gfg.minor1.exception.TxnServiceException;
import org.gfg.minor1.request.CreateReturnTxnRequest;
import org.gfg.minor1.request.CreateTxnRequest;
import org.gfg.minor1.response.GenericResponse;
import org.gfg.minor1.response.TxnSettlementResponse;
import org.gfg.minor1.service.TxnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/txn")
public class TxnController {
    @Autowired
    private TxnService txnService;

//    @PostMapping("/create")
//    public String create(@RequestBody @Valid CreateTxnRequest createTxnRequest) throws TxnServiceException {
//        return txnService.create(createTxnRequest);
//    }

    @PostMapping("/create")
    public ResponseEntity<GenericResponse<String>> create(@RequestBody @Valid CreateTxnRequest createTxnRequest) throws TxnServiceException {
        String txnId = txnService.create(createTxnRequest);
        GenericResponse genericResponse = GenericResponse.builder().
                error(null).
                data(txnId).
                status(HttpStatus.OK).build();

        return new ResponseEntity<>(genericResponse,HttpStatus.OK);
    }

    @PostMapping("/return")
    public TxnSettlementResponse returnBook(@RequestBody CreateReturnTxnRequest createReturnTxnRequest) throws TxnServiceException{
        return txnService.returnBook(createReturnTxnRequest);
    }
}
