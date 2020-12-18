package com.ug.grupa2.tictactoe.utils.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Cannot find user with provided ID")
public class UserNotFoundException extends RuntimeException {
}
