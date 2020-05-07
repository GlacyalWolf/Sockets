package com.example.cliente;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

    /**
     * Controles
     * */
    private EditText input_missatge, input_resposta;
    private Button enviar_b, comprobar_b;

    private Context context = this;

    /**
     * Puerto
     * */
    private static final int SERVERPORT = 5000;
    /**
     * HOST
     * */
    private String ADDRESS = "54.163.192.238";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        input_missatge= findViewById(R.id.input_missatge);
        input_resposta= findViewById(R.id.input_resposta);
        enviar_b= findViewById(R.id.enviar_b);
        comprobar_b= findViewById(R.id.comprobar_b);
        comprobar_b.setBackgroundColor(getResources().getColor(R.color.redComprovation));

        comprobar_b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(input_missatge.getText().toString().length()>0){
                    MyATaskCliente myATaskYW = new MyATaskCliente();
                    myATaskYW.execute(input_missatge.getText().toString());
                }else{
                    Toast.makeText(context, "Escriba \"frase\" o \"libro\" ", Toast.LENGTH_LONG).show();
                }

            }
        });

    }//end:onCreate


    /**
     * Clase para interactuar con el servidor
     * */
    class MyATaskCliente extends AsyncTask<String,Void,String>{

        /**
         * Ventana que bloqueara la pantalla del movil hasta recibir respuesta del servidor
         * */
        ProgressDialog progressDialog;

        /**
         * muestra una ventana emergente
         * */


        /**
         * Se conecta al servidor y trata resultado
         * */
        @Override
        protected String doInBackground(String... values){

            try {
                //Se conecta al servidor

                Log.i("I/TCP Client", "Connecting...");
                Socket socket = new Socket(ADDRESS, SERVERPORT);
                Log.i("I/TCP Client", "Connected to server");
                if(socket.isConnected()){
                    comprobar_b.setBackgroundColor(getResources().getColor(R.color.greenComprovation));
                    Toast.makeText(context, "Escriba \"frase\" o \"libro\" ", Toast.LENGTH_LONG).show();

                }

                //envia peticion de cliente
                Log.i("I/TCP Client", "Send data to server");
                PrintStream output = new PrintStream(socket.getOutputStream());
                String request = values[0];
                output.println(request);


                //recibe respuesta del servidor y formatea a String
                Log.i("I/TCP Client", "Received data to server");
                InputStream stream = socket.getInputStream();
                byte[] lenBytes = new byte[256];
                stream.read(lenBytes,0,256);
                String received = new String(lenBytes,"UTF-8").trim();
                Log.i("I/TCP Client", "Received " + received);
                Log.i("I/TCP Client", "");
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

        /**
         * Oculta ventana emergente y muestra resultado en pantalla
         * */
        @Override
        protected void onPostExecute(String value){
            progressDialog.dismiss();
            input_resposta.setText(value);
        }
    }
}
