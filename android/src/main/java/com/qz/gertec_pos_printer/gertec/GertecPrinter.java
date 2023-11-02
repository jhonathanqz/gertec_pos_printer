package com.qz.gertec_pos_printer.gertec;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
//import com.journeyapps.barcodescanner.BarcodeEncoder;
import br.com.gertec.gedi.GEDI;
import br.com.gertec.gedi.enums.GEDI_PRNTR_e_Alignment;
import br.com.gertec.gedi.enums.GEDI_PRNTR_e_BarCodeType;
import br.com.gertec.gedi.enums.GEDI_PRNTR_e_Status;
import br.com.gertec.gedi.exceptions.GediException;
import br.com.gertec.gedi.interfaces.ICL;
import br.com.gertec.gedi.interfaces.IGEDI;
import br.com.gertec.gedi.interfaces.IPRNTR;
import br.com.gertec.gedi.structs.GEDI_PRNTR_st_BarCodeConfig;
import br.com.gertec.gedi.structs.GEDI_PRNTR_st_PictureConfig;
import br.com.gertec.gedi.structs.GEDI_PRNTR_st_StringConfig;

public class GertecPrinter implements GertecPrinterContract {
    public static String Model = Build.MODEL;
    public static final String G700 = "GPOS700";
    ICL icl = null;
    private final String IMPRESSORA_ERRO = "Impressora com erro.";

    private static boolean isPrintInit = false;

    private Context context;
    private IGEDI iGedi = null;
    private IPRNTR iPrint = null;
    private GEDI_PRNTR_st_StringConfig stringConfig;
    private GEDI_PRNTR_st_PictureConfig pictureConfig;
    private GEDI_PRNTR_e_Status status;

    private ConfigPrintGertec mconfigPrint;

    public ConfigPrintGertec configPrint() {
        return this.mconfigPrint;
    };

    private Typeface typeface;

    /**
     * Método construtor da classe
     *
     * @param c = Context atual que esta sendo inicializada a class
     */
    public GertecPrinter(Context c) {
        this.context = c;
        this.mconfigPrint = new ConfigPrintGertec();
        setConfigImpressao(this.mconfigPrint);
        startIGEDI();
    }

