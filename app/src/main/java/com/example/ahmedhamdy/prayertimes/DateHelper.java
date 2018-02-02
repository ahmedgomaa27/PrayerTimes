package com.example.ahmedhamdy.prayertimes;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by ahmed hamdy on 1/17/2018.
 */

public class DateHelper {

    // to get current day as integar number
    //  int cal = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    // to det current month as integar number
    // remembar to increase one as first month =0
    // int cal = Calendar.getInstance().get(Calendar.MONTH)+1;


    // to get current date

    public static Calendar getInstance(){
      return  Calendar.getInstance();
    }


public static int getCurrentMonthAsInt(){

        // return month as number between 0 to 11 so increment by 1
  return   DateHelper.getInstance().get(Calendar.MONTH)+1;


}

public static Calendar changeCalendarDate(int amount){
    Calendar calendar = DateHelper.getInstance();
      calendar.add(Calendar.DATE,amount);
      return calendar;
}
    
/*
public static Date getNextDate(){

    return DateHelper.changeCalendarDate(1).getTime();
} */


public static int getCurrentDayAsInt(){

    return  DateHelper.getInstance().get(Calendar.DAY_OF_MONTH);
}
public static int getCurrentYear(){
    return DateHelper.getInstance().get(Calendar.YEAR);
}

public static int getMaxNumOfDayesInMonth(){
    int iYear = DateHelper.getCurrentYear();
    int iMonth = DateHelper.getCurrentMonthAsInt()-1; // 1 (months begin with 0)
    int iDay = 1;

// Create a calendar object and set year and month
    Calendar myCal = new GregorianCalendar(iYear, iMonth, iDay);

// Get the number of days in that month
    int daysInMonth = myCal.getActualMaximum(Calendar.DAY_OF_MONTH); // 28 for fabuary;
    return daysInMonth;
}
public static String getCurrentMonthAsString(){

   return  (String)android.text.format.DateFormat.format("MMMM", new Date());
   // or
    // return  DateHelper.getInstance().getDisplayName(Calendar.MONTH,Calendar.LONG, Locale.getDefault());

}


}

