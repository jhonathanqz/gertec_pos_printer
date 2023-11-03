package com.qz.gertec_pos_printer;

import android.content.Context;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.NonNull;

import com.qz.gertec_pos_printer.gertec.GertecPrinter;
import com.qz.gertec_pos_printer.sku210.GertecPrinter210;

import java.util.HashMap;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

/** GertecPosPrinterPlugin */
public class GertecPosPrinterPlugin implements FlutterPlugin, MethodCallHandler {

  private MethodChannel channel;
  private Context context;
  private GertecPrinter gertecPrinter;

  private GertecPrinter210 printer210;

  private Response response;

  GertecPrinter getGertecPrinter() {
    if (gertecPrinter == null) {
      gertecPrinter = new GertecPrinter(this.context);
    }
    return gertecPrinter;
  }

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "qz_gertec_printer");
    context = flutterPluginBinding.getApplicationContext();
    channel.setMethodCallHandler(this);
    printer210 = new GertecPrinter210(context);
    response = new Response();
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    gertecPrinter = new GertecPrinter(this.context);
    if (call.method.equals("callStatusGertec")) {
      String statusImp = getGertecPrinter().getStatusImpressora();
      result.success(response.send("success", statusImp, true));
    } else if (call.method.equals("callFinishPrintGertec")) {
      try {
        getGertecPrinter().ImpressoraOutput();
        result.success(response.send("success", "", true));
      } catch (Exception e) {
        result.success(response.send("Error", e.getMessage(), false));
      }
    } else if (call.method.equals("callNextLine")) {
      try {
        getGertecPrinter().avancaLinha(call.argument("lineQuantity"));
        result.success(response.send("success", "", true));
      } catch (Exception e) {
        result.success(response.send("Error", e.getMessage(), false));
      }
    } else if (call.method.equals("callCutGertec")) {
      try {
        getGertecPrinter().avancaLinha(120);
        getGertecPrinter().ImpressoraOutput();
        result.success(response.send("success", "", true));
      } catch (Exception e) {
        result.success(response.send("Error", e.getMessage(), false));
      }
    } else if (call.method.equals("callPrintGertec")) {
      try {
        getGertecPrinter().getStatusImpressora();
        if (getGertecPrinter().isImpressoraOK()) {
          String typePrint = call.argument("type");
          String messagePrint = call.argument("message");
          switch (typePrint) {
            case "text":
              boolean italic = call.argument("italic");
              boolean underline = call.argument("underline");
              boolean bold = call.argument("bold");
              int fontSize = call.argument("fontSize");
              String fontType = call.argument("fontType");
              String alignment = call.argument("alignment");

              getGertecPrinter().configPrint().setItalico(italic);
              getGertecPrinter().configPrint().setSublinhado(underline);
              getGertecPrinter().configPrint().setNegrito(bold);
              getGertecPrinter().configPrint().setTamanho(fontSize);
              getGertecPrinter().configPrint().setFonte(fontType);
              getGertecPrinter().configPrint().setAlinhamento(alignment);
              getGertecPrinter().setConfigImpressao(getGertecPrinter().configPrint());
              getGertecPrinter().imprimeTexto(messagePrint);

              getGertecPrinter().ImpressoraOutput();
              break;

            case "TodasFuncoes":
              getGertecPrinter().ImprimeTodasAsFucoes();
              break;
          }
        }
        result.success(response.send("success", "", true));
      } catch (Exception e) {
        result.success(response.send("Error", e.getMessage(), false));
      }
    } else if (call.method.equals("callPrint210")) {
      HashMap mapArguments = call.argument("params");
      try {
        printer210.printText(mapArguments);
        result.success(response.send("success", "", true));
      } catch (RemoteException e) {
        result.success(response.send("Error", e.getMessage(), false));
      }
    }else if (call.method.equals("callPrinterStatus210")) {
      int printerState210 = 0;
      try {
        printerState210 = printer210.getPrinterStatus();
        result.success(response.send("success", printerState210, true));
      } catch (RemoteException e) {
        result.success(response.send("Error", e.getMessage(), false));
      }
    }else if (call.method.equals("callCut210")) {
      int cutMode = call.argument("mode");
      try {
        int resultCut = printer210.cut(cutMode);
        result.success(response.send("success", resultCut, true));
      } catch (RemoteException e) {
        result.success(response.send("Error", e.getMessage(), false));
      }
    }else if (call.method.equals("callPrinterBarcode210")) {
      HashMap mapParamsBarcode = call.argument("params");

      try {
        printer210.printBarcode(mapParamsBarcode);
        result.success(response.send("success", "", true));
      } catch (RemoteException e) {
        result.success(response.send("Error", e.getMessage(), false));
      } catch (NullPointerException e) {
        result.success(response.send("Error", e.getMessage(), false));
      }
    }else if (call.method.equals("callPrinterQRCode210")) {
      HashMap mapParamsQRCode = call.argument("params");

      try {
        printer210.printerQRCode(mapParamsQRCode);
        result.success(response.send("success", "", true));
      } catch (RemoteException e) {
        result.success(response.send("Error", e.getMessage(), false));
      } catch (NullPointerException e) {
        result.success(response.send("Error", e.getMessage(), false));
      }
    }else if (call.method.equals("callPrinterWrap210")) {
      int linesWrap210 = call.argument("linesWrap");
      try {
        printer210.wrap(linesWrap210);
        result.success(response.send("success", "", true));
      } catch (RemoteException e) {
        result.success(response.send("Error", e.getMessage(), false));
      }
    }else {
      result.notImplemented();
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
    channel = null;
  }
}
