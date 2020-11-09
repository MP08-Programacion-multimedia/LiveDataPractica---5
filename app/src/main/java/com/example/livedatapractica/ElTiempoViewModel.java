package com.example.livedatapractica;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

public class ElTiempoViewModel extends AndroidViewModel {

    ElTiempo elTiempo;

    LiveData<Integer> tipoDeTiempoLiveData;
    LiveData<String> repeticionTiempoLiveData;

    public ElTiempoViewModel(@NonNull Application application) {
        super(application);

        elTiempo = new ElTiempo();

        tipoDeTiempoLiveData = Transformations.switchMap(elTiempo.ordenLiveData, new Function<String, LiveData<Integer>>() {

            String ejercicioAnterior;

            @Override
            public LiveData<Integer> apply(String orden) {

                String ejercicio = orden.split(":")[0];

                if(!ejercicio.equals(ejercicioAnterior)){
                    ejercicioAnterior = ejercicio;
                    int imagen;
                    switch (ejercicio) {
                        case "EJERCICIO1":
                        default:
                            imagen = R.drawable.rain;
                            // rain image
                            break;
                        case "EJERCICIO2":
                            imagen = R.drawable.snow;
                            // snow image
                            break;
                        case "EJERCICIO3":
                            imagen = R.drawable.sun;
                            // sun image
                            break;
                        case "EJERCICIO4":
                            imagen = R.drawable.storm;
                            // storm image
                            break;
                    }

                    return new MutableLiveData<>(imagen);
                }
                return null;
            }
        });

        repeticionTiempoLiveData = Transformations.switchMap(elTiempo.ordenLiveData, new Function<String, LiveData<String>>() {
            @Override
            public LiveData<String> apply(String orden) {
                return new MutableLiveData<>(orden.split(":")[1]);
            }
        });
    }

    LiveData<Integer> obtenerTipoDeTiempo(){
        return tipoDeTiempoLiveData;
    }

    LiveData<String> obtenerRepeticionDeTiempo(){
        return repeticionTiempoLiveData;
    }
}
