package vista;
import controladores.TemperaturaControlador;
import modelos.Temperatura;

import datechooser.beans.DateChooserCombo;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Map;

public class FrmTemperaturas extends JFrame {

    private DateChooserCombo dcDesde;
    private DateChooserCombo dcHasta;
    private DateChooserCombo dcFecha;

    private JButton btnGraficar;
    private JButton btnConsultar;

    private JPanel panelGrafica;

    private TemperaturaControlador controlador;

    public FrmTemperaturas() {
        setTitle("Temperaturas por Ciudad");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        controlador = new TemperaturaControlador("src/datos/temperaturas.csv");

        initComponents();
    }

    private void initComponents() {

        JLabel lblDesde = new JLabel("Desde:");
        lblDesde.setBounds(20, 20, 100, 25);
        add(lblDesde);

        dcDesde = new DateChooserCombo();
        dcDesde.setBounds(80, 20, 150, 25);
        add(dcDesde);

        JLabel lblHasta = new JLabel("Hasta:");
        lblHasta.setBounds(250, 20, 100, 25);
        add(lblHasta);

        dcHasta = new DateChooserCombo();
        dcHasta.setBounds(310, 20, 150, 25);
        add(dcHasta);

        btnGraficar = new JButton("Graficar");
        btnGraficar.setBounds(500, 20, 120, 25);
        add(btnGraficar);

        JLabel lblFecha = new JLabel("Fecha:");
        lblFecha.setBounds(20, 60, 100, 25);
        add(lblFecha);

        dcFecha = new DateChooserCombo();
        dcFecha.setBounds(80, 60, 150, 25);
        add(dcFecha);

        btnConsultar = new JButton("Consultar");
        btnConsultar.setBounds(250, 60, 120, 25);
        add(btnConsultar);

        panelGrafica = new JPanel();
        panelGrafica.setBounds(20, 120, 740, 400);
        add(panelGrafica);

        // EVENTOS
        btnGraficar.addActionListener(e -> graficar());
        btnConsultar.addActionListener(e -> consultar());
    }

    private LocalDate convertir(DateChooserCombo dc) {
        return dc.getSelectedDate().getTime()
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    private void graficar() {

        LocalDate desde = convertir(dcDesde);
        LocalDate hasta = convertir(dcHasta);

        Map<String, Double> datos = controlador.getPromedios(desde, hasta);

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        datos.forEach((ciudad, promedio) -> {
            dataset.addValue(promedio, "Temperatura", ciudad);
        });

        JFreeChart chart = ChartFactory.createBarChart(
                "Promedio de Temperaturas",
                "Ciudad",
                "Temperatura",
                dataset
        );

        ChartPanel chartPanel = new ChartPanel(chart);

        panelGrafica.removeAll();
        panelGrafica.setLayout(new BorderLayout());
        panelGrafica.add(chartPanel, BorderLayout.CENTER);
        panelGrafica.validate();
    }

    private void consultar() {

        LocalDate fecha = convertir(dcFecha);

        var max = controlador.getMax(fecha);
        var min = controlador.getMin(fecha);

        String mensaje = "";

        if (max.isPresent()) {
            mensaje += "Más calurosa: " +
                    max.get().getCiudad() + " (" +
                    max.get().getTemperatura() + ")\n";
        }

        if (min.isPresent()) {
            mensaje += "Menos calurosa: " +
                    min.get().getCiudad() + " (" +
                    min.get().getTemperatura() + ")";
        }

        JOptionPane.showMessageDialog(this, mensaje);
    }
}