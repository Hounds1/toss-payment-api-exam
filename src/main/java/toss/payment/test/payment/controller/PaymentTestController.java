package toss.payment.test.payment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import toss.payment.test.payment.dto.PaymentReq;
import toss.payment.test.payment.dto.SimplePaymentResponse;
import toss.payment.test.payment.service.PaymentService;

import java.net.URI;
import java.net.URL;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PaymentTestController {

    private final PaymentService paymentService;

    @GetMapping("/payment/test")
    public String getPaymentPage() {
        return "/payment/test";
    }

    @GetMapping("/payment/success")
    public ResponseEntity<SimplePaymentResponse> paymentSuccessTest(@RequestParam(name = "paymentKey") String paymentKey,
                                                                    @RequestParam(name = "orderId") String orderId,
                                                                    @RequestParam(name = "amount") int amount) throws Exception{
        log.info("paymentKey, orderId, amount is [{}],[{}],[{}]", paymentKey, orderId, amount);
        PaymentReq paymentReq = PaymentReq.make(paymentKey, orderId, amount);
        return ResponseEntity.ok().body(paymentService.paymentTest(paymentReq));
    }
}
