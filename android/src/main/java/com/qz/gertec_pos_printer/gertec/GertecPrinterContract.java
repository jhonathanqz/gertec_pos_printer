package com.qz.gertec_pos_printer.gertec;

import br.com.gertec.gedi.enums.GEDI_PRNTR_e_Status;
import br.com.gertec.gedi.exceptions.GediException;

public interface GertecPrinterContract {
    void startIGEDI();

    void setConfigImpressao(ConfigPrintGertec config);

    String getStatusImpressora() throws GediException;

    void imprimeTexto(String texto) throws Exception;

    void imprimeTexto(String texto, int tamanho) throws Exception;

    void imprimeTexto(String texto, boolean negrito) throws Exception;

    void imprimeTexto(String texto, boolean negrito, boolean italico) throws Exception;

    void imprimeTexto(String texto, boolean negrito, boolean italico, boolean sublinhado) throws Exception;

    boolean sPrintLine(String texto) throws Exception;

    boolean imprimeBarCode(String texto, int height, int width, String barCodeType) throws GediException;

    void avancaLinha(int linhas) throws GediException;

    boolean isImpressoraOK();

    void ImpressoraInit() throws GediException;

    void ImpressoraOutput() throws GediException;

}
