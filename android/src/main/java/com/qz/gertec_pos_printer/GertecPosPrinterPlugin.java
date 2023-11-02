package com.qz.gertec_pos_printer;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.qz.gertec_pos_printer.gertec.GertecPrinter;

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
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    gertecPrinter = new GertecPrinter(this.context);
    if (call.method.equals("callStatusGertec")) {
      result.success(getGertecPrinter().getStatusImpressora());
    } else if (call.method.equals("callFinishPrintGertec")) {
      try {
        getGertecPrinter().ImpressoraOutput();
        result.success("success");
      } catch (Exception e) {
        result.success("Error: " + e.getMessage());
      }
    } else if (call.method.equals("callNextLine")) {
      try {
        getGertecPrinter().avancaLinha(call.argument("lineQuantity"));
        result.success("success");
      } catch (Exception e) {
        result.success("Error: " + e.getMessage());
      }
    } else if (call.method.equals("callCutGertec")) {
      try {
        getGertecPrinter().avancaLinha(120);
        getGertecPrinter().ImpressoraOutput();
        result.success("success");
      } catch (Exception e) {
        result.success("Error: " + e.getMessage());
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
        result.success("success");
      } catch (Exception e) {
        result.success("Error: " + e.getMessage());
      }
    } else {
      result.notImplemented();
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
    channel = null;
  }
}
