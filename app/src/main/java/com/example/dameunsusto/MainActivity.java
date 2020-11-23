package com.example.dameunsusto;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceControl;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataOutput;
import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import static java.lang.Thread.*;


public class MainActivity extends AppCompatActivity implements OnPreparedListener {
    Button buttonSustos, buttonPlay;
    Button[] arrayButtonPlay;
    EditText editTextNumeroSustos;
    EditText editTextSegundos;
    EditText[] arrayEditTextSegundos;
    Spinner spinnerSonidos;
    Spinner[] arraySpinnerSonidos;
    int numeroSustos;
    LinearLayout linearlayout;
    int[] idBotonPlay, idTextViewSegundos, idSpinnerSonidos, idVistaCoches;

    int[] arrayImagenes = {R.drawable.abejas, R.drawable.campana, R.drawable.chispaselectricas,
            R.drawable.cortacesped, R.drawable.lobo,
            R.drawable.maquinasdelaboratorio,
             R.drawable.perro,R.drawable.motosierra,
            R.drawable.sonidosanimales, R.drawable.tormento};
    String[] nombressonidos_INGLES_US = {"bees", "beel tool", "electric sparks",
            "chopping", "wolf","lab machines",
             "man eaten by dog","chainsaw",
            "animal sound", "torment"};
    String[] nombressonidos_ES={"abejas","campana","chispas electricas",
    "el cortar","lobo","maquinas de trabajo",
    "hombre comido por perro","Motosierra",
    "sonidos de animales","tormento"};
    MediaPlayer[] mediaPlayers;
    int[] sonidos = {R.raw.bees, R.raw.bell_tool, R.raw.electric_sparks,
            R.raw.chopping, R.raw.wolf,R.raw.lab_machines,
             R.raw.man_waten_by_dog,R.raw.chainsaw,
            R.raw.animal_sounds, R.raw.torment};
    boolean hiloActivo;
    int valorIBorrar;
    ReproduccionSonidos reproduccionSonidos;
    Handler thisHandler;
    String [] segundosEspecificados;
    Locale idiomaDefectoSistema = Locale.getDefault();
    int contadorCuentaAtras;
    int bandera=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextNumeroSustos = findViewById(R.id.editTextNumberSigned);

        buttonSustos = findViewById(R.id.buttonSustos);

        linearlayout = findViewById(R.id.linearlayoutVertical);

        arrayButtonPlay = new Button[numeroSustos];


        buttonSustos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                System.out.println("IDIOMA SYSTEM: "+idiomaDefectoSistema);
                numeroSustos = (Integer.parseInt(editTextNumeroSustos.getText().toString()));
                System.out.println("NUMERO SUSTOS: " + numeroSustos);


