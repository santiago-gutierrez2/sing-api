package com.sign.signApi.signature.web;

import com.sign.signApi.signature.dto.DocumentToSignDTO;
import com.sign.signApi.signature.dto.SignatureOfDocumentDTO;
import com.sign.signApi.signature.dto.VerificationResultDTO;
import com.sign.signApi.signature.dto.VerifySignatureDTO;
import com.sign.signApi.signature.service.SignatureService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/signature")
@RequiredArgsConstructor
public class SignatureController {

    private final SignatureService signatureService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/sign-document")
    public SignatureOfDocumentDTO signDocument(@Valid @RequestBody DocumentToSignDTO documentToSignDTO) {
        return signatureService.signDocument(documentToSignDTO);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/verify-signature")
    public VerificationResultDTO verifySignature(@Valid @RequestBody VerifySignatureDTO verifySignatureDTO) {
        return signatureService.verifySignature(verifySignatureDTO);
    }
}
