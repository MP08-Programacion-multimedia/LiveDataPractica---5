package com.example.livedatapractica;

import androidx.lifecycle.LiveData;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import static java.util.concurrent.TimeUnit.SECONDS;

public class ElTiempo {

    interface ElTiempoListener {
        void cuandoDeLaOrden(String orden);
    }

    Random random = new Random();
    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    ScheduledFuture<?> tiempo;

    void iniciarElTiempo(ElTiempoListener tiempoListener) {
        if (tiempo == null || tiempo.isCancelled()) {
            tiempo = scheduler.scheduleAtFixedRate(new Runnable() {
                int ejercicio;
                int repeticiones = -1;

                @Override
                public void run() {
                    if (repeticiones < 0) {
                        repeticiones = random.nextInt(3) + 3;
                        ejercicio = random.nextInt(5)+1;
                    }
                    tiempoListener.cuandoDeLaOrden("EJERCICIO" + ejercicio + ":" + (repeticiones == 0 ? "CAMBIO" : repeticiones));
                    repeticiones--;
                }
            }, 0, 1, SECONDS);
        }
    }

    void pararEltiempo() {
        if (tiempo != null) {
            tiempo.cancel(true);
        }
    }

    LiveData<String> ordenLiveData = new LiveData<String>() {
        @Override
        protected void onActive() {
            super.onActive();

            iniciarElTiempo(new ElTiempoListener() {
                @Override
                public void cuandoDeLaOrden(String orden) {
                    postValue(orden);
                }
            });
        }

        @Override
        protected void onInactive() {
            super.onInactive();

            pararEltiempo();
        }
    };

}
