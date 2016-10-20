package ExceptionAndError;

import util.HttpHelper;

import java.util.Locale;

/**
 * Created by SCY on 16/10/20.
 */
public class myException extends RuntimeException{

    private static final long serialVersionUID = 8998245782374612L;


    protected ErrorCode errorCode;

    protected Locale locale;
    protected final Boolean toLocalize;


    public myException(){
        super(ErrorCode.GeneralError.toString());
        errorCode = ErrorCode.GeneralError;
        toLocalize = true;
    }


    public myException(ErrorCode error){
        super(error.toString());
        errorCode = error;
        toLocalize = true;
    }


    public myException(ErrorCode error, String msg){
        super(msg);
        errorCode = error;
        toLocalize = false;
    }


}
