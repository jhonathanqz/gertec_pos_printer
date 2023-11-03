package com.qz.gertec_pos_printer.sku210;

import android.app.Application;
import android.content.Context;
import android.os.RemoteException;
import android.util.Log;

import com.topwise.cloudpos.aidl.printer.AidlPrinter;
import com.topwise.cloudpos.aidl.printer.AidlPrinterListener;
import com.topwise.cloudpos.aidl.printer.PrintCuttingMode;
import com.topwise.cloudpos.aidl.printer.PrintItemObj;
import com.topwise.cloudpos.data.PrinterConstant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GertecPrinter210 extends Application {
    private Context context;
    private AidlPrinter printer;

    private AidlPrinterListener mListen = new AidlPrinterListener.Stub() {
        @Override
        public void onError(int i) throws RemoteException {
            Log.d("FLUTTER", "onError: " + i);
        }

        @Override
        public void onPrintFinish() throws RemoteException {

        }
    };

    public GertecPrinter210(Context context) {
        this.context = context;
        printer = DeviceServiceManager.getInstance().getPrintManager(context);
    }

    public boolean wrap(int times) throws RemoteException {
        printer.goPaper(times);
        return true;
    }

    public void printerQRCode(HashMap map) throws RemoteException {
        int widthQRCode = (int) map.get("widthQRCode");
        int heightQRCode = (int) map.get("heightQRCode");
        String textParamsQRCode = (String) map.get("textQRCode");

        printer.addRuiQRCode(textParamsQRCode, widthQRCode, heightQRCode);
        printer.printRuiQueue(mListen);
    }

    public void printBarcode(HashMap map) throws RemoteException {
        int heightBarcode = (int) map.get("height");
        int widthBarcode = (int) map.get("width");
        String barcodeType = (String) map.get("type");
        String barcodeText = (String) map.get("message");
        int barcodeTypeCall = 73;

        switch (barcodeType) {
            case "CODE_128":
                barcodeTypeCall = 73;
                break;
            case "EAN_8":
                barcodeTypeCall = 68;
                break;
            case "EAN_13":
                barcodeTypeCall = 67;
                break;
            case "QR_CODE":
                barcodeTypeCall = 71;
                break;
            case "PDF_417":
                barcodeTypeCall = 70;
                break;

        }

        printer.printBarCode(widthBarcode, heightBarcode, 0, barcodeTypeCall, barcodeText, mListen);
    }

    public int cut(int cut) throws RemoteException {
        return printer.cuttingPaper(cut == 1 ? PrintCuttingMode.CUTTING_MODE_HALT : PrintCuttingMode.CUTTING_MODE_FULL);
    }

    public int getPrinterStatus() throws RemoteException {
        return printer.getPrinterState();
    }

    public void printText(HashMap map) throws RemoteException {
        String text = (String) map.get("message");
        Log.d("printText - input", map.toString());
        int fontSize = map.get("fontSize") != null ? (int) map.get("fontSize") : PrinterConstant.FontSize.NORMAL;
        boolean bold = map.get("bold") != null ? (boolean) map.get("bold") : false;
        boolean underline = map.get("underline") != null ? (boolean) map.get("underline") : false;
        boolean wordwrap =  false;
        int letterSpacing =  0;
        int marginLeft =  0;
        int lineHeight =  29;

        List<PrintItemObj> printItems = new ArrayList<>();
        PrintItemObj printerObject = new PrintItemObj(text);
        PrintItemObj.ALIGN align = PrintItemObj.ALIGN.LEFT;
        if (map.get("alignment") != null) {
            switch ((String) map.get("alignment")) {
                case "LEFT":
                    align = PrintItemObj.ALIGN.LEFT;
                    break;
                case "CENTER":
                    align = PrintItemObj.ALIGN.CENTER;
                    break;
                case "RIGHT":
                    align = PrintItemObj.ALIGN.RIGHT;
                    break;
            }
        }
        printerObject.setLetterSpacing(letterSpacing);
        printerObject.setLineHeight(lineHeight);
        printerObject.setMarginLeft(marginLeft);
        printerObject.setBold(bold);
        printerObject.setUnderline(underline);
        printerObject.setFontSize(fontSize);
        printerObject.setAlign(align);
        printerObject.setWordWrap(wordwrap);
        printItems.add(printerObject);

        printer.printText(printItems, mListen);
    }
}
