package com.mycompany.tsms;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

 
 
public class SMS {
    // Find your Account Sid and Auth Token at twilio.com/console
 
 
    public SMS(String accountSid, String authToken) {                       
        Twilio.init(accountSid, authToken);
    }
 
 
    public void sendSms(String to, String from, String msg) {
 
        Message message = Message   
                .creator(new PhoneNumber(to), //phone number you need new com.twilio.type.phoneNumber("+")

                        new PhoneNumber(from), //from com.twilio.type.phoneNumber("+18147871990"),
                        msg) //"the massage body")
                .create();
        System.out.println(message.getSid());
        
        
//        Message message = Message
//16
//                              .creator(new com.twilio.type.PhoneNumber("+201210205513"),
//17
//                                  new com.twilio.type.PhoneNumber("+18147871990"),
//18
//                                  "Where's Wallace?")
//19
//                              .create();
//20
//
//21
//        System.out.println(message.getBody());
    }
}