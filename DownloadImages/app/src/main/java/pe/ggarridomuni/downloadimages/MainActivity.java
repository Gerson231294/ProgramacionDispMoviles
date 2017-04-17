package pe.ggarridomuni.downloadimages;

import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    ImageView downloadedImg;
    private static int progress;
    private ProgressBar progressBar;
    private int progressStatus = 0;
    private Handler handler = new Handler();

    private void startAnimation(){
        // Con este metodo haremos que barra de progreso funcione
        // usaremos un hilo dentro del asynctask
        progress = 0;
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setMax(200);

        new Thread(new Runnable() {
            public void run() {
                while (progressStatus < 200) {
                    progressStatus = doSomeWork();
                    handler.post(new Runnable() {
                        public void run() {
                            progressBar.setProgress(progressStatus);
                        }
                    });
                }
            }

            private int doSomeWork() {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return ++progress;
            }
        }).start();
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        downloadedImg = (ImageView) findViewById(R.id.imageView);

    }


    public void downloadImage(View view){

        // En este metodo se ejecutará cuando presionemos el boton
        // Creamos una instancia de la clase Imagedownloader que descargará la imagen
        ImageDownloader task = new ImageDownloader();
        //Iniciaremos la aplicacion
        startAnimation();

        // Creamos el bitmap para la imagen
        Bitmap myImage;
        try {
            //Ejecuta la descarga
            myImage = task.execute("http://www.uni.edu.pe/images/demo/logouni.png").get();
            //Setea la imagen obtenida al ImageView
            downloadedImg.setImageBitmap(myImage);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Log.i("Botton: ","Tapped");
    }

    public class ImageDownloader extends AsyncTask<String, Void, Bitmap>{
        // Esta es la clase que se ejecutará en background
        @Override
        protected Bitmap doInBackground(String... urls){

            try{
                //Establece las conexiones
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                //Obtiene la imagen
                InputStream inputStream = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(inputStream);
                return myBitmap;

            }
            catch (MalformedURLException e){
                e.printStackTrace();
            }
            catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }
        //Ahora vamos a ejecutar

    }





}
