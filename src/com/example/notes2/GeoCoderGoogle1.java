package com.example.notes2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

public class GeoCoderGoogle1 {
	Geocoder gcd;
	
	public GeoCoderGoogle1(Context c){
	gcd = new Geocoder(c, Locale.getDefault());
	}
	
	public ArrayList<String> getGoogleLocationArray
		(double lon,double lat){
		List<Address> addresses = null;
		ArrayList<String> al=new ArrayList<String>();
		try {
			addresses = gcd.getFromLocation(lat, lon,5);
		} catch (IOException e) {
			e.printStackTrace();
		}
		al.clear();
		if (addresses.size() > 0 && addresses != null) {
			if (null!=addresses.get(0).getPremises())
			al.add(addresses.get(0).getPremises()+"-");
			
			if (null!=addresses.get(0).getThoroughfare())
			al.add(addresses.get(0).getThoroughfare()+"-");
			
			if (null!=addresses.get(0).getFeatureName())
			al.add(addresses.get(0).getFeatureName()+"-");
			
			if (null!=addresses.get(0).getLocality())
			al.add(addresses.get(0).getLocality()+"-");
			
			if (null!=addresses.get(0).getAdminArea())
			al.add(addresses.get(0).getAdminArea()+"-");
			
			if (null!=addresses.get(0).getPostalCode())
			al.add(addresses.get(0).getPostalCode()+"-");
			
			if (null!=addresses.get(0).getCountryName())
			al.add(addresses.get(0).getCountryName());
		}
		return al;
		
	}
	
}
