package de.tu.darmstadt.exception;

public class ParamsExtractException extends ArrayIndexOutOfBoundsException {

    public ParamsExtractException() {
    }

    public ParamsExtractException(String message) {
        super(message);
    }

}
