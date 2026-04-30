package controladores;

import java.time.LocalDate;
import java.util.*;

import modelos.Temperatura;
import servicios.TemperaturaServicio;

public class TemperaturaControlador {

    private List<Temperatura> datos;

    public TemperaturaControlador(String archivo) {
        datos = TemperaturaServicio.getDatos(archivo);
    }

    public Map<String, Double> getPromedios(LocalDate desde, LocalDate hasta) {
        var filtrados = TemperaturaServicio.filtrar(datos, desde, hasta);
        return TemperaturaServicio.promedioPorCiudad(filtrados);
    }

    public Optional<Temperatura> getMax(LocalDate fecha) {
        return TemperaturaServicio.maxPorFecha(datos, fecha);
    }

    public Optional<Temperatura> getMin(LocalDate fecha) {
        return TemperaturaServicio.minPorFecha(datos, fecha);
    }
}