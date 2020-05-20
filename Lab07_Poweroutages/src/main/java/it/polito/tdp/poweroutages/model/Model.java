package it.polito.tdp.poweroutages.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polito.tdp.poweroutages.DAO.PowerOutageDAO;

public class Model {
	
	PowerOutageDAO podao;
	
	private NercIdMap nercIdMap;
	private List<Nerc> nercList;
	
	private List<PowerOutages> eventList;
	private List<PowerOutages> eventListFiltered;
	private List<PowerOutages> solution;
	
	private int maxAffectedPeople;
	
	public Model() {
		
		podao = new PowerOutageDAO();
		
		nercIdMap = new NercIdMap();
		
		nercList=podao.getNercList(nercIdMap);
		
		eventList=podao.getPowerOutages(nercIdMap);
		
	}
	
	public List<Nerc> getNercList(){
		return this.nercList;
	}
	
	public List<PowerOutages> getWorstCase(int maxYears, int maxHours, Nerc nerc){
		
		solution = new ArrayList<>();
		
		List<PowerOutages> partial = new ArrayList<>();
		
		maxAffectedPeople=0;
		
		eventListFiltered = new ArrayList<>();
		
		for(PowerOutages p : eventList) {
			if(p.getNerc().equals(nerc))
				eventListFiltered.add(p);
		}
		
		Collections.sort(eventListFiltered);
		
		//int livello=0;
		
		ricorsione(partial, maxYears, maxHours);
		
		return solution;
		
	}
	
	public void ricorsione(List<PowerOutages> partial, int maxYears, int maxHours) {
		
		if(getPeopleAffected(partial)>maxAffectedPeople) {
			maxAffectedPeople=getPeopleAffected(partial);
			solution = new ArrayList<PowerOutages>(partial);
		}
		
	
		for(PowerOutages p : eventListFiltered) {
			
			if(!partial.contains(p)) {
				partial.add(p);
				
				if(checkYears(partial, maxYears) && checkHours(partial, maxHours)) {
					ricorsione(partial, maxYears, maxHours);
				}
				
				partial.remove(p);
			}	
		}	
	}
	
	
	public int getPeopleAffected(List<PowerOutages> partial) {
		
		int sum=0;
		
		for(PowerOutages p : partial) {
			sum+=p.getCustomersAffected();
		}
		
		return sum;
		
	}
	
	
	public int getSumHours(List<PowerOutages> partial) {
		
		 int sum=0;
		
		for(PowerOutages p : partial) {
			sum+=p.getCustomersAffected();
		}
		
		return sum;
	}
	
	private boolean checkHours(List<PowerOutages> partial, int maxHours) {
		
		int sum = getSumHours(partial);
		
		if(sum<= maxHours)
			return true;
		
		return false;
	}
	
	private boolean checkYears(List<PowerOutages> partial, int maxYears) {
		
		if(partial.size()>=2) {
			
			int y1 = partial.get(0).getYear();
			int y2 = partial.get(partial.size()-1).getYear();
			
			if(y2-y1>maxYears)
				return false;
			
		}
		
		return true;
		
	}
	
	public List<Integer> getYearList() {
		Set<Integer> yearSet = new HashSet<Integer>();
		for (PowerOutages event : eventList) {
			yearSet.add(event.getYear());
		}
		List<Integer> yearList = new ArrayList<Integer>(yearSet);
		yearList.sort(new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				return o2.compareTo(o1);
			}
			
		});
		return yearList;
	}
}
