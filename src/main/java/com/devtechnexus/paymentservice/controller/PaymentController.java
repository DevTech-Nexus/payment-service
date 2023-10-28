package com.devtechnexus.paymentservice.controller;

import com.devtechnexus.paymentservice.dto.PaymentDto;
import com.devtechnexus.paymentservice.service.LedgerService;
import com.devtechnexus.paymentservice.service.PaymentService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/payments")
@CrossOrigin
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private LedgerService ledgerService;



    private static final String LOCAL = "http://localhost:8084/payments/";
    private static final String CLOUD =  "https://expertmobile-paymentservice.azurewebsites.net/payments/";
    private static final String HOST = LOCAL;
    private static final String SUCCESS_URL = "success";
    private static final String CANCEL_URL = "cancel";

    /**
     * createPayment
     * @param payment Payment DTO
     * @return String redirect to PayPal
     */
    @PostMapping(path = "/process")
    public String createPayment(@RequestBody PaymentDto payment) {


        try {
            //access paypal api to create a payment
            Payment paymentResponse = paymentService.createPayment(payment.getPrice(),
                    payment.getCurrency(),
                    payment.getMethod(),
                    payment.getIntent(),
                    payment.getDescription(),
                    HOST + CANCEL_URL,
                    HOST + SUCCESS_URL
            );

            System.out.println(paymentResponse.getId());
            //create ledger entry
            ledgerService.createLedgerEntry(paymentResponse.getId(), payment);


            for (Links link : paymentResponse.getLinks()) {
                if (link.getRel().equals("approval_url")) {
                    //sends user to PayPal login page
                    return
                            //"redirect:" +
                            link.getHref() + "&user="  + payment.getUser() + "&oid=" + payment.getOid();
                }
            }
        } catch (PayPalRESTException e) {
            e.printStackTrace();
        }
        return "http://localhost:5000/cart";
    }

    /**
     * success
     * if PayPal found no issues in creating the payment
     * i.e, client_id, client_secret, mode, payer, payment objection, etc. are ALL OK,
     * then the payer is redirected to the approval_url (see above), where the user can log in and confirm transaction
     *
     * @param paymentId generated by PayPal
     * @param payerID fetched from PayPal
     * @return String "success" if all OK, else "error"
     */
    @GetMapping(SUCCESS_URL)
    public String success(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerID) {

        try{
            Payment payment = paymentService.executePayment(paymentId, payerID);

            if(payment.getState().equals("approved")){
                //update ledger entry
                ledgerService.successLedgerEntry(paymentId);

                //contact delivery service to update order status to PAID
                //TODO: contact delivery service
                return "success";
            }
        }
        catch(PayPalRESTException e) {
            e.printStackTrace();
        }

        return "error";

    }

    @GetMapping(CANCEL_URL)
    public String cancel(){
        ledgerService.cancelLedgerEntry(,);
        return "cancel";
    }


}
