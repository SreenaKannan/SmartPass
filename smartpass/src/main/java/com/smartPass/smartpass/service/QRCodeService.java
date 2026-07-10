package com.smartPass.smartpass.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Service;

import java.nio.file.FileSystems;
import java.nio.file.Path;

@Service
public class QRCodeService {

    public String generateQRCode(String text)
            throws Exception {

        QRCodeWriter qrCodeWriter =
                new QRCodeWriter();

        BitMatrix bitMatrix =
                qrCodeWriter.encode(
                        text,
                        BarcodeFormat.QR_CODE,
                        300,
                        300);

        Path path =
                FileSystems.getDefault()
                        .getPath(
                                "qr_" +
                                System.currentTimeMillis() +
                                ".png");

        MatrixToImageWriter.writeToPath(
                bitMatrix,
                "PNG",
                path);

        return path.toString();
    }
}