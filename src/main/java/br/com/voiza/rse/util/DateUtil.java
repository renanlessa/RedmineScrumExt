package br.com.voiza.rse.util;

import java.util.Calendar;
import java.util.Date;
import org.joda.time.DateTime;
import org.joda.time.Days;

/**
 * Classe utilitária para tratar situações com datas
 * 
 * @author aneto
 */
public class DateUtil {
    
    /**
     * Calcula a diferença em dias entre duas datas.
     * @param initial Data inicial de onde se deseja obter a diferença
     * @param end Data final de onde se deseja obter a diferença
     * @return a diferença em dias
     */
    public static Integer differenceInDays(Date initial, Date end) {
        return Days.daysBetween(new DateTime(initial), new DateTime(end)).getDays();
    }
    
    /**
     * Define se uma data cai em um fim de semana.
     * @param data A data com o dia que se deseja saber se é fim de semana ou não
     * @return boolean informando se é fim de semana
     */
    public static boolean isWeekend(Calendar data) {
        return data.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || data.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
    }
    
    public static void main(String[] args) {
        Calendar data1 = Calendar.getInstance();
        data1.set(2013, 1, 25);
        data1.set(Calendar.HOUR, 0);
        data1.set(Calendar.MINUTE, 0);
        data1.set(Calendar.SECOND, 0);
        data1.set(Calendar.MILLISECOND, 0);
        Calendar data2 = Calendar.getInstance();
        data2.set(2013, 2, 2);
        data2.set(Calendar.HOUR, 0);
        data2.set(Calendar.MINUTE, 0);
        data2.set(Calendar.SECOND, 0);
        data2.set(Calendar.MILLISECOND, 0);
        
        System.out.println("## " + differenceInDays(data1.getTime(), data2.getTime()));
    }
    
}
