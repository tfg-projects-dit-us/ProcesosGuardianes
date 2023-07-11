package com.kie;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.SortedSet;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jdesktop.swingx.JXMonthView;
import org.jdesktop.swingx.calendar.DateSelectionModel.SelectionMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CalendarUI {

    private SortedSet<Date> selectedDates;

    public SortedSet<Date> getSelectedDates() {
        return selectedDates;
    }

    public void setSelectedDates(SortedSet<Date> selectedDates) {
        this.selectedDates = selectedDates;
    }
    
    private static final Logger logger = LoggerFactory.getLogger(CalendarUI.class);
    
    public void showCalendarDialog(int year, int month) {

        JFrame frame = new JFrame("Calendario");
        frame.setSize(300, 300);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());

        JXMonthView monthView = new JXMonthView();
       

        // Establecer el mes deseado en el JXMonthView
        setMonth(monthView, year, month);
        
        monthView.setFirstDayOfWeek(Calendar.MONDAY);
        monthView.setSelectionMode(SelectionMode.MULTIPLE_INTERVAL_SELECTION);

        monthView.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JXMonthView mv = (JXMonthView) e.getSource();
                selectedDates = mv.getSelection();
            }
        });

        

        JButton botonEnviar = new JButton("Enviar");
        botonEnviar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        /* TODO para el futuro, en vez de un actionlistener, añadir un mouse listener,
         * si click izquierdo, elige festivos, si click derecho, nuevo panel con las 
         * opciones de guardias.
        */
        
        panel.add(botonEnviar, BorderLayout.SOUTH);
        panel.add(monthView, BorderLayout.CENTER);
        frame.add(panel);
        frame.setVisible(true);

        while (frame.isVisible()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    private static void setMonth(JXMonthView monthView, int year, int month) {
        
    	logger.info("El anio es ", year);
    	logger.info("El mes es ", month);
    	// Crear un objeto YearMonth para el mes y año deseados
        YearMonth desiredMonth = YearMonth.of(year, month + 1);

        // Obtener el primer día del mes
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, desiredMonth.getYear());
        calendar.set(Calendar.MONTH, desiredMonth.getMonthValue() - 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date firstDayOfMonth = calendar.getTime();

        // Establecer el primer día del mes
        monthView.setFirstDisplayedDay(firstDayOfMonth);

        // Establecer el día seleccionado como el primer día del mes
        monthView.setSelectionDate(firstDayOfMonth);
    }


}

