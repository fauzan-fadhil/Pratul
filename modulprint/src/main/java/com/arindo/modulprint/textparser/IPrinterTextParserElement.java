package com.arindo.modulprint.textparser;


import com.arindo.modulprint.EscPosPrinterCommands;
import com.arindo.modulprint.exceptions.EscPosConnectionException;
import com.arindo.modulprint.exceptions.EscPosEncodingException;

public interface IPrinterTextParserElement {
    int length() throws EscPosEncodingException;
    IPrinterTextParserElement print(EscPosPrinterCommands printerSocket) throws EscPosEncodingException, EscPosConnectionException;
}
