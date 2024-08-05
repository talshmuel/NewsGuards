package logic.engine.user.registration;

import logic.engine.reliability.management.Rate;
import logic.engine.report.Genre;

import java.util.ArrayList;

public class UserReportsPreferences {
    private ArrayList<Genre> genrePreference;
    private float reliabilityRatePreference;
    private ArrayList<String> countriesPreference;

    public UserReportsPreferences(ArrayList<Genre> genrePreference, float reliabilityRatePreference, ArrayList<String> countriesPreference) {
        this.genrePreference = genrePreference;
        this.reliabilityRatePreference = reliabilityRatePreference;
        this.countriesPreference = countriesPreference;
    }

    public float getReliabilityRatePreference() {
        return reliabilityRatePreference;
    }

    public ArrayList<String> getCountriesPreference() {
        return countriesPreference;
    }

    public ArrayList<Genre> getGenrePreference() {
        return genrePreference;
    }
}