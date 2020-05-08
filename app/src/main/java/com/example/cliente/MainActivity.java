package com.example.cliente;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cliente.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {


    private EditText input_missatge, input_resposta,input_ip;
    private Button enviar_b, comprobar_b,save_b;
    private Switch switch_mode;
    private RadioButton radiant_manual,cinc,quinze,trenta;


    private Context context = this;
    private long execution_time;
    private Handler handler;


    private static final int SERVERPORT = 5000;

    private String ADDRESS = "3.82.218.249";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        input_missatge= findViewById(R.id.input_missatge);
        input_resposta= findViewById(R.id.input_resposta);
        input_ip= findViewById(R.id.input_ip);
        enviar_b= findViewById(R.id.enviar_b);
        save_b= findViewById(R.id.save_b);
        comprobar_b= findViewById(R.id.comprobar_b);
        switch_mode= findViewById(R.id.switch_mode);
        radiant_manual=findViewById(R.id.radiant_manual);
        cinc=findViewById(R.id.cinc);
        quinze=findViewById(R.id.quinze);
        trenta=findViewById(R.id.trenta);


        comprobar_b.setBackgroundColor(getResources().getColor(R.color.redComprovation));

        input_ip.setText(ADDRESS);

        enviar_b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (radiant_manual.isChecked()) {
                    if (input_missatge.getText().toString().length() > 0) {
                        MyATaskCliente myATaskYW = new MyATaskCliente();

                        myATaskYW.execute(input_missatge.getText().toString());

                    } else {
                        Toast.makeText(context, "Escriba \"wonder\" o \"excusa\" ", Toast.LENGTH_LONG).show();
                    }

                }
            }
        });
        //temporitzador
        if(cinc.isChecked() || quinze.isChecked() || trenta.isChecked()){
            if(cinc.isChecked()){
                execution_time=300000;
            }
            if(quinze.isChecked()){
                execution_time=900000;

            }
            if(trenta.isChecked()){
                execution_time=1800000;

            }
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    MyATaskCliente myATaskYW = new MyATaskCliente();

                    myATaskYW.execute("excusa");
                    handler.postDelayed(this,execution_time);
                }
            },execution_time);
        }


        comprobar_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThreadComprovar comprovacion= new ThreadComprovar();
                comprovacion.execute("wonder");
            }
        });
        save_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ADDRESS= input_ip.getText().toString();


            }
        });



    }//end:onCreate



    class MyATaskCliente extends AsyncTask<String,Void,String>{


        @Override
        protected String doInBackground(String... values){

            try {
                //Se conecta al servidor



                Socket socket = new Socket(ADDRESS, SERVERPORT);


                //envia peticion de cliente

                PrintStream output = new PrintStream(socket.getOutputStream());
                String request = values[0];
                output.println(request);


                //recibe respuesta del servidor y formatea a String

                InputStream stream = socket.getInputStream();
                byte[] lenBytes = new byte[256];
                stream.read(lenBytes,0,256);
                String received = new String(lenBytes,"UTF-8").trim();

                //cierra conexion
                socket.close();
                return received;



            }catch (UnknownHostException ex) {
                Log.e("E/TCP Client", "" + ex.getMessage());
                return ex.getMessage();
            } catch (IOException ex) {
                Log.e("E/TCP Client", "" + ex.getMessage());
                return ex.getMessage();
            }
        }


        @Override
        protected void onPostExecute(String value){
            if(switch_mode.isChecked()) {
                input_resposta.setText(value);
            }

        }
    }



    class ThreadComprovar extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... values) {
            Socket socket = null;
            try {
                socket = new Socket(ADDRESS, SERVERPORT);
                if (socket.isConnected()) {
                    comprobar_b.setBackgroundColor(getResources().getColor(R.color.greenComprovation));
                    //
                    PrintStream output = new PrintStream(socket.getOutputStream());
                    String request = values[0];
                    output.println(request);



                    InputStream stream = socket.getInputStream();
                    byte[] lenBytes = new byte[256];
                    stream.read(lenBytes, 0, 256);
                    String received = new String(lenBytes, "UTF-8").trim();

                    return received;


                } else {

                    comprobar_b.setBackgroundColor(getResources().getColor(R.color.redComprovation));
                }
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String value) {
            Toast.makeText(context, value, Toast.LENGTH_SHORT).show();

        }
    }

}
