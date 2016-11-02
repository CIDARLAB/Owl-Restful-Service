package org.cidarlab.owldispatcher.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="No Fasta received")
public class BadRequestException extends RuntimeException{

	private static final long serialVersionUID = -8135778981970783407L;

}
