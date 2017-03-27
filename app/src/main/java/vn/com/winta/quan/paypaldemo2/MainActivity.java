package vn.com.winta.quan.paypaldemo2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import java.math.BigDecimal;

public class MainActivity extends AppCompatActivity {
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId("ARXo-9bSTQrp4pInm99YFKK7bqklQ4B1eDjy3oOGhVlr9q7jMmdbQw1CC3TTOm3O_271nfmMZ0k8kPKG");

    Button btnSubmit;

    int CODE_PAYPAL = 9890;

    String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBuyPress();
            }
        });
    }

    void onBuyPress() {
        PayPalPayment thingToBuy1 = new PayPalPayment(new BigDecimal("9.75"), "USD", "sample item 1",
                PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy1);
        startActivityForResult(intent, CODE_PAYPAL);
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult: " + resultCode);
        if (requestCode == CODE_PAYPAL) {
            if (resultCode == RESULT_OK) {
                Log.e(TAG, "onActivityResult: " + 123);
                PaymentConfirmation config1 = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (config1 != null) {
                    Log.e(TAG, "onActivityResult: " + config1.toJSONObject().toString());
                    Toast.makeText(this, "Payment success", Toast.LENGTH_SHORT).show();
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.e(TAG, "onActivityResult: " + "User Canceled");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.e(TAG, "onActivityResult: " + "An invalid Payment or PayPalConfig was submitted. Please see the docs.");
            }
        }
    }
}
