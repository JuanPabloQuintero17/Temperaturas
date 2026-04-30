package servicios;

import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.*;

import modelos.Temperatura;

public class TemperaturaServicio {

    public static List<Temperatura> getDatos(String archivo) {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("d/M/yyyy");

        try (Stream<String> lineas = Files.lines(Paths.get(archivo))) {

            return lineas.skip(1)
                    .map(l -> l.split(","))
                    .map(t -> new Temperatura(
                            t[0],
                            LocalDate.parse(t[1], formato),
                            Double.parseDouble(t[2])
                    ))
                    .collect(Collectors.toList());

        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public static List<Temperatura> filtrar(List<Temperatura> datos,
                                            LocalDate desde, LocalDate hasta) {

        return datos.stream()
                .filter(t -> !t.getFecha().isBefore(desde) &&
                             !t.getFecha().isAfter(hasta))
                .collect(Collectors.toList());
    }


    public static Map<String, Double> promedioPorCiudad(List<Temperatura> datos) {
        return datos.stream()
                .collect(Collectors.groupingBy(
                        Temperatura::getCiudad,
                        Collectors.averagingDouble(Temperatura::getTemperatura)
                ));
    }

    
    public static Optional<Temperatura> maxPorFecha(List<Temperatura> datos, LocalDate fecha) {
        return datos.stream()
                .filter(t -> t.getFecha().equals(fecha))
                .max(Comparator.comparing(Temperatura::getTemperatura));
    }

    public static Optional<Temperatura> minPorFecha(List<Temperatura> datos, LocalDate fecha) {
        return datos.stream()
                .filter(t -> t.getFecha().equals(fecha))
                .min(Comparator.comparing(Temperatura::getTemperatura));
    }
}