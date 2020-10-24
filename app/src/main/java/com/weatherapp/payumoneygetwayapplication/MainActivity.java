package com.weatherapp.payumoneygetwayapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.payumoney.core.PayUmoneySdkInitializer;
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {

    Button pay;

    private static final String TAG = "MainActivity";
    long number;
    String txnid ="txt12346", amount ="20", phone ="7770878087",
    productinfo ="BlueApp Course", firstname ="yogi", email ="yogiii363@gmail.com",
    merchantId ="5884494", merchantkey="qZtEgloC";
    private String salt = "BuvBXdsVpm";
    String hashSequence;
    PayUmoneySdkInitializer.PaymentParam paymentParam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pay = findViewById(R.id.pay);

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sent();
            }
        });

    }
    void sent(){

        hashSequence=merchantkey+"|"+txnid+"|"+amount+"|"+productinfo+"|"+firstname+"|"+email+"|"
                +"udf1"+"|"+"udf2"+"|"+"udf3"+"|"+"udf4"+"|"+"udf5"+"|"+"udf6"+"|"+"udf7"+"|"+"udf8"+"|"
                +"udf9"+"|"+"udf10"+"|"+salt;

        String serverCalculatedHash= getHashkey("SHA-512", hashSequence);

        PayUmoneySdkInitializer.PaymentParam.Builder builder = new PayUmoneySdkInitializer.PaymentParam.Builder();

        builder.setAmount(amount)
                .setTxnId(txnid)
                .setPhone(phone)
                .setProductName(productinfo)
                .setFirstName(firstname)
                .setEmail(email)
                .setsUrl("https://www.payumoney.com/mobileapp/payumoney/success.php")
                .setfUrl("https://www.payumoney.com/mobileapp/payumoney/failure.php")
                .setUdf1("udf1")
                .setUdf2("udf2")
                .setUdf3("udf3")
                .setUdf4("udf4")
                .setUdf5("udf5")
                .setUdf6("udf6")
                .setUdf7("udf7")
                .setUdf8("udf8")
                .setUdf9("udf9")
                .setUdf10("udf10")
                .setIsDebug(false) // Integration environment - true (Debug)/ false(Production)
                .setKey(merchantkey)
                .setMerchantId(merchantId);

        try {
            paymentParam = builder.build();
            paymentParam.setMerchantHash(serverCalculatedHash);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "sent1: "+e.toString());
        }

        PayUmoneyFlowManager.startPayUMoneyFlow(paymentParam, MainActivity.this,
                R.style.AppTheme_default, false);
    }

    public static String getHashkey(String type, String hashString) {
        StringBuilder hash = new StringBuilder();
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance(type);
            messageDigest.update(hashString.getBytes());
            byte[] mdbytes = messageDigest.digest();
            for (byte hashByte : mdbytes) {
                hash.append(Integer.toString((hashByte & 0xff) + 0x100, 16).substring(1));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hash.toString();
    }


}