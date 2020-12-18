package com.ug.grupa2.tictactoe.utils.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//TODO: Check if 'http 409' is appropriate for that exception
@ResponseStatus(value = HttpStatus.CONFLICT, reason = "User already exists")
public class UserAlreadyExistsException extends RuntimeException {

}
