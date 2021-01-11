package it.prova.municipioAbitanteJPA.test;

import it.prova.municipioAbitanteJPA.dao.EntityManagerUtil;
import it.prova.municipioAbitanteJPA.model.Abitante;
import it.prova.municipioAbitanteJPA.model.Municipio;
import it.prova.municipioAbitanteJPA.service.MyServiceFactory;
import it.prova.municipioAbitanteJPA.service.abitante.AbitanteService;
import it.prova.municipioAbitanteJPA.service.municipio.MunicipioService;

public class TestMunicipioAbitante {

	public static void main(String[] args) {

		MunicipioService municipioService = MyServiceFactory.getMunicipioServiceInstance();
		AbitanteService abitanteService = MyServiceFactory.getAbitanteServiceInstance();

		try {

			// creo nuovo municipio
			Municipio nuovoMunicipio = new Municipio("Municipio III", "III", "Via dei Nani");
			// salvo
			municipioService.inserisciNuovo(nuovoMunicipio);
			System.out.println("Municipio appena inserito: " + nuovoMunicipio);
			// da questa riga in poi municipio ha un nuovo id

			// creo nuovo abitante
			Abitante nuovoAbitante = new Abitante("Pluto", "Plutorum", 77, "Via Lecce");
			// lo lego al municipio appena inserito
			nuovoAbitante.setMunicipio(nuovoMunicipio);
			// salvo il nuovo abitante
			abitanteService.inserisciNuovo(nuovoAbitante);

			// ricarico il municipio per vederne gli aggiornamenti
			// questa, durante la system.out che richiede gli abitanti, solleverebbe
			// una LazyInitializationException in quanto il contesto di persistenza è chiuso
			Municipio municipioInstance = municipioService.caricaSingoloMunicipio(nuovoMunicipio.getId());
			// allora usiamo un caricamento EAGER sovrascrivendo municipioInstance
			municipioInstance = municipioService.caricaSingoloMunicipioConAbitanti(nuovoMunicipio.getId());
			System.out
					.println("Stampo gli abitanti del municipio appena ricaricato:" + municipioInstance.getAbitanti());

			System.out.println("########### RIMOZIONE ABITANTE ########################");
			long idAbitanteEsistente = 19;
			Abitante abitanteEsistente2 = abitanteService.caricaSingoloAbitante(idAbitanteEsistente);
			if (abitanteEsistente2 != null) {
				abitanteService.rimuovi(abitanteEsistente2);
				//proviamo a vedere se è stato rimosso
				abitanteEsistente2 = abitanteService.caricaSingoloAbitante(idAbitanteEsistente);
				if (abitanteEsistente2 == null)
					System.out.println("Cancellazione ok");
				else
					System.out.println("Cancellazione fallita!!!");
			}
			System.out.println("########### FINE RIMOZIONE ABITANTE ########################");

			System.out.println("########### RIMOZIONE MUNICIPIO ########################");
			long idMunicipioEsistente = 2L;
			Municipio municipioDaEliminare = municipioService.caricaSingoloMunicipio(idMunicipioEsistente);
			if (municipioDaEliminare != null) {
				municipioService.rimuovi(municipioDaEliminare);
				// proviamo a vedere se è stato rimosso
				municipioDaEliminare = municipioService.caricaSingoloMunicipio(idMunicipioEsistente);
				if (municipioDaEliminare == null)
					System.out.println("Cancellazione ok");
				else
					System.out.println("Cancellazione fallita!!!");
			}
			
			System.out.println("########### LIST MUNICIPIO ########################");
			System.out.println("Elenco i municipi:");
			for (Municipio municipioItem : municipioService.listAllMunicipi()) {
				System.out.println(municipioItem);
			}

			// elenca tutti i Pluto
			System.out.println("Ecco i Pluto....");
			for (Abitante abitanteItem : abitanteService.cercaTuttiGliAbitantiConNome("Pluto")) {
				System.out.println(abitanteItem);
			}
			
			System.out.println("########### LIST ABITANTI ########################");
			System.out.println("Elenco i municipi:");
			for (Abitante abitanteItem : abitanteService.listAllAbitanti()) {
				System.out.println(abitanteItem);
			}

			System.out.println("########### FIND ABITANTE BY COGNOME########################");
			System.out.println("Ecco i Plutorum....");
			for (Abitante abitanteItem : abitanteService.cercaTuttiGliAbitantiConCognome("Plutorum")) {
				System.out.println(abitanteItem);
			}

			System.out.println("########### FIND DOVE CODICE MUNICIPIO INIZIA CON ########################");
			for (Abitante abitanteItem : abitanteService.cercaTuttiDoveMunicipioIniziaCon("I")) {
				System.out.println(abitanteItem);
			}

			System.out.println("########### FIND DOVE DESCRIZIONE MUNICIPIO INIZIA CON ########################");
			for (Municipio municipioItem : municipioService.cercaTuttiIMunicipiDoveDescrizioneIniziaCon("M")) {
				System.out.println(municipioItem);
			}

			System.out.println("########### FIND MUNICIPIO DOVE CI SONO MINORENNI ########################");
			for (Municipio municipioItem : municipioService.cercaTuttiIMunicipiConMinorenni()) {
				System.out.println(municipioItem);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// questa è necessaria per chiudere tutte le connessioni quindi rilasciare il
			// main
			EntityManagerUtil.shutdown();
		}

	}

}
