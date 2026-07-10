package com.smartPass.smartpass.controller;

import com.smartPass.smartpass.service.QRCodeService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/qr")
public class QRCodeController {

    private final QRCodeService qrCodeService;

    public QRCodeController(
            QRCodeService qrCodeService) {

        this.qrCodeService = qrCodeService;
    }

    @GetMapping("/{cardId}")
    public String generateQR(
            @PathVariable Long cardId)
            throws Exception {

        String data =
                "CARD_ID:" + cardId;

        return qrCodeService.generateQRCode(data);
    }
}