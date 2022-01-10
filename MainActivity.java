package com.guiadeprogramacao.guiatv.eliezer;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private AdView mAdView;
    private CardView programacaoCanais, enviarOi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        programacaoCanais = findViewById(R.id.programacaoCanaisCardView);
        enviarOi = findViewById(R.id.cardViewHelloWorld);


        programacaoCanais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, com.guiadeprogramacao.guiatv.eliezer.programacaoCanaisActivity.class);
                startActivity(intent);
            }
        });

        enviarOi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //criar um menu flutuante - passo 1
                registerForContextMenu(enviarOi);
                openContextMenu(enviarOi);

            }
        });

    }

    final int CONTEXT_MENU_CELULAR = 1;


    @Override
    public void onCreateContextMenu (ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        //Context menu
        menu.setHeaderTitle("Escolha uma opção");
        menu.add(Menu.NONE, CONTEXT_MENU_CELULAR, Menu.NONE, "Enviar Ola Mundo em Ingles ;)");
    }

    @Override
    public boolean onContextItemSelected (MenuItem item){
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case CONTEXT_MENU_CELULAR: {
                Toast.makeText(this, "Redirecionando para o WhatsApp", Toast.LENGTH_SHORT).show();
                String message = "https://wa.link/prjuwy";
                Intent vaiZap = new Intent(Intent.ACTION_VIEW);
                vaiZap.setData(Uri.parse(message));
                startActivity(vaiZap);
            }
            break;

        }

        return super.onContextItemSelected(item);

    }

}
