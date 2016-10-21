package ExceptionAndError;

import util.HttpHelper;

import java.util.Locale;

/**
 * Created by SCY on 16/10/20.
 */
public class MyException extends RuntimeException{

    private static final long serialVersionUID = 8998245782374612L;


    protected ErrorCode errorCode;

    protected Locale locale;
    protected final Boolean toLocalize;


    public MyException(){
        super(ErrorCode.GeneralError.toString());
        errorCode = ErrorCode.GeneralError;
        toLocalize = true;
    }


    public MyException(ErrorCode error){
        super(error.toString());
        errorCode = error;
        toLocalize = true;
    }


    public MyException(ErrorCode error, String msg){
        super(msg);
        errorCode = error;
        toLocalize = false;
    }


}
