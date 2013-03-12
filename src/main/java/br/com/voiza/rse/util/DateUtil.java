package br.com.voiza.rse.util;

import de.jollyday.Holiday;
import de.jollyday.HolidayCalendar;
import de.jollyday.HolidayManager;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import net.objectlab.kit.datecalc.common.DateCalculator;
import net.objectlab.kit.datecalc.common.DefaultHolidayCalendar;
import net.objectlab.kit.datecalc.common.HolidayHandlerType;
import net.objectlab.kit.datecalc.joda.LocalDateKitCalculatorsFactory;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.chrono.ISOChronology;

/**
 * Classe utilitária para tratar situações com datas
 *
 * @author aneto
 */
public class DateUtil {

    public static final SimpleDateFormat DIA_MES = new SimpleDateFormat("dd/MM");

    /**
     * Calcula a diferença em dias entre duas datas.
     *
     * @param initial Data inicial de onde se deseja obter a diferença
     * @param end Data final de onde se deseja obter a diferença
     * @return a diferença em dias
     */
    public static Integer differenceInDays(Date initial, Date end) {
        return Days.daysBetween(new DateTime(initial).toDateMidnight(), new DateTime(end).toDateMidnight()).getDays();
    }

    /**
     * Retorna a quantidade de dias úteis presentes no período informado. Serão
     * descontados feriados e finais de semana.
     *
     * @param initial
     * @param end
     * @return
     */
    public static Integer businessDaysInPeriod(Date initial, Date end) {
        LocalDate dataInicialTemporaria = new LocalDate(initial);
        LocalDate dataFinalTemporaria = new LocalDate(end);

        Integer diasNaoUteis = 0;
        while (!dataInicialTemporaria.isAfter(dataFinalTemporaria)) {
            if (isBusinessDay(dataInicialTemporaria)) {
                diasNaoUteis++;
            }
            dataInicialTemporaria = dataInicialTemporaria.plusDays(1);
        }
        
        int dias = differenceInDays(initial, end);
        
        return dias - diasNaoUteis;
    }

    /**
     * Verifica se um dia é útil
     * Serão considerados como não útil: Sábados, Domingos e Feriados
     * @param data Date
     * @return 
     */
    public static boolean isBusinessDay(Calendar data) {
        return isBusinessDay(new LocalDate(data));
    }    
    
    /**
     * Verifica se um dia é útil
     * Serão considerados como não útil: Sábados, Domingos e Feriados
     * @param data Date
     * @return 
     */
    public static boolean isBusinessDay(Date data) {
        return isBusinessDay(new LocalDate(data));
    }
    
    /**
     * Verifica se um dia é útil
     * Serão considerados como não útil: Sábados, Domingos e Feriados
     * @param data LocalDate
     * @return 
     */
    public static boolean isBusinessDay(LocalDate data) {
        DefaultHolidayCalendar<LocalDate> holidays = new DefaultHolidayCalendar<LocalDate>(getHolidaysList());
        LocalDateKitCalculatorsFactory.getDefaultInstance().registerHolidays("BR", holidays);
        DateCalculator calendar = LocalDateKitCalculatorsFactory.getDefaultInstance().getDateCalculator("BR", HolidayHandlerType.FORWARD);

        return calendar.isNonWorkingDay(data);
    }

    /**
     * Retorna a lista de feriados presentes para o Brasil no ano atual
     *
     * @return
     */
    private static Set<LocalDate> getHolidaysList() {
        HolidayManager holidayManager = HolidayManager.getInstance(HolidayCalendar.BRAZIL);

        Set<Holiday> holidays = holidayManager.getHolidays(new DateTime().getYear());
        Set<LocalDate> holidaysList = new HashSet<LocalDate>();
        for (Holiday holiday : holidays) {
            holidaysList.add(new LocalDate(holiday.getDate(), ISOChronology.getInstance()));
        }

        return holidaysList;
    }

}
