public class Flight {
    String airline, from, to, depTime, arrTime, depAirport, arrAirport, duration, stops, price;

    public Flight(String airline, String from, String to, String depTime, String arrTime,
                  String depAirport, String arrAirport, String duration, String stops, String price) {
        this.airline = airline;
        this.from = from;
        this.to = to;
        this.depTime = depTime;
        this.arrTime = arrTime;
        this.depAirport = depAirport;
        this.arrAirport = arrAirport;
        this.duration = duration;
        this.stops = stops;
        this.price = price;
    }
}