    /**
     * Método que instância a classe GEDI da lib
     *
     * @apiNote = Este mátodo faz a instância da classe GEDI através de uma Thread.
     *          Será sempre chamado na construção da classe. Não alterar...
     */
    @Override
    public void startIGEDI() {
        new Thread(() -> {
            GEDI.init(this.context);
            this.iGedi = GEDI.getInstance(this.context);
            this.iPrint = this.iGedi.getPRNTR();
            icl = GEDI.getInstance().getCL(); // Get ICL
            try {
                new Thread().sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Método que recebe a configuração para ser usada na impressão
     *
     * @param config = Classe {@link ConfigPrintGertec} que contém toda a
     *               configuração
     *               para a impressão
     */
    @Override
    public void setConfigImpressao(ConfigPrintGertec config) {

        this.mconfigPrint = config;

        this.stringConfig = new GEDI_PRNTR_st_StringConfig(new Paint());
        this.stringConfig.paint.setTextSize(mconfigPrint.getTamanho());
        this.stringConfig.paint.setTextAlign(Paint.Align.valueOf(mconfigPrint.getAlinhamento()));
        this.stringConfig.offset = mconfigPrint.getOffSet();
        this.stringConfig.lineSpace = mconfigPrint.getLineSpace();

        switch (mconfigPrint.getFonte()) {
            case "NORMAL":
                this.typeface = Typeface.create(mconfigPrint.getFonte(), Typeface.NORMAL);
                break;
            case "DEFAULT":
                this.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL);
                break;
            case "DEFAULT BOLD":
                this.typeface = Typeface.create(Typeface.DEFAULT_BOLD, Typeface.NORMAL);
                break;
            case "MONOSPACE":
                this.typeface = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL);
                break;
            case "SANS SERIF":
                this.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL);
                break;
            case "SERIF":
                this.typeface = Typeface.create(Typeface.SERIF, Typeface.NORMAL);
                break;
            default:
                this.typeface = Typeface.createFromAsset(this.context.getAssets(), mconfigPrint.getFonte());
        }

        if (this.mconfigPrint.isNegrito() && this.mconfigPrint.isItalico()) {
            typeface = Typeface.create(typeface, Typeface.BOLD_ITALIC);
        } else if (this.mconfigPrint.isNegrito()) {
            typeface = Typeface.create(typeface, Typeface.BOLD);
        } else if (this.mconfigPrint.isItalico()) {
            typeface = Typeface.create(typeface, Typeface.ITALIC);
        }

        if (this.mconfigPrint.isSublinhado()) {
            this.stringConfig.paint.setFlags(Paint.UNDERLINE_TEXT_FLAG);
        }

        this.stringConfig.paint.setTypeface(this.typeface);
    }

    /**
     * Método que retorna o atual estado da impressora
     *
     * @throws GediException = vai retorno o código do erro.
     *
     * @return String = traduzStatusImpressora()
     *
     */
    @Override
    public String getStatusImpressora() {
        try {
            Log.d("print", "***ENTREI NA FUNCAO GERTEC");
            ImpressoraInit();
            this.status = this.iPrint.Status();
        } catch (GediException e) {
            // throw new GediException(e.getErrorCode());
            return "ERRO DESCONHECIDO";
        }

        return traduzStatusImpressora(this.status);
    }

    /**
     * Método que recebe o atual texto a ser impresso
     *
     * @param texto = Texto que será impresso.
     *
     * @throws Exception = caso a impressora esteja com erro.
     *
     */
    public void imprimeTexto(String texto) throws Exception {

        // this.getStatusImpressora();
        try {
            if (!isImpressoraOK()) {
                throw new Exception(IMPRESSORA_ERRO);
            }
            sPrintLine(texto);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Método que recebe o atual texto e o tamanho da fonte que deve ser usado na
     * impressão.
     *
     * @param texto   = Texto que será impresso.
     * @param tamanho = Tamanho da fonte que será usada
     *
     * @throws Exception = caso a impressora esteja com erro.
     *
     * @apiNote = Esse mátodo só altera o tamanho do texto na impressão que for
     *          chamado a classe {@link ConfigPrintGertec} não será alterada para
     *          continuar sendo usado na impressão da proxíma linha
     *
     */
    public void imprimeTexto(String texto, int tamanho) throws Exception {

        int tamanhoOld;

        // this.getStatusImpressora();

        try {
            if (!isImpressoraOK()) {
                throw new Exception(IMPRESSORA_ERRO);
            }
            tamanhoOld = this.mconfigPrint.getTamanho();
            this.mconfigPrint.setTamanho(tamanho);
            sPrintLine(texto);
            this.mconfigPrint.setTamanho(tamanhoOld);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Método que recebe o atual texto e ser o mesmo será impresso em negrito.
     *
     * @param texto   = Texto que será impresso.
     * @param negrito = Caso o texto deva ser impresso em negrito
     *
     * @throws Exception = caso a impressora esteja com erro.
     *
     * @apiNote = Esse mátodo só altera o tamanho do texto na impressão que for
     *          chamado * a classe {@link ConfigPrintGertec} não será alterada para
     *          continuar sendo usado na impressão da * proxíma linha
     *
     */
    @Override
    public void imprimeTexto(String texto, boolean negrito) throws Exception {

        boolean negritoOld = false;

        // this.getStatusImpressora();

        try {
            if (!isImpressoraOK()) {
                throw new Exception(IMPRESSORA_ERRO);
            }
            negritoOld = this.mconfigPrint.isNegrito();
            this.mconfigPrint.setNegrito(negrito);

            sPrintLine(texto);

            this.mconfigPrint.setNegrito(negritoOld);

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Método que recebe o atual texto e ser o mesmo será impresso em negrito e/ou
     * itálico.
     *
     * @param texto   = Texto que será impresso.
     * @param negrito = Caso o texto deva ser impresso em negrito
     * @param italico = Caso o texto deva ser impresso em itálico
     *
     * @throws Exception = caso a impressora esteja com erro.
     *
     * @apiNote = Esse mátodo só altera o tamanho do texto na impressão que for
     *          chamado * a classe {@link ConfigPrintGertec} não será alterada para
     *          continuar sendo usado na impressão da * proxíma linha
     *
     */
    @Override
    public void imprimeTexto(String texto, boolean negrito, boolean italico) throws Exception {

        boolean negritoOld = false;
        boolean italicoOld = false;

        // this.getStatusImpressora();

        try {
            if (!isImpressoraOK()) {
                throw new Exception(IMPRESSORA_ERRO);
            }
            negritoOld = this.mconfigPrint.isNegrito();
            italicoOld = this.mconfigPrint.isItalico();
            this.mconfigPrint.setNegrito(negrito);
            this.mconfigPrint.setItalico(italico);

            sPrintLine(texto);

            this.mconfigPrint.setNegrito(negritoOld);
            this.mconfigPrint.setItalico(italicoOld);

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Método que recebe o atual texto e ser o mesmo será impresso em negrito,
     * itálico e/ou sublinhado.
     *
     * @param texto      = Texto que será impresso.
     * @param negrito    = Caso o texto deva ser impresso em negrito
     * @param italico    = Caso o texto deva ser impresso em itálico
     * @param sublinhado = Caso o texto deva ser impresso em itálico.
     *
     * @throws Exception = caso a impressora esteja com erro.
     *
     * @apiNote = Esse mátodo só altera o tamanho do texto na impressão que for
     *          chamado * a classe {@link ConfigPrintGertec} não será alterada para
     *          continuar sendo usado na impressão da * proxíma linha
     *
     */
    @Override
    public void imprimeTexto(String texto, boolean negrito, boolean italico, boolean sublinhado) throws Exception {

        boolean negritoOld = false;
        boolean italicoOld = false;
        boolean sublinhadoOld = false;

        // this.getStatusImpressora();

        try {
            if (!isImpressoraOK()) {
                throw new Exception(IMPRESSORA_ERRO);
            }
            negritoOld = this.mconfigPrint.isNegrito();
            italicoOld = this.mconfigPrint.isItalico();
            sublinhadoOld = this.mconfigPrint.isSublinhado();

            this.mconfigPrint.setNegrito(negrito);
            this.mconfigPrint.setItalico(italico);
            this.mconfigPrint.setSublinhado(sublinhado);

            sPrintLine(texto);

            this.mconfigPrint.setNegrito(negritoOld);
            this.mconfigPrint.setItalico(italicoOld);
            this.mconfigPrint.setSublinhado(sublinhadoOld);

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Método privado que faz a impressão do texto.
     *
     * @param texto = Texto que será impresso
     *
     * @throws GediException = retorna o código do erro
     *
     */
    @Override
    public boolean sPrintLine(String texto) throws Exception {
        // Print Data
        try {
            ImpressoraInit();
            this.iPrint.DrawStringExt(this.stringConfig, texto);
            this.avancaLinha(mconfigPrint.getAvancaLinhas());
            // ImpressoraOutput();
            return true;
        } catch (GediException e) {
            throw new GediException(e.getErrorCode());
        }
    }

    /**
     * Método que faz a impressão de código de barras
     *
     * @param texto       = Texto que será usado para a impressão do código de
     *                    barras
     * @param height      = Tamanho
     * @param width       = Tamanho
     * @param barCodeType = Tipo do código que será impresso
     *
     * @throws IllegalArgumentException = Argumento passado ilegal
     * @throws GediException            = retorna o código do erro.
     *
     */
    @Override
    public boolean imprimeBarCode(String texto, int height, int width, String barCodeType) throws GediException {

        try {

            GEDI_PRNTR_st_BarCodeConfig barCodeConfig = new GEDI_PRNTR_st_BarCodeConfig();
            // Bar Code Type
            barCodeConfig.barCodeType = GEDI_PRNTR_e_BarCodeType.valueOf(barCodeType);

            // Height
            barCodeConfig.height = height;
            // Width
            barCodeConfig.width = width;

            ImpressoraInit();
            this.iPrint.DrawBarCode(barCodeConfig, texto);
            this.avancaLinha(mconfigPrint.getAvancaLinhas());
            // ImpressoraOutput();
            // this.iPrint.Output();
            return true;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e);
        } catch (GediException e) {
            throw new GediException(e.getErrorCode());
        }

    }

    /**
     * Método que faz o avanço de linhas após uma impressão.
     *
     * @param linhas = Número de linhas que dever ser pulado após a impressão.
     *
     * @throws GediException = retorna o código do erro.
     *
     * @apiNote = Esse método não deve ser chamado dentro de um FOR ou WHILE, o
     *          número de linhas deve ser sempre passado no atributo do método.
     *
     */
    @Override
    public void avancaLinha(int linhas) throws GediException {
        try {
            if (linhas > 0) {
                this.iPrint.DrawBlankLine(linhas);
            }
        } catch (GediException e) {
            throw new GediException(e.getErrorCode());
        }
    }

    /**
     * Método que retorno se a impressora está apta a fazer impressões
     *
     * @return true = quando estiver tudo ok.
     *
     */
    @Override
    public boolean isImpressoraOK() {

        if (status.getValue() == 0) {
            return true;
        }
        return false;
    }

    /**
     * Método que faz a inicialização da impressao
     *
     * @throws GediException = retorno o código do erro.
     *
     */
    @Override
    public void ImpressoraInit() throws GediException {
        try {
            if (this.iPrint != null && !isPrintInit) {
                this.icl.PowerOff(); // Desligar Módulo NFC - comando Mandatório antes de enviar comandos para a
                // impressora."
                this.iPrint.Init();
                isPrintInit = true;
            }
        } catch (GediException e) {
            e.printStackTrace();
            throw new GediException(e.getErrorCode());
        }
    }

    /**
     * Método que faz a finalizacao do objeto iPrint
     *
     * @throws GediException = retorno o código do erro.
     *
     */
    @Override
    public void ImpressoraOutput() throws GediException {
        try {
            if (this.iPrint != null) {
                this.iPrint.Output();
                isPrintInit = false;
            }
        } catch (GediException e) {
            e.printStackTrace();
            throw new GediException(e.getErrorCode());
        }
    }

    /**
     * Método que faz a tradução do status atual da impressora.
     *
     * @param status = Recebe o {@link GEDI_PRNTR_e_Status} como atributo
     *
     * @return String = Retorno o atual status da impressora
     *
     */
    public String traduzStatusImpressora(GEDI_PRNTR_e_Status status) {
        String retorno;
        switch (status) {
            case OK:
                retorno = "IMPRESSORA OK";
                break;

            case OUT_OF_PAPER:
                retorno = "SEM PAPEL";
                break;

            case OVERHEAT:
                retorno = "SUPER AQUECIMENTO";
                break;

            default:
                retorno = "ERRO DESCONHECIDO";
                break;
        }

        return retorno;
    }

    public void ImprimeTodasAsFucoes() {
        mconfigPrint.setItalico(false);
        mconfigPrint.setNegrito(true);
        mconfigPrint.setTamanho(20);
        mconfigPrint.setFonte("MONOSPACE");
        setConfigImpressao(mconfigPrint);
        try {
            getStatusImpressora();

            mconfigPrint.setiWidth(339);
            mconfigPrint.setiHeight(837);
            mconfigPrint.setAlinhamento("CENTER");
            setConfigImpressao(mconfigPrint);
            imprimeTexto("==[Iniciando Impressao Imagem]==");
            // imprimeImagem("cupomteste");
            avancaLinha(10);
            imprimeTexto("====[Fim Impressão Imagem]====");
            avancaLinha(10);

            mconfigPrint.setAlinhamento("CENTER");
            mconfigPrint.setTamanho(30);
            setConfigImpressao(mconfigPrint);
            imprimeTexto("CENTRALIZADO");
            avancaLinha(10);

            mconfigPrint.setAlinhamento("LEFT");
            mconfigPrint.setTamanho(40);
            setConfigImpressao(mconfigPrint);
            imprimeTexto("ESQUERDA");
            avancaLinha(10);

            mconfigPrint.setAlinhamento("RIGHT");
            mconfigPrint.setTamanho(20);
            setConfigImpressao(mconfigPrint);
            imprimeTexto("DIREITA");
            avancaLinha(10);

            mconfigPrint.setNegrito(true);
            mconfigPrint.setAlinhamento("LEFT");
            mconfigPrint.setTamanho(20);
            setConfigImpressao(mconfigPrint);
            imprimeTexto("=======[Escrita Netrigo]=======");
            avancaLinha(10);

            mconfigPrint.setNegrito(false);
            mconfigPrint.setItalico(true);
            mconfigPrint.setAlinhamento("LEFT");
            mconfigPrint.setTamanho(20);
            setConfigImpressao(mconfigPrint);
            imprimeTexto("=======[Escrita Italico]=======");
            avancaLinha(10);

            mconfigPrint.setNegrito(false);
            mconfigPrint.setItalico(false);
            mconfigPrint.setSublinhado(true);
            mconfigPrint.setAlinhamento("LEFT");
            mconfigPrint.setTamanho(20);
            setConfigImpressao(mconfigPrint);
            imprimeTexto("======[Escrita Sublinhado]=====");
            avancaLinha(10);

            mconfigPrint.setNegrito(false);
            mconfigPrint.setItalico(false);
            mconfigPrint.setSublinhado(false);
            mconfigPrint.setAlinhamento("CENTER");
            mconfigPrint.setTamanho(20);
            setConfigImpressao(mconfigPrint);
            imprimeTexto("====[Codigo Barras CODE 128]====");
            imprimeBarCode(
                    "12345678901234567890",
                    120,
                    120,
                    "CODE_128");
            avancaLinha(10);

            mconfigPrint.setNegrito(false);
            mconfigPrint.setItalico(false);
            mconfigPrint.setSublinhado(true);
            mconfigPrint.setAlinhamento("LEFT");
            mconfigPrint.setTamanho(20);
            setConfigImpressao(mconfigPrint);
            imprimeTexto("=======[Escrita Normal]=======");
            avancaLinha(10);

            mconfigPrint.setNegrito(false);
            mconfigPrint.setItalico(false);
            mconfigPrint.setSublinhado(true);
            mconfigPrint.setAlinhamento("LEFT");
            mconfigPrint.setTamanho(20);
            setConfigImpressao(mconfigPrint);
            imprimeTexto("=========[BlankLine 50]=========");
            avancaLinha(50);
            imprimeTexto("=======[Fim BlankLine 50]=======");
            avancaLinha(10);

            mconfigPrint.setNegrito(false);
            mconfigPrint.setItalico(false);
            mconfigPrint.setSublinhado(false);
            mconfigPrint.setAlinhamento("CENTER");
            mconfigPrint.setTamanho(20);
            setConfigImpressao(mconfigPrint);
            imprimeTexto("=====[Codigo Barras EAN13]=====");
            imprimeBarCode("7891234567895", 120, 120, "EAN_13");
            avancaLinha(10);

            setConfigImpressao(mconfigPrint);
            imprimeTexto("===[Codigo QrCode Gertec LIB]==");
            avancaLinha(10);
            imprimeBarCode(
                    "Gertec Developer Partner LIB",
                    240,
                    240,
                    "QR_CODE");

            avancaLinha(120);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
