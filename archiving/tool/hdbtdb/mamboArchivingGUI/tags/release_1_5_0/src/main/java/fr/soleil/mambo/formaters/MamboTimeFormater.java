package fr.soleil.mambo.formaters;

import fr.soleil.mambo.tools.Messages;

public class MamboTimeFormater
{
    /**
     * Returns a String representing the value of a time represented as days, hours, minutes, seconds and milliseconds
     * @param totalTime the time, in milliseconds, to convert
     * @return a String representing the value of a time represented as days, hours, minutes, seconds and milliseconds.
     * example : totalTime = 96000000 will return "1d 2hr 40mn"
     */
    public static String formatTime(long totalTime)
    {
        StringBuffer result = new StringBuffer();
        long days, hours, minutes, seconds, milliseconds;
        int dayTime = 1000 * 60 * 60 * 24;
        int hourTime = 1000 * 60 * 60;
        int minuteTime = 1000* 60;
        int secondTime = 1000;

        days = totalTime / dayTime;
        totalTime -= days * dayTime;

        hours = totalTime / hourTime;
        totalTime -= hours * hourTime;

        minutes = totalTime / minuteTime;
        totalTime -= minutes * minuteTime;

        seconds = totalTime / secondTime;

        totalTime -= seconds * secondTime;
        milliseconds = totalTime;

        if (days > 0)
        {
            result.append(days).append(Messages.getMessage("STANDARD_MESSAGES_DAYS")).append(" ");
        }

        if (hours > 0)
        {
            result.append(hours).append(Messages.getMessage("STANDARD_MESSAGES_HOURS")).append(" ");
        }

        if (minutes > 0)
        {
            result.append(minutes).append(Messages.getMessage("STANDARD_MESSAGES_MINUTES")).append(" ");
        }

        if (seconds > 0)
        {
            result.append(seconds).append(Messages.getMessage("STANDARD_MESSAGES_SECONDS")).append(" ");
        }

        if (milliseconds > 0)
        {
            result.append(milliseconds).append(Messages.getMessage("STANDARD_MESSAGES_MILLISECONDS"));
        }
        else if (days == 0 && hours == 0 && minutes == 0 && seconds == 0 && milliseconds == 0)
        {
            result.append(milliseconds).append(Messages.getMessage("STANDARD_MESSAGES_MILLISECONDS"));
        }

        return result.toString();
    }
}
