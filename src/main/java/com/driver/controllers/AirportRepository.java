package com.driver.controllers;

import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class AirportRepository {
    int baseFare=3000;
    private Map<String,Airport> airport_Data=new HashMap<>();
    private Map<Integer, Flight> flightData =new HashMap<>();
    private Map<Integer, Passenger> passengerData =new HashMap<>();
    private Map<Integer, List<Integer>> flightToPassenger =new HashMap<>();
    public void add(Airport airport) {
        airport_Data.put(airport.getAirportName(),airport);
    }
    public String getLargetAirpotName() {
        int max=Integer.MIN_VALUE;
        String s="";
        for(Airport a:airport_Data.values()){
            if(a.getNoOfTerminals()>max){
                max=a.getNoOfTerminals();
                s=a.getAirportName();
            }
            else if(a.getNoOfTerminals()==max){
                if(a.getAirportName().compareTo(s)<0){
                    s=a.getAirportName();
                }
            }
        }
        return s;
    }
    public String addFlight(Flight flight) {
        flightData.put(flight.getFlightId(),flight);
        return "SUCCESS";
    }
    public double getShortestPath(City fromCity, City toCity) {
        double duration=Double.MAX_VALUE;
        for(Flight f:flightData.values()){
            if(f.getFromCity().equals(fromCity) && f.getToCity().equals(toCity)){
                if(f.getDuration()<duration){
                    duration=f.getDuration();
                }
            }
        }
        if(duration==Double.MAX_VALUE){
            return -1;
        }
        return duration;
    }

    public int getNumberOfPeopleOn(Date date, String airportName) {
        Airport airport=airport_Data.get(airportName);
        if(airport==null){
            return 0;
        }
        City city=airport.getCity();
        int count=0;
        for(Flight f:flightData.values()){
            if(f.getFlightDate().equals(date)){
                if(f.getToCity().equals(city) || f.getFromCity().equals(city)){
                    int flightId=f.getFlightId();
                    count+=flightToPassenger.get(flightId).size();
                }
            }
        }
        return count;
    }

    public int calculateFlightFare(Integer flightId) {
        int noOfPeopleBookAlready=flightToPassenger.get(flightId).size();
        return noOfPeopleBookAlready*50 + baseFare;
    }

    public String bookATicket(Integer flightId, Integer passengerId) {
        if(flightToPassenger.get(flightId)!=null && flightToPassenger.get(flightId).size()<flightData.get(flightId).getMaxCapacity()){
            List<Integer> passenger=flightToPassenger.get(flightId);
            if(passenger.contains(passengerId)){
                return "FAILURE";
            }
            passenger.add(passengerId);
            flightToPassenger.put(flightId,passenger);
            return "SUCCESS";
        }
        else if(flightToPassenger.get(flightId)==null){
            flightToPassenger.put(flightId,new ArrayList<>());
            List<Integer> passenger=flightToPassenger.get(flightId);
            if(passenger.contains(passengerId)){
                return "FAILURE";
            }
            passenger.add(passengerId);
            flightToPassenger.put(flightId,passenger);
            return "SUCCESS";
        }
        return "FAILURE";
    }

    public String cancelATicket(Integer flightId, Integer passengerId) {
        List<Integer>passenger=flightToPassenger.get(flightId);
        if(passenger==null){
            return "FAILURE";
        }
        if(passenger.contains(passengerId)){
            passenger.remove(passengerId);
            return "SUCCESS";
        }
        return "FAILURE";
    }

    public int countOfBookingsDoneByPassengerAllCombined(Integer passengerId) {
        int count=0;
        for(Map.Entry<Integer,List<Integer>> entry:flightToPassenger.entrySet()){
            List<Integer> passengers=entry.getValue();
            for(Integer passenger : passengers) {
                if(passenger==passengerId){
                    count++;
                }
            }
        }
        return count;
    }

    public String getAirportNameFromFlightId(Integer flightId) {
        if(flightData.containsKey(flightId)){
            City city=flightData.get(flightId).getFromCity();
            for(Airport airport:airport_Data.values()){
                if(airport.getCity().equals(city)){
                    return airport.getAirportName();
                }
            }
        }
        return null;
    }

    public int calculateRevenueOfAFlight(Integer flightId) {
        int noOfPeopleBooked=flightToPassenger.get(flightId).size();
        int totalRevenue=(25 * noOfPeopleBooked * noOfPeopleBooked ) + ( noOfPeopleBooked * 2975 );
        return totalRevenue;
    }

    public String addPassenger(Passenger passenger) {
        passengerData.put(passenger.getPassengerId(),passenger);
        return "SUCCESS";
    }
}





