               if (numeroSustos >= 11 || numeroSustos<=0) {
                    Toast.makeText(getApplicationContext(), "El rango está comprendido entre 1 y 10", Toast.LENGTH_SHORT).show();
                }
                else if(editTextNumeroSustos.getText().toString()==null){
                    Toast.makeText(getApplicationContext(), "No ha introducido un número", Toast.LENGTH_SHORT).show();
                }
                else {
                    System.out.println("Hola 2 ");
                    CrearDiseñoDinamico();
                }


            }


        });

    }

    public void CrearDiseñoDinamico() {
        idBotonPlay = new int[numeroSustos];
        idTextViewSegundos = new int[numeroSustos];
        idSpinnerSonidos = new int[numeroSustos];
        arrayButtonPlay = new Button[numeroSustos];
        arrayEditTextSegundos = new EditText[numeroSustos];
        arraySpinnerSonidos = new Spinner[numeroSustos];
        mediaPlayers = new MediaPlayer[numeroSustos];
        segundosEspecificados=new String[numeroSustos];


        for (int i = 0; i < numeroSustos; i++) {

            LinearLayout.LayoutParams layoutParamsSpinner = new LinearLayout.LayoutParams(500, 200);

            spinnerSonidos = new Spinner(MainActivity.this);
            arraySpinnerSonidos[i] = spinnerSonidos;
            arraySpinnerSonidos[i].setId(View.generateViewId());//Generar id aleatorio
            idSpinnerSonidos[i] = spinnerSonidos.getId();//Guardar Id
            arraySpinnerSonidos[i].setLayoutParams(layoutParamsSpinner);

            arraySpinnerSonidos[i].setX(5);
            arraySpinnerSonidos[i].setY(300);


            //Traducción de manera dinámica

            ArrayList<String> arrayListSonidos = new ArrayList<>();

            if(idiomaDefectoSistema.equals(Locale.US)){
                arrayListSonidos.add(nombressonidos_INGLES_US[i]);

            }
            else{
                arrayListSonidos.add(nombressonidos_ES[i]);
            }

            ArrayAdapter<String> adaptadorModulos;
            adaptadorModulos = new ArrayAdapter<String>(MainActivity.this,
                    R.layout.support_simple_spinner_dropdown_item, arrayListSonidos);
            arraySpinnerSonidos[i].setAdapter(adaptadorModulos);

            linearlayout.addView(arraySpinnerSonidos[i]);


            LinearLayout.LayoutParams layoutParamsSegundos = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            editTextSegundos = new EditText(MainActivity.this);
            editTextSegundos.setInputType(InputType.TYPE_CLASS_NUMBER);
            arrayEditTextSegundos[i] = editTextSegundos;
            arrayEditTextSegundos[i].setId(View.generateViewId());//Generar id aleatorio
            idTextViewSegundos[i] = editTextSegundos.getId();//Guardar id
            arrayEditTextSegundos[i].setLayoutParams(layoutParamsSegundos);
            arrayEditTextSegundos[i].setWidth(200);
            arrayEditTextSegundos[i].setX(450);
            arrayEditTextSegundos[i].setY(140);
            arrayEditTextSegundos[i].setText("0");



            LinearLayout.LayoutParams layoutParamsPlay = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            buttonPlay = new Button(MainActivity.this);
            arrayButtonPlay[i] = buttonPlay;
            arrayButtonPlay[i].setId(View.generateViewId());//Generar id Aleatorio
            idBotonPlay[i] = buttonPlay.getId();//Guardar id
            arrayButtonPlay[i].setLayoutParams(layoutParamsPlay);
            arrayButtonPlay[i].setGravity(Gravity.RIGHT);
            int contador = 0;
            contador += 300;
            buttonPlay.setY(10);
            buttonPlay.setX(700);
            int fondobotonplay = android.R.drawable.ic_media_play;
            arrayButtonPlay[i].setBackgroundResource(fondobotonplay);




            int ii = i;


                arrayButtonPlay[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (view.getId() == arrayButtonPlay[ii].getId()) {
                            //Inhanilitar el boton del Play
                            // para evitar que de un error cuando esta reproduciendo al pulsar varias veces
                            for (int j = 0; j < numeroSustos; j++) {
                                arrayButtonPlay[j].setEnabled(false);
                            }
                            System.out.println("ID :" + view.getId());
                            System.out.println("Valor: " + ii);
                            System.out.println("SOnidos Longitud Array: " + sonidos.length);
                            System.out.println("Numero Sustos: " + numeroSustos);


                            mediaPlayers[ii] = MediaPlayer.create(getApplicationContext(), sonidos[ii]);
                            onPrepared(mediaPlayers[ii]);


                            valorIBorrar = ii;

                        }
                    }

                });



            linearlayout.addView(arrayEditTextSegundos[i]);
            linearlayout.addView(arrayButtonPlay[i]);

        }


    }


    public void BorrarControles() {

        arraySpinnerSonidos[valorIBorrar].setVisibility(View.INVISIBLE);
        arrayButtonPlay[valorIBorrar].setVisibility(View.INVISIBLE);
        arrayEditTextSegundos[valorIBorrar].setVisibility(View.INVISIBLE);


    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {

        reproduccionSonidos = new ReproduccionSonidos(mediaPlayer);
        reproduccionSonidos.execute();

    }


    private class ReproduccionSonidos extends AsyncTask<String, String, String> {

        MediaPlayer mediaPlayer;

        public ReproduccionSonidos(MediaPlayer medplayer) {
            mediaPlayer = medplayer;
        }

        @Override
        protected void onPreExecute() {
            hiloActivo = true;


        }


        @Override
        protected String doInBackground(String... strings) {

      //Comprobar el Editext que tiene un valor mayor que 0 y almacenarlo y salir del Bucle
            for(int i=0;i<numeroSustos;i++){
                contadorCuentaAtras = Integer.parseInt(arrayEditTextSegundos[i].getText().toString());
                if(contadorCuentaAtras>0){
                    
                    if (valorIBorrar==i){
                        break;
                    }

                }

            }

                while (hiloActivo) {


                    //Si se esta Reproduciendo
                    if (mediaPlayer.isPlaying()) {
                        //Si en el EditText los Segundos son igual a 0 se reproduce el sonido entero
                        /*if (Integer.parseInt(segundosEspecificados[valorIBorrar]) == 0) {
                            System.out.println("Segundos Especificados: " + segundosEspecificados[valorIBorrar]);

                            //Si la posicion actual en segundos es igual a la duracion total
                            // de la canción pues se para
                            if (mediaPlayer.getCurrentPosition() == mediaPlayer.getDuration()) {

                                System.out.println("Hilo ACtivo: " + hiloActivo);

                                mediaPlayer.release();//Retirar los recursos de memoria y procesador asignados a MediaPlayer
                                reproduccionSonidos.cancel(true);//Interrumpir el metodo doInbackground y ir al método Cancel

                            }
                            //Si en el EditText por ejemplo introduce 5 segundos
                            //pues se reproduce el sonido hasta llegar al 5 segundo y hay se para
                         else  if (mediaPlayer.getCurrentPosition() == Integer.parseInt(segundosEspecificados[valorIBorrar]) && contadorCuentaAtras==0) {

                                System.out.println("Hilo ACtivo: " + hiloActivo);

                                mediaPlayer.release();//Retirar los recursos de memoria y procesador asignados a MediaPlayer
                                reproduccionSonidos.cancel(true);//Interrumpir el metodo doInbackground y ir al método Cancel

                            }



                         */

                        //Si la posicion actual en segundos es igual a la duracion total
                        // de la canción pues se para

                        if(Build.VERSION.SDK_INT<26){//Para Android Anterior a Oreo
                            bandera=1;
                        }
                      else if (mediaPlayer.getCurrentPosition() == mediaPlayer.getDuration()) {//Para Android Superior a Oreo

                            System.out.println("Hilo ACtivo: " + hiloActivo);
                            mediaPlayer.stop();
                            mediaPlayer.reset();
                            mediaPlayer.release();//Retirar los recursos de memoria y procesador asignados a MediaPlayer
                           mediaPlayer=null;
                           reproduccionSonidos.cancel(true);//Interrumpir el metodo doInbackground y ir al método Cancel
                            break;
                        }

                    }   //Si no se esta reproduciendo
                     else {

                             if (contadorCuentaAtras > 0) {
                                 contadorCuentaAtras--;

                                 try {
                                     //Haremos que el Thread (Hilo) espere 1 segundo antes de restar 1
                                     Thread.sleep(1000);

                                 } catch (InterruptedException ex) {

                                 }
                                 if (contadorCuentaAtras > 0) {
                                     publishProgress(contadorCuentaAtras + "");
                                 } else {

                                         publishProgress("0");
                                         mediaPlayer.start();

                                 }
                             } else {
                                 //Si no he especificado los segundos comienza al darle al Play
                                 if(Build.VERSION.SDK_INT<26){//Para Android Anterior a Oreo
                                     if(bandera==1){//Cuando termina de Reproducir entra por aquí
                                        bandera=0;
                                         mediaPlayer.stop();
                                         mediaPlayer.reset();
                                         mediaPlayer.release();
                                         reproduccionSonidos.cancel(true);//Interrumpir el metodo doInbackground y ir al método Cancel
                                        break;
                                     }
                                     else{//Cuando le doy al Play Reproduce
                                         mediaPlayer.start();
                                     }

                                 }
                                 else{//Si la Versión de Android es superior a Oreo
                                     mediaPlayer.start();
                                 }
                             }
                         }
                    if (Looper.myLooper() == null) {
                        Looper.prepare();

                    }
                    thisHandler = new Handler();
                    }

            return null;

        }
            @Override
            protected void onPostExecute (String s){
                hiloActivo = false;


                System.out.println("Estoy en onPostExecute");




            }

        @Override
        protected void onProgressUpdate(String... values) {
            arrayEditTextSegundos[valorIBorrar].setText(String.valueOf(values[0]));
        }
//Cuando termina de reproducir un sonido se viene al método onCancelled
        @Override
            protected void onCancelled (String s){
                hiloActivo = true;//Pongo el hilo Activo, para cuando pulse
                    // otro botón pues que me entre en el While del método doInBackground()


                BorrarControles();

                //Habilitar los botones para poder otra vez darle al Play
                for(int j=0;j<numeroSustos;j++){
                    arrayButtonPlay[j].setEnabled(true);
                }


                System.out.println("Estoy en OnCancelled");



            //Comprueba si hay algún EditText que tenga más de 0 segundos
                for(int i=0;i<numeroSustos;i++){
                    contadorCuentaAtras = Integer.parseInt(arrayEditTextSegundos[i].getText().toString());

                    valorIBorrar=i;

                    if(contadorCuentaAtras>0){
                        mediaPlayers[i] = MediaPlayer.create(getApplicationContext(), sonidos[i]);
                        onPrepared(mediaPlayers[valorIBorrar]);//Vuelve a ejecutar el Asyntask
                        break;

                    }

                }

            }


        }





   /* private class AdaptadorSonidos extends ArrayAdapter<String>{
        public AdaptadorSonidos(@NonNull Context context, int resource, @NonNull String[] objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            return CrearFila(position,convertView,parent);
        }

        @Override
        public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            return CrearFila(position,convertView,parent);
        }

        public View CrearFila(int posicion, View convertView, ViewGroup viewGroupPadre) {


            LayoutInflater layoutInflater = getLayoutInflater();
            View vista_sonidos = layoutInflater.inflate(R.layout.vista_sonidos, viewGroupPadre, false);

            textViewNombresonido = vista_sonidos.findViewById(R.id.textViewNombreSonido);
            imageViewSonido = vista_sonidos.findViewById(R.id.imageView);

            textViewNombresonido.setText(nombressonidos[posicion]);
            imageViewSonido.setImageResource(arrayImagenes[posicion]);

            System.out.println("Position: " + posicion);


            return vista_sonidos;
        }
    }

    */


}