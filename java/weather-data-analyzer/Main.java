import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

class WeatherData {
    private String date;
    private double temperature;
    private double humidity;

    public WeatherData(String date, double temperature, double humidity) {
        this.date = date;
        this.temperature= temperature;
        this.humidity = humidity;
    }

    public String getDate() {
        return date;
    }
    public double getTemperature() {
        return temperature;
    }
    public double getHumidity() {
        return humidity;
    }
    public boolean isHot() {
        return temperature>30;
    }
    public boolean isHumidDay() {
        return humidity>70;
    }

}


public class Main {

    public static void main(String[] args) {
  
        System.out.println("Welcome to the Weather Data Analyzer!");
        analyzeWeatherDataFromCSV();
    }

 
    public static double calculateAverageTemperature(WeatherData[] data) {
        double total= 0;
        for (WeatherData d :  data){
            total+= d.getTemperature();
        }
        return total/data.length;
    }

    public static long calculateHotDayCount(WeatherData[] data) {
        long totalHotDay= 0l;
        for (WeatherData day: data) {
            if (day.getTemperature()>25) {
                totalHotDay+=1;
            }
        }
        return totalHotDay;
    }

    public static void analyzeWeatherDataFromCSV() {
       
        WeatherData[] weatherDatas = readWeatherDataFromCSV("weather_data.csv");
        double avgTemp=  calculateAverageTemperature(weatherDatas);
        long hotDayCount= calculateHotDayCount(weatherDatas);
        System.out.println("Average Temperature from csv: " + avgTemp);
        System.out.println("Hot day count: " + hotDayCount);
    }


    public static WeatherData[] readWeatherDataFromCSV(String fileName) {
        ArrayList<WeatherData> weatherDataList =new ArrayList<>();
        try (BufferedReader br= new BufferedReader(new FileReader(fileName))) {
            String line;
            br.readLine();
            while ((line=br.readLine()) != null) {
                
                String[] splittedLine = line.split(",");
                String date= splittedLine[0];
                double temperature = Double.parseDouble(splittedLine[1]);
                double humidity = Double.parseDouble(splittedLine[2]);

                WeatherData weatherData= new WeatherData(date, temperature, humidity);
                weatherDataList.add(weatherData);

            }
    


        }
        catch (IOException e) {

            System.out.println("An error occured" + e.getMessage());
        }
        return  weatherDataList.toArray(new WeatherData[0]);
    }
}
