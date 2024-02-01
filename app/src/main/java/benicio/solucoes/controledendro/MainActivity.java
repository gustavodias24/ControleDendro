package benicio.solucoes.controledendro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import benicio.solucoes.controledendro.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {


    private ActivityMainBinding mainBinding;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        sharedPreferences = getSharedPreferences("preferences", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        mainBinding.edtIp.setText(sharedPreferences.getString("ip", ""));

        mainBinding.saveIp.setOnClickListener( view -> {
            editor.putString("ip", mainBinding.edtIp.getText().toString().trim().replace(",",".")).apply();
            Toast.makeText(this, "Ip Salvo!", Toast.LENGTH_SHORT).show();
        });

        mainBinding.maisRed.setOnClickListener(view -> enviarInformacao("+ red"));
        mainBinding.menosRed.setOnClickListener(view -> enviarInformacao("- red"));

        mainBinding.maisYellow.setOnClickListener(view -> enviarInformacao("+ yellow"));
        mainBinding.menosYellow.setOnClickListener(view -> enviarInformacao("- yellow"));


        mainBinding.maisZoom.setOnClickListener(view -> enviarInformacao("+ zoom"));
        mainBinding.menosZoom.setOnClickListener(view -> enviarInformacao("- zoom"));


    }

    private void enviarInformacao(String info){
        
        String ip = mainBinding.edtIp.getText().toString().trim().replace(",",".");
        
        new Thread(() -> {
            try {
                Socket socket = new Socket(ip, 4203); // IP do servidor
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                out.println(info);
                socket.close();
            } catch (IOException e) {
                runOnUiThread(() -> Toast.makeText(this, "Controle Desconectado!", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
}