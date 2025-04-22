package com.practica.ems.covid;


import java.util.Iterator;
import java.util.LinkedList;
import com.practica.excecption.EmsDuplicateLocationException;
import com.practica.excecption.EmsLocalizationNotFoundException;
import com.practica.genericas.FechaHora;
import com.practica.genericas.PosicionPersona;

public class Localizacion {
	LinkedList<PosicionPersona> lista;

	public Localizacion() {
		super();
		this.lista = new LinkedList<PosicionPersona>();
	};
	
	public LinkedList<PosicionPersona> getLista() {
		return lista;
	}

	public void setLista(LinkedList<PosicionPersona> lista) {
		this.lista = lista;
	}

	public void addLocalizacion (PosicionPersona p) throws EmsDuplicateLocationException {
		try {
			findLocalizacion(p.getDocumento(), p.getFechaPosicion().getFecha().toString(),p.getFechaPosicion().getHora().toString() );
			throw new EmsDuplicateLocationException();
		}catch(EmsLocalizationNotFoundException e) {
			lista.add(p);
		}
	}
	
	public int findLocalizacion (String documento, String fecha, String hora) throws EmsLocalizationNotFoundException {
	    int cont = 0;
	    Iterator<PosicionPersona> it = lista.iterator();
	    while(it.hasNext()) {
	    	cont++;
	    	PosicionPersona pp = it.next();
	    	FechaHora fechaHora = this.parsearFecha(fecha, hora);
	    	if(pp.getDocumento().equals(documento) && 
	    	   pp.getFechaPosicion().equals(fechaHora)) {
	    		return cont;
	    	}
	    } 
	    throw new EmsLocalizationNotFoundException();
	}
	public void delLocalizacion(String documento, String fecha, String hora) throws EmsLocalizationNotFoundException {
	    int pos=-1;
	    /**
	     *  Busca la localización, sino existe lanza una excepción
	     */
	    try {
			pos = findLocalizacion(documento, fecha, hora);
		} catch (EmsLocalizationNotFoundException e) {
			throw new EmsLocalizationNotFoundException();
		}
	    this.lista.remove(pos);
	    
	}
	
	void printLocalizacion() {
		for(PosicionPersona p : this.lista) {
			p.toString();
		}
	}

	@Override
	public String toString() {
		String cadena = "";
		for(int i = 0; i < this.lista.size(); i++) {
			PosicionPersona pp = lista.get(i);
	        cadena += String.format("%s;", pp.getDocumento());
	        FechaHora fecha = pp.getFechaPosicion();        
	        cadena+=String.format("%02d/%02d/%04d;%02d:%02d;", 
	        		fecha.getFecha().getDia(), 
	        		fecha.getFecha().getMes(), 
	        		fecha.getFecha().getAnio(),
	        		fecha.getHora().getHora(),
	        		fecha.getHora().getMinuto());
	        cadena+=String.format("%.4f;%.4f\n", pp.getCoordenada().getLatitud(), 
	        		pp.getCoordenada().getLongitud());
	    }
		
		return cadena;		
	}


	@SuppressWarnings("unused")
	private FechaHora parsearFecha (String fecha) {
		int[] valores = CalcularDatos(fecha, "\\/");
		return new FechaHora(valores[0], valores[1], valores[2], 0, 0);
	}
	
	private  FechaHora parsearFecha (String fecha, String hora) {
		int[] dataFecha = CalcularDatos(fecha, "\\/"), dataHora = CalcularDatos(hora, "\\:");
		return new FechaHora(dataFecha[0], dataFecha[1], dataFecha[2], dataHora[0], dataHora[1]);
	}

	private int[] CalcularDatos(String data, String regex){
		String[] valores = data.split(regex);
		return TransformInt(valores);
	}

	private int[] TransformInt(String[] valores){
		int[] valInt = new int[valores.length];
		for(int i = 0; i < valores.length; i++) {
			valInt[i] = Integer.parseInt(valores[i]);
		}
		return valInt;
	}
}
